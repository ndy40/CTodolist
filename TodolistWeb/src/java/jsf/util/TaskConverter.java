/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.util;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import uk.ac.sussex.ejb.AccountEJB;
import uk.ac.sussex.entity.User;
import uk.sussex.bean.controllers.AccountsController;

/**
 *
 * @author ndy40
 */
@ManagedBean
@SessionScoped
@FacesConverter(value = "uk.sussex.TaskConverter")
public class TaskConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        AccountEJB account;
        User user = null;
        try {
            account = (AccountEJB) new InitialContext().lookup("java:comp/env/AccountEJB");
            user = account.getUser(Long.parseLong(value));
            return user;
        } catch (NamingException ex) {
            Logger.getLogger(TaskConverter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return user;        
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value.toString();
    }
}
