/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sussex.ejb;

import java.util.ArrayList;
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
    public void delete(long id){
        Task t = getTask(id);
        delete(t);
    }
    
    @Override
    public Task getTask(long id){
        return em.find(Task.class, id);
    }

    @Override
    public java.util.List<Task> getTasks(User owner, User assigned, int status, Date startDate, Date endDate, int startIndex, int maxSize) {
        ArrayList<String> sqlWhere = new ArrayList<String>();
       int pCount = -1;
       StringBuilder builder = new StringBuilder();
       builder.append("SELECT t FROM Task t WHERE ");
       
       if(owner != null){
           sqlWhere.add("t.owner = :owner");
       }
       if(assigned != null){
           sqlWhere.add("t.assignedTo = := assignedTo");
       }
       
       sqlWhere.add("(t.createDate between :startDate and :endDate)");
       
       boolean taskStatus = false;
       if(status > -1){
           taskStatus = Boolean.parseBoolean(String.valueOf(status));
           sqlWhere.add("t.completed = :status");
       }
       
       
       for(int i = 0 ; i < sqlWhere.size(); i++){
           if(i == 0){
               builder.append(sqlWhere.get(i));               
           }else{
               builder.append(" and ").append(sqlWhere.get(i));
           }
       }
       
       Query query = em.createQuery(builder.toString(),Task.class);
       if(owner != null){
            query.setParameter("owner",owner);
       }
       if(assigned != null){
           query.setParameter("assignedTo", assigned);
       }
       
       if(status > -1)
           query.setParameter("status", taskStatus);
       
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
    public int getTasksCount(User owner, User assigned, int status, Date startDate, Date endDate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
