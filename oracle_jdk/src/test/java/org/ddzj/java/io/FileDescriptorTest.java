package org.ddzj.java.io;

import org.junit.Test;

import java.io.FileDescriptor;
import java.io.FileInputStream;

public class FileDescriptorTest {

    @Test
    public void valid() throws Exception{
        FileInputStream is = new FileInputStream("C:\\Users\\woody\\Desktop\\stream.txt");
        FileDescriptor fd = is.getFD();
        System.out.println(fd.valid());
        if(null != is){
            is.close();
        }
    }

    @Test
    public void sync() throws Exception{
        FileInputStream is = new FileInputStream("C:\\Users\\woody\\Desktop\\stream.txt");
        int i;
        char c;

        while ((i = is.read()) != -1){
            c = (char)i;
            System.out.println(c);
        }
        FileDescriptor fd = is.getFD();
        fd.sync();
        while ((i = is.read()) != -1){
            c = (char)i;
            System.out.println(c);
        }
        if(null != is){
            is.close();
        }
    }

    @Test
    public void attach() {
    }

    @Test
    public void closeAll() {
    }
}