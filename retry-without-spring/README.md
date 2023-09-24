# Retry without Spring

Retry practice without Spring. [Referenced.](https://www.inflearn.com/course/%EC%9E%A5%EC%95%A0%EC%97%86%EB%8A%94-%EC%84%9C%EB%B9%84%EC%8A%A4-resilience4j-circuitbreaker/dashboard)


<br/><br/><br/><br/>

## Getting Started

> You should install jdk 17 or higher. <br/>

<br/><br/><br/>

## Run Build

````text
$ ./gradlew build
````

<br/><br/><br/>

## Result

The program will be terminated after 5 retries.

````shell
ERROR project.resilience4j.retrywithoutspring.Main -- Error, parameter is HELLO
ERROR project.resilience4j.retrywithoutspring.Main -- Error, parameter is HELLO
ERROR project.resilience4j.retrywithoutspring.Main -- Error, parameter is HELLO
ERROR project.resilience4j.retrywithoutspring.Main -- Error, parameter is HELLO
ERROR project.resilience4j.retrywithoutspring.Main -- Error, parameter is HELLO
INFO project.resilience4j.retrywithoutspring.Main -- Request: HELLO
````

<br/><br/>

## Env

&nbsp;&nbsp; - Java 17  <br/>

<br/>
