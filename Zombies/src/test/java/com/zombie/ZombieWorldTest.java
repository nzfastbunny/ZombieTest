package com.zombie;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ZombieWorldTest {
    private final ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
    private final ByteArrayOutputStream testError = new ByteArrayOutputStream();
    private final PrintStream systemOut = System.out;
    private final PrintStream systemError = System.err;

    @Before
    public void redirectOutput() {
        System.setOut(new PrintStream(testOutput));
        System.setErr(new PrintStream(testError));
    }

    @After
    public void restoreOutput() {
        System.setOut(systemOut);
        System.setErr(systemError);
    }

    @Test
    public void main_noArgs() {
        ZombieWorld.main(new String[0]);
        Assert.assertEquals("zombies score: 3\r\nzombies positions: (3, 0) (2, 1) (1, 0) (0, 0)\r\n",
                testOutput.toString());
    }

    @Test
    public void main_invalidPath() {
        ZombieWorld.main(new String[]{"C:/invalidPath.txt"});
        Assert.assertEquals("The zombie world could not be set up...\r\n", testOutput.toString());
        Assert.assertThat(testError.toString(), CoreMatchers.containsString("FileNotFoundException"));
    }

}
