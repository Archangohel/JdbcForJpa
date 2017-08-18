package jdbc.forjpa.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Database sequence way of generating an ID.
 *
 * @author Archan
 */
@Component("dbSequenceBasedIdGenerator")
public class DbSequenceBasedIdGenerator implements IdGenerator<Long> {

    @Autowired
    @Qualifier("coreJdbcTemplate")
    private NamedParameterJdbcTemplate jdbcTemplate;

    private String sequenceName;

    private String sql;

    /**
     * Generate new Long id. Currently working for PG only.
     *
     * @return
     */
    private Long generateNewLongId() {
        if (sql == null) {
            sequenceName = StringUtils.isEmpty(sequenceName) ? "hibernate_sequence" : sequenceName;
            this.sql = "select nextval('public." + sequenceName + "')";
        }
        return jdbcTemplate.getJdbcOperations().queryForObject(sql, null, Long.class);
    }

    public Long generate() {
        return generateNewLongId();
    }
}
