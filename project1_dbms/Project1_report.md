# CS213 Database Project1

- Student Name: 王子恒

- Student ID: 12310401

## 引言

### 背景查询

通过查阅教材：

>**文件处理系统(file-processing system)** 是传统的操作系统所支持的。永久记录被存储在多个不同的文件中，人们编写不同的应用程序来将记录从有关文件中取出或加入到适当的文件中。在数据库管理系统（DBMS）出现以前，各个组织通常都采用这样的系统来存储信息。
>
>在文件处理系统中存储组织信息的主要弊端包括：
>
>数据的冗余和不一致；数据访问困难；数据孤立；完整性问题；原子性问题；并发访问异常；安全性问题；
>
>--引自《数据库系统概念（原书第六版）》[^1]

### project目的

比较文件系统和PostgreSQL在数据检索、更新等操作上的性能差异。

### 研究问题

1. 不同存储机制对性能的影响
2. 数据库使用的数据结构的作用

### Report结构

报告对于两个不同的数据源分别探讨较小数据集单个表和较大数据集关系型数据库的性能差异，分别进行了I/O和JDBC的对比。

将从数据库设计，数据导入，IO与DBMS功能，程序实现，性能测试，结果分析与机制对比，六个方面展开。

## 数据源

首先在和鲸社区上找到一个较小的（12M）数据集[【B站每周必看】截至最新237期数据](https://www.heywhale.com/mw/dataset/6523c441b0f4556a312692c1)[^2]。后来发现数据量偏小，对于性能测试差距不明显，又使用了一个较大的数据集[b站user和user制作的短视频数据](https://www.heywhale.com/mw/dataset/6347b53efcd8fca1f7dfba16/content)[^3],而且因为含关注关系，可以探究关系型数据库的相关性能。


## B站每周必看数据集（较小数据单表的I/O与JDBC对比）

### 数据库设计

由于这次只探究数据库和文件系统的性能差异，所以数据库设计较为简单，只有一个表`videos`，包含以下字段(对应csv的heads)：

- term 期数
- duration 期数描述
- title 标题
- part 视频类型
- description 视频标签
- url 视频链接
- last_time 视频时长
- author up主
- mid up主_id
- aid aid（视频id）
- coins 投币数
- danmus 弹幕数
- favorites 收藏数
- likes 点赞数
- comments 评论数
- shares 分享数
- plays 播放数
- date 发布时间

主键选择，由于可能出现同一个视频在不同期数被推荐，所以选择`term`和`aid`作为联合主键。

### 数据导入

实现并探究四种方式的数据导入：

1. PostgreSQL数据库：
   1. 使用sql `INSERT`语句逐条插入。
   2. 使用sql `COPY`语句批量插入。
   3. 使用JAVA JDBC批量插入。
2. IO文件系统：
   1. 使用JAVA读取文件逐条插入。

### IO与DBMS功能

仅为了测试DBMS的性能，实现了几个基础功能：

1. 建表`public void createtable()`(JDBC)
2. 导入数据`public void videos_reader()`
3. 插入数据`public boolean ref_video`
4. 删除数据`public boolean delete_video`
5. 查询数据
   1. `public boolean select_author` 根据作者查询
   2. `public boolean select_aid` 根据视频aid查询
6. 更新数据`public boolean update_video`

### 程序实现

#### 运行环境

>本来是在windows下使用DataGrip进行开发，但是由于其的高内存占用（也可能是一年没用DataGrip了，电脑莫名跑不动，但是不想重装了）尝试了几次都是，导入仅仅8000行，18字段的数据就使用了1.5h左右的时间，而在Cmd下使用`psql -i`运行同样的sql文件只需要几秒，为了保证性能测试不被DataGrip影响，最后更换到Mac OS下直接使用zsh和vscode进行开发。

- System:MacOS 15.0 (24A335)
- Chip: Apple M1
- Total Number of Cores: 8 (4 performance and 4 efficiency)
- Memory: 8 GB

#### 数据预处理

使用Java的[commons-csv](https://commons.apache.org/proper/commons-csv/apidocs/org/apache/commons/csv/package-summary.html)[^4]库读取csv文件，将数据以`INSERT`语句的形式写入`sqlscript.sql`文件。

值得一提的是，使用commons-csv库读取csv文件时，就不用再对字段中存在的特殊符号再进行处理，比如`'`，`"`等，否则使用正则表达式处理会十分麻烦。

#### psql下建表及导入数据

1. 逐条插入
    `sqlscript.sql`文件中的数据以`INSERT`语句的形式插入。

    ```sql
    BEGIN TRANSACTION;
    INSERT INTO videos (term, duration, title, part, url, last_time, author, mid, aid, coins, danmus, favorites, likes, comments, shares, plays, date) VALUES (1, '2019第1期 03.22 - 03.28', 'the real suger baby已知最高画质', '音乐综合', 'https://b23.tv/BV16b411b71j', 29, '一嗷垚', 30194033, 45031807, 240890, 21841, 703355, 772538, 19575, 103901, 29236360, '2019-03-01 18:48:18.0');
    ...--8103行
    COMMIT;
    ```

    在zsh下：

    ```bash
    psql -d project1
    project1=#
        VACUUM; #清理此前测试时数据库的数据
        \timing; #开启计时
        create table videos(
            term int  not null,
            duration varchar not null ,
            title varchar(100) not null ,
            part varchar(100) not null ,
            description varchar(10000) ,
            url varchar(100) not null ,
            last_time numeric not null,
            author varchar(100) not null,
            mid numeric not null,
            aid numeric not null ,
            coins numeric not null ,
            danmus numeric not null ,
            favorites numeric not null,
            likes numeric not null ,
            comments numeric not null ,
            shares numeric not null ,
            plays numeric not null ,
            date timestamp(0) not null,
            primary key (term,aid)
        );
        \i sqlscript.sql; #导入数据
    ```

2. 批量插入
    postgresql支持使用`COPY`直接从csv文件导入数据。

    ```sql
    COPY videos FROM './B站每周必看数据.csv' DELIMITER ',' CSV HEADER;
    ```

#### DBMS以及IO数据导入

Java项目结构如下：

```none
- Client                    # 客户端main函数
    - Video                 # 数据类
    - DataScipt             # 数据库建立及数据导入
        - DataBaseScript    # PostgreSQL数据库存储
        - IOFileSCript      # JSON文件存储
    - DataManner            # 操作实现接口
        - DBManner          # DBMS操作实现
        - FileManner        # 文件操作实现
```

代码说明：

1. `Video`类：数据类，包含了视频的所有字段。
2. `DataScipt`：
   1. 类`DataBaseScript`
        - 使用postgresql.42.2.5的`Connection`连接数据库，并在其中实现了建表和数据导入。使用[commons-csv](https://commons.apache.org/proper/commons-csv/apidocs/org/apache/commons/csv/package-summary.html)[^4]库读取csv文件，数据导入使用**关闭自动提交**的`PreparedStatement`，并使用批处理在插入**1000**条数据后提交一次。
   2. 类`IOFileScript`
        - 使用BufferReader读取csv文件并使用Google的[Gson](https://github.com/google/gson)[^5]库将数据写入json文件。
3. `DataManner`：
   1. 接口`DataManner`：定义了数据库操作的接口。
   2. 类`DBManner`：实现了`DataManner`接口，使用`Connection`连接数据库，实现了建表，插入，删除，查询，更新等操作。
   3. 类`FileManner`：实现了`DataManner`接口，使用`BufferedReader`读取csv文件，实现了插入，删除，查询，更新等操作。

详细代码见附件或[github]()。

### 性能测试

在Client.java中分别指定`String manipulator="DataBaseScript";`, `String manipulator="IOFileScript";`，得到JDBC和I/O运行结果，以运行时间作为衡量标准。

#### JDBC

![alt text](Project1_report/image-2.png)

#### I/O

![alt text](Project1_report/image.png)

## B站user和user制作的短视频数据（较大数据关系型数据库与文件系统对比）

其实两个csv文件可以由视频和up主建立更复杂的关系型数据库，但是这里只为了对比性能，所以只用user.csv建立了两个表user和follows，并用up主的mid作为外键进行联系。

由于在使用过程中发现*b站user和user制作的短视频数据*数据集中的数据结构前后不一致，且关系无法自洽，想要建立外键约束时发现数据不一致，所以模仿该数据集结构，使用python脚本使用随机函数造了一个2000000行，1.7G的数据集。

### 数据集生成

- mid使用1000000-3000000的所有不重复整数，并且打乱顺序。
- following_list使用随机函数生成，每个用户关注的人数在1-200之间，关注的人的mid在1000000-3000000之间。确保外键约束。

详细代码见附件或github。

### 数据库设计

数据分析：user.csv内的字段有"mid",,"level","name","official_role","official_title","rank","sign","following_list"。

user表设计：

```sql
    mid numeric primary key,
    is_followed int not null,
    level int not null,
    name varchar(100),
    official_role int not null,
    official_title varchar(100),
    rank int not null,
    sign varchar(100),
    following int not null,
    follower int not null
```

follows表设计：

```sql
    following numeric,
    follower numeric,
    primary key (follower,following),
    foreign key (follower) references user(mid),
    foreign key (following) references user(mid)
```

### IO与DBMS功能

仅为了测试DBMS的性能，实现了几个基础功能：

1. 建表`public void createtable()`(JDBC)
2. 导入数据`public void users_reader()`
3. 插入数据
   1. `public boolean sign_user`
   2. `public boolean follow_user`
4. 删除数据
   1. `public boolean logout_user`
   2. `public boolean unfollow_user`
5. 查询数据
   1. `public boolean select_mid` 根据作者查询
   2. `public boolean select_follower` 查询关注当前用户的用户
   3. `public boolean select_following` 查询当前用户关注的用户
6. 更新数据`public boolean update_user`

其他操作同上

JDBC运行结果图
![alt text](Project1_report/image-4.png)
![alt text](Project1_report/image-5.png)
![alt text](Project1_report/image-6.png)
![alt text](Project1_report/image-7.png)
![alt text](Project1_report/image-8.png)
![alt text](Project1_report/image-9.png)

I/O运行结果图
![alt text](Project1_report/image-10.png)
![alt text](Project1_report/image-11.png)

## 结果分析

### 单表I/O与JDBC对比，小型数据

|操作|JDBC(ms)|I/O(ms)|
|-|-|-|
|存储数据|324|276|
|查询(非主键)|11|78|
|查询(主键)|5|40|
|插入|2|205|
|更新|4|106|
|删除|0|106|
|占用空间|10 MB|5.2 MB|

### 关系型数据库与文件系统对比，大型数据，两表

|操作|JDBC(ms)|I/O(ms)|
|-|-|-|
|存储数据|33108+2562193=2595301|14971|
|查询mid|28|33389|
|查询following|10851|13432|
|查询follower|10986|19883|
|插入user|17|87353|
|插入follows|1|42764|
|删除关注|7681|36671|
|更新|12|34991|
|删除用户|15772|38959|
|占用空间|4502 MB|1.24 GB|

## 结果分析与机制对比

1. 存储时间：当数据量较小时，I/O的存储时间较短，但是差距不明显，当数据量较大时，DBMS的存储时间远远大于I/O。这是因为在DBMS插入数据时，除了进行数据的转化与存储，还要进行的操作有
   1. 日志记录
   2. 事务管理，在事务提交前，数据会被存储在内存中，而不是直接写入磁盘，这样可以减少磁盘I/O次数，提高性能。虽然我的代码中已经使用了5000条(small)，200000条(big)插入语句一次进行一次事务提交，但是还是会导致比I/O多处管理时间。
   3. 索引更新。小表的插入，大表插入user表，它们和I/O操作都是进行了一次完整的对数据csv文件的读取，而对于索引（在这里只有主键）的更新是两者时间差异的主要来源之一。进一步查看`pg_indexes`，postgresql主键索引是使用`btree`索引。我在《数据库系统概念》的11.3详细阅读了有关`btree`，一些笔记记录在后文中。
        ![alt text](Project1_report/image-12.png)
   4. 约束检查，DBMS需要检查数据的完整性，外键约束，唯一性约束等，我的表中大部分字段含有非空约束，这也是导致DBMS插入时间远远大于I/O的原因之一。
   5. 外键约束的检查。可以发现，在大型数据集的follows表进行插入语句时，DBMS所需要的时间是user的近80倍，I/O的近160倍，这是因为follows表的两个字段都有外键约束，导致了大量的检查操作。
   6. 锁管理，数据库在插入操作时需要锁定相关的表或行，以防止并发操作导致数据的不一致或冲突。
   ***除了上述原因，经过查阅教材和google，还有一些操作是DBMS在存储数据时需要进行，并可能导致更多时间消耗的，比如：***

   7. trigger的执行
   8. 分区和分布式存储，在我的电脑上并不存在分布式系统，但对于分布式存储(DM-MIMD/SIMD)的超算或者服务器来说，数据库可以决定将数据存储到特定的分区或者节点。而各种网络协议、消息队列、以及IB交换机的配置等都会影响数据库的存储时间。使用单个文件I/O的方式，显然不需要考虑这些问题，但也会受限于单一硬件设备的读写速度。所以分布式存储也能够在其他性能上为DB带来显著的提升。$_{(TODO:超算比赛中对于这一块有很多值得探索的地方，可以写在后面或者blog里。)}$
   9. 

## 参考资料

[^1]: 《数据库系统概念（原书第六版）》，Abraham Silberschatz; Henry F. Korth; S. Sudarshan，杨冬青,李红燕,唐世渭等译，2-3页。

[^2]: Blues 🐋🐋. 【B站每周必看】截至最新237期数据. 2023. [https://www.heywhale.com/mw/dataset/6523c441b0f4556a312692c1](https://www.heywhale.com/mw/dataset/6523c441b0f4556a312692c1).

[^3]: LXRee. b站user和user制作的短视频数据. 2022. [https://www.heywhale.com/mw/dataset/6347b53efcd8fca1f7dfba16](https://www.heywhale.com/mw/dataset/6347b53efcd8fca1f7dfba16).

[^4]: Apache Commons CSV. [https://commons.apache.org/proper/commons-csv/user-guide.html](https://commons.apache.org/proper/commons-csv/user-guide.html).
[^5] Google Gson [https://github.com/google/gson](https://github.com/google/gson).
