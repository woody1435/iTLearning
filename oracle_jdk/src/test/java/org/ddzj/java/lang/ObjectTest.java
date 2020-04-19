package org.ddzj.java.lang;

import org.ddzj.base.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ObjectTest {

    /**
     * @param ；
     * @return void
     * @description 类方法功能描述
     * @author woody
     * @修改人及修改内容
     * @see Object#clone()
     * @since 0.0.1
     */
    @Test
    public void cloneTest() throws Exception {
        System.out.println("结论：修改基本数据类型或包装类型，不会影响原对象/克隆对象，因为重新从常量池中获取此常量的引用；" +
                "如果是对象类型，因为两个引用指向相同，所以会影响原对象/克隆对象");
        System.out.println("===============================测试案例一=========================================");
        Person person = new Person();
        person.setName("亚当");
        person.setAge(999);
        Cell cell = new Cell("1", "2", "3", "4", "5");
        person.setCell(cell);
        System.out.println("原对象：" + person.hashCode() + "：" + person);

        Person p1 = new Person();
        p1.setName("亚当");
        p1.setAge(999);
        Cell cell1 = new Cell("1", "2", "3", "4", "5");
        p1.setCell(cell1);
        System.out.println("构造方法构造的对象：" + p1.hashCode() + "：" + p1);

        Person p2 = (Person) Class.forName("org.ddzj.base.Person").newInstance();
        p2.setName("亚当");
        p2.setAge(999);
        Cell cell2 = new Cell("1", "2", "3", "4", "5");
        p2.setCell(cell2);
        System.out.println("反射构造的对象：" + p2.hashCode() + "：" + p2);

        Person p3 = person.clone();
        System.out.println("克隆创建对象：" + p3.hashCode() + "：" + p3);

        System.out.println("=============修改person后==============");
        person.setName("夏娃");
        cell.setXiBaoBi("11");
        System.out.println(person.hashCode() + ":" + person + ":" + person.getCell().hashCode());
        System.out.println(p3.hashCode() + ":" + p3 + ":" + p3.getCell().hashCode());

        System.out.println("=============修改 克隆对象 的对象属性的属性 后==============");
        p3.setName("夏娃娃");
        Cell cell4 = p3.getCell();
        cell4.setXiBaoBi("111");
        System.out.println(person.hashCode() + ":" + person);
        System.out.println(p3.hashCode() + ":" + p3);

        System.out.println("=============测试案例二==============");
        List<Object> list = new ArrayList() {
        };
        list.add("a");
        list.add(3);
        Object[] objs = {1, list}; //初始化
        Object[] objClone = objs.clone(); //克隆

        System.out.println(objs[0] == objClone[0]); //判断objs的1是否和objClone的1是否是相同对象（即引用是否相同）
        objClone[0] = 2; //修改
        List<Object> _list1 = (List<Object>) objs[1];
        List<Object> _list2 = (List<Object>) objClone[1];
        System.out.println(_list1.get(1) == _list2.get(1));
        _list2.set(1, 4); //修改数组对象
        for (Object obj : objs) {
            System.out.println(obj);
        }
        for (Object obj : objClone) {
            System.out.println(obj);
        }

        System.out.println("=============测试案例三==============");
    }

    @Test
    public void deepCloneTest() throws Exception {
        Person pp = new Person();
        pp.setName("亚当");
        pp.setAge(999);
        Cell cc = new Cell("1", "2", "3", "4", "5");
        pp.setCell(cc);

        Person person1 = pp.deepClone1();
        System.out.println(person1);
        person1.getCell().setXiBaoBi("111");
        System.out.println(person1);
        System.out.println(pp);

        System.out.println("=============串行化深克隆==============");
        Person person2 = pp.deepClone2();
        System.out.println(person2);
        person2.getCell().setXiBaoBi("222");
        System.out.println(person2);
        System.out.println(pp);
    }

    /**
     * @param ；
     * @return void
     * @description clone与 new 效率对比
     * @author woody
     * @修改人及修改内容
     * @since 0.0.1
     */
    public void cloneCompareNew() throws CloneNotSupportedException {
        int COUNT = 10000 * 1000;

        long s1 = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            Person bean = new Person();
            bean.setName("");
            bean.setAge(11);
//            bean.setCell(new Cell());
        }
        long s2 = System.currentTimeMillis();
        System.out.println("new  = " + (s2 - s1));

        long s3 = System.currentTimeMillis();
        Person bean = new Person();
        bean.setName("");
        bean.setAge(11);
//        bean.setCell(new Cell());
        for (int i = 0; i < COUNT; i++) {
            Person b = bean.clone();
        }
        long s4 = System.currentTimeMillis();
        System.out.println("clone = " + (s4 - s3));
    }

    @Test
    public void hashCodeTest() {
        Person p1 = new Person();
        p1.setName("亚当");
        Person p2 = new Person();
        p2.setName("亚当");
        System.out.println(p1.hashCode());
        System.out.println(p2.hashCode());
        System.out.println(p1.equals(p2));

        System.out.println("================================================");
        String A = "aaa";
        String B = "aaa";
        System.out.println(A.hashCode());
        System.out.println(B.hashCode());
        System.out.println(A.equals(B));
    }

    @Test
    public void equals() {
        String A = "aaa";
        String B = "aaa";
        System.out.println(A == B); //此时A、B指向常量，其存放在常量池中，内存地址是一样的

        System.out.println("================================================");
        String AA = new String("aaa");
        String BB = new String("aaa");
        System.out.println(AA == BB); //此时AA、BB指向对象，虽然其引用还是指向常量池中，但对象是存放在堆中的，内存地址不一样

        System.out.println("================================================");
        //当intern()方法被调用，如果池中已经有了这个String对象，那么直接返回，另一方面如果没有那么就将这个String对象添加到池中，并返回这个字符串的引用
        String AAA = AA.intern();
        String BBB = BB.intern();
        System.out.println(AAA == BBB);

        System.out.println("================================================");
        //调用重写的equals方法
        Person p1 = new Person("亚当", 999);
        Person p2 = new Person("亚当", 999);
        System.out.println(p1.equals(p2));

        System.out.println("================================================");
        //不重写则返回false
        System.out.println(p1.hashCode() == p2.hashCode());
    }

    /**
     * @param ；
     * @return void
     * @description 因Object为所有类基类，所以方法名一样，返回值一样
     * @author woody
     * @修改人及修改内容
     * @since 0.0.1
     */
    @Test
    public void getClassTest(){
        Person person = new Person();
        person.setName("11");
        person.setAge(11);
        Class<? extends Person> c = person.getClass();
        System.out.println(c.getName());
    }


    @Test
    public void toStringTest() {
        Object obj = new Object();
        System.out.println( obj.hashCode());
        System.out.println(obj.toString());
    }

    /**
     * 案例：有一个生产者和一个消费者，当仓库有一个商品时就不能生产，让消费者来消费，当没有商品时就不能消费，让生产者来生产
     *
     * 需要使用main方法，否则线程停止
     *
     * 生产者生产：娃哈哈:冰红茶
     * 消费者消费：娃哈哈:冰红茶
     * 生产者生产：康师傅:牛肉面
     * 消费者消费：康师傅:牛肉面
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("========================线程同步与通知========================");
        Product product = new Product();
        Runnable producer = new Producer(product);
        Runnable consumer = new Consumer(product);

        new Thread(producer).start();
        new Thread(consumer).start();
    }

    @Test
    public void notifyTest() {
    }

    @Test
    public void notifyAllTest() {
    }

    @Test
    public void waitTest() {
    }

    @Test
    public void wait1() {
    }

    @Test
    public void wait2() {
    }

    @Test
    public void finalize() {
        Person person = new Person("亚当",11,new Cell("1","2","3","4","5"));
        try {
            person.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}