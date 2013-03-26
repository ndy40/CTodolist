/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.sussex.bean.backing;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.faces.validator.FacesValidator;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author ne51
 */
@Named (value = "regBean")
@RequestScoped
@FacesValidator(value = "uk.sussex.bean.RegPassword")
public class RegisterationBean implements Serializable{
    @NotNull
    @Size(min = 3, max = 20,message = "First name must be at least 3 letters")
    private String firstName;
    @NotNull
    @Size(min = 3, max = 20,message = "Last name must be at least 3 letters")
    private String lastName;
    @NotNull
    private String email;
    @NotNull
    @Size(min = 6, max = 20,message = "Password must be at least 6 letters")
    private String password;
    @NotNull
    @Size(min = 3, max = 20,message = "Confirm Password must be at least 3 letters")
    private String cpassword;
    
    private String title;

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the cpassword
     */
    public String getCpassword() {
        return cpassword;
    }

    /**
     * @param cpassword the cpassword to set
     */
    public void setCpassword(String cpassword) {
        this.cpassword = cpassword;
    }

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
    
    public boolean isValid(){
        return password.equals(cpassword);
    }
    
    
}
