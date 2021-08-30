package Impl

import akka.Done
import akka.actor.ActorSystem
import infra.{FileHandler, SensorMap}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Analyzer(path: String = "src/main/resources") {

  implicit val system = ActorSystem("Sys")
  val handler = FileHandler
  val filePath: String = path

  def isDigit(arr: String) = {
    arr.exists(_.isDigit)
  }

  val sensorMap = SensorMap

  val resultFuture: Future[Done] = handler
    .readFromFiles(filePath)
    .filter(p => isDigit(p))
    .map(p => p.split(","))
    .map(p => (p(0), p(1)))
    .runForeach {
      p =>
        if (p._2.contains("NaN")) {
          sensorMap.countNan()
          sensorMap.addNan(p._1.trim.toInt)
        }
        else {
          sensorMap.addData(p._1.toInt, p._2.trim.toInt)
          if (sensorMap.emptySensor.contains(p._1.trim.toInt))
            sensorMap.remoNan(p._1.trim.toInt)
        }
    }

  val numFiles = handler.listDirectory(filePath).size

  val result = resultFuture
    .map(_ => sensorMap.print())
    .foreach{
      p =>
        println(s"Num of processed files: ${numFiles}")
        println(p._1)
        println(p._2)
        println(p._3)
        p._4.foreach(println)
        p._5.foreach(println)
    }
}
