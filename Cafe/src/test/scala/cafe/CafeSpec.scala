package cafe

import org.scalatest._

class CafeSpec extends FlatSpec with Matchers {
    "The Cafe object" should "say hello" in {
        Cafe.greeting shouldEqual "hello"
    }
}
