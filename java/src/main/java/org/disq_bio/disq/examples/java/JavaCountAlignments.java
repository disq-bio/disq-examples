/*
 * Disq
 *
 * MIT License
 *
 * Copyright (c) 2018-2019 Disq contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.disq_bio.disq.examples.java;

import htsjdk.samtools.SAMRecord;

import org.apache.spark.SparkConf;

import org.apache.spark.SparkContext;
import org.apache.spark.SparkContext;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;

import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;

import org.apache.spark.rdd.RDD;

import org.disq_bio.disq.HtsjdkReadsRdd;
import org.disq_bio.disq.HtsjdkReadsRddStorage;

import scala.Function1;
import scala.Option;
import scala.Tuple2;

/**
 * Count alignments example implemented in Java.
 */
public final class JavaCountAlignments {

  /**
   * Main.
   *
   * @param args command line arguments
   */
  public static void main(final String[] args) throws Exception {
    if (args.length < 1) {
      System.err.println("at least one argument required, e.g. foo.sam");
      System.exit(1);
    }

    SparkConf conf = new SparkConf()
      .setAppName("Java Count Alignments")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .set("spark.kryo.registrator", "org.disq_bio.disq.serializer.DisqKryoRegistrator")
      .set("spark.kryo.referenceTracking", "true");

    JavaSparkContext jsc = new JavaSparkContext(new SparkContext(conf));

    HtsjdkReadsRddStorage htsjdkReadsRddStorage = HtsjdkReadsRddStorage.makeDefault(jsc);
    HtsjdkReadsRdd htsjdkReadsRdd = htsjdkReadsRddStorage.read(args[0]);

    JavaRDD<SAMRecord> reads = htsjdkReadsRdd.getReads();

    JavaRDD<String> referenceNames = reads.map(new Function<SAMRecord, String>() {
        @Override
        public String call(final SAMRecord rec) {
          return rec.getReadUnmappedFlag() ? "unmapped" : rec.getReferenceName();
        }
      });

    JavaPairRDD<String, Integer> counts = referenceNames.mapToPair(new PairFunction<String, String, Integer>() {
        @Override
        public Tuple2<String, Integer> call(final String referenceName) {
          return new Tuple2<String, Integer>(referenceName, Integer.valueOf(1));
        }
      });

    JavaPairRDD<String, Integer> reducedCounts = counts.reduceByKey(new Function2<Integer, Integer, Integer>() {
        @Override
        public Integer call(final Integer value0, final Integer value1) {
          return Integer.valueOf(value0.intValue() + value1.intValue());
        }
      });

    reducedCounts.foreach(new VoidFunction<Tuple2<String, Integer>>() {
        @Override
        public void call(final Tuple2<String, Integer> count) {
          System.out.println(count.toString());
        }
      });
  }
}
