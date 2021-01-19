# message-stream-email

基于邮件实现的消息流

## 说明

* 指定邮件账号接收到邮件转为输入消息
* 发布消息转为邮件发送
* 接收消息时, Message中的address表示为发送者账户
* 发布消息时必须指定address, 表示要发送给的邮件账户

## 启用

``` yaml
message:
  stream:
    email:
      enable: true
      host: <邮件SMTP服务器>
      username: <邮件账户>
      password: <密码>
      pull:
        host: <拉取服务器配置>
```

## 配置

配置说明请参见源码注释

* [基础配置: message.stream.email](./src/main/java/tech/guyi/component/message/stream/email/configuration/EmailConfiguration.java)
* [拉取配置: message.stream.email.pull](./src/main/java/tech/guyi/component/message/stream/email/configuration/EmailPullConfiguration.java)
* [拉取SSL配置: message.stream.email.pull.ssl](./src/main/java/tech/guyi/component/message/stream/email/configuration/EmailPullSslConfiguration.java)
* [标题提取配置: message.stream.email.extract](./src/main/java/tech/guyi/component/message/stream/email/configuration/EmailExtractorConfiguration.java)