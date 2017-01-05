package com.maalka

/**
  * Created by tadassugintas on 04/01/2017.
  */
object WeatherNormalization {

  def segmented(x: Array[Double], y: Array[Double]) = {

    val R = org.ddahl.rscala.RClient()

    R eval
      """
        library(segmented)
        myfunc <- function(param1) {
        ## x <- c(1:10, 13:22)
        ## x <- param1
        y <- numeric(20)
        ## Create first segment
        y[1:10] <- 20:11 + rnorm(10, 0, 1.5)
        ## Create second segment
        y[11:20] <- seq(11, 15, len=10) + rnorm(10, 0, 1.5)
        lin.mod <- lm(y~x)
        segmented.mod <- segmented(lin.mod, seg.Z = ~x, psi=14)
        }
      """

    R.set ("x", x)

    val result = R.evalD2(s"myfunc()[['psi']]")

    Console.println("Intial: " + result(0)(0))
    Console.println("Est.: " + result(0)(1))
    Console.println("Std.Err.: " + result(0)(2))

  }

}
