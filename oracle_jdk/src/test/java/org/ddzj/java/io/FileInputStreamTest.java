package org.ddzj.java.io;

import org.junit.Test;

import java.io.*;
import java.nio.channels.FileChannel;

public class FileInputStreamTest {

    @Test
    public void read() throws Exception {
        InputStream is = new FileInputStream("C:\\Users\\woody\\Desktop\\stream.txt");
        int i;
        char c;
        while ((i = is.read()) != -1){
            c = (char) i;
            System.out.println(c);
        }
        if(null != is){
            is.close();
        }
    }

    @Test
    public void read1() {
    }

    @Test
    public void read2() throws Exception{
        FileInputStream is = new FileInputStream("C:\\Users\\woody\\Desktop\\stream.txt");
        byte[] bytes = new byte[5];
        is.read(bytes, 2, 3);
        for (byte aByte : bytes) {
            System.out.println(aByte);
        }
        if (null != is) {
            is.close();
        }
    }

    @Test
    public void skip() throws Exception {
        InputStream is = new FileInputStream("C:\\Users\\woody\\Desktop\\stream.txt");
        int i;
        char c;

        // skip 5 byte
        is.skip(5);
        while ((i = is.read()) != -1) {
            // converts int to char
            c = (char) i;

            // prints character
            System.out.println("Character Read: " + c);
        }

        if (null != is) {
            is.close();
        }
    }

    @Test
    public void available() throws Exception {
        InputStream is = new FileInputStream("C:\\Users\\woody\\Desktop\\stream.txt");
        is.skip(3);
        System.out.println(is.available());
        if(null != is){
            is.close();
        }
    }

    @Test
    public void close() {
    }

    @Test
    public void getFD() throws IOException {
        FileInputStream is = new FileInputStream("C:\\Users\\woody\\Desktop\\stream.txt");
        FileDescriptor fd = is.getFD();
        System.out.println(fd.valid());
        if(null != is){
            is.close();
        }
    }

    @Test
    public void getChannel() throws Exception{
        FileInputStream is = new FileInputStream("C:\\Users\\woody\\Desktop\\stream.txt");
        int i;
        char c;

        while ((i = is.read()) != -1) {
            // converts int to char
            c = (char) i;
            FileChannel channel = is.getChannel();
            long position = channel.position();
            // prints character
            System.out.println("Character Read: position=" +position+":"+ c);
        }
    }

    @Test
    public void finalize() {
    }
}