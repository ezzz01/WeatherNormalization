package com.maalka.examples

/**
  * Created by tadassugintas on 05/01/2017.
  */
object SumThis {

  val R = org.ddahl.rscala.RClient()

  def sumThis(input: Array[Int]): Double = {
   R eval
     """
         myfunc <- function(param1) {
            output <- sum(x)
         }
     """
    R.set ("x", input)

    val result: Double = R.evalD0(s"myfunc()")

    result
  }

}
