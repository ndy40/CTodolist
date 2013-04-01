/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sussex.ejb;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import uk.ac.sussex.entity.Task;
import uk.ac.sussex.entity.User;

/**
 *
 * @author ne51
 */
@Stateless
public class TaskEJB implements TaskEJBLocal {

    @PersistenceContext(unitName = "TodolistEJBPU")
    private EntityManager em;

    @Override
    public void createTask(Task task) {
        persist(task);
    }

    @Override
    public void update(Task task) {
        em.merge(task);
    }

    @Override
    public void delete(Task task) {
        Task t = em.merge(task);
        em.remove(t);
    }

    @Override
    public java.util.List<Task> getTasks(User owner, User assigned, boolean status, String orderBy, Date startDate, Date endDate, int startIndex, int maxSize) {
       StringBuilder builder = new StringBuilder();
       builder.append("SELECT t FROM TASKS t WHERE t.owner = :owner");
       if(assigned != null)
           builder.append(" and t.assignedTo = := assignedTo");
       builder.append(" and (t.createDate between :startDate and :endDate)");
       
       Query query = em.createQuery(builder.toString());
       query.setParameter("owner",owner);
       if(assigned != null){
           query.setParameter("assignedTo", assigned);
       }
       startDate = (startDate == null)? (new GregorianCalendar(2000,01,01)).getTime():startDate;
       endDate = (endDate == null)? (new GregorianCalendar(2020,01,01)).getTime():endDate;
       query.setParameter("startDate", startDate);
       query.setParameter("endDate",endDate);
       
       if(startIndex > -1){
           query.setFirstResult(startIndex);
       }
       
       if(maxSize > 0){
           query.setMaxResults(maxSize);
       }
       
        return (List<Task>)query.getResultList();
    }

    public void persist(Object object) {
        em.persist(object);
    }

    @Override
    public int getTasksCount(User owner, User assigned, boolean status, String orderBy, Date startDate, Date endDate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
