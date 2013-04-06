/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.sussex.bean.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import jsf.util.JsfUtil;
import uk.ac.sussex.ejb.AccountEJBLocal;
import uk.ac.sussex.entity.Group;
import uk.ac.sussex.entity.User;
import uk.ac.sussex.exceptions.DuplicateEmailException;
import uk.ac.sussex.exceptions.InvalidLoginException;
import uk.sussex.bean.backing.Login;
import uk.sussex.bean.backing.RegisterationBean;

/**
 *
 * @author ne51
 */
@ManagedBean(name = "accountsController")
@RequestScoped
public class AccountsController implements Serializable {

    @ManagedProperty(value = "#{regBean}") 
    private RegisterationBean regBean;
    @EJB
    private AccountEJBLocal accountsEJB;
    @ManagedProperty(value = "#{loginBean}")
    private Login loginBean;
    protected String originalUrl;
    
    private List<User> otherUsers;

    /**
     * Creates a new instance of AccountsController
     */
    public AccountsController() {                
    }

    @PostConstruct
    public void init() {
        ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
        originalUrl = (String) extContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);

        if (originalUrl == null) {
            originalUrl = extContext.getRequestContextPath() + "/HelperServlet";
        } else {
            String originalQuery = (String) extContext.getRequestMap().get(RequestDispatcher.FORWARD_QUERY_STRING);

            if (originalUrl != null) {
                originalUrl += "?" + originalQuery;
            }
        }

    }

    public String registerUser() {
        FacesMessage msg = null;
        try {
            if (!regBean.isValid()) {
                throw new Exception("Passwords must match");
            }
            Group group = getAccountsEJB().getGroup(1);
            User user = new User();
            user.setFirstName(regBean.getFirstName());
            user.setLastName(regBean.getLastName());
            user.setEmail(regBean.getEmail());
            user.setPassword(regBean.getPassword());
            user.setTitle(regBean.getTitle());
            user.setGroup(group);
            getAccountsEJB().createUser(user);
            regBean = new RegisterationBean();
            msg = new FacesMessage("User successfully created");
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
        } catch (DuplicateEmailException ex) {
            msg = new FacesMessage(ex.getMessage());
        } catch (Exception ex) {
            msg = new FacesMessage(ex.getMessage());
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
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

    public void login() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        FacesMessage message = new FacesMessage();
        try {
            User user = getAccountsEJB().getUser(loginBean.getEmail(), loginBean.getPassword());
            if (user != null && request.getUserPrincipal() == null) {
                request.login(user.getEmail(), user.getPassword());
                loginBean.setUser(user);
                loginBean.setIsLogin(true);
                externalContext.getSessionMap().put("user", loginBean);
                externalContext.redirect(originalUrl);
            } else if(loginBean.isIsLogin()) {
                loginBean.setIsLogin(true);
                externalContext.getSessionMap().put("user", loginBean);
                externalContext.redirect(originalUrl);
            }

        } catch(ServletException ex){
            message.setSummary(ex.getMessage());
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage(null, message);
        }catch (Exception ex) {
            message.setSummary(ex.getMessage());
            Logger.getLogger(AccountsController.class.getName()).log(Level.SEVERE, ex.getMessage());
        }
        
    }
    
    public void logout() throws IOException{
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.invalidateSession();
        loginBean.setIsLogin(false);
        externalContext.redirect(externalContext.getRequestContextPath()+"/login.xhtml");
    }

    /**
     * @return the otherUsers
     */
    public SelectItem[] getOtherUsers() {
        List<User> others = getAccountsEJB().getUsers();
        others.remove(loginBean.getUser());  
        return JsfUtil.getSelectItems(others, true);
    }

    /**
     * @param otherUsers the otherUsers to set
     */
    public void setOtherUsers(List<User> otherUsers) {
        this.otherUsers = otherUsers;
    }

    /**
     * @return the accountsEJB
     */
    public AccountEJBLocal getAccountsEJB() {
        return accountsEJB;
    }
    
    
}
