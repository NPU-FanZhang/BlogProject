

# F4NBlog Project Deployment Instructions

## 1. 打包

1. 项目父工程安装打包插件。

   ```xml
   <build>
       <plugins>
           <plugin>
               <groupId>org.apache.maven.plugins</groupId>
               <artifactId>maven-resources-plugin</artifactId>
               <version>3.1.0</version>
           </plugin>
           <plugin>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-maven-plugin</artifactId>
           </plugin>
       </plugins>
   </build>
   ```

2. 配置项目`application.properties`中的项目数据库和`redis`的环境。

   ```properties
   server.port=8888
   spring.application.name=blog
   
   spring.datasource.url=jdbc:mysql://localhost:3306/blog?useUnicode=true&characterEncoding=UTF-8\
     &serverTimeZone=UTC
   spring.datasource.username=root
   spring.datasource.password=123456
   
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   
   mybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
   mybatis-plus.global-config.db-config.table-prefix=zf_
   
   spring.redis.host=localhost
   spring.redis.port=6379
   qiniu.accessKey= kk5dtifEXBoNbg3X4Wv8VY6YFLg2yXEIdNtfhKdT
   qiniu.accessSecretKey = wpenoq7m2wyznTv9ppOmbuArcSdxfguWQb7YaNll
   # 上传文件总的最大值
   spring.servlet.multipart.max-request-size=20MB
   # 单个文件的最大值
   spring.servlet.multipart.max-file-size=4MB
   ```

3. 项目打包成Jar包。



## 2. 服务器环境配置

1. 更新yum

   ```sh
   yum update
   # 添加仓库源
   yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
   ```

2. 安装Java环境

   ```sh
   yum -y list java* #查看列表
   yum install java-1.8.0-openjdk.x86_64 # 安装Jdk
   #通过yum安装的默认路径为：/usr/lib/jvm
   #将jdk的安装路径加入到JAVA_HOME
   vim /etc/profile
   #----------------------
   #set java environment
   JAVA_HOME=/usr/lib/jvm/jre-1.6.0-openjdk.x86_64
   PATH=$PATH:$JAVA_HOME/bin
   CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
   export JAVA_HOME CLASSPATH PATH
   #---------------------------
   . /etc/profile （注意 . 之后应有一个空格） # 让配置的文件生效
   java -version #查看版本和是否安装成功
   ```

### 1. 安装docker

   ```sh
   #安装yum-utils
   yum install -y yum-utils
   # 安装docker
   yum install -y docker-ce
   # 查看docker版本
   docker -v
   # 启动docker
   /bin/systemctl start docker.service
   ```

### 2. docker拉取镜像

```shell
docker pull nginx
docker pull redis:5.0.3
docker pull java:8
docker pull mysql:5.7
```

### 3. 安装Mysql

1. 创建mysql环境

   ```sh
   # 创建目录
   mkdir -p /blog/docker/mysql
   cd /blog/docker/mysql
   # 创建下面三个目录
   mkdir conf
   mkdir data
   mkdir logs
   ```

2. 创建配置文件

   在`/blog/docker/mysql/conf` 创建`my.cnf`

   ```sh
   #-----------------------------------
   [mysqld]
   #
   # Remove leading # and set to the amount of RAM for the most important data
   # cache in MySQL. Start at 70% of total RAM for dedicated server, else 10%.
   # innodb_buffer_pool_size = 128M
   #
   # Remove leading # to turn on a very important data integrity option: logging
   # changes to the binary log between backups.
   # log_bin
   #
   # Remove leading # to set options mainly useful for reporting servers.
   # The server defaults are faster for transactions and fast SELECTs.
   # Adjust sizes as needed, experiment to find the optimal values.
   # join_buffer_size = 128M
   # sort_buffer_size = 2M
   # read_rnd_buffer_size = 2M
   datadir=/var/lib/mysql
   socket=/var/lib/mysql/mysql.sock
   character-set-server=utf8
   # Disabling symbolic-links is recommended to prevent assorted security risks
   symbolic-links=0
   lower_case_table_names=1
   pid-file=/var/run/mysqld/mysqld.pid
   sql_mode=STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION
   ```

3. 运行

   ```sh
   # 运行docker容器中的 mysql 并映射目录
   docker run -id \
   -p 3307:3306 \   		#端口映射 对外暴露3307
   --name=blog_mysql \ 	#程序别名
   -v /blog/docker/mysql/conf:/etc/mysql/conf.d \	#mysql配置目录映射
   -v /blog/docker/mysql/logs:/logs \ 	
   -v /blog/docker/mysql/data:/var/lib/mysql \ 
   -e MYSQL_ROOT_PASSWORD=123456 \
   mysql:5.7
   ```

4. 查看mysql对应的docker容器Ip地址，并将Ip配置到项目中

   ```sh
   docker inspect  blog_mysql
   #....
   "Networks": {
       "bridge": {
           "IPAMConfig": null,
           "Links": null,
           "Aliases": null,
           "NetworkID": "228ae7ab742a0a93926f506f1287eca0244c4b36de5639ecc8c7e9a69dfbecc3",
           "EndpointID": "1982adbb8d341bdd2ec943fcdada4da4cfc837a8f10e5dd93d4fc53fa4cf9a49",
           "Gateway": "172.17.0.1",
           "IPAddress": "172.17.0.2",
           "IPPrefixLen": 16,
           "IPv6Gateway": "",
           "GlobalIPv6Address": "",
           "GlobalIPv6PrefixLen": 0,
           "MacAddress": "02:42:ac:11:00:02",
           "DriverOpts": null
       }
   }
   #....
   ```

5. 在项目中新建`application-prod.properties`，在其中配置生产环境的参数

   ```properties
   spring.datasource.url=jdbc:mysql://172.17.0.2:3307/blog?useUnicode=true&characterEncoding=UTF-8\
     &serverTimeZone=UTC
   ```
   

### 4. 安装redis

```sh
 docker run -id --name=redis -p 6379:6379 redis:5.0.3
```

查看redis对应的docker容器Ip地址，并将Ip配置到项目中

```sh
docker inspect  redis
#.........
"Networks": {
    "bridge": {
        "IPAMConfig": null,
        "Links": null,
        "Aliases": null,
        "NetworkID": "228ae7ab742a0a93926f506f1287eca0244c4b36de5639ecc8c7e9a69dfbecc3",
        "EndpointID": "ca72f5a5974f194e78c1a3f1abdb2d9053be42160db474c939ec8eca490fe09b",
        "Gateway": "172.17.0.1",
        "IPAddress": "172.17.0.3",
        "IPPrefixLen": 16,
        "IPv6Gateway": "",
        "GlobalIPv6Address": "",
        "GlobalIPv6PrefixLen": 0,
        "MacAddress": "02:42:ac:11:00:03",
        "DriverOpts": null
    }
}
```

### 5. Dockerfile SpringBoot 程序

* Dockerfile 是一个文本文件
* 包含了一条条的指令
* 每一条指令构建一层，基于基础镜像，最终构建出一个新的镜像
* 对于开发人员：可以为开发团队提供一个完全一致的开发环境
* 对于测试人员：可以直接拿开发时所构建的镜像或者通过Dockerfile文件
  构建一个新的镜像开始工作了
* 对于运维人员：在部署时，可以实现应用的无缝移植

1. 定义dockerfile，发布springboot项目

   **实现步骤** 

   ​     ① 定义父镜像：FROM java:8 

   ​     ② 定义作者信息：MAINTAINER F4N <demo@f4nblog.com>

   ​     ③ 将jar包添加到容器： ADD ./blog.jar /app.jar

   ​     ④ 定义容器启动执行的命令：CMD java –jar app.jar 

   ​     ⑤ 通过dockerfile构建镜像：docker bulid –f dockerfile文件路径 –t 镜像名称:版本

   ~~~shell
   FROM java:8
   MAINTAINER F4N <demo@f4nblog.com>
   ADD ./blog.jar /app.jar
   CMD java -jar /app.jar --spring.profiles.active=prod
   ~~~

2. 将项目打包，由于配置了多环境，打包要注意

   ![image-20220303110220546](asset/F4NBlog%20Project%20Deployment%20Instructions.assets/image-20220303110220546.png)

3. 打包完成，将打好的Jar包移动到服务器自己创建的app目录下。

4. 然后改名为dockerfile里定义的文件名

   ```sh
   mv blog-1.0-SNAPSHOT.jar blog.jar
   ```

5. 构建对应的容器

   ```sh
   [root@VM-4-9-centos app]# docker build -f ./blog-dockerfile -t app .
   Sending build context to Docker daemon  42.94MB
   Step 1/4 : FROM java:8
    ---> d23bdf5b1b1b
   Step 2/4 : MAINTAINER F4N <demo@f4nblog.com>
    ---> Running in 19e38afeab0d
   Removing intermediate container 19e38afeab0d
    ---> cee9db1c9a45
   Step 3/4 : ADD ./blog.jar /app.jar
    ---> 0c12ad45015b
   Step 4/4 : CMD java -jar /app.jar --spring.profiles.active=prod
    ---> Running in 7fda396dbfeb
   Removing intermediate container 7fda396dbfeb
    ---> 545d2632d73d
   Successfully built 545d2632d73d
   Successfully tagged app:latest
   ```

6. 查看创建的镜像

   ```sh
   [root@VM-4-9-centos app]# docker images
   REPOSITORY    TAG       IMAGE ID       CREATED         SIZE
   app           latest    545d2632d73d   3 minutes ago   686MB
   nginx         latest    c919045c4c2b   37 hours ago    142MB
   mysql         5.7       ae552624b4bd   37 hours ago    448MB
   hello-world   latest    d1165f221234   12 months ago   13.3kB
   redis         5.0.3     0f88f9be5839   2 years ago     95MB
   java          8         d23bdf5b1b1b   5 years ago     643MB
   ```

### 6. 服务编排

1. 安装Docker Compose

   ```sh
   # Compose目前已经完全支持Linux、Mac OS和Windows，在我们安装Compose之前，需要先安装Docker。下面我 们以编译好的二进制包方式安装在Linux系统中。 
   curl -L https://github.com/docker/compose/releases/download/1.22.0/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
   # 设置文件可执行权限 
   chmod +x /usr/local/bin/docker-compose
   # 查看版本信息 
   docker-compose -version
   ```

2. 创建docker-compose目录并打开

   ```sh
   mkdir /blog/docker/docker-compose
   cd /blog/docker/docker-compose
   ```

3. 在docker-compose目录先创建nginx目录和docker-compose的配置文件.

   ```sh
   mkdir -p /blog/docker/docker-compose/nginx
   ```

   ```sh
   vim docker-compose.yml
   ```

   ```sh
   version: '3'
   services:
     nginx:
      image: nginx
      container_name: nginx
      ports:
       - 80:80
       - 443:443
      links:
      	- app
      depends_on:
       - app
      volumes:
       - /mnt/docker/docker-compose/nginx/:/etc/nginx/
       - /mnt/mszlu/web:/mszlu/web
       - /mnt/mszlu/blog:/mszlu/blog
      network_mode: "bridge"
     app:
       image: app
       container_name: app
       expose:
         - "8888"
       network_mode: "bridge"
   ```

   

4. 在./nginx目录下 编写nginx.conf文件
