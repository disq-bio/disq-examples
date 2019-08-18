# disq-examples

Examples for the [Disq](https://github.com/disq-bio/disq) library.

[![Build Status](https://travis-ci.org/disq-bio/disq-examples.svg?branch=master)](https://travis-ci.org/disq-bio/disq-examples)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

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

Then an [`JavaRDD`](https://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.api.java.JavaRDD) is read using the
[Disq APIs](https://www.javadoc.io/doc/org.disq-bio/disq)

```java
HtsjdkReadsRddStorage htsjdkReadsRddStorage = HtsjdkReadsRddStorage.makeDefault(jsc);
HtsjdkReadsRdd htsjdkReadsRdd = htsjdkReadsRddStorage.read(args[0]);

JavaRDD<SAMRecord> reads = htsjdkReadsRdd.getReads();
```

and analysis is performed via the [Spark JavaRDD APIs](https://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.api.java.JavaRDD).


### Running Java examples

These examples can be run on the command line via `spark-submit`, e.g.

```bash
$ spark-submit \
    --packages org.disq-bio:disq:${disq.version} \
    --class org.disq_bio.disq.examples.java.JavaCountAlignments \
    disq-examples-java_2.11-${disq.version}.jar \
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
val htsjdkReadsRdd: HtsjdkReadsRdd = htsjdkReadsRddStorage.read(args(0));

val reads: RDD[SAMRecord] = htsjdkReadsRdd.getReads();
```

and analysis is performed via the [Spark RDD APIs](https://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.rdd.RDD).


### Running Scala examples

These examples can be run on the command line via `spark-submit`, e.g.

```bash
$ spark-submit \
    --packages org.disq-bio:disq:${disq.version} \
    --class org.disq_bio.disq.examples.scala.ScalaCountAlignments \
    disq-examples-scala_2.11-${disq.version}.jar \
    sample.bam

(unmapped,30)
(1,4887)
```

### Building for multiple Scala versions

The `disq-examples-scala` module allows for building for multiple Scala versions, currently
Scala 2.11 and Scala 2.12.

[Build scripts](https://github.com/disq-bio/disq-examples/tree/master/scripts) are provided to modify `pom.xml` in place.

```bash
$ ./scripts/move_to_scala_2.12.sh
$ mvn clean install
...
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary for disq-examples_2.11 ${disq.version}:
[INFO]
[INFO] disq-examples_2.11 ................................. SUCCESS [  0.775 s]
[INFO] disq-examples-java_2.11 ............................ SUCCESS [  3.276 s]
[INFO] disq-examples-scala_2.11 ........................... SUCCESS [ 17.169 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------


$ ./scripts/move_to_scala_2.12.sh
$ mvn clean install
...
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary for disq-examples_2.12 ${disq.version}:
[INFO]
[INFO] disq-examples_2.12 ................................. SUCCESS [  0.889 s]
[INFO] disq-examples-java_2.12 ............................ SUCCESS [  3.840 s]
[INFO] disq-examples-scala_2.12 ........................... SUCCESS [ 17.821 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```
