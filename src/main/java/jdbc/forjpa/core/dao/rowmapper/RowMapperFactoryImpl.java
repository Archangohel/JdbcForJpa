package jdbc.forjpa.core.dao.rowmapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by archangohel on 16/08/17.
 */
@Component("rowMapperFactory")
public class RowMapperFactoryImpl implements RowMapperFactory {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private Map<Class<?>, RowMapper<?>> beanCache = new ConcurrentHashMap<>();

    private Object getRowMapperLock = new Object();

    @Override
    public <T> RowMapper<T> getRowMapper(Class<T> clazz) {
        synchronized (getRowMapperLock) {
            if (!beanCache.containsKey(clazz)) {
                registerRowMapper(clazz, getDefaultRowMapper(clazz));
            }
            return (RowMapper<T>) beanCache.get(clazz);
        }
    }

    @Override
    public <T> void registerRowMapper(Class<T> clazz, RowMapper<T> rowMapper) {
        logger.info("Registering the RowMapper for class {} , rowMapper {}", clazz, rowMapper);
        beanCache.put(clazz, rowMapper);
    }

    @Override
    public <T> RowMapper<T> getDefaultRowMapper(Class<T> clazz) {
        return new BeanPropertyRowMapper<T>(clazz);
    }

}
