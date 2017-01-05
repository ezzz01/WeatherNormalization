package com.maalka.examples

/**
  * Created by tadassugintas on 04/01/2017.
  */


object ScalaToRTest {

  def main(args: Array[String]) = {
    val R = org.ddahl.rscala.RClient()

    val a = R.evalD0("rnorm(8)")
    val b = R.evalD1("rnorm(8)")
    val c = R.evalD2("matrix(rnorm(8),nrow=4)")

    R eval
      """
  v <- rbinom(8,size=10,prob=0.4)
  m <- matrix(v,nrow=4)
"""
    val v1 = R.get("v")
    val v2 = R.get("v")._1.asInstanceOf[Array[Int]]
    // This works, but is not very convenient
    val v3 = R.v._1.asInstanceOf[Array[Int]]
    // Slightly better
    val v4 = R.getI0("v")
    // Get the first element of R's "v" as a Int
    val v5 = R.getI1("v")
    // Get R's "v" as an Array[Int]
    val v6 = R.getI2("m") // Get R's "m" as an Array[Array[Int]]


    Console.println("sixth element of v3: " + v3(6))
  }
}

