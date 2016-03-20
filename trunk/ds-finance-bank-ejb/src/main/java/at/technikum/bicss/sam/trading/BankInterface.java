/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

/**
 * BankInterface Local Interface consumed by web applicaton.
 */
@Local
@DeclareRoles({"bank", "customer"})
@RolesAllowed({"customer", "bank"})
public interface BankInterface {

    /**
     * createCustomer creates a new customer and stores its credentials in
     * webserver realm, customer data is stored via JPA.
     *
     * @param customer new customer to be created.
     * @param password credentials.
     * @throws CustomerCreationFailedException
     */
    
    @RolesAllowed("bank")
    public void createCustomer(Customer customer, String password) throws CustomerCreationFailedException;

    /**
     * createCustomer creates a new customer and stores its credentials in
     * @return the list of customers.
    */
    public List<Customer> listCustomer();

    /**
     * volume used by bank role.
     * @return the volume which is available as of now.
    */
    
    @RolesAllowed("bank")
    public double volume();

}
