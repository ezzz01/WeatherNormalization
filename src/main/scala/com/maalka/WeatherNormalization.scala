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

          segments <- segmented(fit, seg.Z = ~temperature, psi=NA, seg.control(K=K_input))
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

      val break1 =  BreakPoint(breakpoints(0), breakpoints(1), breakpoints(2))
      // not sure what indexes unlist function will give to breakpoint2
      //val break2 =  BreakPoint(breakpoints(3), breakpoints(4), breakpoints(5))

      val residuals: Array[Double] = R.evalD1(s"unlist(${ref}['residuals'])")

      Some(SegmentedRegressionResult(List(break1), residuals))

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
}
