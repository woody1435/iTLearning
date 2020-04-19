package org.ddzj.java.lang;

import org.junit.Test;

import java.io.Console;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Properties;

public class SystemTest {

    @Test
    public void setIn() throws Exception {
        System.setIn(System.class.getResourceAsStream("/config.properties"));
        char ret = (char)System.in.read();
        System.out.println(ret);

        int read = System.in.read(new byte[]{'a','b','c'});
        System.out.println(read);
    }

    @Test
    public void setOut() throws Exception {
        // create file
        FileOutputStream f = new FileOutputStream("C:\\Users\\woody\\Desktop\\temp.txt");
        System.setOut(new PrintStream(f));

        // this text will get redirected to file
        System.out.println("This is System class!!!");

        System.out.print("输出");
    }

    @Test
    public void setErr() throws FileNotFoundException {
        // create a file
        FileOutputStream f = new FileOutputStream("C:\\Users\\woody\\Desktop\\temp.txt");

        System.setErr(new PrintStream(f));
        // redirect the output
        System.err.println("This will get redirected to file");
    }

    @Test
    public void console() {
        Console console = System.console();
        console.printf("abc");
        console.flush();
    }

    @Test
    public void inheritedChannel() {
    }

    @Test
    public void setSecurityManager() {
    }

    @Test
    public void getSecurityManager() {
    }

    @Test
    public void currentTimeMillis() {
        System.out.println(System.currentTimeMillis());
    }

    @Test
    public void nanoTime() {
        System.out.println(System.nanoTime());
        System.out.println(System.currentTimeMillis());
    }

    @Test
    public void arraycopy() {
        byte[] b1 = {'a','b'};
        byte[] b2 = {'c','d','e'};
        System.arraycopy(b1,0,b2,1,2);
        System.out.println(new String(b2));
    }

    @Test
    public void identityHashCode() {
    }

    @Test
    public void getProperties() {
    }

    @Test
    public void lineSeparator() {
    }

    @Test
    public void setProperties() {
    }

    @Test
    public void getProperty() {
        Properties p = System.getProperties();
        p.list(System.out);
    }

    @Test
    public void getProperty1() {
    }

    @Test
    public void setProperty() {
    }

    @Test
    public void clearProperty() {
    }

    @Test
    public void getenv() {
        System.out.println(System.getenv("PATH"));
        System.out.println(System.getenv("TEMP"));
        System.out.println(System.getenv("USERNAME"));

    }

    @Test
    public void getenv1() {
    }

    @Test
    public void exit() {
        for(int i = 0;i<100;i++){
            if(i>5){
                System.exit(0);
                System.out.println("exit...");
            }
            System.out.println("i="+i);
        }
    }

    @Test
    public void gc() {
    }

    @Test
    public void runFinalization() {
    }

    @Test
    public void runFinalizersOnExit() {
    }

    @Test
    public void load() {
    }

    @Test
    public void loadLibrary() {
    }

    @Test
    public void mapLibraryName() {
        System.out.println(System.getProperty("os.name"));
        String str = System.mapLibraryName("os.name");
        System.out.println(str);
    }
}