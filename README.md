eladmin-cloud
该项目后端基于El-Admin开发，参考了RuoYi-Cloud和Pig-Cloud的设计模式， 
将其改造为微服务的架构模式，目前集成了网关Spring Cloud GateWay， 
认证授权Spring Cloud Security:Oauth2，服务注册中心Spring Cloud Alibaba Nacos, 
RPC框架Spring Cloud OpenFeign. 之后会集成分布式任务调度平台xxl-job,分布式事务中间件seata,
流量管理框架sentinel 分布式配置中心Spring Cloud Alibaba Nacos Config分库分表中间件Sharding-Jdbc.