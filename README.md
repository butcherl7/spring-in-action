# 使用 H2 Database 总结

1. 可使用的 JDBC URL 参考 [Database URL Overview](https://h2database.com/html/features.html#database_url)，例如创建内存型还是持久化的
   DB. 例如：

| Topic                       | URL Format and Examples                       |
|-----------------------------|-----------------------------------------------|
| Embedded (local) connection | jdbc:h2:\[file:\]\[\<path\>\]\<databaseName\> |
| In-memory (named)           | 	jdbc:h2:mem:\<databaseName\>                 |

## 遇到的一些问题

1. 若不添加 jdbc 依赖则不会自动创建数据库，同时确保 `spring.h2.console.enabled = true`.

```xml

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
```

2. 报错：*Database "mem:h2" not found, either pre-create it or allow remote database creation (not recommended in secure
   environments) [90149-210] 90149/90149*
    - [Tutorial - Creating New Databases](https://h2database.com/html/tutorial.html#creating_new_databases)
    - [Database .. not found, either pre-create it or allow remote ... 解决 - 锐洋智能 - 博客园](https://www.cnblogs.com/interdrp/p/15817251.html)