package cafe

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

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
        result = myCafe.calculateTotal(List(("Cola", "Cold")))
        assertResult(BigDecimal("0.50"))(result)

        // Items = 1.00
        // Service = 0.00
        // Total = 1.00
        result = myCafe.calculateTotal(List(("Coffee", "Hot")))
        assertResult(BigDecimal("1.00"))(result)

        // Items = 2.00
        // Service = 0.20 (10%)
        // Total = 2.20
        result = myCafe.calculateTotal(List(("Cheese Sandwich", "Cold")))
        assertResult(BigDecimal("2.20"))(result)

        // Items = 2.50
        // Service = 0.50 (20%)
        // Total = 3.00
        result = myCafe.calculateTotal(List(("Cheese Sandwich", "Hot")))
        assertResult(BigDecimal("3.00"))(result)

        // Items = 4.00
        // Service = 0.40 (10%)
        // Total = 4.40
        result = myCafe.calculateTotal(List(("Steak Sandwich", "Cold")))
        assertResult(BigDecimal("4.40"))(result)

        // Items = 4.50
        // Service = 0.90 (20%)
        // Total = 5.40
        result = myCafe.calculateTotal(List(("Steak Sandwich", "Hot")))
        assertResult(BigDecimal("5.40"))(result)
    }

    /**
     * TEST 2 - Combined Prices
     */
    test("Calculate total with 2 or more items") {

        // Items = 0.50 + 0.50 = 1.00
        // Service = 0.00
        // Total = 1.00
        result = myCafe.calculateTotal(List(("Cola", "Cold"), ("Cola", "Cold")))
        assertResult(BigDecimal("1.00"))(result)

        // Items = 0.50 + 1.00 = 1.50
        // Service = 0.00
        // Total = 1.50
        result = myCafe.calculateTotal(List(("Cola", "Cold"), ("Coffee", "Hot")))
        assertResult(BigDecimal("1.50"))(result)

        // Items = 0.50 + 2.00 = 2.50
        // Service = 0.25 (10%)
        // Total = 2.75
        result = myCafe.calculateTotal(List(("Cola", "Cold"), ("Cheese Sandwich", "Cold")))
        assertResult(BigDecimal("2.75"))(result)

        // Items = 0.50 + 4.50 = 5.00
        // Service = 1.00 (20%)
        // Total = 6.00
        result = myCafe.calculateTotal(List(("Cola", "Cold"), ("Steak Sandwich", "Hot")))
        assertResult(BigDecimal("6.00"))(result)

        // Items = 1.00 + 2.00 + 4.50 = 7.50
        // Service = 1.50 (20%)
        // Total = 9.00
        result = myCafe.calculateTotal(List(("Coffee", "Hot"), ("Cheese Sandwich", "Cold"), ("Steak Sandwich", "Hot")))
        assertResult(BigDecimal("9.00"))(result)

        // Items = 0.50 + 1.00 + 2.00 + 4.50 = 8.00
        // Service = 1.60 (20%)
        // Total = 9.60
        result = myCafe.calculateTotal(List(("Cola", "Cold"), ("Coffee", "Hot"), ("Cheese Sandwich", "Cold"), ("Steak Sandwich", "Hot")))
        assertResult(BigDecimal("9.60"))(result)

        // Items = 0.50 + 1.00 + 2.00 + 2.50 + 4.00 + 4.50 = 14.50
        // Service = 2.90
        // Total = 17.40
        result = myCafe.calculateTotal(List(("Cola", "Cold"), ("Coffee", "Hot"), ("Cheese Sandwich", "Cold"), ("Cheese Sandwich", "Hot"), ("Steak Sandwich", "Cold"), ("Steak Sandwich", "Hot")))
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
        assertResult(myCafe.MAX_SERVICE_CHARGE)(result)
        result = myCafe.calculateServiceCharge(BigDecimal("200.00"), true, true)
        assertResult(myCafe.MAX_SERVICE_CHARGE)(result)
        result = myCafe.calculateServiceCharge(BigDecimal("300.00"), true, true)
        assertResult(myCafe.MAX_SERVICE_CHARGE)(result)
    }

    /**
     * TEST 4 - Invalid Items
     */
    test("Calculate total with invalid items") {

        try {
            result = myCafe.calculateTotal(List(("Cola", "abc")))
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
