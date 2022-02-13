package edu.sumdu.tss.elephant.helper.utils;

import org.junit.jupiter.api.Test;

public class CmdUtilTest {

    @Test
    void exec() {
        CmdUtil.exec("echo hello");
    }
}