package com.example;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.support.JdbcDaoSupport;


public abstract class  AbstractBaseTable<R extends BaseRecord> extends JdbcDaoSupport {

    private SimpleJdbcInsert insert = null;
    private NamedParameterJdbcTemplate namedTemplate = null;

    public AbstractBaseTable(DataSource dataSource) {
        setDataSource(dataSource);
    }

    public NamedParameterJdbcTemplate getNamedTemplate() {
        if (namedTemplate == null) {
            namedTemplate = new NamedParameterJdbcTemplate(getDataSource());
        }
        return namedTemplate;
    }

    protected SimpleJdbcInsert getInsert() {
        if (insert == null) {
            insert = buildInsert();
        }
        return insert;
    }

    protected SimpleJdbcInsert buildInsert() {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate());
        insert.withTableName(tableName());
        insert.withSchemaName(schemaName());
        return insert;
    }

    protected final SimpleJdbcInsert buildInsert(String generatedKeyName) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate());
        insert.setGeneratedKeyName(generatedKeyName);
        insert.withTableName(tableName());
        insert.withSchemaName(schemaName());
        return insert;
    }

    public int insert(BaseRecord rec) {
        SimpleJdbcInsert insert = getInsert();
        if (insert.getGeneratedKeyNames().length > 0) {
            return insertAndReturnKey(rec).intValue();
        }
        return insert.execute(rec.asMap());
    }

    public Number insertAndReturnKey(BaseRecord rec) {
        SimpleJdbcInsert insert = getInsert();
        Map<String, Object> map = rec.asMap();
        map.remove(insert.getGeneratedKeyNames()[0]);
        return insert.executeAndReturnKey(map);
    }

    public List<R> query(String sql, Object...args) {
        return getJdbcTemplate().query(sql, rowMapper(), args);
    }

    protected Optional<R> optionalSingle(String sql, Object...args) {
        return Optional.ofNullable(DataAccessUtils.singleResult(query(sql, args)));
    }

    protected R requiredSingle(String sql, Object...args) {
        return DataAccessUtils.requiredSingleResult(query(sql, args));
    }

    public abstract RowMapper<R> rowMapper();

    protected int update(String sql, Object...args) {
        return getJdbcTemplate().update(sql, args);
    }

    public List<R> queryAll() {
        return getJdbcTemplate().query(selectStar(), rowMapper());
    }

    public String selectStar() {
        return "select * from "+fullTableName()+" ";
    }

    public abstract String tableName();
    
    public abstract String schemaName();
    
    public String fullTableName() {
        return schemaName() + "." + tableName();
    }

    /** 
     * Support for queries with named parameters.
     */
     
    public static class ParamValue {
        private String name;
        private  Object value;
        
        
        public ParamValue(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public Object getValue() {
            return value;
        }    
    }
    
    public static ParamValue param(String name, Object value) {
        return new ParamValue(name, value);
    }
    
    public List<R> namedQuery(String sql, ParamValue... args) {
        MapSqlParameterSource source = new MapSqlParameterSource();
        for(ParamValue pv : args) {
            source.addValue(pv.getName(), pv.getValue());
        }
        return this.getNamedTemplate().query(sql, source ,rowMapper() );        
    }
}
