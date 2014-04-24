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

import static org.junit.Assert.assertTrue;
import io.github.huherto.springyRecords.generator.DataBaseGenerator;
import io.github.huherto.springyRecords.generator.TableTool;
import io.github.huherto.springyRecords.test.OwnerRecord;
import io.github.huherto.springyRecords.test.OwnerTable;
import io.github.huherto.springyRecords.test.PetRecord;
import io.github.huherto.springyRecords.test.PetTable;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class TestDatabaseGenerator extends BaseTest {

    @Test @Ignore
    public void generateExtendTableTool() {
        DataSource ds = createDs();

        DataBaseGenerator dbGenerator = new DataBaseGenerator(ds, "io.github.huherto.springyRecords.test") {

            @Override
            public TableTool createTableTool() {
                return new ExtendedTableTool();
            }

        };

        dbGenerator.printInformationSchema();
        dbGenerator.processTableList("def", "test", Arrays.asList("owner", "pet"));
        dbGenerator.processAllTables("def", "test");
    }

    @Test @Ignore
    public void insertPet() {
        DataSource ds = createDs();

        JdbcTemplate jt = new JdbcTemplate(ds);
        jt.execute("delete from pet");

        PetTable table = new PetTable(ds);
        PetRecord r = new PetRecord();
        r.birth = new Date();
        r.name = "Manchas";
        r.owner = "Humberto";
        r.sex = "M";
        r.species = "Dog";
        table.insert(r);

        List<PetRecord> pets = table.queryAll();
        assertTrue(pets.size() >= 1);

    }

    @Test @Ignore
    public void insertOwner() {
        DataSource ds = createDs();

        OwnerTable table = new OwnerTable(ds);
        OwnerRecord r = new OwnerRecord();
        r.name = "Humberto";

        table.insert(r);

        List<OwnerRecord> owners = table.queryAll();
        assertTrue(owners.size() >= 1);

    }

}
