package jdbc.forjpa.core.service.sql;

import jdbc.forjpa.core.metadata.JpaEntityMetadataFactory;
import jdbc.forjpa.core.metadata.JpaEntityMetadataHelper;
import jdbc.forjpa.core.metadata.JpaEntityMetadataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Simple utility for generating sqls for JPA based Entity.
 *
 * @author Archan
 */
@Component("commonJdbcJpaEntitySqlGenerator")
public class CommonJdbcJpaEntitySqlGenerator {

    @Autowired
    private CommonJdbcSqlCache commonJdbcSqlCache;

    @Autowired
    private JpaEntityMetadataFactory jpaEntityMetadataFactory;

    @Autowired
    private JpaEntityMetadataHelper jpaEntityMetadataHelper;


    public CommonJdbcSqlCache getCommonJdbcSqlCache() {
        return commonJdbcSqlCache;
    }

    public void setCommonJdbcSqlCache(CommonJdbcSqlCache commonJdbcSqlCache) {
        this.commonJdbcSqlCache = commonJdbcSqlCache;
    }

    public JpaEntityMetadataFactory getJpaEntityMetadataFactory() {
        return jpaEntityMetadataFactory;
    }

    public void setJpaEntityMetadataFactory(JpaEntityMetadataFactory jpaEntityMetadataFactory) {
        this.jpaEntityMetadataFactory = jpaEntityMetadataFactory;
    }

    public JpaEntityMetadataHelper getJpaEntityMetadataHelper() {
        return jpaEntityMetadataHelper;
    }

    public void setJpaEntityMetadataHelper(JpaEntityMetadataHelper jpaEntityMetadataHelper) {
        this.jpaEntityMetadataHelper = jpaEntityMetadataHelper;
    }

    public <T> SqlAndParams generateInsertSql(Class<T> clazz, String schema) {
        JpaEntityMetadataInfo<T> metadataInfo = jpaEntityMetadataFactory.getEntityMetadata(clazz);
        CommonJdbcSqlHolder sqlHolder = commonJdbcSqlCache.getSqlsForEntity(clazz.getName());
        if (sqlHolder == null || sqlHolder.getInsertSql() == null) {
            SqlAndParams sqlAndParams = new SqlAndParams();
            StringBuilder sqlBuilder = new StringBuilder();

            sqlBuilder.append("INSERT INTO ").append(schema).append(".");

            sqlBuilder.append(metadataInfo.getTableName()).append("(");
            List<String> fields = jpaEntityMetadataHelper.getAllColumnsForEntity(metadataInfo);

            Assert.notEmpty(fields, "No fields for entity" + clazz);

            sqlBuilder.append(StringUtils.collectionToCommaDelimitedString(fields));
            sqlBuilder.append(") VALUES (");
            for (int index = 0; index < fields.size(); index++) {
                sqlBuilder.append("?,");
            }
            sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
            sqlBuilder.append(")");

            sqlAndParams.setSql(sqlBuilder.toString());
            sqlAndParams.setParams(fields);
            // set in cache
            CommonJdbcSqlHolder holder = null;
            if (sqlHolder == null) {
                holder = new CommonJdbcSqlHolder();
            } else {
                holder = commonJdbcSqlCache.getSqlsForEntity(clazz.getName());
            }
            holder.setInsertSql(sqlAndParams);
            commonJdbcSqlCache.setSqlsForEntity(clazz.getName(), holder);

            return sqlAndParams;
        } else {
            return sqlHolder.getInsertSql();
        }
    }

    public <T> SqlAndParams generateUpdateSql(Class<T> clazz, String schema) {
        JpaEntityMetadataInfo<T> metadataInfo = jpaEntityMetadataFactory.getEntityMetadata(clazz);
        CommonJdbcSqlHolder sqlHolder = commonJdbcSqlCache.getSqlsForEntity(clazz.getName());
        if (sqlHolder == null || sqlHolder.getUpdateSql() == null) {
            SqlAndParams sqlAndParams = new SqlAndParams();
            StringBuilder sqlBuilder = new StringBuilder();

            sqlBuilder.append("UPDATE ").append(schema).append(".");
            sqlBuilder.append(metadataInfo.getTableName()).append(" SET ");

            List<String> allColumns = new ArrayList<>();
            Set<String> idColumns = metadataInfo.getIdentityColumns();

            allColumns.addAll(jpaEntityMetadataHelper.getAllColumnsForEntity(metadataInfo));
            allColumns.removeAll(idColumns);
            Assert.notEmpty(allColumns, "No fields for entity" + clazz);

            for (String field : allColumns) {
                sqlBuilder.append(field).append("=?,");
            }
            sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
            sqlBuilder.append(" WHERE ");
            idColumns.stream().forEach(r -> sqlBuilder.append(r).append("=? and "));
            sqlBuilder.delete(sqlBuilder.length() - 4, sqlBuilder.length());

            List<String> params = allColumns;
            params.addAll(idColumns);
            sqlAndParams.setSql(sqlBuilder.toString());
            sqlAndParams.setParams(params);
            // set in cache
            CommonJdbcSqlHolder holder = null;
            if (sqlHolder == null) {
                holder = new CommonJdbcSqlHolder();
            } else {
                holder = commonJdbcSqlCache.getSqlsForEntity(clazz.getName());
            }
            holder.setUpdateSql(sqlAndParams);
            commonJdbcSqlCache.setSqlsForEntity(clazz.getName(), holder);

            return sqlAndParams;
        } else {
            return sqlHolder.getUpdateSql();
        }
    }


    public <T> SqlAndParams generateDeleteSql(Class<T> clazz, String schema) {
        JpaEntityMetadataInfo<T> metadataInfo = jpaEntityMetadataFactory.getEntityMetadata(clazz);
        CommonJdbcSqlHolder sqlHolder = commonJdbcSqlCache.getSqlsForEntity(clazz.getName());
        if (sqlHolder == null || sqlHolder.getDeleteSql() == null) {
            SqlAndParams sqlAndParams = new SqlAndParams();
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("DELETE FROM ").append(schema).append(".");
            sqlBuilder.append(metadataInfo.getTableName());
            Set<String> idColumns = metadataInfo.getIdentityColumns();
            sqlBuilder.append(" WHERE ");
            idColumns.stream().forEach(r -> sqlBuilder.append(r).append("=? and "));
            sqlBuilder.delete(sqlBuilder.length() - 4, sqlBuilder.length());

            List<String> params = new ArrayList<String>();
            params.addAll(idColumns);
            sqlAndParams.setSql(sqlBuilder.toString());
            sqlAndParams.setParams(params);
            // set in cache
            CommonJdbcSqlHolder holder = null;
            if (sqlHolder == null) {
                holder = new CommonJdbcSqlHolder();
            } else {
                holder = commonJdbcSqlCache.getSqlsForEntity(clazz.getName());
            }
            holder.setDeleteSql(sqlAndParams);
            commonJdbcSqlCache.setSqlsForEntity(clazz.getName(), holder);

            return sqlAndParams;
        } else {
            return sqlHolder.getDeleteSql();
        }
    }


    public <T> SqlAndParams generateSelectSql(Class<T> clazz, List<String> filters, String schema) {
        if (filters == null || filters.size() == 0) {
            // get all select
            return handleSelectAllSql(clazz, filters, schema);
        } else {
            // select with filters.
            return handleSelectWithFiltersSql(clazz, filters, schema);
        }

    }

    private <T> SqlAndParams handleSelectWithFiltersSql(Class<T> clazz, List<String> filters, String schema) {
        final String queryKey = getSelectQueryKeyForFilter(filters);

        // filtered select
        CommonJdbcSqlHolder sqlHolder = commonJdbcSqlCache.getSqlsForEntity(clazz.getName());

        if (sqlHolder == null || sqlHolder.getOtherSqls().size() == 0) {
            SqlAndParams selectSqlAndParam = getSelectSql(clazz, filters, schema);
            // set in cache
            CommonJdbcSqlHolder holder = null;
            if (sqlHolder == null) {
                holder = new CommonJdbcSqlHolder();
            } else {
                holder = commonJdbcSqlCache.getSqlsForEntity(clazz.getName());
            }
            holder.getOtherSqls().put(queryKey, selectSqlAndParam);
            commonJdbcSqlCache.setSqlsForEntity(clazz.getName(), holder);
            return selectSqlAndParam;
        } else {
            SqlAndParams selectSqlAndParamsFromCache = sqlHolder.getOtherSqls().get(queryKey);
            if (selectSqlAndParamsFromCache == null) {
                SqlAndParams selectSqlAndParam = getSelectSql(clazz, filters, schema);
                // set in cache
                sqlHolder.getOtherSqls().put(queryKey, selectSqlAndParam);
                return selectSqlAndParam;
            } else {
                return selectSqlAndParamsFromCache;
            }
        }
    }

    private <T> SqlAndParams handleSelectAllSql(Class<T> clazz, List<String> filters, String schema) {
        CommonJdbcSqlHolder sqlHolder = commonJdbcSqlCache.getSqlsForEntity(clazz.getName());
        if (sqlHolder == null || sqlHolder.getSelectSql() == null) {
            SqlAndParams selectSqlAndParam = getSelectSql(clazz, filters, schema);
            // set in cache
            CommonJdbcSqlHolder holder = null;
            if (sqlHolder == null) {
                holder = new CommonJdbcSqlHolder();
            } else {
                holder = commonJdbcSqlCache.getSqlsForEntity(clazz.getName());
            }
            holder.setSelectSql(selectSqlAndParam);
            commonJdbcSqlCache.setSqlsForEntity(clazz.getName(), holder);

            return selectSqlAndParam;
        } else {
            return sqlHolder.getSelectSql();
        }
    }

    private String getSelectQueryKeyForFilter(List<String> filters) {
        return StringUtils.collectionToDelimitedString(filters, "-");
    }

    private <T> SqlAndParams getSelectSql(Class<T> clazz, List<String> filters, String schema) {
        JpaEntityMetadataInfo<T> metadataInfo = jpaEntityMetadataFactory.getEntityMetadata(clazz);

        SqlAndParams sqlAndParams = new SqlAndParams();
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT ");
        List<String> fields = jpaEntityMetadataHelper.getAllColumnsForEntity(metadataInfo);
        Assert.notEmpty(fields, "No fields for entity" + clazz);

        List<String> fieldsWithAs = generateSelectClauseFields(clazz, fields);

        sqlBuilder.append(StringUtils.collectionToCommaDelimitedString(fieldsWithAs)).append(" FROM ");
        sqlBuilder.append("").append(schema).append(".").append(metadataInfo.getTableName());
        List<String> params = new ArrayList<String>();
        if (filters != null && filters.size() > 0) {
            sqlBuilder.append(" WHERE ");
            for (String filter : filters) {
                sqlBuilder.append(filter).append("=? and ");
            }
            sqlBuilder.delete(sqlBuilder.length() - 4, sqlBuilder.length());
            params.addAll(filters);
        }

        sqlAndParams.setSql(sqlBuilder.toString());
        sqlAndParams.setParams(params);
        return sqlAndParams;
    }

    private <T> List<String> generateSelectClauseFields(Class<T> clazz, List<String> columns) {
        List<String> values = new ArrayList<>(columns.size());
        JpaEntityMetadataInfo<T> metadataInfo = jpaEntityMetadataFactory.getEntityMetadata(clazz);
        for (String column : columns) {
            String fieldName = metadataInfo.getColumnsToFieldMap().get(column);
            if (fieldName != null) {
                values.add(column + " as " + fieldName);
            } else {
                JpaEntityMetadataInfo<?> embeddedMetadataInfo = checkEmbeddedEntity(clazz, column);
                if (embeddedMetadataInfo != null) {
                    String embeddedFieldName = embeddedMetadataInfo.getColumnsToFieldMap().get(column);
                    values.add(column + " as " + embeddedFieldName);
                }
            }
        }
        return values;
    }

    private <T> JpaEntityMetadataInfo checkEmbeddedEntity(Class<T> clazz, String columnName) {
        JpaEntityMetadataInfo<T> metadataInfo = jpaEntityMetadataFactory.getEntityMetadata(clazz);
        for (Map.Entry<String, Class<?>> embeddedFieldEntry : metadataInfo.getEmbeddedFieldsMap().entrySet()) {
            JpaEntityMetadataInfo<T> embeddedMetadataInfo = jpaEntityMetadataFactory.getEntityMetadata(embeddedFieldEntry.getValue());
            if (embeddedMetadataInfo.getAllColumns().contains(columnName)) {
                return embeddedMetadataInfo;
            }
        }
        return null;
    }
}
