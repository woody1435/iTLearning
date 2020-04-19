package org.ddzj.java.io;

import org.junit.Test;

import java.io.ByteArrayInputStream;

public class ByteArrayInputStreamTest {

    @Test
    public void read() throws Exception {
        byte[] bytes = {'a', 'b', 'c', 'd'};
        ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
        int i;
        while ((i = bi.read()) != -1) {
            System.out.println(i);
        }
        if (null != bi)
            bi.close();
    }

    @Test
    public void read1() {
    }

    @Test
    public void skip() {
    }

    @Test
    public void available() {
    }

    @Test
    public void markSupported() throws Exception {
        byte[] bytes = {'a', 'b', 'c', 'd'};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        System.out.println(byteArrayInputStream.markSupported());
        if (null != byteArrayInputStream)
            byteArrayInputStream.close();
    }

    @Test
    public void mark() {
    }

    @Test
    public void reset() {
    }

    @Test
    public void close() {
    }
}