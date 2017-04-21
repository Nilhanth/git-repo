package cafe

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import cafe.CafeConstants._
import scala.math.BigDecimal
import scala.collection.mutable.Map

class CafeTest extends FunSuite with BeforeAndAfter {

    var myCafe: Cafe = _
    var result: BigDecimal = _

    before {
        myCafe = new Cafe()
        result = BigDecimal("0.00")
    }

    /**
     * TEST 1 - Individual Prices
     */
    test("Calculate total for individual items") {

        // Items = 0.50
        // Service = 0.00
        // Total = 0.50
        result = myCafe.calculateTotal(List((COLA, COLD)))
        assertResult(BigDecimal("0.50"))(result)

        // Items = 1.00
        // Service = 0.00
        // Total = 1.00
        result = myCafe.calculateTotal(List((COFFEE, HOT)))
        assertResult(BigDecimal("1.00"))(result)

        // Items = 2.00
        // Service = 0.20 (10%)
        // Total = 2.20
        result = myCafe.calculateTotal(List((CHEESE_SANDWICH, COLD)))
        assertResult(BigDecimal("2.20"))(result)

        // Items = 2.50
        // Service = 0.50 (20%)
        // Total = 3.00
        result = myCafe.calculateTotal(List((CHEESE_SANDWICH, HOT)))
        assertResult(BigDecimal("3.00"))(result)

        // Items = 4.00
        // Service = 0.40 (10%)
        // Total = 4.40
        result = myCafe.calculateTotal(List((STEAK_SANDWICH, COLD)))
        assertResult(BigDecimal("4.40"))(result)

        // Items = 4.50
        // Service = 0.90 (20%)
        // Total = 5.40
        result = myCafe.calculateTotal(List((STEAK_SANDWICH, HOT)))
        assertResult(BigDecimal("5.40"))(result)
    }

    /**
     * TEST 2 - Combined Prices
     */
    test("Calculate total with 2 or more items") {

        // Items = 0.50 + 0.50 = 1.00
        // Service = 0.00
        // Total = 1.00
        result = myCafe.calculateTotal(List((COLA, COLD), (COLA, COLD)))
        assertResult(BigDecimal("1.00"))(result)

        // Items = 0.50 + 1.00 = 1.50
        // Service = 0.00
        // Total = 1.50
        result = myCafe.calculateTotal(List((COLA, COLD), (COFFEE, HOT)))
        assertResult(BigDecimal("1.50"))(result)

        // Items = 0.50 + 2.00 = 2.50
        // Service = 0.25 (10%)
        // Total = 2.75
        result = myCafe.calculateTotal(List((COLA, COLD), (CHEESE_SANDWICH, COLD)))
        assertResult(BigDecimal("2.75"))(result)

        // Items = 0.50 + 4.50 = 5.00
        // Service = 1.00 (20%)
        // Total = 6.00
        result = myCafe.calculateTotal(List((COLA, COLD), (STEAK_SANDWICH, HOT)))
        assertResult(BigDecimal("6.00"))(result)

        // Items = 1.00 + 2.00 + 4.50 = 7.50
        // Service = 1.50 (20%)
        // Total = 9.00
        result = myCafe.calculateTotal(List((COFFEE, HOT), (CHEESE_SANDWICH, COLD), (STEAK_SANDWICH, HOT)))
        assertResult(BigDecimal("9.00"))(result)

        // Items = 0.50 + 1.00 + 2.00 + 4.50 = 8.00
        // Service = 1.60 (20%)
        // Total = 9.60
        result = myCafe.calculateTotal(List((COLA, COLD), (COFFEE, HOT), (CHEESE_SANDWICH, COLD), (STEAK_SANDWICH, HOT)))
        assertResult(BigDecimal("9.60"))(result)

        // Items = 0.50 + 1.00 + 2.00 + 2.50 + 4.00 + 4.50 = 14.50
        // Service = 2.90
        // Total = 17.40
        result = myCafe.calculateTotal(List((COLA, COLD), (COFFEE, HOT), (CHEESE_SANDWICH, COLD), (CHEESE_SANDWICH, HOT), (STEAK_SANDWICH, COLD), (STEAK_SANDWICH, HOT)))
        assertResult(BigDecimal("17.40"))(result)
    }

    /**
     * TEST 3 - Apply Service Charges
     */
    test("Calculate total with service charge") {

        // COLD but NO FOOD - 0%
        result = myCafe.calculateServiceCharge(BigDecimal("10.00"), false, false)
        assertResult(BigDecimal("0.00"))(result)

        // HOT but NO FOOD - 0%
        result = myCafe.calculateServiceCharge(BigDecimal("10.00"), false, true)
        assertResult(BigDecimal("0.00"))(result)

        // COLD with FOOD - 10%
        result = myCafe.calculateServiceCharge(BigDecimal("10.00"), true, false)
        assertResult(BigDecimal("1.00"))(result)

        // HOT with FOOD - 20%
        result = myCafe.calculateServiceCharge(BigDecimal("10.00"), true, true)
        assertResult(BigDecimal("2.00"))(result)

        // ROUNDING UP - 10%
        result = myCafe.calculateServiceCharge(BigDecimal("10.55"), true, false)
        assertResult(BigDecimal("1.06"))(result)

        // ROUNDING DOWN - 10%
        result = myCafe.calculateServiceCharge(BigDecimal("10.54"), true, false)
        assertResult(BigDecimal("1.05"))(result)

        // ROUNDING UP - 20%
        result = myCafe.calculateServiceCharge(BigDecimal("10.58"), true, true)
        assertResult(BigDecimal("2.12"))(result)

        // ROUNDING DOWN - 20%
        result = myCafe.calculateServiceCharge(BigDecimal("10.57"), true, true)
        assertResult(BigDecimal("2.11"))(result)

        // MAXIMUM SERVICE CHARGE
        result = myCafe.calculateServiceCharge(BigDecimal("100.00"), true, true)
        assertResult(MAX_SERVICE_CHARGE)(result)
        result = myCafe.calculateServiceCharge(BigDecimal("200.00"), true, true)
        assertResult(MAX_SERVICE_CHARGE)(result)
        result = myCafe.calculateServiceCharge(BigDecimal("300.00"), true, true)
        assertResult(MAX_SERVICE_CHARGE)(result)
    }

    /**
     * TEST 4 - Quantities and Running Totals
     */
    test("Calculate total quantities and their running totals") {

        // Setup empty list
        var purchasedItems: Map[String, (Integer, BigDecimal)] = Map[String, (Integer, BigDecimal)]()

        // Add 4 x Item1
        myCafe.addPurchasedItemToList(purchasedItems, "Item1", BigDecimal("1.00"))
        myCafe.addPurchasedItemToList(purchasedItems, "Item1", BigDecimal("1.00"))
        myCafe.addPurchasedItemToList(purchasedItems, "Item1", BigDecimal("1.00"))

        // Add 2 x Item2
        myCafe.addPurchasedItemToList(purchasedItems, "Item2", BigDecimal("1.00"))
        myCafe.addPurchasedItemToList(purchasedItems, "Item2", BigDecimal("1.00"))

        // Add 5 x Item3
        myCafe.addPurchasedItemToList(purchasedItems, "Item3", BigDecimal("1.00"))
        myCafe.addPurchasedItemToList(purchasedItems, "Item3", BigDecimal("1.00"))
        myCafe.addPurchasedItemToList(purchasedItems, "Item3", BigDecimal("1.00"))
        myCafe.addPurchasedItemToList(purchasedItems, "Item3", BigDecimal("1.00"))
        myCafe.addPurchasedItemToList(purchasedItems, "Item3", BigDecimal("1.00"))

        // Ensure correct quantities
        assertResult(3)(purchasedItems("Item1")._1)
        assertResult(2)(purchasedItems("Item2")._1)
        assertResult(5)(purchasedItems("Item3")._1)

        // Ensure correct running totals
        assertResult(BigDecimal("3.00"))(purchasedItems("Item1")._2)
        assertResult(BigDecimal("2.00"))(purchasedItems("Item2")._2)
        assertResult(BigDecimal("5.00"))(purchasedItems("Item3")._2)
    }

    /**
     * TEST 5 - Invalid Items
     */
    test("Calculate total with invalid items") {

        // VALID ITEM & STATE, BUT SELECTION NOT ON MENU
        try {
            result = myCafe.calculateTotal(List((COLA, HOT)))
            // FAIL
            fail("Method should throw exception")
        } catch {
            case nsee: NoSuchElementException => {
                // PASS
                assert(true)
            }
        }

        // INVALID ITEM NAME
        try {
            result = myCafe.calculateTotal(List(("Pepsi", COLD)))
            // FAIL
            fail("Method should throw exception")
        } catch {
            case nsee: NoSuchElementException => {
                // PASS
                assert(true)
            }
        }

        // INVALID ITEM STATE
        try {
            result = myCafe.calculateTotal(List((COLA, "medium")))
            // FAIL
            fail("Method should throw exception")
        } catch {
            case nsee: NoSuchElementException => {
                // PASS
                assert(true)
            }
        }
    }

    after {
        myCafe = null
        result = null
    }
}
