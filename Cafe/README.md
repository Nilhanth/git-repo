# GitHub README

### Project Name: "Developer Exercise - Cafe"
Customers don’t know how much to tip and staff need tips to survive! This project produces a total bill and calculates a service charge when passing in a list of purchased items.
#####Assumptions:
* When rounding the service charge to 2 decimal places, the staff want it applied to both the 10% and 20% scenarios, even though it is only mentioned in the 10% scenario bullet point in the requirements.
* When applying the maximum limit of £20 to the service charge, the staff want it applied to both the 10% and 20% scenarios, even though it is only mentioned in the 20% scenario bullet point in the requirements.
* Whilst certain product items are listed as either Hot **or** Cold, they may need to offer both Hot **and** Cold versions of the same product to their customers (e.g. someone may prefer to buy a **Hot** Cheese Sandwich or a **Cold** Steak Sandwich).


### Installation
#####Required Software:
* Scala 2.12.0
* SBT 0.13.15
* Eclipse (recommended)

### Usage
#####Compile
1. Open a command prompt at the root of the "Cafe" directory, and run the command "sbt"
1. Use "compile" command to build the project

#####Run
1. See main Demo object **CafeDemo** to see how **Cafe** class is used when passing in a list of items.
1. Use "run" command to execute the main Demo object

#####Test
1. See test class **CafeTest** to see how **Cafe** class methods are tested.
1. Use "test" command to execute the test cases
