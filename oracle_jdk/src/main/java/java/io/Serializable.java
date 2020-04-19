/*
 * Copyright (c) 1996, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package java.io;

/**
 * @see org.ddzj.java.io.SerializableTest#main(java.lang.String[])
* @作用 标记型接口
 *
 * 序列化接口没有方法或字段，仅用于标识可序列化的语义
 * 序列化作用：
 * 简单说就是为了保存在内存中的各种对象的状态，并且可以把保存的对象状态再读出来。虽然你可以用你自己的各种各样的方法来保存Object States，但是Java给你提供一种应该比你自己好的保存对象状态的机制,那就是序列化
 * 什么情况下需要序列化:
 * a）当你想把的内存中的对象保存到一个文件中或者数据库中时候；
 * b）当你想用套接字在网络上传送对象的时候；
 * c）当你想通过RMI传输对象的时候；
 *
 * 如：A.java在编译时，除了将A.class保存在磁盘上，额外生成一个用于比对序列化版本信息用的文件，当在需要从磁盘上恢复A时，
 * 通过恢复后的文件得到新的串行化版本ID，与原串行化版本ID比较，一致表示没修改过
 *
* @原理 原理描述
*  @see https://blog.csdn.net/u014750606/article/details/80040130
 * @see https://www.cnblogs.com/gw811/archive/2012/10/10/2718331.html
 * 1.当实现java.io.Serializable接口中没有显示的定义serialVersionUID变量的时候，JAVA序列化机制会根据Class自动
 * 生成一个serialVersionUID作序列化版本比较用，这种情况下，如果Class文件(类名，方法明等)没有发生变化
 * (增加空格，换行，增加注释等等)，就算再编译多次，serialVersionUID也不会变化的。
 * 2.如果我们不希望通过编译来强制划分软件版本，即实现序列化接口的实体能够兼容先前版本，就需要显示的定义一个serialVersionUID
 * ，该字段必须是静态 (static)、最终 (final) 的 long 型字段。不修改这个变量值的序列化实体，都可以相互进行序列化和反序列化。
 *
* @备注
* 1.当一个对象被序列化时，只保存对象的非静态成员变量（包括声明为private的变量），不能保存任何的成员方法和静态的成员变量
 * (静态成员变量虽然在反序列化后还能打印出来，但那不是序列化文件中对象的状态值，是当前JVM中对应static变量的值，
 * 比如序列化后将静态成员变量值1改为2，则打印出来为2)。
* 2.如果一个对象的成员变量是一个对象，那么这个对象的数据成员也会被序列化。
* 3.如果一个可序列化的对象包含对某个不可序列化的对象的引用，那么整个序列化操作将会失败，并且会抛出一个
 * NotSerializableException。 我们可以将这个引用标记为transient，表示这个成员不需要序列化，那么对象就可以序列化。
 * @see sun_jdk/doc/010.关键字.md:121
* 4.当一个父类实现序列化，子类自动实现序列化，不需要显式实现Serializable接口；
*
* @since 0.0.1
*
* @author woody
*/
public interface Serializable {
}
