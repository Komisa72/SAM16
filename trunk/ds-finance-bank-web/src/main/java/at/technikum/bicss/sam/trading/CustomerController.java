/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;


/**
 *
 */
@Named("customerController")
@RequestScoped
public class CustomerController {

    @EJB(name = "BankEJB")
    private BankInterface bank;
    
    
    @Inject
    @Named("tradingModel") 
    private TradingModel model;
    
    
    private String password;
    
    /**
     * Creates a new instance of CustomerController
     */
    public CustomerController() {
    }
    /**
     * Create a new instance of a customer.
     * 
     * @param name of the customer to be created
     * @param password of this customer
     * @return navigation case output.
     * @throws CustomerCreationFailedException 
     */
    public String createCustomer(String name, String password) throws CustomerCreationFailedException
    {
        Customer customer = new Customer();
        customer.setName(name);
        
      //  customer.setDepot(null);
        
        
        try {
            bank.createCustomer(customer, password);
           // bank.createDepot(customer);
            model.updateCustomerModel();
            
        } catch (CustomerCreationFailedException  ex) {
            
            if(ex instanceof CustomerCreationFailedException) {
                System.out.println("Customer could not be created.");
            }
         
            
        }
        return "showCustomers";
    }
    
    /**
     * Navigate to selected customer view.
     * @return navigation output.
     */
    public String showCustomer()
    {
        return "showCustomer";
        
    }
    
     /**
     * Navigate to customers list view.
     * @return navigataion output.
     */
    public String showCustomers() {
        return "showCustomers";
    }

    /**
     * Getter of password.
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter of password.
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
}
