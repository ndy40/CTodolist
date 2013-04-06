/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sussex.ejb;

import java.util.Date;
import javax.ejb.Local;
import uk.ac.sussex.entity.Task;
import uk.ac.sussex.entity.User;

/**
 *
 * @author ne51
 */
@Local
public interface TaskEJBLocal {

    void createTask(Task task);

    void update(Task task);

    void delete(Task task);

    java.util.List<Task> getTasks(User owner, User assigned, int status,Date startDate, Date endDate, int startIndex, int maxSize);

    public int getTasksCount(User owner, User assigned, int status, Date startDate, Date endDate);

    public Task getTask(long id);

    public void delete(long id);
    
}
