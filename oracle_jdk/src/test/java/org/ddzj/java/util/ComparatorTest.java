package org.ddzj.java.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ComparatorTest {

    @Test
    public void compare() {
        List<Integer> iList = new ArrayList<>();
        iList.add(2);
        iList.add(4);

        Collections.sort(iList, new Comparator<Integer>() {
            /**
             * @description 整型数据从小到大排序
             * @version 1.0.0
             * @param i1 Integer；i2 Integer；
             * @return int
             */
            @Override
            public int compare(Integer i1, Integer i2) {
                return i1 - i2;
            }
        });
        iList.stream().forEach(item -> System.out.println(item));
    }

    @Test
    public void equals() {
    }

    @Test
    public void reversed() {
    }

    @Test
    public void thenComparing() {
    }

    @Test
    public void thenComparing1() {
    }

    @Test
    public void thenComparing2() {
    }

    @Test
    public void thenComparingInt() {
    }

    @Test
    public void thenComparingLong() {
    }

    @Test
    public void thenComparingDouble() {
    }

    @Test
    public void reverseOrder() {
    }

    @Test
    public void naturalOrder() {
    }

    @Test
    public void nullsFirst() {
    }

    @Test
    public void nullsLast() {
    }

    @Test
    public void comparing() {
    }

    @Test
    public void comparing1() {
    }

    @Test
    public void comparingInt() {
    }

    @Test
    public void comparingLong() {
    }

    @Test
    public void comparingDouble() {
    }
}