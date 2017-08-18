package jdbc.forjpa.core.dao.rowmapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Registers any spring beans that are of type {@link org.springframework.jdbc.core.RowMapper}
 * <p>
 * Created by archangohel on 16/08/17.
 */
@Component("rowMapperBeanRegistrar")
public class RowMapperBeanRegistrar implements BeanPostProcessor {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RowMapperFactory rowMapperFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        logger.trace("postProcessAfterInitialization for bean {} , beanName {}", bean, beanName);
        if (RowMapper.class.isAssignableFrom(bean.getClass())) {
            Type genericTypeOfRowMapper = getGenericType(bean);
            if (genericTypeOfRowMapper != null) {
                rowMapperFactory.registerRowMapper((Class)genericTypeOfRowMapper, (RowMapper) bean);
            }
        }
        return bean;
    }

    private Type getGenericType(Object bean) {
        Type[] genericInterfaces = bean.getClass().getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType) {
                Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
                for (Type genericType : genericTypes) {
                    logger.debug("Generic type: {}", genericType);
                    return genericType;
                }
            }
        }
        return null;
    }
}
