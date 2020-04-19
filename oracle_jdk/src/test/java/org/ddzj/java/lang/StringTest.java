package org.ddzj.java.lang;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Locale;

public class StringTest {

    /**
     * 内部类：判断String s = "";String s = new String("");存储位置
     */
    class StringStorage {

        public String name = "hello world";

        public String phone;

        public StringStorage(String phone) {
            this.phone = phone;
        }
    }

    /**
     * 结论：String s = "abc"直接指向了 常量池 中。而new String("abc")是在Heap中创建了一个指向 常量池 的引用
     *
     * @param ；
     * @return void
     * @description 判断String s = "";String s = new String("");存储位置
     * @author woody
     * @修改人及修改内容
     * @see https://blog.csdn.net/penyoudi1/article/details/79163567
     * @since 0.0.1
     */
    @Test
    public void StringStorage() throws Exception {
        StringStorage a = new StringStorage("hello world");
        StringStorage b = new StringStorage("hello world");
        String c = "hello world";
        String e = new String("hello world");

        //首先判断不同对象中的字符串地址是否相等
        System.out.println("a.name==a.phone:" + (a.name == a.phone)); //true
        System.out.println("a.name==b.phone:" + (a.name == b.phone)); //true => 说明内存地址一致
        System.out.println("a.name==c:" + (a.name == c)); //true
        System.out.println("e==c:" + (e == c) + "\n"); //false -> c在常量池，e在堆内存

        //然后修改c在内存中的值
        Field hello_field = String.class.getDeclaredField("value");
        System.out.println(hello_field.toString());
        hello_field.setAccessible(true);

        char[] value = (char[]) hello_field.get(c);
        value[5] = '_';

        //首先判断不同对象中的字符串地址是否相等
        System.out.println("a.name==a.phone:" + (a.name == a.phone)); //true
        System.out.println("a.name==b.phone:" + (a.name == b.phone)); //true
        System.out.println("a.name==c:" + (a.name == c)); //true
        System.out.println("e==c:" + (e == c) + "\n"); //false

        //直接输出值判断是否发生变化 -> 存储的值全部发生改变
        System.out.println("a.name: " + a.name); //hello_world
        System.out.println("b.name: " + b.name); //hello_world
        System.out.println("a.phone: " + a.phone); //hello_world
        System.out.println("b.phone: " + b.phone); //hello_world
        System.out.println("c: " + c); //hello_world
        System.out.println("e: " + e); //hello_world
    }

    @Test
    public void newString() {
        String s1 = new String();
        System.out.println(s1);

        System.out.println("==========================================");
        String s2 = new String("str");
        System.out.println(s2);

        System.out.println("==========================================");
        char str[] = {'1', '2', '3'};
        String s3 = new String(str);
        System.out.println(s3);

        System.out.println("==========================================");
        char str1[] = {'1', '2', '3'};
        String s4 = new String(str1, 1, 2);
        System.out.println(s4);

        System.out.println("==========================================");
        int i1[] = {100, 99, 01111111};
        String s5 = new String(i1, 0, 3);
        System.out.println(s5);

        System.out.println("==========================================");
        byte[] b1 = {'a', 'b', 'c'};
        try {
            String s6 = new String(b1, 1, 2, "UTF-16");
            System.out.println(s6);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println("==========================================");
        byte[] b2 = {'a', 'b', 'c'};
        try {
            System.out.println(Charset.defaultCharset().name());
            String s7 = new String(b1, 1, 2, Charset.defaultCharset());
            System.out.println(s7);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("==========================================");
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("a");
        stringBuffer.append("bc");
        String s8 = new String(stringBuffer);
        System.out.println(s8);

        System.out.println("==========================================");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("e");
        stringBuilder.append("fh");
        String s9 = new String(stringBuilder);
        System.out.println(s9);

    }

    /**
     * @param ；
     * @return void
     * @description 当intern()方法被调用，如果池中已经有了这个String对象，那么直接返回，
     * 另一方面如果没有那么就将这个String对象添加到池中，并返回这个字符串的引用
     * @author woody
     * @修改人及修改内容
     * @since 0.0.1
     */
    @Test
    public void intern() {
        // 使用char数组来初始化a，避免在a被创建之前字符串池中已经存在了值为“abcd”的对象
        String a = new String(new char[]{'a', 'b', 'c', 'd'});
        String b = a.intern();
        if (b == a) {
            System.out.println("b被加入了字符串池中，没有新建对象"); // => 说明池中已经有此abcd字符串了
        } else {
            System.out.println("b没被加入字符串池中，新建了对象");
        }

        System.out.println("=============================================");
        String str1 = "a";
        String str2 = "b";
        String str3 = "a"+"b";
        String str4 = str1+str2;

        System.out.println(str3 == str4); //false -> str3在常量池，str4在堆内存
        str4 = (str1 + str2).intern();
        System.out.println(str3 == str4); //true  str4在常量池
    }

    @Test
    public void length() {
        System.out.println("abc".length());
    }

    @Test
    public void isEmpty() {
        System.out.println("".isEmpty());
    }

    @Test
    public void charAt() {
        String s11 = "1234567890";
        System.out.println(s11.charAt(9));
    }

    @Test
    public void codePointAt() {
        String s12 = "123456789@";
        System.out.println(s12.codePointAt(9)); //@的ascii码是64
    }

    @Test
    public void codePointBefore() {
        String s12 = "123456789@";
        System.out.println(s12.codePointBefore(9));
    }

    @Test
    public void codePointCount() {
        String s12 = "123456789@";
        System.out.println(s12.codePointCount(8, 9));
    }

    @Test
    public void offsetByCodePoints() {
        String s12 = "123456789@";
        System.out.println(s12.offsetByCodePoints(7, 2));
    }

    @Test
    public void getChars() {
        String s13 = new String("abc");
        byte[] b3 = s13.getBytes();
        for (byte b : b3) {
            System.out.println(b);
        }
    }

    @Test
    public void getChars1() {
    }

    @Test
    public void getBytes() {
    }

    @Test
    public void getBytes1() {
    }

    @Test
    public void getBytes2() {
    }

    @Test
    public void getBytes3() {
    }

    @Test
    public void equals() {
        boolean abc = "abc".equals(new String("abc"));
        System.out.println(abc);
    }

    @Test
    public void contentEquals() {
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer append = stringBuffer.append('a').append('b');
        System.out.println("ab".contentEquals(append));
    }

    @Test
    public void contentEquals1() {
    }

    @Test
    public void equalsIgnoreCase() {
        System.out.println("ab".equalsIgnoreCase("AB"));
    }

    @Test
    public void compareTo() {
        int i = "ab".compareTo("aB");
        System.out.println(i);
    }


    @Test
    public void compareToIgnoreCase() {
        int i = "ab".compareToIgnoreCase("aB");
        System.out.println(i);
    }

    @Test
    public void regionMatches() {
        boolean ac = "ab".regionMatches(0, "ac", 0, 1);
        System.out.println(ac);
    }

    @Test
    public void regionMatches1() {
        boolean ac = "ab".regionMatches(true,0, "Ac", 0, 1);
        System.out.println(ac);
    }

    @Test
    public void startsWith() {
        boolean ab = "abc".startsWith("ab");
        System.out.println(ab);
    }

    @Test
    public void startsWith1() {
        boolean ab = "abc".startsWith("bc",1);
        System.out.println(ab);
    }

    @Test
    public void endsWith() {
        boolean ab = "abc".endsWith("bc");
        System.out.println(ab);
    }

    @Test
    public void hashCodeTest() {
        String str = "ab";
        System.out.println(str.hashCode()); //97*31+98 =3105
    }

    @Test
    public void indexOf() {
        String str = "abcde";
        System.out.println(str.indexOf(99,2));
    }

    @Test
    public void indexOf1() {
    }

    @Test
    public void lastIndexOf() {
    }

    @Test
    public void lastIndexOf1() {
    }

    @Test
    public void indexOf2() {
        String str = "abcdef";
        System.out.println(str.indexOf("bcd",0));
    }

    @Test
    public void indexOf3() {
    }

    @Test
    public void indexOf4() {
    }

    @Test
    public void indexOf5() {
    }

    @Test
    public void lastIndexOf2() {
        String str = "abcdefabc";
        System.out.println(str.lastIndexOf("abc",6));
    }

    @Test
    public void lastIndexOf3() {
    }

    @Test
    public void lastIndexOf4() {
    }

    @Test
    public void lastIndexOf5() {
    }

    @Test
    public void substring() {
        String str = "abcdef";
        System.out.println(str.substring(2));
    }

    @Test
    public void substring1() {
        String str = "abcdef";
        System.out.println(str.substring(2,5));
    }

    @Test
    public void subSequence() {
        String str = "abcdef";
        System.out.println(str.subSequence(2,5));
    }

    @Test
    public void concat() {
        System.out.println("hello ".concat("world"));
    }

    @Test
    public void replace() {
        System.out.println("abc".replace("bc","cb"));
    }

    @Test
    public void matches() {
        String likeType = "23";
        String pattern = "[a-zA-Z0-9]*[" + likeType + "]{1}[a-zA-Z0-9]*";
        String sourceStr = "adfjaslfj23ldfalsf";
        System.out.println(sourceStr.matches(pattern));
    }

    @Test
    public void contains() {
        System.out.println("abcdef".contains("cde"));
    }

    @Test
    public void replaceFirst() {
        System.out.println("abcdefabc".replaceFirst("abc","hhh"));
    }

    @Test
    public void replaceAll() {
        System.out.println("abcdefabc".replaceAll("abc","hhh"));
    }

    @Test
    public void replace1() {
        System.out.println("abcdefabc".replace("abc","hhh"));
    }

    @Test
    public void split() {
        String[] abcs = "abcdefabc".split("b",2);
        for (String abc : abcs) {
            System.out.println(abc);
        }
    }

    @Test
    public void split1() {
    }

    @Test
    public void join() {
        System.out.println("abc".join(",","d","e","f"));
    }

    @Test
    public void join1() {
    }

    @Test
    public void toLowerCase() {
        System.out.println("ABC".toLowerCase(Locale.US));
    }

    @Test
    public void toLowerCase1() {
    }

    @Test
    public void toUpperCase() {
    }

    @Test
    public void toUpperCase1() {
    }

    @Test
    public void trim() {
        System.out.println(" ab c ".trim());
    }

    @Test
    public void toStringTest() {
    }

    @Test
    public void toCharArray() {
        char[] chars = " ab c ".toCharArray();
        for (char aChar : chars) {
            System.out.println(aChar);
        }
    }

    @Test
    public void format() {
        System.out.println(String.format("Hi,%s %s %s", "小明","是个","大帅哥"));
    }

    @Test
    public void format1() {
    }

    @Test
    public void valueOf() {
        String str = null;
        System.out.println(String.valueOf(str));
    }

    @Test
    public void valueOf1() {
    }

    @Test
    public void valueOf2() {
        char[] c = {'a','b','e','d'};
        System.out.println(String.valueOf(c,1,3));
    }

    @Test
    public void copyValueOf() {
    }

    @Test
    public void copyValueOf1() {
    }

    @Test
    public void valueOf3() {
    }

    @Test
    public void valueOf4() {
        long l = 12345l;
        System.out.println(String.valueOf(l));
    }

    @Test
    public void valueOf5() {
    }

    @Test
    public void valueOf6() {
    }

    @Test
    public void valueOf7() {
    }

    @Test
    public void valueOf8() {
    }

}