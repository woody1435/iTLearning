/**
 * Copyright (C), 2015-2020, yourchoice
 * FileName: Person
 * Author:   woody
 * Date:     2020/4/12 12:08
 * Description: 人类
 * Version: 0.0.1
 */
package org.ddzj.base;

import java.io.*;

/**
 * 对象要使用clone方法，需implements Cloneable接口，然后覆盖clone方法
 * 使用串行化克隆对象，需实现Serializable接口
 */
public class Person implements Cloneable, Serializable, Comparable<Person> {
    //名称
    private String name;
    //年龄
    private Integer age;
    //细胞
    private transient Cell cell;

    public Person() {
    }

    private static final long SerialVersionUID = 12311413424L;

    public Person(String name) {
        this.name = name;
    }

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public Person(Integer age, Cell cell) {
        this.age = age;
        this.cell = cell;
    }

    public Person(String name, Integer age, Cell cell) {
        this.name = name;
        this.age = age;
        this.cell = cell;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    /**
     * @return
     * @see Object#toString()
     * 覆盖toString()，否则打印结果为：org.ddzj.base.Person@548e7350
     */
    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", cell=" + cell +
                '}';
    }

    /**
     * 访问修改符改为public
     * 返回类型强转为Person
     *
     * @see Cloneable
     */
    @Override
    public Person clone() throws CloneNotSupportedException {
        return (Person) super.clone();
    }

    /**
     * @param ；
     * @return org.ddzj.base.Person
     * @description 深克隆方式1
     * @author woody
     * @修改人及修改内容
     * @since 0.0.1
     */
    public Person deepClone1() throws CloneNotSupportedException {
        Person person = (Person) super.clone();
        person.cell = cell.clone(); //引用类型重新指向与原本地址不一样的克隆对象，具体没想明白！！！
        return person;
    }

    /**
     * @param ；
     * @return org.ddzj.base.Person
     * @description 深克隆方式2，利用串行
     * 需实现{@code Serializable}接口
     * @author woody
     * @修改人及修改内容
     * @see https://blog.csdn.net/pandajava/article/details/42042325
     * @since 0.0.1
     */
    public Person deepClone2() throws Exception {
        //将对象写到流中
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(this);
        //从流中读出来
        ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
        ObjectInputStream oi = new ObjectInputStream(bi);
        return (Person) oi.readObject();
    }

    /**
     * @param o Person；
     * @return boolean
     * @description 重写equals方法，只要姓名和年龄相同，则表示同一个人
     * @author woody
     * @修改人及修改内容
     * @since 0.0.1
     */
    @Override
    public boolean equals(Object o) {
        return this.getName().equals(((Person) o).getName()) && this.getAge().equals(((Person) o).getAge());
    }

    /**
     * 重写equals要重写hashCode方法
     *
     * @return
     */
    @Override
    public int hashCode() {
        return this.getName().hashCode() * 31 + age;
    }

    /**
     * @param ；
     * @return void
     * @description 覆盖Object finalize
     * @author woody
     * @修改人及修改内容
     * @see sun_jdk/doc/010.关键字.md:40
     * @since 0.0.1
     */
    @Override
    public void finalize() throws Throwable {
        System.out.println(this.getClass().getName() + " 我被回收了");
        super.finalize();
    }

    @Override
    public int compareTo(Person person) {
        if (this.getAge() > person.getAge()) {
            return 1;
        } else if (this.getAge() < person.getAge()) {
            return -1;
        } else {
            return 0;
        }
    }
}
