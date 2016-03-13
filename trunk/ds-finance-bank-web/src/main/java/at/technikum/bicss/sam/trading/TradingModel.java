/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named("tradingModel")
@SessionScoped
public class TradingModel implements Serializable {

    
    private static final long serialVersionUID = 1L;
    
    @EJB(name = "BankEJB", mappedName = "EJB-Model-BankEJB")
    private BankInterface bank;
    

    /**
     * @return the role
     */
    public Role getRole() {
        return role;
    }
    
    private String helpi = "5";

    private String t = null;
    
    /**
     * @param role the role to set
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * @return the help
     */
    public String getHelpi() {
        return helpi;
    }

    /**
     * @param help the help to set
     */
    public void setHelpi(String help) {
        this.helpi = help;
    }

    public enum Role {
        CUSTOMER,
        BANK;

        public boolean isBank() {
            return Role.BANK.equals(this);

        }
    }

    private Role role = Role.BANK;

    /**
     * Creates a new instance of TradingController
     */
    public TradingModel() {
    }
    
    
    @PostConstruct
    public void init()
    {
        System.out.println("init star");
        role = Role.BANK;
        System.out.println("init role setzen vorbei");
        // Aufruf der Business-Logik (via Local interface)
        bank.sayHello();

        System.out.println("say hello vorbei");

        
    }
    
    
    public String create()
    {
        return "erzeuge";
    }

    //private User current;
    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        setRole(Role.CUSTOMER);
        /* to be on the safe side */
        return "faces/index.xhtml?faces-redirect=true";
    }

}
