package jdbc.forjpa.core.metadata;

import jdbc.forjpa.demo.entity.SampleEntity;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by archangohel on 14/08/17.
 */
public class JpaEntityMetadataFactoryTest {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private JpaEntityMetadataFactory jpaEntityMetadataFactory = new JpaEntityMetadataFactory();

    @Test
    public void testFactoryNewEntry() {
        JpaEntityMetadataInfo<SampleEntity> metadataInfo = jpaEntityMetadataFactory.getEntityMetadata(SampleEntity.class);
        logger.info("New metadata info created :: {}", metadataInfo);
        Assert.assertNotNull(metadataInfo);
    }

    @Test
    public void testFactoryFromCache() {
        JpaEntityMetadataInfo<SampleEntity> metadataInfo = jpaEntityMetadataFactory.getEntityMetadata(SampleEntity.class);
        logger.info("New metadata info created :: {}", metadataInfo);
        JpaEntityMetadataInfo<SampleEntity> metadataInfoFromCache = jpaEntityMetadataFactory.getEntityMetadata(SampleEntity.class);
        logger.info("Cached metadata info :: {}", metadataInfoFromCache);
        Assert.assertEquals(metadataInfoFromCache, metadataInfo);
    }

    @Test
    public void testFactoryCleanCache() {
        JpaEntityMetadataInfo<SampleEntity> metadataInfo = jpaEntityMetadataFactory.getEntityMetadata(SampleEntity.class);
        logger.info("New metadata info created :: {}", metadataInfo);
        jpaEntityMetadataFactory.cleanCache();
        JpaEntityMetadataInfo<SampleEntity> metadataInfo1 = jpaEntityMetadataFactory.getEntityMetadata(SampleEntity.class);
        logger.info("Another metadata info :: {}", metadataInfo1);
        Assert.assertNotEquals(metadataInfo1, metadataInfo);
    }


}
