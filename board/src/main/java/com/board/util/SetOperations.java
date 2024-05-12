package com.board.util;

import java.util.List;
import java.util.stream.Collectors;

public class SetOperations {

    public static <T> List<T> findDifference(List<T> list1, List<T> list2) {
        return list1.stream()
                .filter(item -> list2.stream().noneMatch(item::equals))
                .collect(Collectors.toList());
    }
}
