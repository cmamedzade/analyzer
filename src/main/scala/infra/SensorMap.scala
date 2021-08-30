package infra

object SensorMap {
  case class Sensor(min: Int, avg: Int, max: Int, counter: Int)

  var map = Map.empty[Int, Sensor]
  var emptySensor = Array.empty[Int]
  var nanCounter = 0

  def calculator(sensor: Sensor, value: Int) = {
    val mi = if (sensor.min > value) value else sensor.min
    val ma = if (sensor.max < value) value else sensor.max
    val av = (sensor.avg + value) / (sensor.counter + 1)
    (mi, ma, av)
  }


  def addData(id: Int, data: Int) = {
    map.get(id) match {
      case Some(sensor) =>
        val result = calculator(sensor, data)
        map = map.updated(id, sensor.copy(min = result._1, result._3, result._2, counter = sensor.counter + 1))
      case None =>
        map = map ++ Map(id -> Sensor(data, data, data, 1))
    }
  }

  def addNan(id: Int) = {
    map.get(id) match {
      case Some(_) => map
      case None => emptySensor = emptySensor ++ Array(id)
    }
  }

  def remoNan(id: Int) = {
    emptySensor = emptySensor.filterNot(_ == id)
  }

  def countNan() = {
    nanCounter += 1
  }

  def print() = {
    (
    s"Num of processed measurements: ${nanCounter + map.foldLeft(0)((a,b) => a  + b._2.counter)}",
    s"Num of failed measurements: ${nanCounter}",
    s"Sensors with highest avg humidity:",
    map
      .toSeq
      .sortWith(_._2.avg > _._2.avg)
      .map{
        p =>
          s"${p._1}, ${p._2.min}, ${p._2.avg}, ${p._2.max}"
      },
    emptySensor
      .map(s => s"${s}, NaN, NaN, NaN"))
  }
}
