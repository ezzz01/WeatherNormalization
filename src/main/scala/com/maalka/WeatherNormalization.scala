package com.maalka

import org.ddahl.rscala.RObject

/**
  * Created by tadassugintas on 04/01/2017.
  */
object WeatherNormalization {

  def segmentedRegression(temperature: Array[Double], energy: Array[Double]): Option[SegmentedRegressionResult] = {

    val R = org.ddahl.rscala.RClient()

    R eval
      """
        library(segmented)
        regress <- function() {

          formula <- energy ~ temperature

          #fit a linear regression to the temperature, energy vectors
          fit <- lm(formula)

          segs <- segmented(fit, seg.Z = ~temperature, psi=NA, seg.control(K=K_input))
          coefs = coef(segs)
          result <- list(coefficients = coefs, psi = segs['psi'], residuals = segs['residuals'])
        }
      """

    R.set ("temperature", temperature)
    R.set ("energy", energy)

    try {

      if (temperature.length > 11) {
        R.set("K_input", 1)
        // run once more with 1 if 2 fails
        // R.set("K_input", 2)
      } else if (temperature.length > 6 && temperature.length <= 11) {
        R.set("K_input", 1)
      }

      val ref:RObject = R.evalR(s"regress()")

      val breakpoints = R.evalD1(s"unlist(${ref}['psi'])")
      val coefficients = R.evalD1(s"unlist(${ref}['coefficients'])")
      val residuals = R.evalD1(s"unlist(${ref}['residuals'])")

      val break1 =  BreakPoint(breakpoints(0), breakpoints(1), breakpoints(2))

      // not sure what indexes unlist function will give to breakpoint2
      //val break2 =  BreakPoint(breakpoints(3), breakpoints(4), breakpoints(5))

      Some(SegmentedRegressionResult(List(break1), residuals, coefficients))

    } catch {
      case re: RuntimeException => Console.println("Exception: " + re.getMessage)
        Option.empty
    }
  }

  def linearRegression(temperature: Array[Double], energy: Array[Double]): Array[Double] = {

    val R = org.ddahl.rscala.RClient()

    R eval
      """
        library(segmented)

        #linear regression
        polyfit <- function() {
          inputdata <- data.frame(energy, temperature)
          formula <- energy ~ temperature
          result <- lm(formula, inputdata)
        }

      """

    R.set ("temperature", temperature)
    R.set ("energy", energy)

    var linearRegressionResult = Array[Double]()

    try {
      linearRegressionResult = R.evalD1(s"polyfit()[['coefficients']]")
      Console.println("Intercept: " + linearRegressionResult(0))
      Console.println("x: " + linearRegressionResult(1))

  } catch {
    case re: RuntimeException => Console.println("Exception: " + re.getMessage)
  }
    linearRegressionResult
  }


  /**
    * calculate polynomial for all values in the temperature array
    * @param temperature
    * @param energy
    * @return
    */
  def polyval(temperature: Array[Double], energy: Array[Double]): Array[Double] = {
    val R = org.ddahl.rscala.RClient()

    R eval
      """
        library(signal)

        polyvalFunc <- function() {
          inputdata <- data.frame(energy, temperature)
          formula <- energy ~ temperature
          lmresult <- lm(formula, inputdata)
          polyval(c(lmresult[['coefficients']]), temperature)
        }

      """

    R.set ("temperature", temperature)
    R.set ("energy", energy)

    var polyvalResult = Array[Double]()

    try {
      polyvalResult = R.evalD1(s"polyvalFunc()")
      Console.println("linear regression function at point 0: " + polyvalResult(0))
      Console.println("linear regression function at point 1: " + polyvalResult(1))

    } catch {
      case re: RuntimeException => Console.println("Exception: " + re.getMessage)
    }
    polyvalResult
  }

  def calculateOutput(temperature: Array[Double], energy: Array[Double]): Unit = {
    val output = segmentedRegression(temperature, energy)

    output.map { o =>

      //    m1 = r("coefs['x']")[0]
      //    m2 = r("coefs['U1.x']")[0]	+ m1
      //    b = r("coefs['(Intercept)']")[0]
      //
      //    break1 = r("seg$psi[,'Est.']")[0]
      //
      //    rez = r("seg$residuals")
      //    rez = sum(list(map(abs,rez)))
      //
      //    fitted_values = r("seg$fitted.values")
      //    #print fitted_values
      //
      //    if (num_change == 1):
      //
      //    m3 = None
      //    break2 = None
      //
      //    p_break1 = [break1, b + m1*break1]
      //    p_0 = [min(x), p_break1[1] - m1*(break1-min(x))]
      //    p_max = [max(x), m2*(max(x)-break1) +p_break1[1]]
      //
      //    #for plotting the regression
      //    x_p = [p_0[0],p_break1[0], p_max[0]]
      //    y_p = [p_0[1],p_break1[1], p_max[1]]
      //
      //    good_fit = rParamCheck.check(b,m1,m2,break1,num_change)
      //
      //    if good_fit == True:
      //      y_hat = "y_hat (when Temperature <= " + str(break1) + ") = " + str(b) + " + " + str(m1) + "*Temperature\n" \
      //      + "y_hat (when Temperature > " + str(break1) + ") = " + str(p_break1[1]) + " + " + str(m2) + "*(Temperature - " + str(break1) + ")"
      //    CP_ref = "Change Point References @ " + "Start=" + str(p_0) + "Break_1=" + str(p_break1) + "End=" +str(p_max)

      val m1: Double = o.coefficients(0)
      val m2: Double = o.coefficients(1) + m1
      val b: Double = o.coefficients(2)

      val break1: Double = o.psi(0).est

      val re: Array[Double] = o.residuals
      val rez: Double =  re.map {r => r.abs}.sum

      val m3 = Option.empty
      val break2 = Option.empty
      val p_break1 = List(break1, b + m1 * break1)
      val p_0 = List(temperature.min, p_break1(1) - m1*(break1 - temperature.min))
      val p_max = List(temperature.max, m2*(temperature.max - break1) + p_break1(1))

      val x_p = List(p_0(0), p_break1(0), p_max(0))
      val y_p = List(p_0(1), p_break1(1), p_max(1))

      val y_hat = "y_hat (when Temperature <= " + break1 + ") = " + b + " + " + m1 + "*Temperature\n" +
            "y_hat (when Temperature > " + break1 + ") = " + p_break1(1) + " + " + m2 + "*(Temperature - " + break1 + ")"
      val CP_ref = "Change Point References @ " + "Start=" + p_0 + "Break_1=" + p_break1 + "End=" + p_max

      Console.println(y_hat)
      Console.println(CP_ref)
    }

  }
}
