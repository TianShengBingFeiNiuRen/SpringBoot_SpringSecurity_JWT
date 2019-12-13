# SpringBoot整合SpringSecurity+JWT实现单点的登录

**一、JSON Web Token（JWT）**
> 官方解释：JSON Web Token（JWT）是一个开放标准（RFC 7519），它定义了一种紧凑且自包含的方式，用于在各方之间作为JSON对象安全地传输信息。由于此信息是经过数字签名的，因此可以被验证和信任。可以使用秘密（使用HMAC算法）或使用RSA或ECDSA的公钥/私钥对对JWT进行签名。

**二、项目背景**
	公司的业务越来越复杂，随着业务的扩展需要将现有单个后端web系统进行拆分，并在同为顶级域名的多个web系统之间实现单点登录及权限控制，同时也为以后的服务集群部署做准备。

**三、实现方式及效果**
实现的方式：基于现有的完整权限控制项目，之前整理过的一篇博文 [**前后端分离 SpringBoot整合SpringSecurity权限控制（动态拦截url）**](https://blog.csdn.net/weixin_39792935/article/details/84541194)，在此基础之上引入JWT实现单点登录。
实现的效果：

 - 用户不带token访问系统B，系统B响应状态码401（需要认证）
 - 用户登录系统A，系统A校验用户名密码成功，生成并响应token及状态码200
 - 用户没有登录系统B而是携带系统A响应的token去访问系统B
 - 系统B解析token并进行权限校验，无权限访问资源则响应403（权限不足），权限验证成功则响应正常的json数据
 - 访问系统C、系统D或分布式集群亦是如此

**maven依赖：**

```java
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        
		<dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt</artifactId>
            <version>0.9.1</version>
        </dependency>
```


跳转: [**WebSecurityConfigure.java**](https://github.com/TianShengBingFeiNiuRen/SpringBoot_SpringSecurity_JWT/blob/master/src/main/java/com/nonce/restsecurity/config/WebSecurityConfigure.java)

**CSDN**: [**link**](https://blog.csdn.net/weixin_39792935/article/details/103528008).
