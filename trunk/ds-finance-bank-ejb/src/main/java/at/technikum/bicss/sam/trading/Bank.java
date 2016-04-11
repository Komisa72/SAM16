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
import javax.persistence.NoResultException;

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
    private StartupTradingService info;

    // precision of US dollar is 2
    private static final int PRICE_SCALE = 2;

    // 1 Mrd. US dollar, limit to be checked against when a share is bought
    private final static BigDecimal MAX_VOLUME = new BigDecimal("1E9",
            new MathContext(PRICE_SCALE));

    @WebServiceRef(name = "TradingWebServiceService")
    private TradingWebServiceService stockService;
    private TradingWebService proxy;

    //get current total value of depots
    //public double currentValue = getDepotValue();
    private BigDecimal currentValue = new BigDecimal("2449010.20149",
            new MathContext(PRICE_SCALE));

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
    public BigDecimal getDepotValue() {
        List<PublicStockQuote> allShares;

        BigDecimal sum = new BigDecimal("0", new MathContext(PRICE_SCALE));
        Query query = em.createNamedQuery("getDepot");
        allShares = query.getResultList();
        for (PublicStockQuote a : allShares) {
            sum.add(a.getLastTradePrice().multiply(new BigDecimal(a.getFloatShares())));
        }

        return sum;
    }

    /**
     *
     * @return current volume that can be invested
     */
    @Override
    public BigDecimal volume() {
        return MAX_VOLUME.subtract(getOverallDepotValues());
    }

    /**
     * getCustomer retrieves the customer with the given name.
     *
     * @param name of the customer.
     * @return the found customer.
     */
    @Override
    public Customer getCustomer(String name) {
        try {
            Query query = em.createNamedQuery("singleCustomer");
            query.setParameter("customerName", name);
            return (Customer) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * getDepot retrieves the depot with the given name.
     *
     * @param id of the depot.
     * @return the found depot.
     */
    @Override

    public Depot getDepot(Long id) {
        Query query = em.createNamedQuery("getDepotById");
        try {
            query.setParameter("depotId", id);
            return (Depot) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    @Override
    public List<Share> getDepotShares(Long id) {
        List<Share> allShares;
        try {
            Query query = em.createNamedQuery("getDepotShares");
            query.setParameter("depotId", id);
            allShares = query.getResultList();

            return allShares;
        } catch (NoResultException e) {
            return null;
        }

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
    public Long createDepot(Customer customer) throws DepotCreationFailedException {

        Depot depot = new Depot(customer.getId(), 10409.100);

        try {
            em.persist(depot);
            System.out.println("Depot was added. Id: " + depot.getId());
        } catch (Exception ex) {
            System.out.println("Could not add depot");
            throw new DepotCreationFailedException("Could not add depot to database.", ex);
        }

        return depot.getId();
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
     * Bury a share from public stock quote.
     * @param customer for which customer
     * @param what shares to be bought
     * @param count of shares to be bought
     * @throws StockExchangeUnreachableException
     * @throws BuySharesException
     */
    @Override
    public void buy(Customer customer, Share what, int count)
            throws StockExchangeUnreachableException, BuySharesException {

        BigDecimal buyShares;

        try {
            buyShares = new BigDecimal(proxy.buy(what.getSymbol(), count), new MathContext(PRICE_SCALE));
            BigDecimal sum;

            sum = buyShares.add(getOverallDepotValues());
            if (sum.compareTo(MAX_VOLUME) == 1) {
                throw new BuySharesException("Could not buy shares, because volume would exceed allowed value.\n");
            }

            System.out.println("Bought shares" + buyShares);
            setOverallDepotValues(sum);

            // TODO weil ausgemacht war, dass das Depot erst beim 1. Einkauf 
            // angelegt wird, prüfen ob nicht null sonst anlegen
            //if (customer.getDepot() == null) {
                //customer.createDepot();
                //em.merge(customer);
            //}
            // TODO AM add the persisted share to the depot
            /*
            Share bought = new Share(what.getSymbol(), what.getCompanyName(), 
                    count, buyShares, customer.getDepot()); 
            em.persist(bought);
            customer.getDepot().add(bought);
            */
            
        } catch (BuySharesException | TradingWSException_Exception e) {
            if (e instanceof TradingWSException_Exception) {
                throw new StockExchangeUnreachableException("Stock exchange unreachable.", e);
            }
        }

    }

    /**
     *
     * @param symbol of shares that are sold
     * @param shares number of shares sold
     * @return sold shares
     * @throws StockExchangeUnreachableException
     */
    @Override
    public BigDecimal sell(String symbol, int shares)
            throws StockExchangeUnreachableException {
        BigDecimal sellShares;

        try {
            sellShares = new BigDecimal(proxy.sell(symbol, shares),
                    new MathContext(PRICE_SCALE));
            System.out.println("Sold shares " + sellShares);

            //adapting current investing volume of the bank
            setOverallDepotValues(getOverallDepotValues().subtract(sellShares));
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

        if (company == null) {
            System.out.println("findShare company null.");
            return new ArrayList<>();
        }
        if (company.trim().isEmpty()) {
            System.out.println("findShare company is empty.");
            return new ArrayList<>();
        }
        companyShares = null;
        try {
            companyShares = proxy.findStockQuotesByCompanyName(company);
        } catch (TradingWSException_Exception e) {
            throw new StockExchangeUnreachableException("Stock exchange unreachable.", e);
        }
        found = new ArrayList<>();
        if (companyShares != null) {
            for (PublicStockQuote temp : companyShares) {
                found.add(new Share(temp.getSymbol(), temp.getCompanyName(),
                        temp.getFloatShares(), temp.getLastTradePrice(), null));
            }
        }
        return found;
    }

    /**
     * Retrieve the sum of all depot values.
     *
     * @return overall sum of depot values.
     */
    private BigDecimal getOverallDepotValues() {
        return currentValue;
    }

    /**
     * Retrieve the sum of all depot values.
     *
     * @return overall sum of depot values.
     */
    private void setOverallDepotValues(BigDecimal newValue) {
        currentValue = newValue;
    }

}
