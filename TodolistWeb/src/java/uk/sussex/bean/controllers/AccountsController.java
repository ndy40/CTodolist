/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.sussex.bean.controllers;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import uk.ac.sussex.ejb.AccountEJBLocal;
import uk.ac.sussex.entity.Group;
import uk.ac.sussex.entity.User;
import uk.ac.sussex.exceptions.DuplicateEmailException;
import uk.sussex.bean.backing.Login;
import uk.sussex.bean.backing.RegisterationBean;

/**
 *
 * @author ne51
 */
@ManagedBean(name = "accountsController")
@ViewScoped
public class AccountsController {

    @Inject
    private RegisterationBean regBean;
    @EJB
    AccountEJBLocal accountsEJB;
    @Inject
    private Login loginBean;

    /**
     * Creates a new instance of AccountsController
     */
    public AccountsController() {
    }

    public String registerUser() {
        FacesMessage msg = null;
        try {
            if(!regBean.isValid())
                throw new Exception("Passwords must match");
            Group group = accountsEJB.getGroup("USER");
            User user = new User();
            user.setFirstName(regBean.getFirstName());
            user.setLastName(regBean.getLastName());
            user.setEmail(regBean.getEmail());
            user.setPassword(regBean.getPassword());
            user.setTitle(regBean.getTitle());
            user.setGroup(group);
            accountsEJB.createUser(user);
            regBean = new RegisterationBean();
            msg = new FacesMessage("User successfully created");
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
        } catch (DuplicateEmailException ex) {
            msg = new FacesMessage(ex.getMessage());
        } catch (Exception ex) {
            msg = new FacesMessage(ex.getMessage());
        }
        FacesContext.getCurrentInstance().addMessage("regmessage", msg);
        return "index";
    }

    /**
     * @return the regBean
     */
    public RegisterationBean getRegBean() {
        return regBean;
    }

    /**
     * @param regBean the regBean to set
     */
    public void setRegBean(RegisterationBean regBean) {
        this.regBean = regBean;
    }

    public String authenticate() {
        if (getLoginBean() == null) {
            return "login_fail";
        }
        

        return "login_success";
    }

    /**
     * @return the loginBean
     */
    public Login getLoginBean() {
        return loginBean;
    }

    /**
     * @param loginBean the loginBean to set
     */
    public void setLoginBean(Login loginBean) {
        this.loginBean = loginBean;
    }
}
