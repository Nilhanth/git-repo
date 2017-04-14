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

        result = myCafe.calculateTotal(List("Cola"))
        assertResult(BigDecimal("0.50"))(result)

        result = myCafe.calculateTotal(List("Coffee"))
        assertResult(BigDecimal("1.00"))(result)

        result = myCafe.calculateTotal(List("Cheese Sandwich"))
        assertResult(BigDecimal("2.00"))(result)

        result = myCafe.calculateTotal(List("Steak Sandwich"))
        assertResult(BigDecimal("4.50"))(result)
    }

    /**
     * TEST 2 - Combined Prices
     */
    test("Calculate total with 2 or more items") {

        result = myCafe.calculateTotal(List("Cola", "Cola"))
        assertResult(BigDecimal("1.00"))(result)

        result = myCafe.calculateTotal(List("Cola", "Coffee"))
        assertResult(BigDecimal("1.50"))(result)

        result = myCafe.calculateTotal(List("Cola", "Cheese Sandwich"))
        assertResult(BigDecimal("2.50"))(result)

        result = myCafe.calculateTotal(List("Cola", "Steak Sandwich"))
        assertResult(BigDecimal("5.00"))(result)

        result = myCafe.calculateTotal(List("Coffee", "Cheese Sandwich", "Steak Sandwich"))
        assertResult(BigDecimal("7.50"))(result)

        result = myCafe.calculateTotal(List("Cola", "Coffee", "Cheese Sandwich", "Steak Sandwich"))
        assertResult(BigDecimal("8.00"))(result)
    }

    /**
     * TEST 3 - Invalid Items
     */
    test("Calculate total with invalid items") {

        try {
            result = myCafe.calculateTotal(List("Cola", "abc", "Coffee"))
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
