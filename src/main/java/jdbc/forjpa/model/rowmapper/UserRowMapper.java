package jdbc.forjpa.model.rowmapper;

import jdbc.forjpa.model.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by archangohel on 16/08/17.
 */
@Component
public class UserRowMapper extends AbstractEntityRowMapper<User> implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setName(rs.getString("name"));
        user.setId(rs.getLong("id"));
        super.mapAbstractEntity(user,rs);
        return user;
    }
}
