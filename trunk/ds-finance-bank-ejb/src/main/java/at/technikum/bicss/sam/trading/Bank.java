/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

import javax.ejb.Local;
import javax.ejb.Stateless;

import net.froihofer.util.jboss.WildflyAuthDBHelper;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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

    @PersistenceContext
    private EntityManager em;
    @EJB
    StartupTradingService info;

    // 1 Mrd. dollar, limit to be checked against when a share is bought
    private final static double MAX_VOLUME = 1E9;

    @WebServiceRef(name = "TradingWebServiceService")
    private TradingWebServiceService stockService;
    private TradingWebService proxy;
    
    //get current total value of depots
    //public double currentValue = getDepotValue();
    public double currentValue = 2449010.20149;
    
    //TODO: sinnvolle Instanz anlegen für die Persistierung
    public Depot myDepot = new Depot();
    
    //TODO: sinnvolle Instanz anlegen für die Persistierung - hier rein für Testzwecke
    public Share myShare = new Share("471147114711", "Dummy 4711 Koelnisch Wasser",
                4711, new BigDecimal(47.11d, new MathContext(2)));

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
        Query query = em.createNamedQuery("allCustomers");
        return query.getResultList();
    }
    /**
     * 
     * @return total value of bought shares 
     */    
 
    @Override
    public double getDepotValue() {
        List<PublicStockQuote> allShares;
        double d = 0;
        double value;
        double valueSum = 0;
        
        double temp = Double.longBitsToDouble(15552451L);
        
        BigDecimal price = new BigDecimal(d);
        
        Query query = em.createNamedQuery("getDepot");
        allShares = query.getResultList();
        
        for (PublicStockQuote a : allShares) {
            temp = a.getFloatShares();
            price = a.getLastTradePrice();
            value = temp * price.doubleValue();
            valueSum = valueSum + value;
        }
        
        return valueSum;
    }
    
    /**
     * 
     * @return current volume that can be invested
     */
    @Override
    public double volume() {
        return MAX_VOLUME - currentValue;
    }

    /**
     * getCustomer retrieves the customer with the given name.
     *
     * @param name of the customer.
     * @return the found customer.
     */
    @Override
    public Customer getCustomer(String name) {
        Query query = em.createNamedQuery("singleCustomer");
        query.setParameter("customerName", name);
        return (Customer) query.getSingleResult();
    }
    
    /**
     * getDepot retrieves the customer with the given name.
     *
     * @param id of the depot.
     * @return the found depot.
     */
    @Override
    public Depot getDepot(String id) {
        Query query = em.createNamedQuery("getDepotById");
        query.setParameter("depotId", id);
        return (Depot) query.getSingleResult();
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

    //TODO CB: Dummy Werte ersetzen gegen sinnvolle Persistierung
    @Override
    public String createDepot(Customer customer) throws DepotCreationFailedException {
        
       // Long Id = (long) (customer.getId() + 1000);
        
        Depot depot = new Depot();
       // depot.setCustomerID(customer.getId());
       /* depot.setId(Id);
        depot.setCustomerID(customer.getId());
        depot.setValue(42000320.205);
        depot.setRating((long)2);
        
*/
        try {
            em.persist(depot);
        } catch (Exception ex) {
            System.out.println("Could not add depot");
            throw new DepotCreationFailedException("Could not add depot to database.", ex);
        }

        return depot.toString();
    }

    /**
     *
     * gets a list of stock quotes by symbol
     *
     * @param symbols symbol for which stock quotes are to be returned
     * @return list of stock quotes
     * @throws StockExchangeUnreachableException
     */
    @Override
    public List<PublicStockQuote> getStockQuotes(List<String> symbols)
            throws StockExchangeUnreachableException {

        List<PublicStockQuote> stockQuotes;

        try {
            stockQuotes = proxy.getStockQuotes(symbols);
            System.out.println(stockQuotes.toString());

        } catch (TradingWSException_Exception e) {
            throw new StockExchangeUnreachableException("Stock exchange unreachable.", e);
        }

        return stockQuotes;
    }

    /**
     *
     * @param symbol of shares to be listet
     * @return list of stock quote history
     * @throws StockExchangeUnreachableException
     */
    @Override
    public List<PublicStockQuote> getStockQuoteHistory(String symbol)
            throws StockExchangeUnreachableException {
        List<PublicStockQuote> stockQuoteHistory;

        try {
            stockQuoteHistory = proxy.getStockQuoteHistory(symbol);
            System.out.println(stockQuoteHistory.toString());

        } catch (TradingWSException_Exception e) {
            throw new StockExchangeUnreachableException("Stock exchange unreachable.", e);
        }

        return stockQuoteHistory;
    }

    /**
     *
     * @param symbol of shares to be bought
     * @param shares number of shares to be bought
     * @return bought shares
     * @throws StockExchangeUnreachableException
     * @throws BuySharesException
     */
    @Override
    public double buy(String symbol, int shares)
            throws StockExchangeUnreachableException, BuySharesException {

        double buyShares = 0;
        try {
            buyShares = proxy.buy(symbol, shares);
            
            if ((buyShares + currentValue) > MAX_VOLUME) {
                throw new BuySharesException("Could not buy shares, because volume would exceed allowed value.\n");
            }
            
            System.out.println("Bought shares" + buyShares);
            currentValue = currentValue + buyShares;
                
        } catch (BuySharesException | TradingWSException_Exception e) {
            if (e instanceof TradingWSException_Exception) {
                throw new StockExchangeUnreachableException("Stock exchange unreachable.", e);
            }
        }

        return buyShares;
    }

    /**
     *
     * @param symbol of shares that are sold
     * @param shares number of shares sold
     * @return sold shares
     * @throws StockExchangeUnreachableException
     */
    @Override
    public double sell(String symbol, int shares)
            throws StockExchangeUnreachableException {
        double sellShares;

        try {
            sellShares = proxy.sell(symbol, shares);
            System.out.println("Sold shares " + sellShares);
            
            //adapting current investing volume of the bank
            currentValue = currentValue - sellShares;
            
        } catch (TradingWSException_Exception e) {
            throw new StockExchangeUnreachableException("Stock exchange unreachable.", e);
        }
        return sellShares;
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
     * findShares finds shares by company name.
     *
     * @param company part of company name, empty strings are not supported.
     * @return list of found shares or empty list.
     * @throws StockExchangeUnreachableException
     */
    @Override
    public List<Share> findShares(String company)
            throws StockExchangeUnreachableException {
        List<PublicStockQuote> companyShares;
        List<Share> found;
        companyShares = null;
        try {
            companyShares = proxy.findStockQuotesByCompanyName(company);
        } catch (TradingWSException_Exception e) {
            throw new StockExchangeUnreachableException("Stock exchange unreachable.", e);
        }
        found = new ArrayList<>();
        for (PublicStockQuote temp : companyShares) {
            found.add(new Share(temp.getSymbol(), temp.getCompanyName(),
                    temp.getFloatShares(), temp.getLastTradePrice()));
        }

        return found;
    }

    /**
     * Store a new Customer.
     *
     * @param customer to be stored as JPA entity.
     */
    private void persist(Customer customer, Share share, Depot depot) {
        em.persist(customer);
        em.persist(share);
        em.persist(depot);
    }

}
