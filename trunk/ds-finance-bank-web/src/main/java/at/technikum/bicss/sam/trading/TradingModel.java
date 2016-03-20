/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;

/**
 *
 */
@Named("tradingModel")
@SessionScoped
public class TradingModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int MAX_LENGTH_NAME = 200;

    @EJB(name = "BankEJB")
    private BankInterface bank;

    private List<Customer> customerList;
    private transient DataModel<Customer> customerModel;
    private Customer selectedCustomer = new Customer();

    public List<Customer> getCustomerList() {
        return customerList;
    }

    /**
     * @return the maxLengthName
     */
    public int getMaxLengthName() {
        return MAX_LENGTH_NAME;
    }

    public void checkUserName(FacesContext context, UIComponent component, Object value) {

        if (value == null) {
            throw new ValidatorException(new FacesMessage("Customer name is empty!"));
        }

        String text = value.toString();
        if (text.length() < 1) {
            throw new ValidatorException(new FacesMessage("Customer name is invalid!"));
        }
    }
    
    
    public double getVolume()
    {
        // TODO read volume from bank interface
      return 500.33f;
    }

    /**
     * @return the role
     */
    public Role getRole() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getExternalContext().isUserInRole("bank")) {
            role = Role.BANK;
        } else {
            role = Role.CUSTOMER;
        }

        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(Role role) {
        this.role = role;
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

    public DataModel<Customer> getModel() {
        if (customerModel == null) {
            customerModel = new ListDataModel<>(customerList);
        }
        return customerModel;
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

        customerList = bank.listCustomer();

    }

    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    public void setSelectedCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
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
