# API开放平台

## 需求分析
背景：
1. 前端开发需要的后台接口
2. 相似的系统(博天api：http://api.btstu.cn/)

基本功能：
1. 管理员可以对接口进行增删查改
2. 提供可视化分析功能，管理可以观察接口的使用情况
3. 用户可以访问前台，查看接口信息
4. 用户可以对接口进行模拟调用

其他要求
1. 防止攻击
2. 不能随意调用（对接口使用进行限制）
3. 统计调用次数
4. 计费
5. 流量保护
6. API接入

## 业务流程
![业务流程](https://i.ibb.co/K043qxX/image.jpg)



## 技术选型
**前端**
- Ant Design Pro
- React
- Ant Design Procomponents
- Umi

**后端**
- Spring Boot
- Spring Boot Starter(SDK开发)
- Dubbo(RPC调用)
- Nacos
- Spring Cloud GateWay(网关)




