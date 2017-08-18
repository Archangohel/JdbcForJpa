package jdbc.forjpa.core.service;

/**
 * Interface to generate new ID.
 *
 * @author Archan
 */
public interface JdbcIdService {

    enum IdGenerationStrategy {
        DB_SEQUENCE, UUID
    }

    ;

    <T> T generateNewId(IdGenerationStrategy strategy, Class<T> clazz);
}
