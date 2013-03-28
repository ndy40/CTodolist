/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sussex.ejb;

import com.google.common.base.Predicates;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import uk.ac.sussex.entity.Task;
import uk.ac.sussex.entity.Task_;
import uk.ac.sussex.entity.User;

/**
 *
 * @author ne51
 */
@Stateless
public class TaskEJBB implements TaskEJBBLocal {

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
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Task> cq = cb.createQuery(Task.class);
        Root<Task> root = cq.from(Task.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(cb.equal(root.get(Task_.completed), status));
        if (assigned != null) {
            predicates.add(cb.equal(root.get(Task_.assignedTo), owner));
        }
        //filter criteria for start and end date
        Date strtDate;
        strtDate = (startDate == null) ? new Date(1900, 01, 01) : startDate;
        Date end;
        end = (startDate == null) ? new Date(1900, 01, 01) : startDate;
        predicates.add(cb.between(root.get(Task_.createDate), strtDate, end));

        cq.where(predicates.toArray(new Predicate[predicates.size()]));
        Query query = em.createQuery(cq);
        if (startIndex > -1) {
            query.setFirstResult(startIndex);
        }

        if (maxSize > 0) {
            query.setMaxResults(maxSize);
        }
        List<Task> tasks = query.getResultList();
        return tasks;
    }

    @Override
    public int getTasksCount(User owner, User assigned, boolean status, String orderBy, Date startDate, Date endDate) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Task> cq = cb.createQuery(Task.class);
        Root<Task> root = cq.from(Task.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(cb.equal(root.get(Task_.completed), status));
        if (assigned != null) {
            predicates.add(cb.equal(root.get(Task_.assignedTo), owner));
        }
        //filter criteria for start and end date
        Date strtDate;
        strtDate = (startDate == null) ? new Date(1900, 01, 01) : startDate;
        Date end;
        end = (startDate == null) ? new Date(1900, 01, 01) : startDate;
        predicates.add(cb.between(root.get(Task_.createDate), strtDate, end));

        cq.where(predicates.toArray(new Predicate[predicates.size()]));
        Query query = em.createQuery(cq);

        List<Task> tasks = query.getResultList();
        return tasks.size();
    }

    public void persist(Object object) {
        em.persist(object);
    }
}
