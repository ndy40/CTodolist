/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.sussex.bean.controllers;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import uk.ac.sussex.ejb.AccountEJBLocal;
import uk.ac.sussex.ejb.TaskEJBLocal;
import uk.ac.sussex.entity.Task;
import uk.ac.sussex.entity.User;
import uk.sussex.bean.backing.Login;
import uk.sussex.bean.backing.Paginator;
import uk.sussex.bean.backing.TaskBean;

/**
 *
 * @author ne51
 */
@Named(value = "taskController")
@SessionScoped
public class TaskController implements Serializable {

    @EJB
    TaskEJBLocal taskEJB;
    @EJB
    AccountEJBLocal accountEJB;
    @Inject
    Login login;
    @Inject
    TaskBean taskBean;
    
    Paginator pagination;

    /**
     * Creates a new instance of TaskController
     */
    public TaskController() {
        
        pagination = new Paginator();
    }

    public String createTask() {
        FacesMessage messages = new FacesMessage();
        try {
            Task task = new Task();
            User assignedUser = accountEJB.getUser(taskBean.getAssignedUser());
            task.setAssignedTo(assignedUser);
            task.setTitle(taskBean.getTitle());
            task.setNote(taskBean.getNote());
            task.setDueDate(taskBean.getDueDate());
            task.setCreateDate(new Date());
            task.setPriority(taskBean.getPriority());
            taskEJB.createTask(task);
            messages.setSummary("Task created!!  ");
        } catch (Exception ex) {
            messages.setSummary(ex.getMessage());
        }
        FacesContext.getCurrentInstance().addMessage(null, messages);
        return "createTask.xhtml";
    }

    public List<Task> getOwnTask() {
        List<Task> tasks = taskEJB.getTasks(login.getUser(), null, false, "ASC", null, null, 0, 20);
        return tasks;
    }

    public List<Task> getAssignedTask() {
        return null;
    }
}
