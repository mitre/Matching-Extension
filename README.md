# Market Simulator

A Java implementation of a market simulator using the spring-boot framework and NetLogo for agent-based modeling.


#### Requires
 - NetLogo 6.1.1 is installed and runnable on local machine.


### NetLogo Installation
 1. Run the `mvn clean compile assembly:single` maven goal to build the jar with all dependencies attached.
 2. The fully built jar should be located in `./target/market-simulator-SNAPSHOT.jar`
 3. Copy jar to your NetLogo installation directory. An example would be: `cp ./target/market-simulator.jar /Applications/NetLogo 6.1.1/extensions/msim/msim.jar`
 4. Now you should be able to run the extension `msim` from NetLogo. It appears as a Conflicting Library under Tools > Extensions
 5. The NetLogo code should include: `extensions [msim]`


#### Documentation
Documentation can be found on MITREpedia at this link: [Market simulator](https://mitrepedia.mitre.org/index.php/Market_simulator).


#### Useful Maven goals
* `mvn clean install` - run test cases and install the jar locally
* `mvn test` - run test cases only
* `mvn spring-boot:run` - run spring-boot application locally
* `mvn spring-boot:build-image` - build docker image of spring-boot application
* `mvn clean compile assembly:single` - build combined jar for NetLogo extension