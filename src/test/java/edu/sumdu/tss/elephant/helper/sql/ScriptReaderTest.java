package edu.sumdu.tss.elephant.helper.sql;

import java.io.*;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ScriptReaderTest {

    static String path;
    static ArrayList<String> lines;

    @BeforeAll
    static void initFile() {
        path = "src/test/resources/scriptForTest.txt";

        try {
            FileWriter writer = new FileWriter(path);

            lines = new ArrayList<>();
            lines.add("/* create new table */");
            lines.add("create table t_name (\"name\" char, number integer)");
            lines.add("// insert first line\ninsert into t_name values ('a',1)");
            lines.add("--insert second line\ninsert into t_name values ('b',2)");
            lines.add("/*/* end of file */*/");

            for (String line : lines) {
                writer.write(line + ";");
            }

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    void testReader() {
        try {
            var reader = new ScriptReader(
                    new BufferedReader(new FileReader(path))
            );

            String line;
            int i = 0;
            while ((line = reader.readStatement()) != null) {
                assertEquals(lines.get(i), line);
                i++;
            }

            assertNull(reader.readStatement());
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testNegative() {
        try {
            var reader = new ScriptReader(
                    new BufferedReader(new FileReader(path))
            );

            reader.close();
            assertThrows(RuntimeException.class, reader::readStatement);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}