package com.github.springRecords.test;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import com.github.springRecords.BaseRecord;
import com.github.springRecords.BaseTable;
import com.github.springRecords.RecordMapper;

/**
 * PetTable – 
 * 
 */
@Repository
public class PetTable extends BaseTable<PetRecord> {

    private RowMapper<PetRecord> rm = RecordMapper.newInstance(PetRecord.class);

    public PetTable(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected RowMapper<PetRecord> rowMapper() {
        return rm;
    }

    @Override
    public String tableName() {
        return "pet";
    }
    
    @Override
    public Class<? extends BaseRecord> recordClass() {
        return PetRecord.class;
    }
}
