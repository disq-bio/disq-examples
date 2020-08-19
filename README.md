# disq-examples

Examples for the [Disq](https://github.com/disq-bio/disq) library.

[![Build Status](https://travis-ci.org/disq-bio/disq-examples.svg?branch=master)](https://travis-ci.org/disq-bio/disq-examples)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## Building disq-examples

Install

 * JDK 1.8 or later, http://openjdk.java.net
 * Apache Maven 3.3.9 or later, http://maven.apache.org

To build

```bash
$ mvn install
```

## Java examples

The [`disq-examples-java`](https://github.com/disq-bio/disq-examples/tree/master/java/src/main/java/org/disq_bio/disq/examples/java)
module contains Disq examples implemented in Java. While not all that exciting on
their own, they provide a reasonable template for building analyses in Java using the Disq library.

Each is implemented as a final class with a `public static void main(final String[] args)` method. After
validating command line arguments, the [Spark context](https://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.api.java.JavaSparkContext) is configured and instantiated

```java
SparkConf conf = new SparkConf()
  .setAppName("Java Disq Example")
  .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
  .set("spark.kryo.registrator", "org.disq_bio.disq.serializer.DisqKryoRegistrator")
  .set("spark.kryo.referenceTracking", "true");

JavaSparkContext jsc = new JavaSparkContext(new SparkContext(conf));
```

Then a [`JavaRDD`](https://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.api.java.JavaRDD) is read using the
[Disq APIs](https://www.javadoc.io/doc/org.disq-bio/disq)

```java
HtsjdkReadsRddStorage htsjdkReadsRddStorage = HtsjdkReadsRddStorage.makeDefault(jsc);
HtsjdkReadsRdd htsjdkReadsRdd = htsjdkReadsRddStorage.read(filePath);

JavaRDD<SAMRecord> reads = htsjdkReadsRdd.getReads();
```

and analysis is performed via the [Spark JavaRDD APIs](https://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.api.java.JavaRDD).


### Running Java examples

These examples can be run on the command line via `spark-submit`, e.g.

```bash
$ spark-submit \
    --packages org.disq-bio:disq:${disq.version} \
    --class org.disq_bio.disq.examples.java.JavaCountAlignments \
    disq-examples-java_2.12-${disq.version}.jar \
    sample.bam

(unmapped,30)
(1,4887)
```


## Scala examples

The [`disq-examples-scala`](https://github.com/disq-bio/disq-examples/tree/master/scala/src/main/scala/org/disq_bio/disq/examples/scala)
module contains Disq examples implemented in Scala. While not all that exciting on
their own, they provide a reasonable template for building analyses in Scala using the Disq library.

Each is implemented as an object with a `def main(args: Array[String])` method. After
validating command line arguments, the [Spark context](https://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.SparkContext)
is configured and instantiated

```scala
val conf = new SparkConf()
  .setAppName("Scala Disq Example")
  .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
  .set("spark.kryo.registrator", "org.disq_bio.disq.serializer.DisqKryoRegistrator")
  .set("spark.kryo.referenceTracking", "true")

val sc = new SparkContext(conf)
```

Then an [`RDD`](https://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.rdd.RDD) is read using the
[Disq APIs](https://www.javadoc.io/doc/org.disq-bio/disq)

```scala
val htsjdkReadsRddStorage: HtsjdkReadsRddStorage = HtsjdkReadsRddStorage.makeDefault(sc);
val htsjdkReadsRdd: HtsjdkReadsRdd = htsjdkReadsRddStorage.read(filePath);

val reads: RDD[SAMRecord] = htsjdkReadsRdd.getReads();
```

and analysis is performed via the [Spark RDD APIs](https://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.rdd.RDD).


### Running Scala examples

These examples can be run on the command line via `spark-submit`, e.g.

```bash
$ spark-submit \
    --packages org.disq-bio:disq:${disq.version} \
    --class org.disq_bio.disq.examples.scala.ScalaCountAlignments \
    disq-examples-scala_2.12-${disq.version}.jar \
    sample.bam

(unmapped,30)
(1,4887)
```

