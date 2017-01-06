import com.maalka.WeatherNormalization
import org.scalatest.{FunSuite, Matchers}

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

    val result = WeatherNormalization.segmentedRegression(temp, energy)
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

    val result = WeatherNormalization.segmentedRegression(temp, energy)
    result(0)(1) shouldBe  68.25 +- 0.2

  }

  test("Linear regression test") {

    // input data taken from https://www.wired.com/2011/01/linear-regression-with-pylab/
    // intercept must be 2.771
    // x (slope) must be 1.076

    val temp = Array[Double](0.2,1.3,2.1,2.9,3.3)
    val energy = Array[Double](3.3,3.9,4.8,5.5,6.9)

    val result = WeatherNormalization.linearRegression(temp, energy)
    result(0) shouldBe  2.771 +- 0.01
    result(1) shouldBe  1.076 +- 0.01

  }

}
