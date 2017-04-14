package cafe

import java.util.NoSuchElementException

class Cafe {

    // Create a map with products and their prices
    var products = scala.collection.mutable.Map[String, BigDecimal]()
    products += ("Cola" -> BigDecimal("0.50"))
    products += ("Coffee" -> BigDecimal("1.00"))
    products += ("Cheese Sandwich" -> BigDecimal("2.00"))
    products += ("Steak Sandwich" -> BigDecimal("4.50"))

    /**
     * Calculates the total price of all the items in a given list
     *
     * @param items - List of items
     * @return The total price of all items in List
     * @throws NoSuchElementException if any items cannot be found
     */
    @throws(classOf[NoSuchElementException])
    def calculateTotal(items: List[String]): BigDecimal = {

        var total = BigDecimal("0.00")

        println("Calculating total for items:")

        for (item: String <- items) {
            try {
                print("Item: '" + item + "' => ")
                total = total.+(products(item))
                println(" Price: " + products(item))
            } catch {
                case nsee: NoSuchElementException => {
                    println("NOT FOUND! Error")
                    throw nsee
                }
            }
        }

        return total
    }
}
