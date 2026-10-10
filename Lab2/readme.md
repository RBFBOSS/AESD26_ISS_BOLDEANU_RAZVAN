So far I only made the compulsory part of this lab.

## Requirements

-   Java 17
-   Maven (or the Maven Wrapper included with the project)
-   IntelliJ IDEA or another Java IDE

## How to Run

1.  Open the project in IntelliJ IDEA.

2.  Wait for Maven to import the dependencies from `pom.xml`.

3.  Run `DemoApplication.main()`

4.  The application starts on port **8081**, as configured in
    `application.properties`.

## Endpoints

  ------------------------ ----------------------- -----------------------------------------
`GET /hello`             Returns the greeting    `http://localhost:8081/hello`
configured by           
`app.greeting`.

`GET /actuator`          Lists the Actuator      `http://localhost:8081/actuator`
endpoints available     
over HTTP.

`GET /actuator/health`   Reports application     `http://localhost:8081/actuator/health`
health.

`GET /actuator/info`     Returns application     `http://localhost:8081/actuator/info`
information, if         
configured.
  ------------------------------------------------------------------------------------------

The configured greeting is `Hello!`, so opening
`http://localhost:8081/hello` should return:

``` text
Hello!
```