import com.maalka.WeatherNormalization
import org.scalatest.FunSuite

import scala.collection.immutable.NumericRange

/**
  * Created by tadassugintas on 05/01/2017.
  */
class TestWeatherNormalization extends FunSuite {

  test("basic test") {

    val x: Array[Double] = Array(1.1, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 13.0, 14.0, 15.0, 16.0, 17.0, 18.0, 19.0, 20.0, 21.0, 22.0)

    val y = Array[Double](4.5, 4.6, 5.7, 1.0)

    WeatherNormalization.segmented(x, y)
  }

}
