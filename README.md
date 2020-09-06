# El-Admin-Cloud

## 项目介绍

|                            | 地址                                         |
| -------------------------- | -------------------------------------------- |
| 前端项目:eladmin-cloud-web | https://github.com/Micah-Z/eladmin-cloud-web |
| 后端项目:eladmin-cloud     | https://github.com/Micah-Z/eladmin-cloud     |

​	

​		该项目后端基于[El-Admin](https://el-admin.vip/)开发，参考了[RuoYi-Cloud](https://doc.ruoyi.vip/ruoyi-cloud/)和[Pig-Cloud](https://www.pig4cloud.com/)的设计模式，将其改造为微服务的架构模式，目前集成了网关[Spring Cloud GateWay](https://docs.spring.io/spring-cloud-gateway/docs/2.2.4.RELEASE/reference/html/#gateway-starter)，认证授权[Spring Cloud Security:Oauth2](https://projects.spring.io/spring-security-oauth/docs/oauth2.html)，服务注册中心[Spring Cloud Alibaba Nacos](https://nacos.io/zh-cn/),RPC框架[Spring Cloud OpenFeign](https://docs.spring.io/spring-cloud-openfeign/docs/2.2.4.RELEASE/reference/html/).之后会集成分布式任务调度平台[xxl-job](https://github.com/xuxueli/xxl-job),分布式事务中间件[seata](https://github.com/seata/seata),流量管理框架[sentinel](https://github.com/alibaba/Sentinel)分布式配置中心[Spring Cloud Alibaba Nacos Config](https://nacos.io/zh-cn/docs/what-is-nacos.html)分库分表中间件[Sharding-Jdbc](http://shardingsphere.apache.org/).

​		项目的前端基于[eladmin](https://github.com/elunez/eladmin-web)前端进行改造。

​		

