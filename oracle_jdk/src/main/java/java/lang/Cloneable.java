/*
 * Copyright (c) 1995, 2004, Oracle and/or its affiliates. All rights reserved.
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

/**
* @作用 用于标识可拷贝的语义
* @see org.ddzj.base.Person
* @原理 原理描述
*
* @备注 子类要使用clone方法，覆盖clone方法，还需实现此Cloneable接口，否则会报CloneNotSupportedException异常
*
* @since 0.0.1
*
* @author woody
*/
public interface Cloneable {
}
