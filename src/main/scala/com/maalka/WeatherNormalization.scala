package com.maalka

/**
  * Created by tadassugintas on 04/01/2017.
  */
object WeatherNormalization {

  def segmented(temperature: Array[Double], energy: Array[Double]): Array[Array[Double]] = {

    val R = org.ddahl.rscala.RClient()

    R eval
      """
        library(segmented)
        regress <- function(param1) {

          formula <- energy~temperature

          #fit a linear regression to the x,y vectors in the R environment
          fit <- lm(formula)

          if(length(temperature) > 11) {
            segments <- segmented(fit, seg.Z = ~temperature, psi=NA, seg.control(K=1))
          } else if (length(temperature) > 6 && length(temperature) <= 11) {
            segments <- segmented(fit, seg.Z = ~temperature, psi=NA, seg.control(K=1))
          }
        }
      """

    R.set ("temperature", temperature)
    R.set ("energy", energy)

    var result = Array(Array[Double]())

    try {

      result = R.evalD2(s"regress()[['psi']]")

      Console.println("Initial: " + result(0)(0))
      Console.println("Est.: " + result(0)(1))
      Console.println("Std.Err.: " + result(0)(2))

    } catch {
      case re: RuntimeException => Console.println("Exception: " + re.getMessage)
    }




    result
  }

}
