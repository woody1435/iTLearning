package org.ddzj.java.lang;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ThrowableTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getMessage() {
    }

    @Test
    public void getLocalizedMessage() {
    }

    @Test
    public void getCause() {
    }

    @Test
    public void initCause() {
    }

    @Test
    public void toStringTest() {
    }

    @Test
    public void printStackTrace() {
    }

    @Test
    public void printStackTrace1() {
    }

    @Test
    public void printStackTrace2() {
    }

    @Test
    public void fillInStackTrace() {
    }

    @Test
    public void getStackTrace() {
        try {
            int i = 1 / 0;
        }catch (Throwable e){
            System.out.println(e.toString()+"~~~~~~~~~");
//            e.printStackTrace();
            StackTraceElement[] stackTrace = e.getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                System.out.println(stackTraceElement.toString());
            }
        }finally {

        }
    }

    @Test
    public void setStackTrace() {
    }

    @Test
    public void getStackTraceDepth() {
    }

    @Test
    public void getStackTraceElement() {
    }

    @Test
    public void addSuppressed() {
    }

    @Test
    public void getSuppressed() {
    }
}