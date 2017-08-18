package jdbc.forjpa.core.metadata;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by archangohel on 14/08/17.
 */
@Component
public class JpaEntityMetadataFactory {
    private Map<String, JpaEntityMetadataInfo> cache = new ConcurrentHashMap<String, JpaEntityMetadataInfo>();

    public <T> JpaEntityMetadataInfo getEntityMetadata(Class<T> clazz) {
        String className = clazz.getName();
        if (!cache.containsKey(className)) {
            Class<T> type = clazz;
            JpaEntityMetadataInfo<T> jpaEntityMetadataInfo = new JpaEntityMetadataInfo<T>(type);
            cache.put(className, jpaEntityMetadataInfo);
        }
        return cache.get(className);
    }

    public void cleanCache() {
        this.cache = new ConcurrentHashMap<String, JpaEntityMetadataInfo>();
    }
}
