package jdbc.forjpa.sql;

import jdbc.forjpa.core.metadata.JpaEntityMetadataFactory;
import jdbc.forjpa.core.metadata.JpaEntityMetadataHelper;
import jdbc.forjpa.core.sql.CommonJdbcJpaEntitySqlGenerator;
import jdbc.forjpa.core.sql.CommonJdbcSqlCache;
import jdbc.forjpa.core.sql.SqlAndParams;
import jdbc.forjpa.core.utils.CommonJdbcUtils;
import jdbc.forjpa.demo.entity.EmbeddedEntity;
import jdbc.forjpa.demo.entity.SampleEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by archangohel on 14/08/17.
 */
public class CommonJdbcJpaEntitySqlGeneratorTest {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private CommonJdbcJpaEntitySqlGenerator commonJdbcJpaEntitySqlGenerator = new CommonJdbcJpaEntitySqlGenerator();
    private CommonJdbcSqlCache commonJdbcSqlCache = new CommonJdbcSqlCache();
    private CommonJdbcUtils commonJdbcUtils = new CommonJdbcUtils();
    private JpaEntityMetadataHelper jpaEntityMetadataHelper = new JpaEntityMetadataHelper();
    private JpaEntityMetadataFactory jpaEntityMetadataFactory = new JpaEntityMetadataFactory();

    @Before
    public void initialize() {
        jpaEntityMetadataHelper.setJpaEntityMetadataFactory(jpaEntityMetadataFactory);

        commonJdbcJpaEntitySqlGenerator.setCommonJdbcSqlCache(commonJdbcSqlCache);
        commonJdbcJpaEntitySqlGenerator.setJpaEntityMetadataFactory(jpaEntityMetadataFactory);
        commonJdbcJpaEntitySqlGenerator.setJpaEntityMetadataHelper(jpaEntityMetadataHelper);

        commonJdbcUtils.setJpaEntityMetadataFactory(jpaEntityMetadataFactory);
        commonJdbcUtils.setJpaEntityMetadataHelper(jpaEntityMetadataHelper);
    }

    @Test
    public void testInsertSql() {
        SqlAndParams sqlAndParams = commonJdbcJpaEntitySqlGenerator.generateInsertSql(SampleEntity.class, "testSchema");
        logger.info("SqlAndParams :: " + sqlAndParams);
        SqlAndParams sqlAndParamsFromCache = commonJdbcJpaEntitySqlGenerator.generateInsertSql(SampleEntity.class,
                "testSchema");
        Assert.assertEquals(sqlAndParams, sqlAndParamsFromCache);
        Assert.assertEquals(sqlAndParams,
                commonJdbcSqlCache.getSqlsForEntity(SampleEntity.class.getName()).getInsertSql());
        SampleEntity entity = getSampleEntity();
        Object[] objArray = commonJdbcUtils.generateJdbcArgsForEntity(entity, sqlAndParams);
        Assert.assertEquals(objArray.length, 9);
    }

    private SampleEntity getSampleEntity() {
        SampleEntity entity = new SampleEntity();
        entity.setId(123l);
        EmbeddedEntity embeddedEntity = new EmbeddedEntity();
        embeddedEntity.setEmbeddedId(321l);
        embeddedEntity.setEmbeddedName("embeddedName");
        entity.setEmbeddedEntity(embeddedEntity);
        return entity;
    }

    @Test
    public void testUpdateSql() {
        SqlAndParams sqlAndParams = commonJdbcJpaEntitySqlGenerator.generateUpdateSql(SampleEntity.class, "testSchema");
        logger.info("SqlAndParams :: " + sqlAndParams);
        SqlAndParams sqlAndParamsFromCache = commonJdbcJpaEntitySqlGenerator.generateUpdateSql(SampleEntity.class,
                "testSchema");
        Assert.assertEquals(sqlAndParams, sqlAndParamsFromCache);
        Assert.assertEquals(sqlAndParams,
                commonJdbcSqlCache.getSqlsForEntity(SampleEntity.class.getName()).getUpdateSql());
        SampleEntity entity = getSampleEntity();
        Object[] objArray = commonJdbcUtils.generateJdbcArgsForEntity(entity, sqlAndParams);
        Assert.assertEquals(objArray.length, 9);
    }

    @Test
    public void testDeleteSql() {
        SqlAndParams sqlAndParams = commonJdbcJpaEntitySqlGenerator.generateDeleteSql(SampleEntity.class, "testSchema");
        logger.info("SqlAndParams :: " + sqlAndParams);
        SqlAndParams sqlAndParamsFromCache = commonJdbcJpaEntitySqlGenerator.generateDeleteSql(SampleEntity.class,
                "testSchema");
        Assert.assertEquals(sqlAndParams, sqlAndParamsFromCache);
        Assert.assertEquals(sqlAndParams,
                commonJdbcSqlCache.getSqlsForEntity(SampleEntity.class.getName()).getDeleteSql());
        SampleEntity entity = getSampleEntity();
        Object[] objArray = commonJdbcUtils.generateJdbcArgsForEntity(entity, sqlAndParams);
        Assert.assertEquals(objArray.length, 1);
    }

    @Test
    public void testSelectAllSql() {
        SqlAndParams sqlAndParams = commonJdbcJpaEntitySqlGenerator.generateSelectSql(SampleEntity.class, null,
                "testSchema");
        logger.info("SqlAndParams (without Filter):: " + sqlAndParams);
        SqlAndParams sqlAndParamsFromCache = commonJdbcJpaEntitySqlGenerator.generateSelectSql(SampleEntity.class, null,
                "testSchema");
        Assert.assertEquals(sqlAndParams, sqlAndParamsFromCache);
        Assert.assertEquals(sqlAndParams,
                commonJdbcSqlCache.getSqlsForEntity(SampleEntity.class.getName()).getSelectSql());

    }

    @Test
    public void testSelectWithFiltersSql() {
        List<String> filter = Arrays.asList(new String[]{"cart_id", "embedded_id"});
        SqlAndParams sqlAndParams = commonJdbcJpaEntitySqlGenerator.generateSelectSql(SampleEntity.class, filter,
                "testSchema");
        logger.info("SqlAndParams (with Filter):: " + sqlAndParams);
        SqlAndParams sqlAndParamsFromCache = commonJdbcJpaEntitySqlGenerator.generateSelectSql(SampleEntity.class,
                filter, "testSchema");
        Assert.assertEquals(sqlAndParams, sqlAndParamsFromCache);
        Assert.assertEquals(sqlAndParams,
                commonJdbcSqlCache.getSqlsForEntity(SampleEntity.class.getName()).getOtherSqls().get("cart_id-embedded_id"));
        SampleEntity entity = getSampleEntity();
        Object[] objArray = commonJdbcUtils.generateJdbcArgsForEntity(entity, sqlAndParams);
        Assert.assertEquals(objArray.length, 2);

    }

}
