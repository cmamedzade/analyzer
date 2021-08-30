package Impl

import scala.util.{Failure, Success, Try}

object RunApp {
    def main(args: Array[String]): Unit = {
      Try(args(0)) match {
        case Failure(_) => new Analyzer()
        case Success(value) => new Analyzer(value)
      }
    }
}
