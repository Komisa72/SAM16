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

/**
 *
 */
@Named("tradingModel")
@SessionScoped
public class TradingModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB(name = "BankEJB")
    private BankInterface bank;

    /**
     * @return the role
     */
    public Role getRole() {
        return role;
    }

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
        String show = bank.sayHello();
        return show;
    }

    /**
     *
     */
    public enum Role {

        /**
         *
         */
        CUSTOMER,

        /**
         *
         */
        BANK;

        /**
         *
         * @return
         */
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

    /**
     *
     */
    @PostConstruct
    public void init() {
        // TODO remove unused code
        // aufgrund der session feststellen, in welcher rolle wir uns gerade 
        // befinden
        System.out.println("init start");
        role = Role.BANK;
        System.out.println("init role setzen vorbei");
    }

    /**
     *
     * @return navigataion output case create.
     */
    public String create() {
        return "create";
    }

    //private User current;

    /**
     *
     * @return navigation output.
     */
    public String logout() {

        // TODO site crashes when automatic session timeout has already triggered
        // subject to change
        //https://murygin.wordpress.com/2012/11/29/jsf-primefaces-session-timeout-handling/
        // for ajax http://stackoverflow.com/questions/11203195/session-timeout-and-viewexpiredexception-handling-on-jsf-primefaces-ajax-request
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        setRole(Role.CUSTOMER);
        /* to be on the safe side */
        return "logout";
    }

}
