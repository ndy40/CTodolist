/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.sussex.bean.controllers;

import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import uk.ac.sussex.ejb.AccountEJBLocal;
import uk.ac.sussex.entity.User;
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

    /**
     * Creates a new instance of AccountsController
     */
    public AccountsController() {
    }

    public String registerUser() {
        try{
        User user = new User();
        user.setFirstName(regBean.getFirstName());
        user.setLastName(regBean.getLastName());
        user.setEmail(regBean.getEmail());
        user.setPassword(regBean.getPassword());
        user.setTitle(regBean.getTitle());
        accountsEJB.createUser(user);
        regBean = new RegisterationBean();
        FacesMessage success = new FacesMessage("User successfully created");
        success.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage(null,success);
        }catch(Exception ex){
            FacesMessage msg = new FacesMessage("Error occured when creating user");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
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
}
