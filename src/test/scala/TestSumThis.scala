import com.maalka.examples.SumThis
import org.scalatest.FunSuite

/**
  * Created by tadassugintas on 05/01/2017.
  */
class TestSumThis extends FunSuite {

  test("try summing in R") {
    val param1 =  (1 to 10).toArray
    val output = SumThis.sumThis(param1)
    assert(output == 55)
  }

}
