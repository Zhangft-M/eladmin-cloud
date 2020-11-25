# El-Admin-Cloud

## 项目介绍

|                            | 地址                                         |
| -------------------------- | -------------------------------------------- |
| 前端项目:eladmin-cloud-web | https://github.com/Micah-Z/eladmin-cloud-web |
| 后端项目:eladmin-cloud     | https://github.com/Micah-Z/eladmin-cloud     |

​	

​		该项目后端基于[eladmin](https://el-admin.vip/)开发，参考了[RuoYi-Cloud](https://doc.ruoyi.vip/ruoyi-cloud/)和[Pig-Cloud](https://www.pig4cloud.com/)的设计模式，将其改造为微服务的架构模式，一共会集成了网关[Spring Cloud GateWay](https://docs.spring.io/spring-cloud-gateway/docs/2.2.4.RELEASE/reference/html/#gateway-starter)，认证授权[Spring Cloud Security:Oauth2](https://projects.spring.io/spring-security-oauth/docs/oauth2.html)，服务注册中心[Spring Cloud Alibaba Nacos](https://nacos.io/zh-cn/),RPC框架[Spring Cloud OpenFeign](https://docs.spring.io/spring-cloud-openfeign/docs/2.2.4.RELEASE/reference/html/),分布式任务调度平台[xxl-job](https://github.com/xuxueli/xxl-job)(),流量管理框架[sentinel](https://github.com/alibaba/Sentinel),分布式配置中心[Spring Cloud Alibaba Nacos Config](https://nacos.io/zh-cn/docs/what-is-nacos.html)，消息中间件[rabbitMQ](https://www.rabbitmq.com/)

项目的前端基于[eladmin](https://github.com/elunez/eladmin-web)前端进行改造.

上述已经整合完毕，由于上学了，项目也没时间更新了，要开始写新的项目了,完成学校的工程训练,太难了，:sob:,以后项目大致已经完善，还有一些第三方工具没有整合，对项目感兴趣的可以看看eladmin和ruoyi-cloud，pig-cloud,这才是真正的大佬...........

### 2020年11月25日

本来传到GitHub上面去只是好玩，没想到有人真的会看，所以今天把sql文件传上去了，所有的文件应该齐全了，没时间把表分出来，所以有点乱，可以参照配置文件来看。

下面这两个，有时间再整合！

分布式事务中间件[seata](https://github.com/seata/seata),分库分表中间件[Sharding-Jdbc](http://shardingsphere.apache.org/)

​		

​		

