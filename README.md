后端采用spring boot + mybatis plus

数据库mysql

小程序端用的是colorui

本项目只是简单即时聊天通讯，其中可能存在很多缺陷，欢迎大家指出。

![Image text](https://www.yueqiuba.top/img/chatList.jpg)
![Image text](https://www.yueqiuba.top/img/chat2.jpg)
![Image text](https://www.yueqiuba.top/img/chat.jpg)
![Image text](https://www.yueqiuba.top/img/moniweb.png)

小程序模拟的是李四用户，

http://localhost:18092/index.html 模拟的是张三用户，

http://localhost:18092/index2.html 模拟的是王五用户。



sql:

chat_record 聊天记录表是存储所有的聊天信息（可用于漫游之类的）

chat_unread 存储用户处于离线或者网络的原因未收到的数据，登录后拉取这些消息




已知存在问题：

1、如果对方消息发送太快，一秒N条，会出现滑不到底部的问题

2、聊天页面本来是打算判断当前消息是否在最底部，不是的话，最新消息就不滑到最底部（因为这个时候可能在浏览之前的消息）,但是不会搞

3、聊天内容为链接等七七八八的东东就会聊天页面排版问题（直接用colorui的，反正也不会前端样式）

4、收到消息后存入到本地缓存，不知道如何直接追加（每次读取缓存，更新数据，再存入数据，消耗资源，还有小程序缓存问题不知道可以存多少，不行就直接拉取所有记录）

5、自己发送消息给他人的时候我就简单的也是和获取别人的消息一样，消息发到服务器后，服务器会再发给自己，然后再展示到页面上（应该是要发送的话就展示在页面上，到时候网络有问题再重新发送，按我当前的做法自己发出的消息有可能再也不会出现在页面上了）

6、不足之处还请大家多多帮忙
