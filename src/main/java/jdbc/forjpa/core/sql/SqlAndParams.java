package jdbc.forjpa.core.sql;

import java.util.ArrayList;
import java.util.List;

/**
 * Sql query and parameter holder.
 *
 * @author Archan
 */
public class SqlAndParams {

    private String sql;
    private List<String> params = new ArrayList<String>();

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "SqlAndParams [sql=" + sql + ", params=" + params + "]";
    }

}
