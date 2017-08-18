package jdbc.forjpa.main.springapp;

import jdbc.forjpa.core.dao.BaseDao;
import jdbc.forjpa.core.dao.DaoFactory;
import jdbc.forjpa.main.AppConstants;
import jdbc.forjpa.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by archangohel on 15/08/17.
 */

public class SpringBootApp {
    private static Logger logger = LoggerFactory.getLogger(SpringBootApp.class);

    public static void main(String args[]) {
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        DaoFactory daoFactory = context.getBean(DaoFactory.class);
        BaseDao<User> userDao = daoFactory.getDaoInstance(User.class);
        long start = 0;
        long end = 0;
        List<Long> durationList = new ArrayList<>();
        for (int count = 0; count < AppConstants.NUMBER_OF_ITERATIONS; count++) {
            start = System.currentTimeMillis();
            Collection<User> usersList = userDao.findAll();
            end = System.currentTimeMillis();
            durationList.add((end - start));
            logger.info("ITERATION => {} ,Time in MS {}", count, (end - start));
            logger.info("ITERATION => {} ,Count {} ", count, usersList.size());
            logger.debug("ITERATION => {} ,Count {} => Users {}", count, usersList.size(), usersList);
        }
        double average = durationList.stream().mapToLong(n -> n).average().getAsDouble();
        logger.info("Average Time in MS {}", average);
    }

    private static void createUsers(BaseDao<User> userDao) {
        for (int i = 0; i < 1000; i++) {
            User user = new User();
            user.setName("UserName" + i);
            userDao.persist(user);
        }
    }
}
