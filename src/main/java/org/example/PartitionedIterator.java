package org.example;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PartitionedIterator<T, G> {

    private final Function<List<T>, G> function;

    public PartitionedIterator(Function<List<T>, G> function) {
        this.function = function;
    }

    public List<G> iterate(Page<T> page) {
        final List<T> content = page.getContent();

        final List<G> result = new ArrayList<>();
        int currentPartitionStart = 0;
        for (int i = 0; i < content.size(); i++) {
            if ((i + 1) >= content.size()) {
                result.add(applyFunction(content, currentPartitionStart, i));
            } else {
                final T current = content.get(i);
                final T next = content.get(i + 1);

                if (!current.equals(next)) {
                    result.add(applyFunction(content, currentPartitionStart, i + 1));
                }
            }
        }

        return result;
    }

    private G applyFunction(List<T> list, int startIndex, int endIndex) {
        final List<T> ts = list.subList(startIndex, endIndex);

        return this.function.apply(ts);
    }

}
