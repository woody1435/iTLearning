/*
 * Copyright (c) 2009, 2013, Oracle and/or its affiliates. All rights reserved.
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
* @作用 保存资源的对象(例如文件)，直到关闭
*
* @原理 原理描述
*
* @备注 实现此接口可以使用try-with-resource 来实现异常处理和资源关闭
*
* @since 0.0.1
*
* @author woody
*/
public interface AutoCloseable {
    /**
    * @作用 关闭这个流并释放与之关联的任何系统资源
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    void close() throws Exception;
}
