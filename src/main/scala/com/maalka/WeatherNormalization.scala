package com.maalka

/**
  * Created by tadassugintas on 04/01/2017.
  */
object WeatherNormalization {

  def segmentedRegression(temperature: Array[Double], energy: Array[Double]): Array[Array[Double]] = {

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

    var segmentedResult = Array(Array[Double]())

    try {

      if (temperature.length > 11) {
        R.set("K_input", 1)
        // run once more with 1 if 2 fails
        // R.set("K_input", 1)
      } else if (temperature.length > 6 && temperature.length <= 11) {
        R.set("K_input", 1)
      }

      segmentedResult = R.evalD2(s"regress()[['psi']]")

      Console.println("Initial: " + segmentedResult(0)(0))
      Console.println("Est.: " + segmentedResult(0)(1))
      Console.println("Std.Err.: " + segmentedResult(0)(2))

    } catch {
      case re: RuntimeException => Console.println("Exception: " + re.getMessage)
    }

    segmentedResult
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
