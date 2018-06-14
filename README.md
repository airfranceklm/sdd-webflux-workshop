# LOPI - Lowest Price Finder

This project is used as workshop material during the Schiphol Developers Group on June 14th 2018 @ KLM.
However the workshop can be followed by anybody interested in playing with some features from 
Spring's WebFlux module.

## Introduction

KLM being an airline we based our examples on our domain :), so we created an example that
will stream the lowest prices (actually just a random number in this example) to the client. The example is meant to inspire
developers to look into [Springs WebFlux module](https://docs.spring.io/spring/docs/current/spring-framework-reference/) and play
around with [Reactor](https://projectreactor.io/). That's means that we encourage participants to go beyond the scope of the assigments
and experiment with the features and possibilities provided by this excellent module. 

The data sets that we are using are coming from [https://github.com/jpatokal/openflights](https://github.com/jpatokal/openflights).

If you are part of the workshop and get stuck then ask an instructor for some assistance (or a neighbour @ your table).

We hope you have a great evening and that you get excited about potentially using WebFlux in your future projects :).

The complementary user interface can be found here [workshop-openflight](https://github.com/maapteh/workshop-openflight).   

## Building

- JDK 10+ is required to build this project
- Clone this repository and run ```mvn spring-boot:run```
- Import this into your favorite Java IDE (which must be IntelliJ obviously :), j/k ... no really IntelliJ is amazing! :))
- This project makes use of [Spring Developer Tools](https://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-devtools.html)
which means that (most of the time) you only have to compile your code to force a refresh of your application (i.e. in IntelliJ on a Mac with ```⌘F9```)
Sometimes however it still requires a full restart, so if you get some strange behaviour restart first and try again before you start debugging :).
- The server is configured to run on port 9090, if you wish to run on a different port change the ```server.port``` property in the 
[application.properties](src/main/resources/application.properties) file.
- This starter kit comes with one available endpoint as a reference *example* that will be available on ```http://localhost:9090/airlines```

## Assignments

### 01: Implement the /airports json resource

Implement your own REST endpoint for the airport data set (exposing application/json as a media type), 
try to implement the following requirements:
- Paged listing of resources
- Searching through the data set with error handling (i.e. 404 if no results are found)
- Play with the possibilities that reactor gives you, *for example*:
    - Filter airports based on country
    - Map airports to another entity of choice, i.e. a list of strings containing only the (non-null) IATA codes of the airports.
    
**Please note** that you can use the airline implementation as a reference implementation, the reference implementation is build using
handlers instead of the classical Spring [@RestController](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/RestController.html).
However if you still prefer to build it the classical way then that is off course fine as well!
 
### 02: Implement a test case for your new /airports

Create a test case for your controller using [@WebFluxTest](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html)
testing all the routes that you have created.

The reference implementation has been build using the new functional style of route definitions, if you chose to base
your controller on the classical approach using @RestController then remember to use ```@WebFluxTest(YourController.class)``` instead.

### 03: Implement the /offers/{origin-airport} resource

Now that you implemented your own controller let's dive directly into our main resource which will have some more complicated operations.

We have a few csv sources containing data for airlines, airports, planes and network routes, we will have to combine all of 
them as well as generate a price for each route we find. Every time we finalize processing a single result we push it to our
client using [server sent events](https://developer.mozilla.org/en-US/docs/Web/API/Server-sent_events).

**Small tip :)**: every repository is converted to a hot source and it will emit all the items whenever a new subscriber
comes along. Use this to your advantage if you know that you will have to reuse the reference.

So what you need to implement is the following:
- Retrieve the network routes (for which the repository conveniently is already providing a Flux).
- Some data might be missing so filter your stream by making sure that the following fields are not ```null```:
    - airline id
    - source airport id
    - destination airport id
- Convert the NetworkRoute model to a FlightOffer.Builder so you can use it to populate the missing fields later and 
generate a random price while you are doing it (a random int from 0 -> 1000 will be fine for this sample). 
- for **each destination airport** retrieve the full airport details from the data set using the airport repository.
- for **each offer** retrieve the full airline details from the data set using the airline repository.
- for **each offer that has a plane id** retrieve the full aircraft details using the plane repository.
- Delay each emitted element by 20 milliseconds to mimic a slower backend system.
- Try to run the reactive stream in parallel! tip: it makes a big difference where you define it in the stream.

**BONUS**: Some developers might be used to using [ThreadLocal's](https://docs.oracle.com/javase/10/docs/api/java/lang/ThreadLocal.html).
This unfortunately will not work in a reactive environment, however reactor has a solution for this exact case. Try to 
set the start time as a value somewhere and log the total processing time **when the stream completes**.


### 04: Experiment!

This sample project can be used to experiment with the features provided by Spring & Reactor, so if you have some time
remaining then define what you would like to experiment with and try it out!

## Conclusion

We hope you found this workshop useful and that it has sparked your interest in using Spring WebFlux & Reactor. The solution
to assignment 02 can be found in branch 'feature-flight-offers'.

## Additional resources

- [Project Reactor Samples](https://github.com/reactor/reactor-samples)
- [Spring WebFlux documentation](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html#spring-webflux)
- [Spring's Reactive WebClient](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html#webflux-client)
    - [WebClient configuration in Boot](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-webclient.html) 