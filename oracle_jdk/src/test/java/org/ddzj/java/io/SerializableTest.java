package org.ddzj.java.io;

import org.ddzj.base.Person;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
* @see java.io.Serializable
 *@see Person
 * @作用 测试序列化
*
* @原理 原理描述
*
* @备注 备注信息
*
* @since 0.0.1
*
* @author woody
*/
public class SerializableTest {

    public static void main(String[] args) throws Exception {
        // 序列化
        Person person = new Person();
        person.setName("woody");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("D://person.ser"));
        objectOutputStream.writeObject(person);
        objectOutputStream.close();

        // 反序列化
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("D://person.ser"));
        Person p = (Person)objectInputStream.readObject();
        System.out.println(p);
    }

}