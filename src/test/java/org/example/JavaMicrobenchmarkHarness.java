package org.example;

import org.openjdk.jmh.annotations.*;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.IntStream;

import static org.example.PartitionedIteratorTest.getListTestRecordFunction;

public class JavaMicrobenchmarkHarness {


    @State(Scope.Thread)
    public static class MyState {

        @Setup(Level.Trial)
        public void doSetup() {
            testRecordList = IntStream.range(0, 1_000_000).mapToObj(operand -> new PartitionedIteratorTest.TestRecord(operand, "tfwef", 123)).toList();
            System.out.println("Do Setup");
        }

        @TearDown(Level.Trial)
        public void doTearDown() {
            System.out.println("Do TearDown");
        }

        public List<PartitionedIteratorTest.TestRecord> testRecordList;
        public final PartitionedIterator<PartitionedIteratorTest.TestRecord, PartitionedIteratorTest.TestRecord> partitionedIterator = new PartitionedIterator<>(getListTestRecordFunction());
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public void theTestMethod(MyState myState) {
        myState.partitionedIterator.iterate(new PageImpl<>(myState.testRecordList));
    }

}
