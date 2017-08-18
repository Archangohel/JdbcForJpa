package jdbc.forjpa.main;

import jdbc.forjpa.model.Cart;
import jdbc.forjpa.model.Items;
import jdbc.forjpa.model.User;
import jdbc.forjpa.util.HibernateAnnotationUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by archangohel on 18/08/17.
 */
public class DataCreationApp {
    private static Logger logger = LoggerFactory.getLogger(DataCreationApp.class);
    private static Random RANDOM_NUM_GENERATOR = new Random();

    public static void main(String[] args) {
        SessionFactory sessionFactory = null;
        Session session = null;
        Transaction tx = null;
        try {
            //Get Session
            sessionFactory = HibernateAnnotationUtil.getSessionFactory();
            List<Long> durationList = new ArrayList<>();
            //start transaction
            session = sessionFactory.getCurrentSession();
            tx = session.beginTransaction();
            Query deleteUsersQuery = session.createQuery("DELETE from User");
            deleteUsersQuery.executeUpdate();
            Query deleteItemsQuery = session.createQuery("DELETE from Items");
            deleteItemsQuery.executeUpdate();
            Query deleteCartQuery = session.createQuery("DELETE from Cart");
            deleteCartQuery.executeUpdate();

            for (User user : generateUsers()) {
                session.persist(user);
            }
            for (Cart cart : generateCarts()) {
                session.persist(cart);
                for (Items item : cart.getItems()) {
                    session.persist(item);
                }
            }
            tx.commit();
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

    private static List<User> generateUsers() {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 5000; i++) {
            User user = new User();
            user.setName("UserName" + i);
            userList.add(user);
        }
        return userList;
    }

    private static List<Cart> generateCarts() {
        List<Cart> cartList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Cart cart = new Cart();
            cart.setName("Cart" + i);
            cart.setTotal(RANDOM_NUM_GENERATOR.nextDouble());
            cart.setItems(generateItems(cart, RANDOM_NUM_GENERATOR.nextInt(10)));
            cartList.add(cart);
        }
        return cartList;
    }

    private static Set<Items> generateItems(Cart cart, int count) {
        Set items = new HashSet<>();
        for (int i = 0; i < count; i++) {
            Items item = new Items();
            item.setItemId("ID-" + i);
            item.setItemTotal(RANDOM_NUM_GENERATOR.nextDouble());
            item.setQuantity(RANDOM_NUM_GENERATOR.nextInt(25));
            item.setCart(cart);
            items.add(item);
        }
        return items;

    }
}
