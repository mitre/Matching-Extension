# Market Simulator

A Java implementation of a market simulator using the spring-boot framework and NetLogo for agent-based modeling.


#### Requires
 - NetLogo 6.1.1 is installed and runnable on local machine.


### NetLogo Installation
 1. Run the `mvn clean compile assembly:single` maven goal to build the jar with all dependencies attached.
 2. The fully built jar should be located in `./target/matching-jar-with-dependencies.jar`
 3. Copy jar to your NetLogo installation directory. An example would be: `cp ./target/matching-jar-with-dependencies.jar /Applications/NetLogo 6.1.1/extensions/matching/matching.jar`
 4. Now you should be able to run the extension `matching` from NetLogo. It appears as a Conflicting Library under Tools > Extensions
 5. The NetLogo code should include: `extensions [matching]`


#### Documentation
Documentation can be found on MITREpedia at this link: [Market simulator](https://mitrepedia.mitre.org/index.php/Market_simulator).


#### Useful Maven goals
* `mvn clean install` - run test cases and install the jar locally
* `mvn test` - run test cases only
* `mvn spring-boot:run` - run spring-boot application locally
* `mvn spring-boot:build-image` - build docker image of spring-boot application
* `mvn clean compile assembly:single` - build combined jar for NetLogo extension


#### Sample NetLogo Code
```
extensions [matching]

to setup
  clear-all
  reset-ticks
end

to go
  ; corresponds to java unit test MatchingEngineTest.testMatchUpdater()
  let t matching:create-default []
  let tmp matching:add-bid t 3 0.345 "GC1!" "sam"
  let tmp matching:add-bid t 5 0.341 "GC1!" "fred"
  let tmp matching:add-offer t 4 0.349 "GC1!" "bob"
  let tmp matching:add-offer t 2 0.347 "GC1!" "sam"
  let tmp matching:add-bid t 5 0.344 "GC1!" "mike"
  let tmp matching:match-update t

  let t matching:create-default []
  let tmp matching:add-offer t 3 0.348 "GC1!" "sam"
  let tmp matching:add-offer t 5 0.349 "GC1!" "fred"
  let tmp matching:add-bid t 4 0.345 "GC1!" "bob"
  let tmp matching:add-bid t 3 0.347 "GC1!" "sam"
  let tmp matching:add-offer t 5 0.354 "GC1!" "mike"
  let tmp matching:match-update t

  tick
end
```