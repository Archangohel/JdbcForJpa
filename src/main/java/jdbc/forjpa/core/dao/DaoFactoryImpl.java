package jdbc.forjpa.core.dao;

import jdbc.forjpa.core.dao.rowmapper.RowMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Dynamically generates Dao for entities.
 * Created by archangohel on 15/08/17.
 */
@Component("daoFactory")
public class DaoFactoryImpl implements DaoFactory {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private RowMapperFactory rowMapperFactory;

    private Object getInstanceLock = new Object();

    private Map<Class<?>, BaseDao<?>> beanCache = new ConcurrentHashMap<>();

    @Override
    public <T> BaseDao<T> getDaoInstance(Class<T> clazz) {
        synchronized (getInstanceLock) {
            if (!beanCache.containsKey(clazz)) {
                RowMapper<T> rowMapper = rowMapperFactory.getRowMapper(clazz);
                BaseDao<T> newBean = applicationContext.getBean(CommonDaoImpl.class, clazz, rowMapper);
                beanCache.put(clazz, newBean);
            }
            return (BaseDao<T>) beanCache.get(clazz);
        }
    }

    @Override
    public <T> BaseDao<T> getDaoInstance(Class<T> clazz , RowMapper<T> rowMapper) {
        synchronized (getInstanceLock) {
            if (!beanCache.containsKey(clazz)) {
                BaseDao<T> newBean = applicationContext.getBean(CommonDaoImpl.class, clazz, rowMapper);
                beanCache.put(clazz, newBean);
            }
            return (BaseDao<T>) beanCache.get(clazz);
        }
    }
}
