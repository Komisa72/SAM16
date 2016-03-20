/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;


/**
 *
 */
@Named("customerController")
@RequestScoped



public class CustomerController {

    @EJB(name = "BankEJB")
    private BankInterface bank;
    
    
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
        } catch (CustomerCreationFailedException ex) {
            // TODO subject to change
            // for now let primefaces do the job to display the exception
            throw ex;
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
