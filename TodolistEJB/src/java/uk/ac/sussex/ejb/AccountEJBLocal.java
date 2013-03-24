/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sussex.ejb;

import java.util.List;
import javax.ejb.Local;
import uk.ac.sussex.entity.User;

/**
 *
 * @author ne51
 */
@Local
public interface AccountEJBLocal {

    void createUser(User user);

    User getUser(long userId);

    List getUsers();

       
}
