# local-server

基于本地文件系统的存储服务

提供接口信息如下：

| 方法   | 接口                           | 说明                                     |
| ------ | ------------------------------ | ---------------------------------------- |
| GET    | /v1/metadatas/{id}             | 指定一个对象ID，返回对象的元数据信息     |
| GET    | /v1/metadatas                  | 指定多个对象ID，返回所有对象的元数据信息 |
| PUT    | /v1/metadatas/{id}/name        | 指定一个对象ID，修改对象元数据name的信息 |
| POST   | /v1/files/upload               | 上传文件                                 |
| GET    | /v1/files/download/{objectId}  | 下载单个文件                             |
| GET    | /v1/files/download/{objectIds} | 下载多个文件                             |
| DELETE | /v1/files/{objectId}           | 删除对象                                 |

#### 配置

修改配置文件`src/resources/application.yml`，配置数据库。

创建数据库及用户脚本：
```
CREATE DATABASE IF NOT EXISTS oss DEFAULT CHARSET utf8;
create user 'ossuser'@'%' identified by 'ossuser';
grant create,alter,drop,select,insert,update,delete on oss.* to ossuser@'%';
flush privileges; 
```

#### 打包

编译，测试，打包：

```
mvn clean install spring-boot:repackage
```

#### 运行

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
docker run -p 9020:9020 --link mysql:mysql --link springboot-embedded-server:springboot-embedded-server --name oss-local-server -v C:\Users\wpw\s8d\oss:/s8d/oss -e "SPRING_PROFILES_ACTIVE=pro" -d registry.cn-hangzhou.aliyuncs.com/s8d/cn.springseed.oss.local-server
```


