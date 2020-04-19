package org.ddzj.java.io;

import org.junit.Test;

import java.io.FileOutputStream;

public class FileOutputStreamTest {

    @Test
    public void write() throws Exception {
        FileOutputStream fs = new FileOutputStream("C:\\Users\\woody\\Desktop\\stream.txt");
        byte[] bytes = {'1','b','c','d','e'};
        fs.write(bytes);
        fs.close();
    }

    @Test
    public void write1() throws Exception{
        FileOutputStream fs = new FileOutputStream("C:\\Users\\woody\\Desktop\\stream.txt");
        fs.write(0);
        fs.close();
    }

    @Test
    public void write2() {
    }

    @Test
    public void close() {
    }

    @Test
    public void getFD() {
    }

    @Test
    public void getChannel() {
    }

    @Test
    public void finalize() {
    }
}