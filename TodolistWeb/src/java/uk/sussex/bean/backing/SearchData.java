/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.sussex.bean.backing;

import java.util.Date;
import java.util.GregorianCalendar;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author ndy40
 */
@ManagedBean
@SessionScoped
public class SearchData {
    private Date startDate = (new GregorianCalendar(2000,01,01)).getTime();
    private Date endDate = (new GregorianCalendar(2090,01,01)).getTime();
    private int status = -1;
    private int taskBy;

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the taskBy
     */
    public int getTaskBy() {
        return taskBy;
    }

    /**
     * @param taskBy the taskBy to set
     */
    public void setTaskBy(int taskBy) {
        this.taskBy = taskBy;
    }
}
