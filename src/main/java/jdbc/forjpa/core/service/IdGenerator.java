package jdbc.forjpa.core.service;

/**
 * Created by archangohel on 14/08/17.
 */
public interface IdGenerator<T> {
    T generate();
}
