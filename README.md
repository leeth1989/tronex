# TronEx
A explorer service for tron(https://github.com/tronprotocol/java-tron), which is an interesting blockchain project.  

TronEx is developed in Kotlin, which is also a JVM language, it should be very easy to integrate and interact with java-tron.

It's not finished yet. There are many works to do:

* Test cases
* Documents
* More tron protocol to implement
  * Account query
  * Transaction query
  * Statistics
  * ...
* More interfaces
* Better project structure
* Better UI

TronEx have no UI at this stage. It should be deployed with my other project tronex-ui, which is a nodejs project. Of course you
can change tronex-ui to any blockchain explorer UI. To integrate, simply try the REST API from TronEx controllers (in directory `src/main/kotlin/tronex/controllers`).

Demo site is deployed at (http://47.98.58.42:8000). NOT production ready!!
