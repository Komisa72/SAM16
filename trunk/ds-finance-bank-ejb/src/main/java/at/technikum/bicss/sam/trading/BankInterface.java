/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

import java.math.BigDecimal;
import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

/**
 * BankInterface Local Interface consumed by web applicaton.
 */
@Local
@DeclareRoles({"bank", "customer"})
@RolesAllowed({"bank", "customer"})
public interface BankInterface {

    /**
     * findShares finds shares by company name.
     *
     * @param company search pattern, part of company name, empty strings are
     * not supported.
     * @return list of shares for the given company search pattern.
     * @throws StockExchangeUnreachableException
     */
    public List<Share> findShares(String company)
            throws StockExchangeUnreachableException;

    /**
     * Buy a share from public stock quote.
     *
     * @param customer for which customer
     * @param what shares to be bought
     * @param count of shares to be bought
     * @throws StockExchangeUnreachableException
     * @throws BuySharesVolumeException
     * @throws at.technikum.bicss.sam.trading.BuySharesNotEnoughException
     */
    public void buy(Customer customer, Share what, int count)
            throws StockExchangeUnreachableException, BuySharesVolumeException, 
            BuySharesNotEnoughException;

    /**
     * Sell a share to public stock quote.
     *
     * @param customer for which customer.
     * @param what shares to be sold.
     * @param count of shares to be sold.
     * @throws StockExchangeUnreachableException
     * @throws SellSharesAmountException
     */
    public void sell(Customer customer, Share what, int count) throws StockExchangeUnreachableException,
            SellSharesAmountException;

    /**
     * createDepot for the given customer.
     * @param customer
     * @return
     * @throws DepotCreationFailedException 
     */
    public Depot createDepot(Customer customer)
            throws DepotCreationFailedException;

    /**
     * createCustomer creates a new customer and stores its credentials in
     * webserver realm, customer data is stored via JPA.
     *
     * @param customer new customer to be created.
     * @param password credentials.
     * @throws CustomerCreationFailedException
     */
    @RolesAllowed("bank")
    public void createCustomer(Customer customer, String password)
            throws CustomerCreationFailedException;

    /**
     * listCustomer retrieve the list of customers.
     *
     * @return the list of customers.
     */
    @RolesAllowed("bank")
    public List<Customer> listCustomer();

    /**
     * getCustomer retrieves the customer with the given name.
     *
     * @param name of the customer.
     * @return the found customer.
     */
    public Customer getCustomer(String name);
    
    /**
     * 
     * @param id
     * @return depot
     */
    public Depot getDepot(Long id);
    
    /**
     * 
     * @param customerId
     * @return depot for customer
     */
    public Depot getCustomerDepot(Long customerId);
    
    /**
     * 
     * @param id
     * @return shares of depot
     */
    public List<Share> getDepotShares(Long id);

    /**
     * Get the current volume of the bank.
     * @return volume that can be invested currently.
     */
    public BigDecimal getVolume();
    
    public List<Customer> getCustomerName(String name);
    

}
