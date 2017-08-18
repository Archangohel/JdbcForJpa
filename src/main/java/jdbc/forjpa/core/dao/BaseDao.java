package jdbc.forjpa.core.dao;

import org.springframework.jdbc.core.RowMapper;

import java.util.Collection;

/**
 * Common interface for db interaction.
 *
 * @author Archan.
 */
public interface BaseDao<T> {
    public T find(Long id);

    public void persist(T entity);

    public T merge(T entity);

    public void remove(T entity);

    public void remove(Long id);

    public Collection<T> findAll();

    public void setRowMapper(RowMapper<T> rowMapper);
}

