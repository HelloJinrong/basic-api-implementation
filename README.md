### 作业描述

<<<<<<< HEAD

* 去掉RsService上的@Service注解
通过使用@Bean这种方式进行spring bean的定义和注入
reference: https://docs.spring.io/spring-javaconfig/docs/1.0.0.M4/reference/html/ch02s02.html

* 阅读：https://docs.spring.io/spring-boot/docs/2.0.x/reference/html/using-boot-spring-beans-and-dependency-injection.html
修改所有的service和controller 将依赖注入的方式改为通过构造函数注入（而非直接在字段上添加@Autowired）
=======
#### 重复课堂上的Demo完成练习
* 给所有接口添加错误处理：
    1. get /rs/list时对start和end进行校验，如果超出范围则返回 400 {error:"invalid request param"}
    2. get /rs/{index} 对index进行校验，如果超出范围则返回 400 {error:"invalid index"}
    3. post /rs/event 对RsEvent进行校验，如果不满足校验规则(User和RsEvent定义的校验规则)，则返回 400 {error:"invalid param"}
    4. post /user 对User进行校验，如果不满足校验规则，则返回 400 {error:"invalid user"}
    git5. 先阅读：https://www.baeldung.com/spring-boot-logging
       在我们的exceptionHandler中添加日志，记录下错误的信息（error级别），运行程序试着观察是否有日志打印
* 先写测试（除了日志）！

<span style="color: red"> 注意：最终需要将改动合并到master分支 </span> 
>>>>>>> master

