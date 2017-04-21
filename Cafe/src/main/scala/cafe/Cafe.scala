package cafe

import java.util.NoSuchElementException
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Map
import scala.math.BigDecimal
import scala.math.BigDecimal.RoundingMode
import cafe.CafeConstants._

class Cafe {

    // Create a multi-key map (to allow both HOT and COLD versions of products)
    implicit class EnhancedMap[A, B, C](m: Map[(A, B), C]) {
        def update(a: A, b: B, c: C) { m((a, b)) = c }
    }

    // Create a map with products and their prices
    val products = Map[(String, String), (String, BigDecimal)](
        ((COLA, COLD) -> (DRINK, BigDecimal("0.50"))),
        ((COFFEE, HOT) -> (DRINK, BigDecimal("1.00"))),
        ((CHEESE_SANDWICH, COLD) -> (FOOD, BigDecimal("2.00"))),
        ((CHEESE_SANDWICH, HOT) -> (FOOD, BigDecimal("2.50"))), // Example of both options ONLY (not in requirements)
        ((STEAK_SANDWICH, COLD) -> (FOOD, BigDecimal("4.00"))), // Example of both options ONLY (not in requirements)
        ((STEAK_SANDWICH, HOT) -> (FOOD, BigDecimal("4.50"))))

    /**
     * Calculates the total price of all the items in a given list
     *
     * @param items - List of items
     * @return The total price of all items in List
     * @throws NoSuchElementException if any items cannot be found
     */
    @throws(classOf[NoSuchElementException])
    def calculateTotal(items: List[(String, String)]): BigDecimal = {

        var total = BigDecimal("0.00")
        var containsFood = false
        var containsHotFood = false
        var purchasedItems: Map[String, (Integer, BigDecimal)] = Map()

        println("============================")
        println("Calculating total for items:")
        println("============================")

        for (item: (String, String) <- items) {
            try {

                var product = products((item._1, item._2))

                // Get price of item
                def itemPrice: BigDecimal = product._2.asInstanceOf[BigDecimal]

                // Add item to list of purchased items
                addPurchasedItemToList(purchasedItems, item._1 + " (" + item._2 + ") @ " + itemPrice, itemPrice)

                // Add price to total                
                total = total.+(itemPrice)

                // If item is food
                if (FOOD.equals(product._1)) {

                    // Then flag as food
                    containsFood = true;

                    // If food is hot
                    if (HOT.equals(item._2)) {
                        // Then flag as hot
                        containsHotFood = true;
                    }
                }

            } catch {
                case nsee: NoSuchElementException => {
                    println("NOT FOUND! Error")
                    throw nsee
                }
            }
        }

        // Print list of purchased items with their quantity and subtotals
        for (purchasedItem: (String, (Integer, BigDecimal)) <- purchasedItems) {
            println(purchasedItem._2._1 + " x " + purchasedItem._1 + " => " + purchasedItem._2._2)
        }

        println("=====\nSubtotal: " + total)

        // Calculate Service Charge
        var serviceCharge = calculateServiceCharge(total, containsFood, containsHotFood)

        // Add Service Charge to total
        total = total.+(serviceCharge)
        println("=====\nTotal amount due: " + total + "\n=====")

        return total
    }

    /**
     * Adds an item to an existing item list and updates it's total quantity.
     * If the item does not exist, it is created added to the list with a quantity of 1.
     * 
     * @param purchasedItemsList - Existing list of items, their quantities and running totals
     * @param purchasedItem - The item being added to the list
     * @param itemPrice - The price of the item being added
     */
    def addPurchasedItemToList(purchasedItemsList: Map[String, (Integer, BigDecimal)], purchasedItem: String, itemPrice: BigDecimal) {

        try {
            // Try updating existing item (if it exists)
            // and add 1 to the quantity
            var runningTotal: BigDecimal = purchasedItemsList(purchasedItem)._2.+(itemPrice)
            var quantity: Integer = purchasedItemsList(purchasedItem)._1 + 1
            purchasedItemsList(purchasedItem) = (quantity, runningTotal)
        } catch {
            case nsee: NoSuchElementException => {
                // If purchased item is not yet in the list
                // then add it with a quantity of 1
                purchasedItemsList(purchasedItem) = (1, itemPrice)
            }
        }
    }

    /**
     * Calculates the Service Charge of a given Total amount
     *
     * @param total - total amount
     * @param containsFood - whether the items include any food
     * @param containsHotFood - whether the items include any HOT food
     * @return the Service Charge
     */
    def calculateServiceCharge(total: BigDecimal, containsFood: Boolean, containsHotFood: Boolean): BigDecimal = {

        var serviceCharge = BigDecimal("0.00")
        var reason = " (Drinks Only)"

        if (true == containsFood) {
            if (true == containsHotFood) {
                // If there is any HOT food, apply 20%
                serviceCharge = total.*(BigDecimal.exact("0.20"))
                reason = " (Hot Food - 20%)"
            } else {
                // Else if there is just food, apply 10%
                serviceCharge = total.*(BigDecimal.exact("0.10"))
                reason = " (Food - 10%)"
            }
            // Round to 2 decimal places
            serviceCharge = serviceCharge.setScale(2, RoundingMode.HALF_UP)
            // Ensure service charge is at or below max
            if (1 == serviceCharge.compareTo(MAX_SERVICE_CHARGE)) {
                serviceCharge = MAX_SERVICE_CHARGE
            }
        }

        println("Service charge: " + serviceCharge + reason)
        return serviceCharge
    }
}
