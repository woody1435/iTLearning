/*
 * Copyright (c) 2003, 2013, Oracle and/or its affiliates. All rights reserved.
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
package java.lang;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

/**
 * Implementing this interface allows an object to be the target of
 * the "for-each loop" statement. See
 * <strong>
 * <a href="{@docRoot}/../technotes/guides/language/foreach.html">For-each Loop</a>
 * </strong>
 *
 * @param <T> the type of elements returned by the iterator
 *
 * @since 1.5
 * @jls 14.14.2 The enhanced for statement
 */
/**
* @作用 实现此接口允许对象成为“for-each loop”语句的目标
*
* @原理 原理描述
*
* @备注 备注信息
*
* @author woody
*/
public interface Iterable<T> {
    /**
    * @作用 返回类型为 T元素的迭代器。
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @author woody
    */
    Iterator<T> iterator();

    /**
     * Performs the given action for each element of the {@code Iterable}
     * until all elements have been processed or the action throws an
     * exception.  Unless otherwise specified by the implementing class,
     * actions are performed in the order of iteration (if an iteration order
     * is specified).  Exceptions thrown by the action are relayed to the
     * caller.
     *
     * @implSpec
     * <p>The default implementation behaves as if:
     * <pre>{@code
     *     for (T t : this)
     *         action.accept(t);
     * }</pre>
     *
     * @param action The action to be performed for each element
     * @throws NullPointerException if the specified action is null
     * @since 1.8
     */
    /**
    * @作用 对 Iterable的每个元素执行给定的操作，直到所有元素都被处理或动作引发异常
    *
    * @原理 原理描述
    *
    * @备注 1.8新增方法，传入一个Consumer对象
    *
    * @author woody
    */
    default void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        for (T t : this) {
            action.accept(t);
        }
    }

    /**
     * Creates a {@link Spliterator} over the elements described by this
     * {@code Iterable}.
     *
     * @implSpec
     * The default implementation creates an
     * <em><a href="Spliterator.html#binding">early-binding</a></em>
     * spliterator from the iterable's {@code Iterator}.  The spliterator
     * inherits the <em>fail-fast</em> properties of the iterable's iterator.
     *
     * @implNote
     * The default implementation should usually be overridden.  The
     * spliterator returned by the default implementation has poor splitting
     * capabilities, is unsized, and does not report any spliterator
     * characteristics. Implementing classes can nearly always provide a
     * better implementation.
     *
     * @return a {@code Spliterator} over the elements described by this
     * {@code Iterable}.
     * @since 1.8
     */
    /**
    * @作用 在Iterable描述的元素上创建一个Iterable
     *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @author woody
    */
    default Spliterator<T> spliterator() {
        return Spliterators.spliteratorUnknownSize(iterator(), 0);
    }
}
