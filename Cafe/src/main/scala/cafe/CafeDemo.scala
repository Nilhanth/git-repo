package cafe

object CafeDemo {

    def main(args: Array[String]) {

        def myCafe: Cafe = new Cafe()

        try {
            var total = myCafe.calculateTotal(List("Cola", "Coffee", "Cola"))
            println("=====\nTotal amount due: " + total)
        } catch {
            case nsee: NoSuchElementException => {
                println("=====\nERROR: Cannot calculate total - '" + nsee.getMessage + "'")
            }
        }

        println("=====")
    }
}
