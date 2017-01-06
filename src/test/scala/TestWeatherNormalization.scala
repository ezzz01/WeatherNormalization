import com.maalka.WeatherNormalization
import org.scalatest.{FunSuite, Matchers}

import scala.collection.immutable.NumericRange

/**
  * Created by tadassugintas on 05/01/2017.
  */
class TestWeatherNormalization extends FunSuite with Matchers {

  test("test with 24 data points") {

    val temp: Array[Double] = Array(58.50980467,
      63.45122707,
      67.43902743,
      73.47472108,
      73.88896326,
      78.20371135,
      35.1165211,
      37.0796274,
      39.56033974,
      40.65875764,
      43.78553948,
      55.25805336,
      58.50980467,
      63.45122707,
      67.43902743,
      73.47472108,
      73.88896326,
      78.20371135,
      35.1165211,
      37.0796274,
      39.56033974,
      40.65875764,
      43.78553948,
      55.25805336)

    val energy = Array[Double](73721,
      89498,
      61194,
      89961,
      72190,
      92936,
      108784,
      121845,
      95001,
      114360,
      85885,
      95352,
      62586,
      58525,
      67290,
      67507,
      76550,
      77018,
      47837,
      75993,
      48123,
      50771,
      51176,
      47931)

    val result = WeatherNormalization.segmented(temp, energy)
    result(0)(1) shouldBe  67.5 +- 0.2
  }

  test("test with 7 data points") {
    val temp: Array[Double] = Array(58.50980467,
      63.45122707,
      67.43902743,
      73.47472108,
      73.88896326,
      78.20371135,
      35.1165211)

    val energy = Array[Double](73721,
      89498,
      61194,
      89961,
      72190,
      92936,
      108784)

    val result = WeatherNormalization.segmented(temp, energy)
    result(0)(1) shouldBe  68.25 +- 0.2

  }

}
