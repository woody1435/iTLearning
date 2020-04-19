package org.ddzj.java.lang;

import org.junit.Test;

import java.util.Properties;

public class IntegerTest {

    @Test
    public void toStringTest() {
    }

    @Test
    public void toUnsignedString() {
    }

    @Test
    public void toHexString() {
    }

    @Test
    public void toOctalString() {
    }

    @Test
    public void toBinaryString() {
    }

    @Test
    public void formatUnsignedInt() {
    }

    @Test
    public void toString1() {
    }

    @Test
    public void toUnsignedString1() {
    }

    @Test
    public void getChars() {
    }

    @Test
    public void stringSize() {
    }

    @Test
    public void parseInt() {
    }

    @Test
    public void parseInt1() {
    }

    @Test
    public void parseUnsignedInt() {
    }

    @Test
    public void parseUnsignedInt1() {
    }

    @Test
    public void valueOf() {
    }

    @Test
    public void valueOf1() {
    }

    @Test
    public void valueOf2() {
    }

    @Test
    public void byteValue() {
        Integer i = 5;
        byte b = i.byteValue();
        System.out.println(b);
//        System.out.println(b instanceof byte); //必须为引用类型，不能是基本类型
    }

    @Test
    public void shortValue() {
    }

    @Test
    public void intValue() {
    }

    @Test
    public void longValue() {
    }

    @Test
    public void floatValue() {
    }

    @Test
    public void doubleValue() {
        Integer i = 5;
        System.out.println(i.doubleValue());
    }

    @Test
    public void toString2() {
    }

    @Test
    public void hashCodeTest() {
    }

    @Test
    public void hashCode1() {
    }

    @Test
    public void equals() {
        Integer i = 3;
        int ii = 3;
        System.out.println(i.equals(ii));
    }

    @Test
    public void getInteger() {
    }

    @Test
    public void getInteger1() {
        Properties properties = System.getProperties();
        System.out.println(properties.toString());
        System.out.println(Integer.getInteger(System.getProperty("os.name"),4));
    }

    @Test
    public void getInteger2() {
    }

    @Test
    public void decode() {
        String i1 = "5";
        System.out.println(Integer.decode(i1));
        String i3 = "-5";
        System.out.println(Integer.decode(i3));
        String i4 = "+5";
        System.out.println(Integer.decode(i4));
        String i6 = "#5";
        System.out.println(Integer.decode(i6));
        String i8 = "0X7ff";
        System.out.println(Integer.decode(i8));
        String i2 = "5.0";
        System.out.println(Integer.decode(i2)); //不可以
        String i7 = "5%";
        System.out.println(Integer.decode(i7)); //不可以
    }

    @Test
    public void compareTo() {
    }

    @Test
    public void compare() {
    }

    @Test
    public void compareUnsigned() {
    }

    @Test
    public void toUnsignedLong() {
    }

    @Test
    public void divideUnsigned() {
    }

    @Test
    public void remainderUnsigned() {
    }

    @Test
    public void highestOneBit() {
    }

    @Test
    public void lowestOneBit() {
        System.out.println(Integer.lowestOneBit(13));
    }

    @Test
    public void numberOfLeadingZeros() {
    }

    @Test
    public void numberOfTrailingZeros() {
    }

    @Test
    public void bitCount() {
    }

    @Test
    public void rotateLeft() {
    }

    @Test
    public void rotateRight() {
    }

    @Test
    public void reverse() {
    }

    @Test
    public void signum() {
    }

    @Test
    public void reverseBytes() {
    }

    @Test
    public void sum() {
    }

    @Test
    public void max() {
    }

    @Test
    public void min() {
    }
}