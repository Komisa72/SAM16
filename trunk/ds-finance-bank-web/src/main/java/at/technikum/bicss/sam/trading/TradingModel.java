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
import net.froihofer.dsfinance.ws.trading.PublicStockQuote;

/**
 *
 * @author amaierhofer
 */
@Named("tradingModel")
@SessionScoped

public class TradingModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int MAX_LENGTH_NAME = 200;

    @EJB(name = "BankEJB")
    private BankInterface bank;
    private String company;

    private List<Customer> customerList;
    private transient DataModel<Customer> customerModel;
    private Customer selectedCustomer = new Customer();
    private List<PublicStockQuote> companyShares;
   
    /**
     *
     * @return
     */
    public List<Customer> getCustomerList() {
        return customerList;
    }
    
     public List<PublicStockQuote> getCompanyShares() {
        companyShares = bank.companyShares(company);
        return companyShares;
    }
     
    public void setCompany(String name) {
        company = name;
        System.out.println(company);
    }
    
    public String getCompany() {
        return company;
    }
     
    
    /**
     *
     */
    public void updateModel()
    {
        customerList = bank.listCustomer();
        customerModel.setWrappedData(customerList);
    }

    /**
     * @return the maxLengthName
     */
    public int getMaxLengthName() {
        return MAX_LENGTH_NAME;
    }

    /**
     *
     * @param context
     * @param component
     * @param value
     */
    public void checkUserName(FacesContext context, UIComponent component, Object value) {

        if (value == null) {
            throw new ValidatorException(new FacesMessage("Customer name is empty!"));
        }

        String text = value.toString();
        if ((text.length() < 1) || (text.length() > getMaxLengthName())) {
            throw new ValidatorException(new FacesMessage("Customer name is invalid!"));
        }
    }
    
    /**
     * Get currect volume of bank.
     * @return
     */
    public double getVolume()
    {
      return bank.volume();
    }
    
    /**
    * Get list of shares for company 
    * @return
    */
    

    /**
     * @return the role
     */
    public Role getRole() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getExternalContext().isUserInRole("bank")) {
            role = Role.BANK;
        } 
        else if (context.getExternalContext().isUserInRole("customer")) {
            role = Role.CUSTOMER;
        } else {
            role = Role.NONE;
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
         * Not logged in.
         */
        NONE,
        
        /**
         * Logged in as customer.
         */
        CUSTOMER,
        /**
         * Logged in as bank.
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
     * @return
     */
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

    /**
     *
     * @return
     */
    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }
    

    /**
     *
     * @param selectedCustomer
     */
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
        if (FacesContext.getCurrentInstance().getExternalContext() != null)
        {
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        }
        setRole(Role.NONE);
        /* to be on the safe side */
        return "logout";
    }

}
