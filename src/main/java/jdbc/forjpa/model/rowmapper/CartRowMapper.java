package jdbc.forjpa.model.rowmapper;

import jdbc.forjpa.model.Cart;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by archangohel on 16/08/17.
 */
@Component
public class CartRowMapper extends AbstractEntityRowMapper<Cart> implements RowMapper<Cart> {
    @Override
    public Cart mapRow(ResultSet rs, int rowNum) throws SQLException {
        Cart cart = new Cart();
        cart.setName(rs.getString("name"));
        cart.setId(rs.getLong("id"));
        cart.setTotal(rs.getDouble("total"));
        super.mapAbstractEntity(cart, rs);
        return cart;
    }
}
