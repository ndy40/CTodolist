/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sussex.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import uk.ac.sussex.entity.User;

/**
 *
 * @author ne51
 */
@Stateless
public class AccountEJB implements AccountEJBLocal {
    @PersistenceContext(unitName = "TodolistEJBPU")
    private EntityManager em;

    
    @Override
    public void createUser(User user) {
        persist(user);
    }

    public void persist(Object object) {
        em.persist(object);
    }

    @Override
    public User getUser(long userId) {
        return null;
    }

    @Override
    public List<User> getUsers() {
        Query query = em.createNamedQuery("User.getAllUsers",User.class);
        List<User> users = query.getResultList();
        return users;
    }
    
    
    

}
