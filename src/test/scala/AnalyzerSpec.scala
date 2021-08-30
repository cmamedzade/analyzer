import Impl.Analyzer
import akka.actor.testkit.typed.javadsl.ActorTestKit
import infra.SensorMap
import infra.SensorMap.{emptySensor, map, nanCounter}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

class AnalyzerSpec extends AnyFlatSpec with Matchers {

  "akka stream" should "check" in {

    val testKit = ActorTestKit.create()
    implicit val system = testKit.system

    val sinkUnderTest = new Analyzer()
    Await.result(sinkUnderTest.resultFuture, 5 seconds)
    emptySensor.length should be (1)
    map
      .toSeq
      .sortWith(_._2.avg > _._2.avg).maxBy(_._2.avg)._2.avg should be (45)
    sinkUnderTest.numFiles should be (2)
    nanCounter should be (2)
    SensorMap.print()._1 should include ("Num of processed measurements:")
    sinkUnderTest.numFiles should be (2)
  }
}
