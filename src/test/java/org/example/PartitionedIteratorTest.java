package org.example;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;


class PartitionedIteratorTest {

    private final PartitionedIterator<TestRecord, TestRecord> partitionedIterator = new PartitionedIterator<>(getListTestRecordFunction());

    @Test
    void test_empty() {
        final PageImpl<TestRecord> testRecords = new PageImpl<>(List.of());
        final List<TestRecord> result = partitionedIterator.iterate(testRecords);

        assertEquals(0, result.size());
    }

    @Test
    void test_1Partition() {
        final PageImpl<TestRecord> testRecords = new PageImpl<>(List.of(givenTest(1L), givenTest(1L)));
        final List<TestRecord> result = partitionedIterator.iterate(testRecords);

        assertEquals(1, result.size());
    }

    @Test
    void test_2Partitions() {
        final PageImpl<TestRecord> testRecords = new PageImpl<>(List.of(givenTest(1L), givenTest(1L),givenTest(2L)));
        final List<TestRecord> result = partitionedIterator.iterate(testRecords);

        assertEquals(2, result.size());
    }


    private TestRecord givenTest(long aLong) {
        return new TestRecord(aLong, "foo", 23);
    }

    private static Function<List<TestRecord>, TestRecord> getListTestRecordFunction() {
        return objects -> objects.stream().findFirst().orElse(null);
    }

    private record TestRecord(long aLong, String aText, int anInt) {
    }


}
