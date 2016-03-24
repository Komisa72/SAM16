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
import javax.annotation.PostConstruct;
import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceRef;
import net.froihofer.dsfinance.ws.trading.Buy;
import net.froihofer.dsfinance.ws.trading.GetStockQuotes;
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
//@DeclareRoles({"bank", "customer"})
public class Bank implements BankInterface {

    @PersistenceContext
    private EntityManager em;
    @EJB
    StartupTradingService info;

    // 1 Mrd. dollar
    private final static double MAX_VOLUME = 1000000000;

    @WebServiceRef(name = "TradingWebServiceService")
    private TradingWebServiceService stockService;
    private TradingWebService proxy;

    private List<PublicStockQuote> companyShares;
    private List<PublicStockQuote> stockQuotes;
    private List<PublicStockQuote> stockQuoteHistory;
    private double buyShares;

    /**
     * Constructor.
     */
    public Bank() {

    }

    /**
     * Retrieves all the Customer
     *
     * @return the list of customers.
     */
    @Override
    public List<Customer> listCustomer() {
        // ds-finance-bank.h2.db
        TypedQuery<Customer> query = em.createQuery(
                "SELECT c FROM Customer c ORDER BY c.id", Customer.class);
        return query.getResultList();
    }

    /**
     * createCustomer creates a new customer and stores its credentials in
     * webserver realm, customer data is stored via JPA.
     *
     * @param customer new customer to be created.
     * @param password credentials.
     * @throws CustomerCreationFailedException
     */
    @Override
    public void createCustomer(Customer customer, String password) throws CustomerCreationFailedException {
        String[] customerRoles = new String[1];
        customerRoles[0] = "customer";

        try {
            // TODO check if addUser is threadsafe
            info.getAuthHelper().addUser(customer.getName(), password, customerRoles);
            em.persist(customer);
        } catch (IOException ex) {
            System.out.println("Could not add customer to password database");
            throw new CustomerCreationFailedException("Could not add customer to password database.", ex);
        }

    }
    
    /**
     * 
     * gets a stock quote by symbol
     * 
     * @param symbols symbol for which stock quotes are to be returned
     * @return list of stock quotes
     * @throws StockExchangeUnreachableException 
     */
    @Override
    public List<PublicStockQuote> getStockQuotes(List<String> symbols) 
            throws StockExchangeUnreachableException {
        
        stockQuotes = null;
        
        try {
            stockQuotes = proxy.getStockQuotes(symbols);
            System.out.println(stockQuotes.toString());

        } catch (TradingWSException_Exception e) {
            System.out.println("passiert");
            throw new StockExchangeUnreachableException("Stock exchange unreachable.", e);
        }
        
        return stockQuotes;
    }
    
    @Override
    public List<PublicStockQuote> getStockQuoteHistory(String symbol)
            throws StockExchangeUnreachableException {
        
        stockQuoteHistory = null;
        
        try {
            stockQuoteHistory = proxy.getStockQuoteHistory(symbol);
            System.out.println(stockQuoteHistory.toString());

        } catch (TradingWSException_Exception e) {
            System.out.println("passiert");
            throw new StockExchangeUnreachableException("Stock exchange unreachable.", e);
        }
        
        return stockQuoteHistory;
    }
    
    @Override
    public double buy(String symbol, int shares)
            throws StockExchangeUnreachableException, BuySharesException {
        
            
        
        
        try {
            buyShares = proxy.buy(symbol, shares);
            System.out.println("Bought shares" + buyShares);

        } catch (Exception  e) {
            if (e instanceof TradingWSException_Exception) {
                System.out.println("passiert");
            throw new StockExchangeUnreachableException("Stock exchange unreachable.", e);
            }
            else if (e instanceof BuySharesException) {
                throw new BuySharesException("Could not buy shares");
            }
        }

        
        return buyShares;
    }

    /**
     * Init this EJB.
     */
    @PostConstruct
    private void init() {

        proxy = stockService.getTradingWebServicePort();
        BindingProvider bindingProvider = (BindingProvider) proxy;
        bindingProvider.getRequestContext().put(
                BindingProvider.USERNAME_PROPERTY, "bic4b16_06");
        bindingProvider.getRequestContext().put(
                BindingProvider.PASSWORD_PROPERTY, "Feim0Kah4");

    }

    /**
     * volume used by bank role.
     *
     * @return the volume which is available as of now.
     */
    @Override
    public double volume() {
        double volume;
        try {
            // TODO subject to change
            // this is only a test if webservice can be accessed
            List<PublicStockQuote> list = proxy.findStockQuotesByCompanyName("OMV");
            volume = list.size();
        } catch (TradingWSException_Exception ex) {
            volume = 0;
        }
        // TODO calculate the current volume from shares as of now
        return volume;
    }

    @Override
    public List<PublicStockQuote> companyShares(String company)
            throws StockExchangeUnreachableException {
        // TODO
        companyShares = null;
        System.out.println("company name");
        System.out.println(company);
        System.out.println("Holla");
        try {
            companyShares = proxy.findStockQuotesByCompanyName(company);
            System.out.println(companyShares.toString());

        } catch (TradingWSException_Exception e) {
            System.out.println("passiert");
            throw new StockExchangeUnreachableException("Stock exchange unreachable.", e);
        }

        return companyShares;
    }

    /**
     * Store a new Customer.
     *
     * @param customer to be stored as JPA entity.
     */
    private void persist(Customer customer) {
        em.persist(customer);
    }

}
