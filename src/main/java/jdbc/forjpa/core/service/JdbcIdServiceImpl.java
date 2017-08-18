package jdbc.forjpa.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Implementation for id generation with different strategies (Currently only
 * supported is Sequence base).
 *
 * @author Archan
 */
@Component("jdbcIdService")
@DependsOn({"uuidGenerator", "dbSequenceBasedIdGenerator"})
public class JdbcIdServiceImpl implements JdbcIdService {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("dbSequenceBasedIdGenerator")
    private IdGenerator<Long> sequenceBasedIdGenerator;

    @Autowired
    @Qualifier("uuidGenerator")
    private IdGenerator<UUID> uuidGenerator;

    @SuppressWarnings("unchecked")
    public <T> T generateNewId(IdGenerationStrategy stategy, Class<T> clazz) {
        if (clazz.isAssignableFrom(Long.class)) {
            if (IdGenerationStrategy.DB_SEQUENCE.equals(stategy)) {
                return (T) sequenceBasedIdGenerator.generate();
            } else if (IdGenerationStrategy.UUID.equals(stategy)) {
                return (T) uuidGenerator.generate();
            } else {
                logger.error("Invalid Id generation strategy passed {} for class {} ", stategy, clazz);
            }
        } else {
            logger.error("Id generator not supported for class {}", clazz);
        }
        return null;
    }
}
