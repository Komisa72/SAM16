/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.froihofer.util.jboss.WildflyAuthDBHelper;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceRef;
import net.froihofer.dsfinance.ws.trading.PublicStockQuote;
import net.froihofer.dsfinance.ws.trading.TradingWSException_Exception;
import net.froihofer.dsfinance.ws.trading.TradingWebService;
import net.froihofer.dsfinance.ws.trading.TradingWebServiceService;

/**
 * Bank class responsible for persting customer and its shares in depots. Also
 * check the maximum amount shares to hold at this bank.
 */
@Stateless(name = "BankEJB")
@Local(BankInterface.class)
public class Bank implements BankInterface {

    private static WildflyAuthDBHelper authHelper;

    @PersistenceContext
    private EntityManager em;

    TradingWebService proxy;

    private double volume;
    private List<PublicStockQuote> companyShares;
    // ds-finance-bank.h2.db
    // 1 Mrd. dollar
    private final static double MAX_VOLUME = 1000000000;
    
        @WebServiceRef(name = "TradingWebServiceService")
    private TradingWebServiceService stockService;

    /**
     * Constructor.
     */
    public Bank() {

    }

    // Retrieves all the Customer
    /**
     * listCustomer
     *
     * @return the list of customers.
     */
    @Override
    public List<Customer> listCustomer() {
        TypedQuery<Customer> query = em.createQuery(
                "SELECT c FROM Customer c ORDER BY c.id", Customer.class);
        return query.getResultList();
    }

    /**
     *
     * @param customer
     * @param password
     * @throws CustomerCreationFailedException
     */
    @Override
    public void createCustomer(Customer customer, String password) throws CustomerCreationFailedException {
        String[] customerRoles = new String[1];
        customerRoles[0] = "customer";

        try {
            authHelper.addUser(customer.getName(), password, customerRoles);
            em.persist(customer);
        } catch (IOException ex) {
            System.out.println("Could not add customer to password database");
            throw new CustomerCreationFailedException("Could not add customer to password database.", ex);
        }

    }



    @PostConstruct
    private void init() {
        // setup initial default bank user with default credentials and role.
        String homeDir = System.getProperty("jboss.home.dir");
        authHelper = new WildflyAuthDBHelper(new File(homeDir));
        proxy = stockService.getTradingWebServicePort();
        BindingProvider bindingProvider = (BindingProvider) proxy;
        bindingProvider.getRequestContext().put(
                BindingProvider.USERNAME_PROPERTY, "bic4b16_06");
        bindingProvider.getRequestContext().put(
                BindingProvider.PASSWORD_PROPERTY, "Feim0Kah4");

        volume = 0;

    }

    @Override
    public double volume() {
        // TODO subject to change
        // this is only a test if webservice can be accessed
        try {
            List<PublicStockQuote> list = proxy.findStockQuotesByCompanyName("Apple");
            volume = list.size();
        } catch (TradingWSException_Exception ex) {
            volume = 0;
        }
        // TODO calculate the current volume from shares as of now
        return volume;
    }

    @Override
    public List<PublicStockQuote>  companyShares() {
        // TODO 
        companyShares = null;
        try {
            companyShares = proxy.findStockQuotesByCompanyName("Apple");
            System.out.println(companyShares.toString());
            
            
        } catch (TradingWSException_Exception e) {
            System.out.println("no shares found");
        }
        
        return companyShares;
    }
    
    /**
     * Store a new Customer.
     *
     * @param customer
     */
    private void persist(Customer customer) {
        em.persist(customer);
    }

}
