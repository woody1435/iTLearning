/*
 * Copyright (c) 1997, 2007, Oracle and/or its affiliates. All rights reserved.
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


package com.sun.jmx.snmp;





/**
* @作用 发生BER编码/解码错误时引发异常。
*
* @原理 原理描述
*
* @备注 备注信息
*
* @author woody
*/
public class BerException extends Exception {
  private static final long serialVersionUID = 494709767137042951L;

  public static final int BAD_VERSION=1;

  private int errorType= 0;

  public BerException() {
    errorType= 0;
  }

  public BerException(int x) {
    errorType= x;
  }

  public boolean isInvalidSnmpVersion() {
    if (errorType == BAD_VERSION)
      return true;
    else
      return false;
  }
}
