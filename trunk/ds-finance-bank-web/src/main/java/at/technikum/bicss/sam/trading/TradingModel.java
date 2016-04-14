/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private String lastCompany = null;
    private Role role = Role.BANK;

    private List<Customer> customerList;
    private DataModel<Customer> customerModel;
    private Customer customer = new Customer();
    private List<Share> companyShares = new ArrayList<>();
    private List<Share> depotShares = new ArrayList<>();
    private Depot depot;
    private String searchId;

    Date lastTime;

    /**
     * @return the selectedShare
     */
    public Share getSelectedShare() {
        return selectedShare;
    }

    /**
     * @param selectedShare the selectedShare to set
     */
    public void setSelectedShare(Share selectedShare) {
        this.selectedShare = selectedShare;
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
     * getCustomerList for view.
     *
     * @return
     */
    public List<Customer> getCustomerList() {
        return customerList;
    }

    /**
     * Number of rows to be shown in any list.
     *
     * @return number of rows to be shown in list.
     */
    public int getRowCount() {
        return 10;
    }

    private Share selectedShare;

    /**
     * Determine if we need a paginator in table.
     *
     * @return number of rows to be shown in list.
     */
    public boolean getCompanyPaginator() {
        return companyShares.size() > getRowCount();
    }

    public List<Share> findShares() throws StockExchangeUnreachableException {
        System.out.println("in web findShares.");
        boolean search = false;
        if (company != null && !company.trim().isEmpty()) {
            // workaround because findShares is called multiple times by primefaces
            // within a second
            if (lastCompany == null) {
                search = true;
            } else if (!lastCompany.equals(company)) {
                search = true;
            } else if ((new java.util.Date()).getTime() - lastTime.getTime() > 1000) {
                search = true;
            }
            if (search) {
                try {
                    companyShares = bank.findShares(company);
                    lastTime = new Date();
                    lastCompany = company;
                } catch (StockExchangeUnreachableException ex) {
                    // TODO what to do +-when stock exchange is not available?
                    throw ex;
                }
            }
        } else {
            companyShares.clear();
        }
        return companyShares;
    }

    public Depot getDepot() {

        return depot;
    }

    private void setDepot() {
        try {
            Customer n_customer = new Customer();
            n_customer.setName("test");
            bank.createCustomer(n_customer, "test");

            Long d_id = bank.createDepot(n_customer);
            depot = bank.getDepot(d_id);
        } catch (Exception e) {
            System.out.println("Could not create Depot");
        }
    }

    public Depot getDepotById() {
        //only for testing
        try {
            depot = bank.getDepot(Long.parseLong(searchId));
            return depot;
        } catch (NumberFormatException e) {
        }
        return null;
    }

    public List<Share> getSharesByDepotId() {

        return depotShares;

    }

    public List<Share> getDepotShares() {
        
        
        try {
            depotShares = bank.getDepotShares(depot.getId());
            return depotShares;
        } catch (NumberFormatException e) {
        }
        return null;
       
    }
    
    public Depot getDepotByCustomer()
    {
          try {
            depot = bank.getCustomerDepot(customer.getId());
            return depot;
        } catch (NumberFormatException e) {
        }
        return null;
    }

    private void setDepotShares() {
        depot = bank.getDepot(Long.parseLong(searchId));
        //depot.setShareList(depot.getId());
        try {
            System.out.println(bank.getDepotShares(depot.getId()));
            depotShares = bank.getDepotShares(depot.getId());

        } catch (NumberFormatException e) {
        }
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String id) {
        searchId = id;
    }

    public void setCompany(String name) {
        company = name;
    }

    public String getCompany() {
        return company;
    }

    /**
     * updateCustomerModel updates the customer view via model.
     */
    public void updateCustomerModel() {
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
    public BigDecimal getVolume() {
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
        return customer;
    }

    /**
     * setSelectedCustomer for role bank.
     *
     * @param selectedCustomer
     */
    public void setSelectedCustomer(Customer selectedCustomer) {
        this.customer = selectedCustomer;
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

            setSelectedCustomer(bank.getCustomer(context.getExternalContext().getRemoteUser()));
            try {
                context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/showcustomer.xhtml");
            } catch (IOException ex) {
                // TODO AM what to do when redirect fails?
            }
        }

        customerList = bank.listCustomer();
      //  setDepot();

    }

}
