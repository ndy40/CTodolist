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
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
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
@ViewScoped
public class AccountsController implements Serializable {

    @Inject
    private RegisterationBean regBean;
    @EJB
    AccountEJBLocal accountsEJB;
    @Inject
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
            originalUrl = extContext.getRequestContextPath() + "/LoginHelper";
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
            Group group = accountsEJB.getGroup(1);
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
            User user = accountsEJB.getUser(loginBean.getEmail(), loginBean.getPassword());
            if (user != null) {
                request.login(user.getEmail(), user.getPassword());
                loginBean.setUser(user);
                loginBean.setIsLogin(true);
                externalContext.getSessionMap().put("user", loginBean);
                externalContext.redirect(originalUrl);
            } else {
                throw new InvalidLoginException("Login failed. Check details and try again");
            }

        } catch (InvalidLoginException ex) {
            message.setSummary(ex.getMessage());
            Logger.getLogger(AccountsController.class.getName()).log(Level.INFO, ex.getMessage());
        } catch(ServletException ex){
            message.setSummary("Unknown login");
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
        externalContext.redirect(externalContext.getRequestContextPath()+"/index.xhtml");
    }

    /**
     * @return the otherUsers
     */
    public List<User> getOtherUsers() {
        List<User> others = accountsEJB.getUsers();
        //others.remove(loginBean.getUser());        
        otherUsers = others;
        return otherUsers;
    }

    /**
     * @param otherUsers the otherUsers to set
     */
    public void setOtherUsers(List<User> otherUsers) {
        this.otherUsers = otherUsers;
    }
    
    
}
