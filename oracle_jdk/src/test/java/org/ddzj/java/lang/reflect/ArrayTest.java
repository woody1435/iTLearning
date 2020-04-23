package org.ddzj.java.lang.reflect;

import org.junit.Test;

import java.lang.reflect.Array;

public class ArrayTest {

    @Test
    public void newInstance() {
        int[] i = (int[])Array.newInstance(int.class, 3);
        i[0] = 3;
        i[1] = 2;
        i[2] = 1;
        System.out.println(Array.getInt(i,1));
    }

    @Test
    public void newInstance1() {
    }

    @Test
    public void getLength() {
        System.out.println(Array.getLength(new char[5]));
    }

    @Test
    public void get() {
    }

    @Test
    public void getBoolean() {
        boolean[] i = {true,false,true};
        System.out.println(Array.getBoolean(i,1));
    }

    @Test
    public void getByte() {
    }

    @Test
    public void getChar() {
    }

    @Test
    public void getShort() {
    }

    @Test
    public void getInt() {
        int[] i = {1,2,3};
        System.out.println(Array.getInt(i,2));
    }

    @Test
    public void getLong() {
    }

    @Test
    public void getFloat() {
    }

    @Test
    public void getDouble() {
    }

    @Test
    public void set() {
        double[] d = {1.2,2.3,3,4};
        Array.set(d,1,3.2);
        System.out.println(Array.get(d,1));
    }

    @Test
    public void setBoolean() {
    }

    @Test
    public void setByte() {
    }

    @Test
    public void setChar() {
    }

    @Test
    public void setShort() {
    }

    @Test
    public void setInt() {
    }

    @Test
    public void setLong() {
    }

    @Test
    public void setFloat() {
    }

    @Test
    public void setDouble() {
    }
}