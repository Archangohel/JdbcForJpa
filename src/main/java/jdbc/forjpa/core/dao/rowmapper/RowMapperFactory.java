package jdbc.forjpa.core.dao.rowmapper;

import org.springframework.jdbc.core.RowMapper;

/**
 * Created by archangohel on 16/08/17.
 */
public interface RowMapperFactory {
    <T> RowMapper<T> getRowMapper(Class<T> clazz);

    <T> void registerRowMapper(Class<T> clazz, RowMapper<T> rowMapper);

    <T> RowMapper<T> getDefaultRowMapper(Class<T> clazz);
}
