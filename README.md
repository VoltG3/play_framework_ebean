# Play JAVA EBean - tutorial
Linux / IntelliJ IDEA / Java Play Frameworks / SBT / Postgress / Crud

This is an example Play application that uses Java and communicates with a postgres database and also uses H2 inMemory database for test purposes via EBean

Step by step will look at how to add a full chapter and test it

### Original sources
>>#### play-java-ebean-example
>>The GitHub location for this project is inside:
>><https://github.com/playframework/play-samples>
>>
>>#### Play
>>Play documentation is here:
>>[https://playframework.com/documentation/latest/Home](https://playframework.com/documentation/latest/Home)
>>
>>#### EBean
>>EBean is a Java ORM library that uses SQL:
>>[https://www.playframework.com/documentation/latest/JavaEbean](https://www.playframework.com/documentation/latest/JavaEbean)
>>and the documentation can be found here:
>>[https://ebean-orm.github.io/](https://ebean-orm.github.io/)

### Preconditions
- Linux https://ubuntu.com/
- IntelliJ IDEA https://www.jetbrains.com/idea/
- SdkMan (java OpenJDK 11.0.14-zulu) https://sdkman.io/
- Docker (PostgreSQL) https://docs.docker.com/engine/install/ubuntu/
- Postman https://www.postman.com/downloads/
- DBeaver https://dbeaver.io/
- SBT https://www.scala-sbt.org/1.x/docs/Installing-sbt-on-Linux.html

### Runs the app
Type in terminal to start application
```
sbt run
```

Open ```http://localhost:9000``` to view it in your browser.

### Versions:
| Tags         | Operation | Changes steps                                                      |
| :----------- | :-------: | :----------------------------------------------------------- |
| [ebean-v1.0.0](https://github.com/VoltG3/ebean/commit/3b4f2534404a0a523e784d85014946f012d11411) | Onload    | [origin source](#ebean-v100)                                               |
| [ebean-v1.0.1](https://github.com/VoltG3/ebean/commit/7b3f97d1d1991ac7b4a79b357b1abc957536222e) | Updated   | [dependencies to access postgress](#ebean-v101)                             |
| [ebean-v1.0.2](https://github.com/VoltG3/ebean/commit/63811c1bf2b5502465894b74ef086b050708a51c) | Added     | [chapter Department: 3.sql 4.sql](#ebean-v102)                              |
| [ebean-v1.0.3](https://github.com/VoltG3/ebean/commit/d5384c20df878d6b15724977786e6d965927278a) | Added     | [chapter Department: models/repository/controller/view/routes](#ebean-v103) |
| [ebean-v1.0.4](https://github.com/VoltG3/ebean/commit/6da5b207771c73c32f4c13e31bb6b473aa16cf2d) | Added     | [chapter Department: CRUD](#ebean-v104)                                     |
| [ebean-v1.0.5](https://github.com/VoltG3/ebean/commit/6d9babf4f6891e0008657fea9a4d820902ce7340) | Added     | [chapter Department: button and header](#ebean-v105)                        |
| [ebean-v1.0.6](https://github.com/VoltG3/ebean/commit/a95fc2206875bb21237b304469a5e0b1d0ed9d8f) | Updated   | [postgresql auto-increment dll](#ebean-v106)                                |
| [ebean-v1.0.7](https://github.com/VoltG3/ebean/commit/3f645fc00efde415c267277da1dcdc913f27719d) | Added     | [h2 test mode](#ebean-v107)                                                 |
| [ebean-v1.0.8](https://github.com/VoltG3/ebean/commit/32a34e508a9f5cb7b750ca52121dd2c3824aae28) | Added     | [chapter Department: Model&Functional test](#ebean-v108) |

# ebean-v1.0.0
- Original source - https://github.com/playframework/play-samples/tree/2.8.x/play-java-ebean-example

  ![step0](https://github.com/VoltG3/doc/blob/master/readme_img/play_frameworks_ebean/step0.png)


# ebean-v1.0.1
- added .gitignore
- added Postgresql Driver  
  https://jdbc.postgresql.org/download.html
- added dependencies to access postgress  
  Access by default can be changed inside: <code>&lt;DIR&gt; conf/application.conf</code>

  <pre>
     db.default.driver=org.postgresql.Driver  
     db.default.url="jdbc:postgresql://localhost:5432/temp0"  
     db.default.username="postgres"  
     db.default.password="postgres"  
  </pre>

- added Evolution autoApplay

  ![step1](https://github.com/VoltG3/doc/blob/master/readme_img/play_frameworks_ebean/step1.png)

- create database before executing this app!

# ebean-v1.0.2
- Added sql scripts for chapter department
- The table department should appear in the database

# ebean-v1.0.3
- Added chapter Department: models/repository/controller/view/routes
- Routes to department table URL:

  <pre>
    http<span>://</span>localhost:9000/department
  </pre>

  ![step3](https://github.com/VoltG3/doc/blob/master/readme_img/play_frameworks_ebean/step3.png)

# ebean-v1.0.4
- Added chapter Department: CRUD

  ![step4a](https://github.com/VoltG3/doc/blob/master/readme_img/play_frameworks_ebean/step4a.png)

- Routes to department table URL:

  <pre>
    http<span>://</span>localhost:9000/department
  </pre>

# ebean-v1.0.5
- Added chapter Department: button/header/message to table header with count of founded items

  ![step5](https://github.com/VoltG3/doc/blob/master/readme_img/play_frameworks_ebean/step5.png)

# ebean-v1.0.6
- Updated postgresql auto-increment dll to fix id sequently

  ![step6b](https://github.com/VoltG3/doc/blob/master/readme_img/play_frameworks_ebean/step6b.png)

# ebean-v1.0.7
- Added H2 inMemory database dependencies to execute sql scripts for test purpose. To execute it:
  <pre>sbt test</pre>

- Tips! Run test mode in background
  <pre>sbt ~test</pre>

# ebean-v1.0.8
- Added chapter Department: Model&Functional test
