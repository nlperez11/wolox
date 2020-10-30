# Wolox-challenge

Wolox Challenge application.

# Pre-requisites

 - [Postgresql](https://www.postgresql.org/download/)

  
### Tech

The tecnologies used in this app are:

* [Java v11] - Programming Language base.
* [SpringBoot v2.3.4.RELEASE] - The powerfull java framework for web apps.
* [JPA] - ORM based in Hibernate
* [Gradle v6.6.1] - as Dependency management and app runner
* [Postgresql] - Relational DB
* [Junit] - For Unit testing

### Installation

This app requires [Java](https://nodejs.org/) v11+ to run.
Create database with the same name to properties file. (wolox)
Install the dependencies and start the server.

Windows
```sh
$ cd path/api
$ gradlew bootRun
```

Linux
```sh
$ cd path/api
$ ./gradlew bootRun
```