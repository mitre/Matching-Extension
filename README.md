# Market Simulator

A Java implementation of a market simulator using the spring-boot framework and NetLogo for agent-based modeling.


#### Requires
 - NetLogo 6.1.1 is installed and runnable on local machine.


### NetLogo Installation
 1. Run the `mvn clean compile assembly:single` maven goal to build the jar with all dependencies attached.
 2. The fully built jar should be located in `./target/market-matching-jar-with-dependencies.jar`
 3. Copy jar to your NetLogo installation directory. An example would be: `cp ./target/market-matching-jar-with-dependencies.jar /Applications/NetLogo 6.1.1/extensions/market-matching/market-matching.jar`
 4. Now you should be able to run the extension `market-matching` from NetLogo. It appears as a Conflicting Library under Tools > Extensions
 5. The NetLogo code should include: `extensions [market-matching]`


#### Documentation
Documentation can be found on MITREpedia at this link: [Market simulator](https://mitrepedia.mitre.org/index.php/Market_simulator).


#### Useful Maven goals
* `mvn clean install` - run test cases and install the jar locally
* `mvn test` - run test cases only
* `mvn spring-boot:run` - run spring-boot application locally
* `mvn spring-boot:build-image` - build docker image of spring-boot application
* `mvn clean compile assembly:single` - build combined jar for NetLogo extension


#### Sample NetLogo
```
extensions [matching]

to setup
  clear-all
  reset-ticks
end

to go
  let t matching:create-default []
  print t
end
```