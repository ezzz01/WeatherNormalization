import com.maalka.WeatherNormalization
import org.scalatest.{FunSuite, Matchers}

/**
  * Created by tadassugintas on 05/01/2017.
  */
class TestWeatherNormalization extends FunSuite with Matchers {

  val temperature: Array[Double] = Array(58.50980467,
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

  val twoBreakPoints = List(
    (1,1.7239191),
    (2,4.2878656),
    (3,3.27346038),
    (4,2.66136032),
    (5,-1.14927723),
    (6,5.43659812),
    (7,8.65423904),
    (8,4.91615703),
    (9,0.69629976),
    (10,0.02794051),
    (11,6.32820731),
    (12,5.26899847),
    (13,10.72214919),
    (14,7.58576613),
    (15,4.46930104),
    (16,6.21018625),
    (17,0.18709438),
    (18,6.7731688),
    (19,-0.86813205),
    (20,6.2294853),
    (21,7.85343624),
    (22,1.4705015),
    (23,2.20744778),
    (24,1.01744464),
    (25,0.04188748),
    (26,6.64326685),
    (27,-3.53320882),
    (28,5.20247412),
    (29,10.15547056),
    (30,2.77228798),
    (31,5.48406741),
    (32,5.74890377),
    (33,5.98693697),
    (34,1.33577526),
    (35,4.02821054),
    (36,7.90823629),
    (37,-0.70376173),
    (38,8.96453582),
    (39,12.87977652),
    (40,9.00326366),
    (41,10.3964663),
    (42,14.48171111),
    (43,13.74171532),
    (44,19.33860527),
    (45,17.16899707),
    (46,20.70187621),
    (47,20.85317312),
    (48,21.06441003),
    (49,27.70659371),
    (50,27.57895917),
    (51,25.26440881),
    (52,28.69244133),
    (53,30.7489612),
    (54,30.79223138),
    (55,31.75451881),
    (56,38.82941154),
    (57,32.13396637),
    (58,36.6687221),
    (59,37.04588465),
    (60,41.49368898),
    (61,38.68815784),
    (62,42.75642698),
    (63,42.39840343),
    (64,42.99214444),
    (65,48.09226953),
    (66,48.63483939),
    (67,48.49687544),
    (68,50.41330169),
    (69,52.4110024),
    (70,56.75985412),
    (71,56.61008613),
    (72,55.72858939),
    (73,53.77154723),
    (74,59.65604798),
    (75,51.79574223),
    (76,58.05488645),
    (77,52.15339319),
    (78,53.49587852),
    (79,56.77761065),
    (80,56.42431531),
    (81,56.7140531),
    (82,54.64947111),
    (83,57.85813668),
    (84,56.54413091),
    (85,56.99742811),
    (86,53.63121477),
    (87,56.52490289),
    (88,56.60286959),
    (89,50.75918551),
    (90,54.72055407),
    (91,53.79721513),
    (92,56.0811964),
    (93,55.67949141),
    (94,56.55870633),
    (95,52.91268638),
    (96,50.17357767),
    (97,54.63292298),
    (98,55.996081),
    (99,60.8370713),
    (100,54.34227929)
  )

  test("test with 24 data points") {

    val result = WeatherNormalization.segmentedRegression(temperature, energy)
    result.map { res => res.psi(0).est shouldBe  67.5 +- 0.2}
    result.map { res => res.residuals(0) shouldBe  3370.0 +- 5.0}
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
    result.map { r => r.psi(0).est shouldBe  68.25 +- 0.2 }

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

  test("test with 2 break points") {

    val temperature = twoBreakPoints.map (pair => pair._1.toDouble).toArray
    val energy = twoBreakPoints.map (pair => pair._2).toArray

    val result = WeatherNormalization.segmentedRegression(temperature, energy)
    result.map{r => r.psi(0).est shouldBe 35.0 +- 0.5}
    result.map{r => r.psi(1).est shouldBe 70.0 +- 1.0}
  }


  test("calculate output with 24 data points and 1 break point") {
    val result = WeatherNormalization.calculateOutput(temperature, energy)
  }

  test("calculate output with 100 data points and 2 break points") {
    val temperature = twoBreakPoints.map (pair => pair._1.toDouble).toArray
    val energy = twoBreakPoints.map (pair => pair._2).toArray
    WeatherNormalization.calculateOutput(temperature, energy)
  }

  test("Polyval test") {

    // input data taken from https://www.wired.com/2011/01/linear-regression-with-pylab/
    // intercept must be 2.771
    // x (slope) must be 1.076

    val temp = Array[Double](0.2,1.3,2.1,2.9,3.3)
    val energy = Array[Double](3.3,3.9,4.8,5.5,6.9)

    val result = WeatherNormalization.polyval(temp, energy)
    result.length should equal(temp.length)

  }

}
