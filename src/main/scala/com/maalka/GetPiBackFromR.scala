package com.maalka

/**
  * Created by tadassugintas on 04/01/2017.
  */

/**
  *
  * example of calling R from Scala
   */

object GetPiBackFromR {

  def main(args: Array[String]) = {
    val R = org.ddahl.rscala.RClient()

    R eval
      """
      a <- pi
    """
    // getting a double back from R
    val x = R.getD0("a")
    // prints:
    // 3.141592653589793
    Console.println(x)
  }

}
