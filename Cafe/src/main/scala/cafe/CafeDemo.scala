package cafe

import scala.collection.mutable.ListBuffer
import cafe.CafeConstants._

object CafeDemo {

    def main(args: Array[String]) {

        var itemList: ListBuffer[(String, String)] = ListBuffer()

        // Retrieve list of item from arguments (if any)
        // Arguments must be in correct format:
        // e.g. ITEM_NAME_1 ITEM_TYPE_1 ITEM_NAME_2 ITEM_TYPE_2 ... ITEM_NAME_N ITEM_TYPE_N
        if (false == args.isEmpty) {
            var i = 0
            try {
                while (i < args.size) {
                    itemList += ((args(i), args(i + 1)))
                    i = i + 2
                }
            } catch {
                case aioooe: ArrayIndexOutOfBoundsException =>
                    println("ERROR: Invalid number of arguments. Please review and try agin.")
                    println("Correct Format: ITEM_NAME_1 ITEM_TYPE_1 ITEM_NAME_2 ITEM_TYPE_2 ... ITEM_NAME_N ITEM_TYPE_N")
                    System.exit(0)
            }
        } else {
            // If no arguments, then create sample list of items (specifying Hot/Cold for each item)
            itemList += ((COLA, COLD))
            itemList += ((COFFEE, HOT))
            itemList += ((CHEESE_SANDWICH, COLD))
            itemList += ((CHEESE_SANDWICH, HOT))
            itemList += ((STEAK_SANDWICH, COLD))
            itemList += ((STEAK_SANDWICH, HOT))
        }

        def myCafe: Cafe = new Cafe()

        try {
            var total = myCafe.calculateTotal(itemList.toList)
            println("Amount returned from method: " + total)
        } catch {
            case nsee: NoSuchElementException => {
                println("'Exception message returned from method: " + nsee.getMessage + "'")
            }
        }

    }
}
