package java.lang;

/**
 * 所有类的基类
 * 可在其它所有类上使用 ctrl+h 查看类继承关系
 * 在JDK1.6中，类会在编译期间显式的加上extends关键字来达到默认继承Object的效果；
 * JDK1.7及以上版本会在JVM运行期间判断是否定义父级，然后进行继承操作
 */
public class Object {

    /**
    * @description 本地方法，用native修饰，底层代码使用c/c++/汇编 实现，通过jvm的本地方法栈实现交互
     *
     * 注册本地方法（一个Java程序要想调用一个本地方法，需要执行两个步骤：第一，通过System.loadLibrary()将包含
     * 本地方法实现的动态文件加载进内存；第二，当Java程序需要调用本地方法时，虚拟机在加载的动态文件中定位并链接该本地方法
     * ，从而得以执行本地方法。registerNatives()方法的作用就是取代第二步，让程序主动将本地方法链接到调用方，
     * 当Java程序需要调用本地方法时就可以直接调用，而不需要虚拟机再去定位并链接。）
     *当包含registerNatives()方法的类被加载的时候，注册的方法就是该类所包含的除了registerNatives()方法以外的所有本地方法。
    *@see System#loadLibrary(java.lang.String)
     *
    * @since 0.0.1
    * @param   ；
    * @return void
    * @author woody
    * @修改人及修改内容
    */
    private static native void registerNatives();
    static {
        registerNatives();
    }

    /**
    * @作用 获取对象运行时的类信息。
     * 类是用来描述对象的属性和行为的，获得实例的class对象之后，你就可以调用其中的一些方法获得该类型的信息了
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public final native Class<?> getClass();

    /**
     * 返回对象哈希值
     * 如果两个对象的HashCode相同，不代表两个对象就相同，只能说明这两个对象在散列存储结构中，存放于同一个位置
     * 如果两个对象equals相等，那么这两个对象的HashCode一定也相同（规范（所以 如果对象的equals方法被重写，那么对象的HashCode方法也要重写））
     *
     * @link https://www.cnblogs.com/whgk/p/6071617.html
     * 哈希值对应的是在hash表中的位置（通过散列算法：将任意输入变换成固定长度输出），hash表的位置是有限的，
     * 所以hash表中一个位置可能会存多个值。例如：有两个对象，其内存地址为8和15，假设散列算法计算方式为除7取余，
     * 则hashCode 8%7 == 15%7 == 1，都存放在hash表中第1个位置
     *
     * 这样做的好处就是，比如，检查1000个数中是否包含A，只需得到A的hashCode，
     * 再去散列表中对应位置去找有没有equals的对象即可判断，而不用写一个1000的循环来比较
     *
     * 如果对象的equals方法被重写，那么对象的HashCode方法也要重写
     * 参考以下实现
     * @see String#equals(java.lang.Object)
     * @see String#hashCode()
     *
     * @see Double#equals(java.lang.Object)
     * @see Double#hashCode(double)
     *
     * @see     java.lang.Object#equals(java.lang.Object)
     * @see     java.lang.System#identityHashCode
     */
    public native int hashCode();

    /**
     * 比较两个对象的引用是否相等，即是否指向同一个对象
     * ==是一个比较运算符号,既可以比较基本数据类型,也可以比较引用数据类型,基本数据类型比较的是值,引用数据类型比较的是地址值
     *
     * java中基本类型的包装类的大部分都实现了常量池技术，即Byte,Short,Integer,Long,Character,Boolean。
     * 这5种包装类默认创建了数值[-128，127]的相应类型的缓存数据，但是超出此范围仍然会去创建新的对象。
     * 两种浮点数类型的包装类Float,Double并没有实现常量池技术
     *
     * 如果对象的equals方法被重写，那么对象的HashCode方法也要重写
     * @see     #hashCode()
     * 参考以下实现
     * @see String#equals(java.lang.Object)
     * @see String#hashCode()
     *
     * @see Double#equals(java.lang.Object)
     * @see Double#hashCode(double)
     *
     */
    public boolean equals(Object obj) {
        return (this == obj);
    }

    /**
     * 浅拷贝，即拷贝对象中属性与原对象中属性引用地址一样。（拿人手短）
     * 拷贝后对象与原对象hashCode值不一样。
     * 如果属性是对象类型，修改会影响原对象/克隆对象；如果是基本数据类型或包装类型，不会影响原对象，会重新从常量池中分配此常量的引用。
     * @see org.ddzj.java.lang.ObjectTest#cloneTest()
     *
     * 要使用{@code clone}方法，需实现 {@code Cloneable}接口
     * @see java.lang.Cloneable
     * {@code java.lang.Object}不能使用{@code clone}，因为其没有实现{@code Cloneable}接口，会报运行时异常
     *
     * java.lang.Object#clone()使用场景：
     * 对象字段为基本数据类型或包装类型且不是很多时可以使用new，其效率甚至比clone快。
     * 有对象类型字段时，只修改基本数据类型或包装类型时，可以节省开销
     * @see org.ddzj.java.lang.ObjectTest#cloneCompareNew()
     *
     *
     * 为什么我们在派生类中覆盖Object的clone()方法时，一定要调用super.clone()呢？
     * 在运行时刻，Object中的clone()识别你要复制的是哪一个对象，然后为此对象分配空间，
     * 并进行对象的复制，将原始对象的内容（指引用）一一复制到新对象的存储空间中。
     *
     *
     * 深拷贝：在计算机中开辟一块新的内存地址用于存放复制的对象，改变不会使原数据一同改变(自食其力)
     * @see org.ddzj.java.lang.ObjectTest#deepCloneTest()
     */
    protected native Object clone() throws CloneNotSupportedException;

    /**
     * @作用
     * 返回表示该对象信息的一个字符串表达形式，全类名+哈希值转换后的无符号表示形式
     * 建议所有子类重写此方法
     */
    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }

    /**
    * @作用 唤醒在此对象监视器上（也叫锁）等待的单个线程。如果所有线程都在此对象上等待，则会选择唤醒其中一个线程。
     * 选择是任意性的，并在对实现做出决定时发生； 等待池中的线程不能获取锁，而是需要被唤醒进入锁池，才有获取到锁的机会
    *@see org.ddzj.java.lang.ObjectTest#main(java.lang.String[])
     *
    * @原理 原理描述
    *
    * @备注 wait、notify、notifyAll等方法之所以要提供在Object类中而不是在Thread类中，是因为任意对象都可能成为锁
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public final native void notify();

    /**
    * @作用 唤醒在此对象监视器上等待的所有线程
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public final native void notifyAll();

    /**
    * @作用 在其他线程调用此对象的 notify() 方法或 notifyAll() 方法
     * 或在指定已经过去的时间，进入锁池（不是一定会进入），需要当前线程必须拥有该对象的监视器。
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public final native void wait(long timeout) throws InterruptedException;

    /**
    * @作用 在其他线程调用此对象的 notify() 方法或 notifyAll() 方法 	或在指定已经过去的时间，
     * 附加时间在毫微秒范围0-999999，进入锁池（不是一定会进入），需要当前线程必须拥有该对象的监视器
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public final void wait(long timeout, int nanos) throws InterruptedException {
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }

        if (nanos < 0 || nanos > 999999) {
            throw new IllegalArgumentException(
                                "nanosecond timeout value out of range");
        }

        if (nanos > 0) {
            timeout++;
        }

        wait(timeout);
    }

    /**
    * @作用 线程在指定的监视器上等待
    * @see org.ddzj.java.lang.ObjectTest#main(java.lang.String[])
    * @原理 原理描述
    *
    * @备注 每个对象都拥有两个池，分别为锁池(EntrySet)和(WaitSet)等待池。
    * 锁池：假如已经有线程A获取到了锁，这时候又有线程B需要获取这把锁(比如需要调用synchronized修饰的方法或者
     * 需要执行synchronized修饰的代码块)，由于该锁已经被占用，所以线程B只能等待这把锁，这时候线程B将会进入这把锁的锁池。
    * 等待池：假设线程A获取到锁之后，由于一些条件的不满足(例如生产者消费者模式中生产者获取到锁，然后判断队列为满)，
     * 此时需要调用对象锁的wait方法，那么线程A将放弃这把锁，并进入这把锁的等待池
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public final void wait() throws InterruptedException {
        wait(0);
    }

    /**
    * @作用 当垃圾回收器确定不存在对该对象的引用时，垃圾回收器调用此方法, 相当于对象死亡之前的回调
    *
    * @原理 原理描述
    *
    * @备注 但GC只有在内存不足时才会进行垃圾回收
     * 但使用终结方法会导致行为不稳定、降低性能，以及可移植性问题。所以，我们应该避免使用终结方法
    *@see https://www.jb51.net/article/125728.htm
     *
    * @since 0.0.1
    *
    * @author woody
    */
    protected void finalize() throws Throwable { }
}
