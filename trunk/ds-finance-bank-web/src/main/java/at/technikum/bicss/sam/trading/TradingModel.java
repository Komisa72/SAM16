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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
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
    private Role role = Role.BANK;

    private List<Customer> customerList;
    private DataModel<Customer> customerModel;
    private Customer selectedCustomer = new Customer();
    private List<PublicStockQuote> companyShares;

    /**
     *
     * @return
     */
    public List<Customer> getCustomerList() {
        return customerList;
    }

    /**
     *
     * @return
     */
    public List<PublicStockQuote> getCompanyShares() {
        return companyShares;
    }

    /**
     * updateModel updates the view via model.
     */
    public void updateModel() {
        customerList = bank.listCustomer();
        customerModel.setWrappedData(customerList);
    }

    /**
     * @return the maxLengthName
     */
    public int getMaxLengthName() {
        // TODO ask bank for maximum length of customer name.
        return MAX_LENGTH_NAME;
    }

    /**
     * checkUserName if it is not empty and not too long.
     * @param context faces context.
     * @param component from ui which request the check.
     * @param value the string input to be ckecked.
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
     * Get current volume of bank.
     *
     * @return volume in USD.
     */
    public double getVolume() {
        return bank.volume();
    }

    /**
     * Give me the current role of the logged in user.
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

    /**
     * Creates a new instance of TradingModel
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

        try {
            companyShares = bank.companyShares();
        } catch (StockExchangeUnreachableException ex) {
            // TODO what do we show when webservice fails?
        }
        customerList = bank.listCustomer();

    }

}
