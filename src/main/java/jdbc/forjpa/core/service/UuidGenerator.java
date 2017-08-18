package jdbc.forjpa.core.service;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Database sequence way of generating an ID.
 *
 * @author Archan
 */
@Component("uuidGenerator")
public class UuidGenerator implements IdGenerator<UUID> {

    /**
     * Generate new Long id. Currently working for PG only.
     *
     * @return
     */

    public UUID generate() {
        return UUID.randomUUID();
    }
}
