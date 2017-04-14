package cafe

import java.util.NoSuchElementException
import scala.collection.mutable.Map
import scala.math.BigDecimal
import scala.math.BigDecimal.RoundingMode

class Cafe {

    // Create a multi-key map (to allow both HOT and COLD versions of products)
    implicit class EnhancedMap[A, B, C](m: Map[(A, B), C]) {
        def update(a: A, b: B, c: C) { m((a, b)) = c }
    }

    // Create a map with products and their prices
    val products = Map[(String, String), (String, BigDecimal)](
        (("Cola", "Cold") -> ("Drink", BigDecimal("0.50"))),
        (("Coffee", "Hot") -> ("Drink", BigDecimal("1.00"))),
        (("Cheese Sandwich", "Cold") -> ("Food", BigDecimal("2.00"))),
        (("Cheese Sandwich", "Hot") -> ("Food", BigDecimal("2.50"))), // Example of both options ONLY (not in requirements)
        (("Steak Sandwich", "Cold") -> ("Food", BigDecimal("4.00"))), // Example of both options ONLY (not in requirements)
        (("Steak Sandwich", "Hot") -> ("Food", BigDecimal("4.50"))))

    val MAX_SERVICE_CHARGE = BigDecimal.exact("20.00")

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
        var containsFood = false;
        var containsHotItems = false;

        println("============================")
        println("Calculating total for items:")
        println("============================")

        for (item: (String, String) <- items) {
            try {
                print(item._1 + "(" + item._2 + ") => ")

                var product = products((item._1, item._2))

                // Add price to total
                def itemPrice: BigDecimal = product._2.asInstanceOf[BigDecimal]
                total = total.+(itemPrice)

                // Identify whether food or drink
                if ("Food".equals(product._1))
                    containsFood = true;

                // Identify whether hot or cold
                if ("Hot".equals(item._2))
                    containsHotItems = true;

                println(" Price: " + itemPrice)
            } catch {
                case nsee: NoSuchElementException => {
                    println("NOT FOUND! Error")
                    throw nsee
                }
            }
        }

        println("=====\nSubtotal: " + total)

        // Calculate Service Charge
        var serviceCharge = calculateServiceCharge(total, containsFood, containsHotItems)

        // Add Service Charge to total
        total = total.+(serviceCharge)
        println("=====\nTotal amount due: " + total + "\n=====")

        return total
    }

    /**
     * Calculates the Service Charge of a given Total amount
     *
     * @param total - total amount
     * @param containsFood - whether the items include any food
     * @param containsHotItems - whether the items include any HOT food
     * @return the Service Charge
     */
    def calculateServiceCharge(total: BigDecimal, containsFood: Boolean, containsHotItems: Boolean): BigDecimal = {

        var serviceCharge = BigDecimal("0.00")
        var reason = " (Drinks Only)"

        if (true == containsFood) {
            if (true == containsHotItems) {
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
