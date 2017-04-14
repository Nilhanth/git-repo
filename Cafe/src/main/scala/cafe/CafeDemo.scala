package cafe

object CafeDemo {

    def main(args: Array[String]) {

        def myCafe: Cafe = new Cafe()

        try {
            var total = myCafe.calculateTotal(List(("Cola", "Cold"), //
                ("Coffee", "Hot"), //
                ("Cheese Sandwich", "Cold"), //
                ("Cheese Sandwich", "Hot"), //
                ("Steak Sandwich", "Cold"), //
                ("Steak Sandwich", "Hot")))
            println("Amount returned from method: " + total)
        } catch {
            case nsee: NoSuchElementException => {
                println("'Exception message returned from method: " + nsee.getMessage + "'")
            }
        }

    }
}
