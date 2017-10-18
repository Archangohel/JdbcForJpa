package jdbc.forjpa.core.service.identity;

/**
 * Created by archangohel on 14/08/17.
 */
public interface IdGenerator<T> {
    T generate();
}
