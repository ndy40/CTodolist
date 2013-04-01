/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sussex.ejb;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import uk.ac.sussex.entity.Group;
import uk.ac.sussex.entity.User;
import uk.ac.sussex.exceptions.DuplicateEmailException;

/**
 *
 * @author ne51
 */
@Stateless
public class AccountEJB implements AccountEJBLocal {

    @PersistenceContext(unitName = "TodolistEJBPU")
    private EntityManager em;

    @Override
    public void createUser(User user) throws DuplicateEmailException {
        if (getUserByEmail(user.getEmail()) != null) {
            throw new DuplicateEmailException("Email already exist");
        }
        persist(user);
    }

    public void persist(Object object) {
        em.persist(object);
    }

    @Override
    public User getUser(long userId) {
        return em.find(User.class, userId);
    }

    @Override
    public List<User> getUsers() {
        Query query = em.createNamedQuery("User.getAllUsers", User.class);
        List<User> users = query.getResultList();
        return users;
    }
    
    @Override
    public User getUser(String email, String password){
        User user = null;
        try{
        Query query = em.createNamedQuery("User.getUserLogin",User.class);
        query.setParameter("email", email);
        query.setParameter("password", password);
        user = (User)query.getSingleResult();
        }catch(NoResultException ex){
            
        }catch(Exception ex){
            Logger.getLogger(AccountEJB.class.getSimpleName()).log(Level.SEVERE, ex.getMessage());
        }
        return  user;
        
    }

    /**
     *
     * @param email
     * @return
     */
    @Override
    public User getUserByEmail(String email) {
        User u = null;
        try {
            Query q = em.createNamedQuery("User.getByEmail", User.class);
            q.setParameter("email", email);
            u = (User) q.getSingleResult();
        } catch (NoResultException ex) {
            
        }
        return u;
    }

    @Override
    public Group getGroup(long id) {
        return (Group) em.find(Group.class, id);
    }

    @Override
    public Group getGroup(String name) {
        Query q = em.createNamedQuery("Group.findByName", Group.class);
        q.setParameter("name", name);
        return (Group) q.getSingleResult();
    }

    @Override
    public void createGroup(Group group) {
        persist(group);
    }
    
    @Override
    public void updateUser(User user){
        em.merge(user);
    }
}
