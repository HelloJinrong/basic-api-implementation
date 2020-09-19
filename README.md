### 作业描述

<<<<<<< HEAD
#### 实现如下接口
* 查询投票记录接口：
    参数传入起止时间，查询在该时间范围内的所有投票记录，写测试验证
* 这是最后一堂JPA课，把前面所有没做完的作业包括课上demo的范例都补上！
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
>>>>>>> master

<span style="color: red"> 注意：最终需要将改动合并到master分支 </span> 

