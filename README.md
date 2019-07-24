# FOR-IDENT Platform
The FOR-IDENT project is a research project sponsored by the german Federal Ministry of Education and Research
with the purpose to improve the identification of organic trace substances by merging resources and standardization
of suspected- and non-target analysis. This GitHub project publishes the result of software development implemented
by the University of Applied Sciences Weihenstephan-Triesdorf. This software is a Java web application based on
the Spring Framework and Vaadin. 

https://www.for-ident.org (Official project website)

https://water.for-ident.org (Official production instance)

## Getting Started
To get a copy of the project clone this GitHub project. See deployment for notes on how to deploy the project on a live system.

### Prerequisites
* **Java 1.8 or later**
* **Java Agent : Spring Instrument 4.2.9+** (when running as JAR see deployment)
* **Gradle 4+**
* **Database (PostgreSQL recommended)**  

### Installing
Build the project
```
gradle clean build
```
Configure the software properties for each profile in the property files found in **application/src/main/resources/config**

Define at least 1 user management and 1 compound database connection through the spring database property.
```
spring.datasource.user
```
```
spring.datasource.*yourDatabaseName*
```
Example compound database implementations can be found in modules :
* **search.service.search.stoffident**
* **search.service.search.plantident**

The compound data must be provided!

Spring profile **initialization** creates an admin user on startup for initial log in.
```
Username: admin
Password: 12345
```
Run the application (development mode)
```
gradle bootRun -x test
```

## Deployment
Build JAR
```
gradle clean build
```
The executable application jar will be build in **application/build/libs/de.hswt.fi.application.jar**

Run JAR
```
java -javaagent:*PATH_TO_SPRING_INSTRUMENTS* -jar *PATH_TO_JAR* --spring.profiles.active=*DESIRED_SPRING_PROFILE* --server.port=*PORT*
```

## Running the tests
Run tests
```
gradle test
```
The test consists of unit tests for the importers, services, etc. and some integration and api tests.

## Built With
* [Spring Boot](https://spring.io/projects/spring-boot) - Java framework
* [Vaadin](https://www.vaadin.com) - The UI framework
* [Gradle](https://gradle.org) - Build Tool

## Authors
* **Marco Luthardt**
* **Tobias Placht**
* **August Gilg**

## License
This project is licensed under the GNU Lesser General Public License **LGPL v2.1** - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

### Special thanks go to 

Our sponsor: 
* **Federal Ministry of Education and Research**

Our partners :
* **Technical University of Munich**
* **Bavarian State Ministry of the Environment**
* **Zweckverband Landeswasserversorgung (state water supply)**
* **Berliner Wasserbetriebe (water supply)**

Collaborators during the project :
* **Environmental Protection Agency**
* **Metfrag**
* **Chemaxon**
* **Agilent, Bruker, Sciex, Thermo Fischer, Waters**
