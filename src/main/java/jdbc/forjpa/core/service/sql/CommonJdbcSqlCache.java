package jdbc.forjpa.core.service.sql;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Keeps the JPA entity sqls {@link SqlAndParams}
 *
 * @author Archan
 */
@Component("commonJdbcSqlCache")
@Scope(value = "singleton")
public class CommonJdbcSqlCache {

    private Map<String, CommonJdbcSqlHolder> cache = new ConcurrentHashMap<String, CommonJdbcSqlHolder>();

    public CommonJdbcSqlHolder getSqlsForEntity(String className) {
        return cache.get(className);
    }

    public void setSqlsForEntity(String className, CommonJdbcSqlHolder sqlHolder) {
        cache.put(className, sqlHolder);
    }
}
