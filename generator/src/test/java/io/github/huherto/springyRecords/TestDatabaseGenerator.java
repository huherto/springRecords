package io.github.huherto.springyRecords;
/*
The MIT License (MIT)

Copyright (c) 2014 <copyright holders>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

import java.util.Arrays;

import javax.sql.DataSource;

import org.junit.Test;

import io.github.huherto.springyRecords.generator.SchemaCrawlerGenerator;
import io.github.huherto.springyRecords.generator.tools.TableToolImpl;

public class TestDatabaseGenerator extends BaseTest {

    @Test
    public void generateExtendTableTool() {
        DataSource ds = createDs();

        SchemaCrawlerGenerator dbGenerator = new SchemaCrawlerGenerator(ds, "com.example", "PetStore") {

            @Override
            protected TableToolImpl createTableTool(String packageName) {
                return new ExtendedTableTool(packageName);
            }

        };

        dbGenerator.printInformationSchema(null);
        dbGenerator.processTableList("PUBLIC.PUBLIC", Arrays.asList("ONWER", "pet"));
        dbGenerator.processAllTables("PUBLIC.PUBLIC");
    }

}