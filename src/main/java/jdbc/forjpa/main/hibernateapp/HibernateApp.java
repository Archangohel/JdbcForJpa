package jdbc.forjpa.main.hibernateapp;


import jdbc.forjpa.main.AppConstants;
import jdbc.forjpa.model.User;
import jdbc.forjpa.util.HibernateAnnotationUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class HibernateApp {
    private static Logger logger = LoggerFactory.getLogger(HibernateApp.class);

    public static void main(String[] args) {
        SessionFactory sessionFactory = null;
        Session session = null;
        Transaction tx = null;
        try {
            //Get Session
            sessionFactory = HibernateAnnotationUtil.getSessionFactory();
            List<Long> durationList = new ArrayList<>();
            //start transaction
            for (int count = 0; count < AppConstants.NUMBER_OF_ITERATIONS; count++) {
                session = sessionFactory.getCurrentSession();
                tx = session.beginTransaction();
                long start = System.currentTimeMillis();
                List<User> usersList = session.createQuery("from User").list();
                long end = System.currentTimeMillis();
                durationList.add((end - start));
                logger.info("ITERATION => {} ,Time in MS {}", count, (end - start));
                logger.info("ITERATION => {} ,Count {} ", count, usersList.size());
                logger.debug("ITERATION => {} ,Count {} => Users {}", count, usersList.size(), usersList);
                tx.commit();
            }
            double average = durationList.stream().mapToLong(n -> n).average().getAsDouble();
            logger.info("Average Time in MS {}", average);

        } catch (Exception e) {
            logger.error("Exception occured. " + e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (!sessionFactory.isClosed()) {
                logger.info("Closing SessionFactory");
                sessionFactory.close();
            }
        }
    }

}
