package jdbc.forjpa.core.dao;

import org.springframework.jdbc.core.RowMapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Common interface for db interaction.
 *
 * @author Archan.
 */
public interface BaseDao<T> {
    public T find(Long id);

    List<T> find(Map<String, Object> filterMap);

    public List<T> find(List<Long> idList);

    public void persist(T entity);

    public T merge(T entity);

    public void remove(T entity);

    public void remove(Long id);

    public Collection<T> findAll();

    public void setRowMapper(RowMapper<T> rowMapper);
}

