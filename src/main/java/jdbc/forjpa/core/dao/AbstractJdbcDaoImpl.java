package jdbc.forjpa.core.dao;

import jdbc.forjpa.core.service.JdbcIdService;
import jdbc.forjpa.core.sql.CommonJdbcJpaEntitySqlGenerator;
import jdbc.forjpa.core.sql.SqlAndParams;
import jdbc.forjpa.core.utils.CommonJdbcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Spring JDBC based interface that can do CRUD operations on JPA entity
 * (Entities having {@Entity} JPA annotations)
 *
 * @param <T>
 * @author Archan
 */
public abstract class AbstractJdbcDaoImpl<T> implements BaseDao<T> {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("coreJdbcTemplate")
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    protected CommonJdbcJpaEntitySqlGenerator commonJdbcJpaEntitySqlGenerator;

    @Autowired
    protected JdbcIdService jdbcIdService;

    @Autowired
    protected CommonJdbcUtils commonJdbcUtils;

    @Value("${jdbc.schema}")
    protected String schema;

    private Class<T> type;

    protected RowMapper<T> rowMapper;

    public AbstractJdbcDaoImpl(Class<T> type, RowMapper<T> rowMapper) {
        logger.trace("Instantiating DAO for {} with rowMapper {}", type, rowMapper);
        this.type = type;
        this.rowMapper = rowMapper;
    }

    public NamedParameterJdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public T find(Long id) {
        List<String> filters = new ArrayList<String>();
        String identityField = commonJdbcUtils.getIdColumnForEntity(type, Long.class);
        filters.add(identityField);
        SqlAndParams sqlAndParams = commonJdbcJpaEntitySqlGenerator.generateSelectSql(type, filters, getSchema());
        try {
            return jdbcTemplate.getJdbcOperations().queryForObject(sqlAndParams.getSql(), new Object[]{id},
                    this.rowMapper);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    /**
     * Single insert.
     */
    public void persist(T entity) {
        commonJdbcUtils.prePersist(entity);
        SqlAndParams sqlAndParams = commonJdbcJpaEntitySqlGenerator.generateInsertSql(entity.getClass(), getSchema());
        Long newId = jdbcIdService.generateNewId(JdbcIdService.IdGenerationStrategy.DB_SEQUENCE, Long.class);
        commonJdbcUtils.setLongIdForEntity(entity, newId);
        Object[] args = commonJdbcUtils.generateJdbcArgsForEntity(entity, sqlAndParams);
        jdbcTemplate.getJdbcOperations().update(sqlAndParams.getSql(), args);
    }

    /**
     * Bulk persist. All the entity should be of a same Type T
     *
     * @param entities
     */
    public void persist(List<T> entities) {
        Assert.notNull(entities);
        T sampleEntity = entities.get(0);
        SqlAndParams sqlAndParams = commonJdbcJpaEntitySqlGenerator.generateInsertSql(sampleEntity.getClass(),
                getSchema());
        for (T entity : entities) {
            Long newId = jdbcIdService.generateNewId(JdbcIdService.IdGenerationStrategy.DB_SEQUENCE, Long.class);
            commonJdbcUtils.setLongIdForEntity(entity, newId);
            Object[] args = commonJdbcUtils.generateJdbcArgsForEntity(entity, sqlAndParams);
            jdbcTemplate.getJdbcOperations().update(sqlAndParams.getSql(), args);
        }

    }

    public T merge(T entity) {
        commonJdbcUtils.preUpdate(entity);
        Object idValue = commonJdbcUtils.fetchIdValue(entity);
        if (idValue != null) {
            SqlAndParams sqlAndParams = commonJdbcJpaEntitySqlGenerator.generateUpdateSql(entity.getClass(),
                    getSchema());
            Object[] args = commonJdbcUtils.generateJdbcArgsForEntity(entity, sqlAndParams);
            jdbcTemplate.getJdbcOperations().update(sqlAndParams.getSql(), args);
            return entity;
        } else {
            logger.error("Merge called on the entity having no identity value. Entity {}", entity);
            throw new RuntimeException(
                    "Merge called on the entity having no identity value. Entity => " + entity.toString());
        }

    }

    public void remove(T entity) {
        Object idValue = commonJdbcUtils.fetchIdValue(entity);
        if (idValue != null) {
            SqlAndParams sqlAndParams = commonJdbcJpaEntitySqlGenerator.generateDeleteSql(entity.getClass(),
                    getSchema());
            Object[] args = commonJdbcUtils.generateJdbcArgsForEntity(entity, sqlAndParams);
            jdbcTemplate.getJdbcOperations().update(sqlAndParams.getSql(), args);
            return;
        } else {
            logger.error("Remove called on the entity having no identity value. Entity {}", entity);
            throw new RuntimeException(
                    "Merge called on the entity having no identity value. Entity => " + entity.toString());
        }

    }

    public void remove(Long id) {
        if (id != null) {
            SqlAndParams sqlAndParams = commonJdbcJpaEntitySqlGenerator.generateDeleteSql(type, getSchema());
            // Object[] args = commonJdbcUtils.generateJdbcArgsForEntity(type,
            // sqlAndParams);
            jdbcTemplate.getJdbcOperations().update(sqlAndParams.getSql(), new Object[]{id});
            return;
        } else {
            logger.error("Remove called on the entity having no identity value.");
            throw new RuntimeException("Remove called on the entity having no identity value.");
        }

    }

    public Collection<T> findAll() {
        SqlAndParams sqlAndParams = commonJdbcJpaEntitySqlGenerator.generateSelectSql(type, null, getSchema());
        List<T> retList = (List<T>) jdbcTemplate.getJdbcOperations().query(sqlAndParams.getSql(),
                this.rowMapper, new Object[]{});
        return retList;
    }

    protected String getSchema() {
        return this.schema;
    }

    public RowMapper<T> getRowMapper() {
        return rowMapper;
    }

    public void setRowMapper(RowMapper<T> rowMapper) {
        if (rowMapper != null) {
            this.rowMapper = rowMapper;
        }
    }
}
