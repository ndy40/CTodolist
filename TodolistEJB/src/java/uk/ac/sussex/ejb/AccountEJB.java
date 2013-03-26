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
        if(getUserByEmail(user.getEmail()) != null)
            throw new DuplicateEmailException("Email already exist");
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
    
    /**
     *
     * @param email
     * @return
     */
    @Override
    public User getUserByEmail(String email){
        Query q = em.createNamedQuery("User.getByEmail",User.class);
        q.setParameter("email", email);
        User u = (User) q.getSingleResult();
        return u;
    }
    
    @Override
    public Group getGroup(long id){
        return (Group)em.find(Group.class, id);
    }
    
    @Override
    public Group getGroup(String name){
        Query q = em.createNamedQuery("Group.findByName",Group.class);
        q.setParameter("name", name);
        return (Group)q.getSingleResult();
    }
    
    
    

}
