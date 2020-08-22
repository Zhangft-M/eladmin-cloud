package org.micah.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.micah.core.base.BaseEntity;
import org.micah.core.util.enums.CodeEnum;
import org.micah.core.web.page.PageResult;
import org.micah.exception.global.BadRequestException;
import org.micah.model.SysUser;
import org.micah.model.dto.RoleSmallDto;
import org.micah.model.dto.SysUserDto;
import org.micah.model.query.UserQueryCriteria;
import org.micah.model.vo.UserPassVo;
import org.micah.security.annotation.Inner;
import org.micah.security.util.SecurityUtils;
import org.micah.system.service.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Api(tags = "系统：用户管理")
@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class SysUserController {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final ISysUserService userService;
    private final IDataService dataService;
    private final IDeptService deptService;
    private final IRoleService roleService;
    private final IVerifyService verificationCodeService;

    // @Log("导出用户数据")
    @ApiOperation("导出用户数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('user:list')")
    public void download(HttpServletResponse response, UserQueryCriteria queryCriteria) throws IOException {
        this.userService.download(this.userService.queryAll(queryCriteria), response);
    }

    // @Log("导出用户数据")
    @ApiOperation("获取当前的用户信息")
    @GetMapping(value = "/info")
    @PreAuthorize("@el.check('user:list')")
    public ResponseEntity<Map<String,Object>> getCurrentUserInfo(){
        return ResponseEntity.ok(this.userService.getCurrentUserInfo());
    }

    // @Log("导出用户数据")
    @ApiOperation("通过用户名查询用户")
    @GetMapping(value = "/username")
    @PreAuthorize("@el.check('user:list')")
    public ResponseEntity<SysUser> queryByUsername(String username){
        return ResponseEntity.ok(this.userService.queryByUsername(username));
    }

    // @Log("查询用户")
    @ApiOperation("查询用户")
    @GetMapping
    @PreAuthorize("@el.check('user:list')")
    public ResponseEntity<PageResult> query(UserQueryCriteria queryCriteria, Pageable pageable){
        if (!Objects.isNull(queryCriteria.getDeptId())) {
            // 先将当前部门的id加入到权限列表中
            queryCriteria.getDeptIds().add(queryCriteria.getDeptId());
            // 再获取当下部门的数据以及子部门数据的id，构建数据权限
            //queryCriteria.getDeptIds().addAll(this.deptService.getDeptChildren(this.deptService.findByPid(queryCriteria.getDeptId())));
            queryCriteria.getDeptIds().addAll(this.deptService.getDeptIds(this.deptService.findByPid(queryCriteria.getId()),queryCriteria.getDeptIds()));
        }
        // 数据权限
        List<Long> dataScopes = this.dataService.getDeptIds(this.userService.findByName(SecurityUtils.getCurrentUsername()));
        // criteria.getDeptIds() 不为空并且数据权限不为空则取交集
        if (!CollectionUtils.isEmpty(queryCriteria.getDeptIds()) && !CollectionUtils.isEmpty(dataScopes)){
            // 取交集
            queryCriteria.getDeptIds().retainAll(dataScopes);
            if(!CollectionUtils.isEmpty(queryCriteria.getDeptIds())){
                return new ResponseEntity<>(this.userService.queryAll(queryCriteria,pageable), HttpStatus.OK);
            }
        } else {
            // 用户传来的数据权限为空或者用户本身具备的数据权限为空
            // 否则取并集
            queryCriteria.getDeptIds().addAll(dataScopes);
            return new ResponseEntity<>(userService.queryAll(queryCriteria,pageable),HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.OK);
    }

    // @Log("新增用户")
    @ApiOperation("新增用户")
    @PostMapping
    @PreAuthorize("@el.check('user:add')")
    public ResponseEntity<Void> create(@Validated(BaseEntity.Create.class) @RequestBody SysUser resources){
        if (!Objects.isNull(resources.getId())){
            throw new BadRequestException("新的用户不应该存在id");
        }
        this.checkLevel(resources);
        // 默认密码 123456
        resources.setPassword(passwordEncoder.encode("123456"));
        this.userService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // @Log("修改用户")
    @ApiOperation("修改用户")
    @PutMapping
    @PreAuthorize("@el.check('user:edit')")
    public ResponseEntity<Void> update(@Validated(SysUser.Update.class) @RequestBody SysUser resources){
        this.checkLevel(resources);
        this.userService.updateSysUser(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // @Log("修改用户：个人中心")
    @ApiOperation("修改用户：个人中心")
    @PutMapping(value = "center")
    public ResponseEntity<Void> center(@Validated(SysUser.Update.class) @RequestBody SysUser resources){
        if(!resources.getId().equals(SecurityUtils.getCurrentUserId())){
            throw new BadRequestException("不能修改他人资料");
        }
        this.userService.updateCenter(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // @Log("删除用户")
    @ApiOperation("删除用户")
    @DeleteMapping
    @PreAuthorize("@el.check('user:del')")
    public ResponseEntity<Void> delete(@RequestBody Set<Long> ids){
        for (Long id : ids) {
            // 当前用户具有的最低的操作等级
            Integer currentLevel =  Collections.min(this.roleService.findByUsersId(SecurityUtils.getCurrentUserId())
                    .stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
            // 进行删除操作所需要的最低的操作等级
            Integer optLevel =  Collections.min(this.roleService.findByUsersId(id)
                    .stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
            if (currentLevel > optLevel) {
                throw new BadRequestException("角色权限不足，不能删除：" + this.userService.findById(id).getUsername());
            }
        }
        this.userService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("修改密码")
    @PostMapping(value = "/updatePass")
    public ResponseEntity<Void> updatePass(@RequestBody UserPassVo passVo) throws Exception {
        // String oldPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey,passVo.getOldPass());
        // String newPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey,passVo.getNewPass());
        SysUserDto user = this.userService.findByName(SecurityUtils.getCurrentUsername());
        if(!passwordEncoder.matches(passVo.getOldPass(), user.getPassword())){
            throw new BadRequestException("修改失败，旧密码错误");
        }
        if(passwordEncoder.matches(passVo.getNewPass(), user.getPassword())){
            throw new BadRequestException("新密码不能与旧密码相同");
        }
        this.userService.updatePassword(user.getUsername(),passwordEncoder.encode(passVo.getNewPass()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("修改头像")
    @PostMapping(value = "/updateAvatar")
    public ResponseEntity<Map<String, String>> updateAvatar(@RequestParam MultipartFile avatar){
        return new ResponseEntity<>(this.userService.updateAvatar(avatar), HttpStatus.OK);
    }

    // @Log("修改邮箱")
    @ApiOperation("修改邮箱")
    @PostMapping(value = "/updateEmail/{code}")
    public ResponseEntity<Void> updateEmail(@PathVariable String code,@RequestBody SysUser user) throws Exception {
        // String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey,user.getPassword());
        SysUserDto userDto = this.userService.findByName(SecurityUtils.getCurrentUsername());
        if(!passwordEncoder.matches(user.getPassword(), userDto.getPassword())){
            throw new BadRequestException("密码错误");
        }
        // 验证验证码是否正确
        this.verificationCodeService.validated(CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey() + user.getEmail(), code);
        this.userService.updateEmail(userDto.getUsername(),user.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 如果当前用户的角色级别低于创建用户的角色级别，则抛出权限不足的错误
     * @param resources /
     */
    private void checkLevel(SysUser resources) {
        Integer currentLevel =  Collections.min(this.roleService.findByUsersId(SecurityUtils.getCurrentUserId())
                .stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
        Integer optLevel = this.roleService.findByRoles(resources.getRoles());
        if (currentLevel > optLevel) {
            log.info("角色的权限不足");
            throw new BadRequestException("角色权限不足");
        }
    }
}