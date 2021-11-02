# Matching Extension

A Java implementation of a matching market simulator using the spring-boot framework and NetLogo for agent-based modeling.


#### Requires
 - NetLogo 6.1.1 is installed on local machine.


### NetLogo Installation
 1. Run the `mvn clean compile assembly:single` maven goal to build the jar with all dependencies attached.
 2. The fully built jar should be located in `./target/matching-jar-with-dependencies.jar`
 3. Copy jar to your NetLogo installation directory. An example would be: `cp ./target/matching-jar-with-dependencies.jar /Applications/NetLogo 6.1.1/extensions/matching/matching.jar`
 4. Now you should be able to run the extension `matching` from NetLogo. It appears as a Conflicting Library under Tools > Extensions
 5. The NetLogo code should include: `extensions [matching]`


#### Sample NetLogo Usage
```
  // create default matching engine, no arguments
  let t matching:create-default []

  // add bid [matching-engine order_size price contract agent timestamp]
  let tmp matching:add-bid t 3 0.345 "GC1!" "sam" tick

  // add offer [matching-engine order_size price contract agent timestamp]
  let tmp matching:add-offer t 2 0.347 "GC1!" "sam" tick

  // run matching engine [matching-engine]
  let tmp matching:match-update t
```