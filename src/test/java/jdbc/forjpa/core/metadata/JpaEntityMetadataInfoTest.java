package jdbc.forjpa.core.metadata;

import jdbc.forjpa.demo.entity.SampleEntity;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by archangohel on 13/08/17.
 */
public class JpaEntityMetadataInfoTest {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void testInit() {
        JpaEntityMetadataInfo<SampleEntity> jpaEntityMetadataInfo = new JpaEntityMetadataInfo<SampleEntity>(SampleEntity.class);
        logger.info("testInit {}", jpaEntityMetadataInfo);
    }
}
