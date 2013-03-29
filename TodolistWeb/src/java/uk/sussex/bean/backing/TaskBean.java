/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.sussex.bean.backing;

import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author ne51
 */
@Named
@RequestScoped
public class TaskBean {
    private String title;
    private String note;
    private boolean completed;
    private Date dueDate;
    private Date createdDate;
    private long assignedUser;
    private long owner;
    private int priority;

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @return the completed
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * @param completed the completed to set
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * @return the dueDate
     */
    public Date getDueDate() {
        return dueDate;
    }

    /**
     * @param dueDate the dueDate to set
     */
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * @return the createdDate
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * @param createdDate the createdDate to set
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return the assignedUser
     */
    public long getAssignedUser() {
        return assignedUser;
    }

    /**
     * @param assignedUser the assignedUser to set
     */
    public void setAssignedUser(long assignedUser) {
        this.assignedUser = assignedUser;
    }

    /**
     * @return the owner
     */
    public long getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(long owner) {
        this.owner = owner;
    }

    /**
     * @return the priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }
    
}
