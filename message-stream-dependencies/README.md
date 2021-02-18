# message-stream-dependencies

因不同的消息流实现本身可能是单独维护的， 所以可能存在相同的大版本下，不同的消息流实现为不同的版本号。

为避免因版本不一致导致的方法缺失、参数不一致、类缺失等问题，使用时引入此项目管理版本。

## 使用方式

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>tech.guyi.component</groupId>
            <artifactId>message-stream-dependencies</artifactId>
            <version>${last.version}</version>
            <scope>import</scope>
            <type>pom</type>
        </dependency>
    </dependencies>
</dependencyManagement>
```