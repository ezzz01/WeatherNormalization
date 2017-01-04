package com.maalka

/**
  * Created by tadassugintas on 04/01/2017.
  */
object WeatherNormalization {

  def main(args: Array[String]) = {
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

    // testing how to pass parameters to R
    // this is the same as this line in R that I've commented out:
    // x <- c(1:10, 13:22)
    val param1 =  ((1 to 10) ++ (13 to 22)).toArray

    R.set ("x", param1)

    val result = R.evalS1(s"myfunc()[[1]]")

    Console.println(result(0))
    Console.println(result(1))
    Console.println(result(2))
    Console.println(result(3))

  }

}
