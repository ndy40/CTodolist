/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.sussex.bean.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import jsf.util.JsfUtil;
import uk.ac.sussex.ejb.AccountEJBLocal;
import uk.ac.sussex.ejb.TaskEJBLocal;
import uk.ac.sussex.entity.Task;
import uk.ac.sussex.entity.User;
import uk.sussex.bean.backing.Login;
import uk.sussex.bean.backing.SearchData;
import uk.sussex.bean.backing.TaskBean;

/**
 *
 * @author ne51
 */
@ManagedBean(name = "taskController")
@SessionScoped
public class TaskController implements Serializable {

    @EJB
    TaskEJBLocal taskEJB;
    @EJB
    AccountEJBLocal accountEJB;
    @ManagedProperty(value = "#{loginBean}")
    private Login login;
//    @ManagedProperty(value = "#{taskBean}")
    private TaskBean taskBean;
    private boolean orderByDuedate = true;
    private List<Task> taskCollection;
    private UIInput assignedUser;
    @ManagedProperty(value = "#{searchData}")
    private SearchData searchData;

    @PostConstruct
    public void init() {
    }

    public TaskBean getSelected() {
        if (taskBean == null) {
            taskBean = new TaskBean();
        }
        return taskBean;
    }

    public String createTask() {

        try {
            Task task = new Task();
            task.setTitle(taskBean.getTitle());
            task.setNote(taskBean.getNote());
            task.setCompleted(taskBean.isCompleted());
            task.setDueDate(taskBean.getDueDate());
            task.setCreateDate(new Date());
            task.setOwner(login.getUser());
            task.setAssignedTo(accountEJB.getUser(taskBean.getAssignedUser()));
            task.setPriority(taskBean.getPriority());

            taskEJB.createTask(task);
            JsfUtil.addSuccessMessage("Task created");
            return prepareCreate();
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex.getMessage());
            return null;
        }


    }

    public List<Task> getAssignedTask() {
        return null;
    }

    /**
     * @return the taskBean
     */
    public TaskBean getTaskBean() {
        return taskBean;
    }

    /**
     * @param taskBean the taskBean to set
     */
    public void setTaskBean(TaskBean taskBean) {
        this.taskBean = taskBean;
    }

    /**
     * @return the login
     */
    public Login getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(Login login) {
        this.login = login;
    }

    public String deleteTask(long id) {
        FacesMessage msg = new FacesMessage();
        try {
            taskEJB.delete(id);
            JsfUtil.addSuccessMessage("Task has been deleted");
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Failed to delete message");
        }

        return prepareTask();
    }

    public void editTask(long id) {
        try {
            //get task 
            Task task = taskEJB.getTask(id);
            //assign task to backing beam
            taskBean = new TaskBean();
            taskBean.setTitle(task.getTitle());
            taskBean.setNote(task.getNote());
            taskBean.setAssignedUser(task.getAssignedTo().getId());
            taskBean.setCompleted(task.isCompleted());
            taskBean.setDueDate(task.getDueDate());
            taskBean.setPriority(task.getPriority());
            taskBean.setOwner(task.getOwner().getId());
            taskBean.setCreatedDate(task.getCreateDate());
            taskBean.setTaskId(task.getId());

            FacesContext ctxt = FacesContext.getCurrentInstance();
            ExternalContext exCtx = ctxt.getExternalContext();
            exCtx.addResponseHeader("taskId", String.valueOf(id));
            exCtx.redirect("edittask.xhtml?action=edit");
        } catch (IOException ex) {
            Logger.getLogger(TaskController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String updateTask() {
        try {
            //fetch task and update before sending to database
            Task t = taskEJB.getTask(taskBean.getTaskId());
            t.setAssignedTo(accountEJB.getUser(taskBean.getAssignedUser()));
            t.setOwner(accountEJB.getUser(taskBean.getOwner()));
            t.setCompleted(taskBean.isCompleted());
            t.setDueDate(taskBean.getDueDate());
            t.setNote(taskBean.getNote());
            t.setTitle(taskBean.getTitle());
            t.setPriority(taskBean.getPriority());
            taskEJB.update(t);
            FacesMessage message = new FacesMessage();
            message.setSummary("Task is updated");
            message.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext ctxt = FacesContext.getCurrentInstance();
            ctxt.addMessage(null, message);
        } catch (Exception ex) {
            Logger.getLogger(TaskController.class.getSimpleName()).log(Level.SEVERE, ex.getMessage());
        }

        return "tasks";
    }

    public void viewTask(long id) {
        try {
            //get task 
            Task task = taskEJB.getTask(id);
            //assign task to backing beam
            taskBean = new TaskBean();
            taskBean.setTitle(task.getTitle());
            taskBean.setNote(task.getNote());
            taskBean.setAssignedUser(task.getAssignedTo().getId());
            taskBean.setCompleted(task.isCompleted());
            taskBean.setDueDate(task.getDueDate());
            taskBean.setPriority(task.getPriority());
            taskBean.setOwner(task.getOwner().getId());
            taskBean.setCreatedDate(task.getCreateDate());
            taskBean.setTaskId(task.getId());
            FacesContext ctxt = FacesContext.getCurrentInstance();
            ExternalContext exCtx = ctxt.getExternalContext();
            exCtx.addResponseHeader("taskId", String.valueOf(id));
            exCtx.redirect("edittask.xhtml?action=view");
        } catch (IOException ex) {
            Logger.getLogger(TaskController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String prepareCreate() {
        taskBean = new TaskBean();
        return "createtask";
    }

    /**
     * @return the assignedUser
     */
    public UIInput getAssignedUser() {
        return assignedUser;
    }

    /**
     * @param assignedUser the assignedUser to set
     */
    public void setAssignedUser(UIInput assignedUser) {
        this.assignedUser = assignedUser;
    }

    /**
     * @return the taskCollection
     */
    public List<Task> getTaskCollection() {
        return taskCollection;
    }

    /**
     * @param taskCollection the taskCollection to set
     */
    public void setTaskCollection(List<Task> taskCollection) {
        this.taskCollection = taskCollection;
    }

    public String prepareTask() {
        User owner = null;
        User assignedTo = null;
        
        if (searchData.getTaskBy() > -1) {
            if (searchData.getTaskBy() > 0) {
                owner = login.getUser();
            }
        }
        taskCollection = taskEJB.getTasks(owner,assignedTo,searchData.getStatus(), searchData.getStartDate(), searchData.getEndDate(), 0, 20);
        return "tasks";


    }
    
    public String prepareTask(ActionEvent event) {
        User owner = null;
        User assignedTo = null;
        
        if (searchData.getTaskBy() > -1) {
            if (searchData.getTaskBy() > 0) {
                owner = login.getUser();
            }
        }
        taskCollection = taskEJB.getTasks(owner,assignedTo,searchData.getStatus(), searchData.getStartDate(), searchData.getEndDate(), 0, 20);
        return "tasks";


    }


    /**
     * @return the orderByDuedate
     */
    public boolean isOrderByDuedate() {
        return orderByDuedate;
    }

    /**
     * @param orderByDuedate the orderByDuedate to set
     */
    public void setOrderByDuedate(boolean orderByDuedate) {
        this.orderByDuedate = orderByDuedate;
    }

    public String orderByDueDate() {
        if (orderByDuedate) {
            Collections.sort(taskCollection, new Comparator<Task>() {
                @Override
                public int compare(Task o1, Task o2) {
                    return o1.getDueDate().compareTo(o2.getDueDate());
                }
            });
            orderByDuedate = false;
        } else {
            Collections.sort(taskCollection, new Comparator<Task>() {
                @Override
                public int compare(Task o1, Task o2) {
                    return o2.getDueDate().compareTo(o1.getDueDate());
                }
            });
            orderByDuedate = true;
        }
        return null;
    }

    public String getSearchTask() {
        User assignedUser = null;
        User owner = null;
        Date startDate = (getSearchData().getStartDate() == null) ? (new GregorianCalendar(2000, 1, 1)).getTime() : getSearchData().getStartDate();
        Date endDate = (getSearchData().getEndDate() == null) ? (new GregorianCalendar(2090, 1, 1)).getTime() : getSearchData().getEndDate();

        return null;
    }

    /**
     * @return the searchData
     */
    public SearchData getSearchData() {
        return searchData;
    }

    /**
     * @param searchData the searchData to set
     */
    public void setSearchData(SearchData searchData) {
        this.searchData = searchData;
    }
}
