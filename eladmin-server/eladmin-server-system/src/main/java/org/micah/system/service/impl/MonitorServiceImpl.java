

package org.micah.system.service.impl;

import cn.hutool.core.date.BetweenFormater;
import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.micah.core.util.FileUtils;
import org.micah.core.util.RequestUtils;
import org.micah.system.service.IMonitorService;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;
import oshi.util.Util;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: eladmin-cloud
 * @description: 系统监控业务实现类
 * @author: Micah
 * @create: 2020-08-10 17:47
 **/
@Slf4j
@Service
public class MonitorServiceImpl implements IMonitorService {

    private final DecimalFormat df = new DecimalFormat("0.00");

    /**
     * 获取服务器信息
     *
     * @return
     */
    @Override
    public Map<String, Object> getServersInfo() {
        // 定义一个map结果集
        Map<String, Object> resultMap = Maps.newHashMapWithExpectedSize(8);
        try {
            SystemInfo systemInfo = new SystemInfo();
            // 获取操作系统
            OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
            // 获取硬件信息
            HardwareAbstractionLayer hardware = systemInfo.getHardware();
            // 封装系统信息
            resultMap.put("sys", this.getSystemInfo(operatingSystem));
            // 封装cpu 信息
            resultMap.put("cpu", this.getCpuInfo(hardware.getProcessor()));
            // 封装内存信息
            resultMap.put("memory", this.getMemoryInfo(hardware.getMemory()));
            // 封装交换区信息
            resultMap.put("swap", this.getSwapInfo(hardware.getMemory()));
            // 封装磁盘信息
            resultMap.put("disk", this.getDiskInfo(operatingSystem));
            // 封装当前时间信息
            resultMap.put("time", DateUtil.format(new Date(), "HH:mm:ss"));
        } catch (Exception e) {
            log.error("获取服务器信息失败，请联系管理员");
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * 获取磁盘信息
     * @param operatingSystem
     * @return
     */
    private Map<String,String> getDiskInfo(OperatingSystem operatingSystem) {
        Map<String,String> diskInfo = Maps.newLinkedHashMapWithExpectedSize(4);
        // 获取文件系统信息
        FileSystem fileSystem = operatingSystem.getFileSystem();
        // 获取所有磁盘分区信息
        List<OSFileStore> fileStores = fileSystem.getFileStores();
        // 遍历
        for (OSFileStore fileStore : fileStores) {
            // TODO: 2020/8/10 有个小bug，有多个驱动器的话，后面的数据会覆盖前面的数据，需结合前端修改 
            // 获取该驱动器的总的大小
            String totalSize = fileStore.getTotalSpace() > 0 ? FileUtils.transformSize(fileStore.getTotalSpace()) : "?";
            diskInfo.put("total",totalSize);
            // 获取已经使用的大小
            diskInfo.put("used",FileUtils.transformSize(fileStore.getTotalSpace()-fileStore.getUsableSpace()));
            // 获取可以使用的大小
            diskInfo.put("available",FileUtils.transformSize(fileStore.getUsableSpace()));
            // 获取可以使用的比率
            diskInfo.put("usageRate", df.format((fileStore.getTotalSpace()-fileStore.getUsableSpace())/(double)fileStore.getTotalSpace() * 100));
        }
        return diskInfo;
    }

    /**
     * 获取交换区信息(虚拟内存信息)
     * @param memory
     * @return
     */
    private Map<String,String> getSwapInfo(GlobalMemory memory) {
        Map<String,String> virtualMemory = Maps.newLinkedHashMapWithExpectedSize(4);
        virtualMemory.put("total",FormatUtil.formatBytes(memory.getVirtualMemory().getSwapTotal()));
        virtualMemory.put("used",FormatUtil.formatBytes(memory.getVirtualMemory().getSwapUsed()));
        virtualMemory.put("available",FormatUtil.formatBytes(memory.getVirtualMemory().getSwapTotal()-memory.getVirtualMemory().getSwapUsed()));
        virtualMemory.put("usageRate", df.format(memory.getVirtualMemory().getSwapUsed()/(double)memory.getVirtualMemory().getSwapTotal() * 100));
        return virtualMemory;
    }

    /**
     * 获取内存信息
     *
     * @param memory
     * @return
     */
    private Map<String,String> getMemoryInfo(GlobalMemory memory) {
        Map<String, String> memoryInfo = Maps.newLinkedHashMapWithExpectedSize(4);
        // 获取总的内存的大小
        memoryInfo.put("total", FormatUtil.formatBytes(memory.getTotal()));
        // 获取空闲的内存大小
        memoryInfo.put("available", FormatUtil.formatBytes(memory.getAvailable()));
        // 获取已经使用的大小
        memoryInfo.put("used", FormatUtil.formatBytes(memory.getTotal() - memory.getAvailable()));
        // 获取使用的百分比
        memoryInfo.put("usageRate", df.format((memory.getTotal() - memory.getAvailable()) / (double) memory.getTotal() * 100));
        return memoryInfo;
    }

    /**
     * 获取cpu信息
     *
     * @param processor
     * @return
     */
    private Map<String,String> getCpuInfo(CentralProcessor processor) {
        Map<String, String> cpuInfo = Maps.newLinkedHashMapWithExpectedSize(7);
        cpuInfo.put("name", processor.getProcessorIdentifier().getName());
        cpuInfo.put("package", processor.getPhysicalPackageCount() + "个物理CPU");
        cpuInfo.put("core", processor.getPhysicalProcessorCount() + "个物理核心");
        cpuInfo.put("coreNumber", String.valueOf(processor.getPhysicalProcessorCount()));
        cpuInfo.put("logic", processor.getLogicalProcessorCount() + "个逻辑CPU");
        // CPU信息
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        // 等待1秒...
        Util.sleep(1000);
        // 获取系统cup的负载刻度
        long[] ticks = processor.getSystemCpuLoadTicks();
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long sys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long totalCpu = user + nice + sys + idle + iowait + irq + softirq + steal;
        cpuInfo.put("used", df.format(100d * user / totalCpu + 100d * sys / totalCpu));
        cpuInfo.put("idle", df.format(100d * idle / totalCpu));
        return cpuInfo;
    }

    /**
     * 获取系统信息
     *
     * @param operatingSystem
     * @return
     */
    private Map<String,String> getSystemInfo(OperatingSystem operatingSystem) {
        Map<String, String> systemInfo = Maps.newLinkedHashMapWithExpectedSize(3);
        // jvm运行的时间
        // 获取起始运行的时间
        long startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
        Date date = new Date(startTime);
        // 计算项目运行时间
        String formatBetween = DateUtil.formatBetween(date, new Date(), BetweenFormater.Level.HOUR);
        // 系统信息
        systemInfo.put("os", operatingSystem.toString());
        systemInfo.put("day", formatBetween);
        systemInfo.put("ip", RequestUtils.getLocalIp());
        return systemInfo;
    }
}
