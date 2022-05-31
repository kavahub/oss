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

运行:
``` 
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

#### 阿里云镜像服务
阿里云提供个人的，免费的镜像服务。开通服务，在本机上运行
```
docker login --username=<阿里云账号> registry.cn-hangzhou.aliyuncs.com
```

然后运行命令
```
mvn compile jib:build -Pali-docker 
```

命令成功运行后，在阿里云镜像服务中可以查询到

本地拉取并运行镜像
```
docker run -p 9020:9020 --link mysql:mysql --link springboot-embedded-server:springboot-embedded-server --link minio:minio --name oss-minio-server -v C:\Users\wpw\s8d\oss:/s8d/oss -e "SPRING_PROFILES_ACTIVE=pro" -d registry.cn-hangzhou.aliyuncs.com/s8d/cn.springseed.oss.minio-server
```

