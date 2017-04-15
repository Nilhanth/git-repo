package cafe

import cafe.CafeConstants._

object CafeDemo {

    def main(args: Array[String]) {

        def myCafe: Cafe = new Cafe()

        // Create list of items (specifying Hot/Cold for each item)
        var itemList = List( //
            (COLA, COLD), //
            (COFFEE, HOT), //
            (CHEESE_SANDWICH, COLD), //
            (CHEESE_SANDWICH, HOT), //
            (STEAK_SANDWICH, COLD), //
            (STEAK_SANDWICH, HOT))

        try {
            var total = myCafe.calculateTotal(itemList)
            println("Amount returned from method: " + total)
        } catch {
            case nsee: NoSuchElementException => {
                println("'Exception message returned from method: " + nsee.getMessage + "'")
            }
        }

    }
}
