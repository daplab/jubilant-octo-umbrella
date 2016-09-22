/**
 * Created by christophebovigny on 04.08.15.
 */

import scala.math.random

import org.apache.spark._

/** Computes an approximation to pi */
object SparkPi {
  def main(args: Array[String]) {
    // Here we define the configuration of spark
    val conf = new SparkConf().setAppName("Spark Pi").setMaster("local")
    val spark = new SparkContext(conf)
    // We can give the number of slice as argument
    val slices = if (args.length > 0) args(0).toInt else 100
    val n = math.min(100000L * slices, Int.MaxValue).toInt // avoid overflow
    // We parallelize the computation by the number of slices
    val count = spark.parallelize(1 until n, slices).map { i =>
        val x = random * 2 - 1
        val y = random * 2 - 1
        if (x*x + y*y < 1) 1 else 0
        // And finally we reduce the computation of x and y values by add the two values.
      }.reduce(_ + _)

    println("Pi is roughly " + 4.0 * count / n)
    spark.stop()
  }
}