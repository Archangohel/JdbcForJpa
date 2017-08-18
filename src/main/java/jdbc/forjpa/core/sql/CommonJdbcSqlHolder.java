package jdbc.forjpa.core.sql;

import jdbc.forjpa.core.sql.SqlAndParams;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class to keep the common CRUD sqls for JPA entities.
 *
 * @author Archan
 */
public class CommonJdbcSqlHolder {

    private SqlAndParams insertSql;
    private SqlAndParams updateSql;
    private SqlAndParams deleteSql;
    private SqlAndParams selectSql;
    private Map<String, SqlAndParams> otherSqls = new ConcurrentHashMap<String, SqlAndParams>();

    public SqlAndParams getInsertSql() {
        return insertSql;
    }

    public void setInsertSql(SqlAndParams insertSql) {
        this.insertSql = insertSql;
    }

    public SqlAndParams getUpdateSql() {
        return updateSql;
    }

    public void setUpdateSql(SqlAndParams updateSql) {
        this.updateSql = updateSql;
    }

    public SqlAndParams getDeleteSql() {
        return deleteSql;
    }

    public void setDeleteSql(SqlAndParams deleteSql) {
        this.deleteSql = deleteSql;
    }

    public SqlAndParams getSelectSql() {
        return selectSql;
    }

    public void setSelectSql(SqlAndParams selectSql) {
        this.selectSql = selectSql;
    }

    public Map<String, SqlAndParams> getOtherSqls() {
        return otherSqls;
    }

    public void setOtherSqls(Map<String, SqlAndParams> otherSqls) {
        this.otherSqls = otherSqls;
    }

}
