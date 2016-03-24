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
import net.froihofer.dsfinance.ws.trading.PublicStockQuote;


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
     * @param company search pattern, part of company name, empty strings are not supported.
     * @return list of shares for the given company search pattern.
     * @throws StockExchangeUnreachableException
     */
    public List<Share> findShares(String company) 
            throws StockExchangeUnreachableException;
    
    public List<PublicStockQuote> getStockQuotes(List<String> symbols)
            throws StockExchangeUnreachableException;
    
    public List<PublicStockQuote> getStockQuoteHistory(String symbol)
            throws StockExchangeUnreachableException;
    
    public double buy(String symbol, int shares)
            throws StockExchangeUnreachableException, BuySharesException;
    
    public double sell(String symbol, int shares)
            throws StockExchangeUnreachableException;
            
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
    public List<Customer> listCustomer();

    /**
     * volume used by bank role.
     *
     * @return the volume which is available as of now.
     */
    public double volume();
    
    

}
