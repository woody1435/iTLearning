/*
 * Copyright (c) 2012, Oracle and/or its affiliates. All rights reserved.
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

package java.lang.annotation;


/**
 * Indicates that a field defining a constant value may be referenced
 * from native code.
 *
 * The annotation may be used as a hint by tools that generate native
 * header files to determine whether a header file is required, and
 * if so, what declarations it should contain.
 *
 * @since 1.8
 */
/**
* @作用 表明一个字段引用的值可能来自于本地代码
*
* @原理 原理描述
*
* @备注 备注信息
*
* @since 0.0.1
*
* @author woody
*/
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface Native {
}
