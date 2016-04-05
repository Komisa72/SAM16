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
    
    public String createCustomer(String name, String password) throws CustomerCreationFailedException
    {
        Customer customer = new Customer();
        customer.setName(name);
        
        
        try {
            bank.createCustomer(customer, password);
            bank.createDepot(customer);
            model.updateModel();
        } catch (CustomerCreationFailedException | DepotCreationFailedException ex) {
            
            if(ex instanceof CustomerCreationFailedException) {
                System.out.println("Customer could not be created.");
            }
            else if (ex instanceof DepotCreationFailedException) {
                System.out.println("Depot could not be created.");
            }
        }
        return "showCustomers";
    }
    
    public String showCustomer()
    {
        return "showCustomer";
        
    }
    
    
    
        /**
     *
     * @return navigataion output case create.
     */
    public String showCustomers() {
        return "showCustomers";
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
    


    
}
