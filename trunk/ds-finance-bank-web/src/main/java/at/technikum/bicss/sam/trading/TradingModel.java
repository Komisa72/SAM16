/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;

/**
 *
 * @author amaierhofer
 */
@Named("tradingModel")
@SessionScoped

public class TradingModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB(name = "BankEJB")
    private BankInterface bank;
    private String company;
    private Role role = Role.BANK;

    private List<Customer> customerList;
    private DataModel<Customer> customerModel;
    private Customer selectedCustomer = new Customer();
    private List<Share> companyShares = new ArrayList<>();

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

    /**
     * getCustomerList for view.
     *
     * @return
     */
    public List<Customer> getCustomerList() {
        return customerList;
    }

    public List<Share> findShares() throws StockExchangeUnreachableException {
        if (company != null && !company.trim().isEmpty()) {
            try {
                companyShares = bank.findShares(company);
            } catch (StockExchangeUnreachableException ex) {
                // TODO what to do when stock exchange is not available?
                throw ex;
            }
        } else {
            companyShares.clear();
        }
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
     * updateModel updates the customer view via model.
     */
    public void updateModel() {
        customerList = bank.listCustomer();
        customerModel.setWrappedData(customerList);
    }

    /**
     * checkUserName if it is not empty and not too long.
     *
     * @param context faces context.
     * @param component from ui which request the check.
     * @param value the string input to be ckecked.
     */
    public void checkUserName(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            throw new ValidatorException(new FacesMessage("Customer name is empty!"));
        }

        String text = value.toString();
        if ((text.length() < 1) || (text.length() > Customer.getMaxLengthName())) {
            throw new ValidatorException(new FacesMessage("Customer name is invalid!"));
        }
    }

    /**
     * Get current volume of bank.
     *
     * @return volume in USD.
     */
    public double getVolume() {
        return bank.volume();
    }

    /**
     * Give me the current role of the logged in user.
     *
     * @return the role
     */
    public Role getRole() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getExternalContext().isUserInRole("bank")) {
            role = Role.BANK;
        } else if (context.getExternalContext().isUserInRole("customer")) {
            role = Role.CUSTOMER;
        } else {
            role = Role.NONE;
        }

        return role;
    }

    /**
     * getSelectedCustomer for role bank.
     *
     * @return
     */
    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    /**
     * setSelectedCustomer for role bank.
     *
     * @param selectedCustomer
     */
    public void setSelectedCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
    }

    /**
     * Creates a new instance of TradingModel
     */
    public TradingModel() {
    }

    /**
     * getModel as data model for all the xhtml views.
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
     * Init this bean.
     */
    @PostConstruct
    public void init() {
        // TODO remove unused code
        // aufgrund der session feststellen, in welcher rolle wir uns gerade
        // befinden
        Role who = getRole();
        if (who == Role.CUSTOMER) {
            FacesContext context = FacesContext.getCurrentInstance();

            //context.redirect(context.getRequestContextPath() + "/showcustomer.xhtml");
        }

        customerList = bank.listCustomer();

    }

}
