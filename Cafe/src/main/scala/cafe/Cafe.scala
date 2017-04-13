package cafe

object Cafe extends Greeting with App {
    println(greeting)
}

trait Greeting {
    lazy val greeting: String = "hello"
}
