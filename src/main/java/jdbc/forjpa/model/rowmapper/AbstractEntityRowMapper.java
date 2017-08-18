package jdbc.forjpa.model.rowmapper;

import jdbc.forjpa.model.AbstractEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by archangohel on 16/08/17.
 */
public abstract class AbstractEntityRowMapper<T extends AbstractEntity> {
    public T mapAbstractEntity(T entity, ResultSet rs) throws SQLException {
        entity.setCreatedBy(rs.getString("createdBy"));
        entity.setCreatedOn(rs.getTimestamp("createdOn"));
        entity.setUpdatedBy(rs.getString("updatedBy"));
        entity.setUpdatedOn(rs.getTimestamp("updatedOn"));
        return entity;
    }
}
