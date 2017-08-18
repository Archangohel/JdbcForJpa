package jdbc.forjpa.core.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

/**
 * Created by archangohel on 15/08/17.
 */
@Component("commonDao")
@Scope(value = "prototype")
public class CommonDaoImpl<T> extends AbstractJdbcDaoImpl<T> {

    public CommonDaoImpl(Class<T> type, RowMapper<T> rowMapper) {
        super(type, rowMapper);
    }

    public CommonDaoImpl() {
        super(null, null);
    }
}
