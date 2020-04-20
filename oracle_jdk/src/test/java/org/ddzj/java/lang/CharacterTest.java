package org.ddzj.java.lang;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CharacterTest {

    @Before
    public void setUp() throws Exception {
        System.out.println("before~~~~~~");
    }

    @After
    public void setDown() throws Exception {
        System.out.println("down~~~~~~");
    }

    @Test
        public void isValidCodePoint() {
        Character character = new Character('工');
        System.out.println(Character.isValidCodePoint(character));
    }

    @Test
    public void isWhitespace(){
        Character character = new Character(' ');
        System.out.println(Character.isWhitespace(character));
    }

    @Test
    public void isLowerCase(){
        System.out.println(Character.isLowerCase('a'));
    }
}