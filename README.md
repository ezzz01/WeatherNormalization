# Instructions:

Make sure you have R installed on your system.

Install R packages. From R console run:

install.packages("segmented")
install.packages("signal")

## Run standalone:

Run unit tests. Easiest way to test if Scala -> R integration works is to run TestSumThis.scala
More thorough tests are in TestWeatherNormalization.scala. 


## Integrate into another project:

* build jar file with "sbt package"
* include jar in your classpath, for example by adding to the 'lib' folder in Play application
* add rscala dependency as well: "org.ddahl" %% "rscala" % "1.0.14"
* call methods on WeatherNormalization object:

    val output = WeatherNormalization.calculateOutput(temperature: Array[Double], energy:Array[Double])

## R methods

* Segmented regression - tested with K values 2 and 1 (in layman's terms: first searching for 2 breakpoints and if that fails, searching for 1 breakpoint). There are 3 datasets: 100 (x, y) points with 2 breakpoints, 24 (x, y) points with 1 breakpoint and 7 (x, y) points with 1 breakpoint. 

* Linear regression - tested with small dataset of random values taken from a blog post

* Polyval calculation - calculates polynomial. 



