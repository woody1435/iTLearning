package org.ddzj;

import org.junit.Assert;
import org.junit.Test;

public class UtilTest {

    @Test
    public void method() {
        int method = Util.method(12, 13);
        Assert.assertEquals(25,method);
    }
}