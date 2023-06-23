package com.example.productshibernate.repository;

import com.example.productshibernate.model.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Component
public class ProductRepository {
    private static final EntityManager entityManager;
    private static final SessionFactory sessionFactory;

    static {
        sessionFactory = new Configuration()
                .configure("hibernate.conf.xml")
                .buildSessionFactory();
        entityManager = sessionFactory.createEntityManager();
    }

    public List<Product> findAll() {
        String query = "select p from Product p";
        TypedQuery<Product> typedQuery = entityManager.createQuery(query, Product.class);
        return typedQuery.getResultList();
    }

    public void save(Product product) {
        Transaction transaction = null;
        Product p;
        if (product.getId() != null) {
            p = findById(product.getId());
        } else {
            p = new Product();
        }
        try {
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction() ;

            p.setName(product.getName());
            p.setPrice(product.getPrice());
            p.setQuantity(product.getQuantity());

            session.saveOrUpdate(product);
            transaction.commit();
        } catch (Exception e) {
            assert transaction != null;
            transaction.rollback();
            e.printStackTrace();
        }
    }

    public Product findById(Long id) {
        String queryStr = "SELECT p FROM Product p WHERE p.id = :id";
        TypedQuery<Product> query = entityManager.createQuery(queryStr, Product.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    public void delete(Long id) {
        Transaction transaction = null;
        Product product = findById(id);
        try {
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction() ;
            session.remove(product);
            transaction.commit();
        } catch (Exception e) {
            assert transaction != null;
            transaction.rollback();
            e.printStackTrace();
        }
    }
}
