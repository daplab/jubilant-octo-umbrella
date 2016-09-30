

# Pointers


* https://ci.apache.org/projects/flink/flink-docs-release-1.1/apis/streaming/connectors/kafka.html
* https://github.com/apache/incubator-beam/blob/master/runners/flink/examples/src/main/java/org/apache/beam/runners/flink/examples/streaming/KafkaIOExamples.java

# Issues

Flink is not lambda friendly, for instance:
```
  .map(t -> Tuple3.of(t.getCardNumber(), t.getClientCountry(), t.getTimestamp()))
```

throws 

```
org.apache.flink.api.common.functions.InvalidTypesException: The return type of function 'internalRunWithZkAndKafkaAndTopic(RunFlink.java:69)' could not be determined automatically, due to type erasure. You can give type information hints by using the returns(...) method on the result of the transformation call, or by letting your function implement the 'ResultTypeQueryable' interface.

	at org.apache.flink.streaming.api.transformations.StreamTransformation.getOutputType(StreamTransformation.java:269)
	at org.apache.flink.streaming.api.datastream.DataStream.addSink(DataStream.java:1069)
	at ch.daplab.stream_processing.flink.RunFlink.internalRunWithZkAndKafkaAndTopic(RunFlink.java:71)
	at ch.daplab.yarn.AbstractZkAndKafkaAndTopicAppLauncher.internalRun(AbstractZkAndKafkaAndTopicAppLauncher.java:34)
	at ch.daplab.yarn.AbstractAppLauncher.run(AbstractAppLauncher.java:65)
	at org.apache.hadoop.util.ToolRunner.run(ToolRunner.java:70)
```

while 

```
    .map(new MapFunction<Transaction, Tuple3<String, String, Long>>() {
        public Tuple3<String, String, Long> map(Transaction t) throws Exception {
            return Tuple3.of(t.getCardNumber(), t.getClientCountry(), t.getTimestamp());
        }
    })
```

would work properly.
