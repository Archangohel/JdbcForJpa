package jdbc.forjpa.core.dao;


import org.springframework.jdbc.core.RowMapper;

/**
 * Created by archangohel on 15/08/17.
 */
public interface DaoFactory {
    /**
     * Get the DAO instance for JPA entity. It will get the default row mapper or the
     * one that is a spring bean and implements {@link RowMapper<T>}.
     *
     * @param clazz
     * @param <T>
     * @return
     */
    <T> BaseDao<T> getDaoInstance(Class<T> clazz);

    /**
     * Get the DAO instance for JPA entity. User can pass their own row mapper here.
     *
     * @param clazz
     * @param rowMapper
     * @param <T>
     * @return
     */
    <T> BaseDao<T> getDaoInstance(Class<T> clazz, RowMapper<T> rowMapper);
}
