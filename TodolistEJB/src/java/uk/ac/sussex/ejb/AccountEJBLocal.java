/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sussex.ejb;

import java.util.List;
import javax.ejb.Local;
import uk.ac.sussex.entity.Group;
import uk.ac.sussex.entity.User;
import uk.ac.sussex.exceptions.DuplicateEmailException;

/**
 *
 * @author ne51
 */
@Local
public interface AccountEJBLocal {

    void createUser(User user) throws DuplicateEmailException;

    User getUser(long userId);

    List getUsers();

    public User getUserByEmail(String email);

    public Group getGroup(long id);

    public Group getGroup(String name);

    public void createGroup(Group group);

    public User getUser(String email, String password);

    public void updateUser(User user);

       
}
