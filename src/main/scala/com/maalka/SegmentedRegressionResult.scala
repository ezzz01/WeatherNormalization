package com.maalka

/**
  * Created by tadassugintas on 09/01/2017.
  */
case class SegmentedRegressionResult(psi: List[BreakPoint], residuals: Array[Double])

/**
  * Estimated break-points and relevant (approximate) standard errors
  * @param initial
  * @param est
  * @param stErr
  */
case class BreakPoint(val initial: Double, val est: Double, val stErr: Double)

/**
  * the model frame
  */
case class Model(frame: List[ModelLine])

/**
  *
  * @param y
  * @param x
  * @param u1x - U1.x
  * @param psi1x - psi1.x
  */
case class ModelLine(y: Double, x: Double, u1x: Double, psi1x: Double)