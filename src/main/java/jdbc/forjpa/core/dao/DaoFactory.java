package jdbc.forjpa.core.dao;


import org.springframework.jdbc.core.RowMapper;

/**
 * Created by archangohel on 15/08/17.
 */
public interface DaoFactory {
    <T> BaseDao<T> getDaoInstance(Class<T> clazz);

    <T> BaseDao<T> getDaoInstance(Class<T> clazz, RowMapper<T> rowMapper);
}
