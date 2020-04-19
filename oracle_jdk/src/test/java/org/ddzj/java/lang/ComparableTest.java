package org.ddzj.java.lang;

import org.ddzj.base.Person;
import org.junit.Test;

public class ComparableTest {

    @Test
    public void compareTo() {
        Person p1 = new Person("亚当", 18);
        Person p2 = new Person("夏娃",18);
        System.out.println(p2.compareTo(p1));
    }
}