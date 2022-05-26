# minio-server

基于MinIO的存储服务

#### 帮助

编译，测试，打包：

```
mvn clean install spring-boot:repackage
```

如果有测试的MinIO服务器，可以测试：

```
mvn clean install -Plive-test 
```

#### 运行

``` 
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```