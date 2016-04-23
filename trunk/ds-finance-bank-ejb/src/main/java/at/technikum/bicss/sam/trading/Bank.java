/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

import javax.ejb.Local;
import javax.ejb.Stateless;

import java.io.IOException;
import java.math.BigDecimal;
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

    @WebServiceRef(name = "TradingWebServiceService")
    private TradingWebServiceService stockService;
    private TradingWebService proxy;


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
     * @return current volume that can be invested
     */
    @Override
    public BigDecimal getVolume() {
        return em.find(Volume.class, new Long(1)).getInvestVolume();
    }

    /**
     * getCustomer retrieves the customer with the given name.
     *
     * @param name of the customer.
     * @return the found customer or null.
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
    /**
     * 
     * @param customerId
     * @return customer depot 
     */
    @Override
    public Depot getCustomerDepot(Long customerId) {
        Query query = em.createNamedQuery("getCustomerDepot");
        try {
            query.setParameter("customerId", customerId);
            return (Depot) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    /**
     * 
     * @param id
     * @return shares that belong to a depot
     */
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
            em.persist(customer);
            info.getAuthHelper().addUser(customer.getName(), password, customerRoles);

        } catch (IOException ex) {
            System.out.println("Could not add customer to password database");
            throw new CustomerCreationFailedException("Could not add customer to password database.", ex);
        }

    }
    /**
     * 
     * @param customer
     * @return depot that was created
     * @throws DepotCreationFailedException 
     */
    @Override
    public Depot createDepot(Customer customer) throws DepotCreationFailedException {

        Depot depot = new Depot();
        depot.setCustomer(customer);
        customer.setDepot(depot);

        try {
            em.persist(depot);
            depot.setCustomer(customer);
            em.merge(customer);
            em.persist(depot);
            System.out.println("Depot was added. Id: " + depot.getId());
        } catch (Exception ex) {
            System.out.println("Could not add depot");
            throw new DepotCreationFailedException("Could not add depot to database.", ex);
        }

        return depot;
    }

    /**
     *
     * gets a list of stock quotes by symbol
     *
     * @param symbols symbol for which stock quotes are to be returned
     * @return list of stock quotes
     * @throws StockExchangeUnreachableException
     */
    public List<PublicStockQuote> getStockQuotes(List<String> symbols)
            throws StockExchangeUnreachableException {

        List<PublicStockQuote> stockQuotes;

        try {
            stockQuotes = proxy.getStockQuotes(symbols);
            System.out.println(stockQuotes.toString());

        } catch (TradingWSException_Exception e) {
            throw new StockExchangeUnreachableException(e);
        }

        return stockQuotes;
    }

    /**
     *
     * @param symbol of shares to be listet
     * @return list of stock quote history
     * @throws StockExchangeUnreachableException
     */
    public List<PublicStockQuote> getStockQuoteHistory(String symbol)
            throws StockExchangeUnreachableException {
        List<PublicStockQuote> stockQuoteHistory;

        try {
            stockQuoteHistory = proxy.getStockQuoteHistory(symbol);
            System.out.println(stockQuoteHistory.toString());

        } catch (TradingWSException_Exception e) {
            throw new StockExchangeUnreachableException(e);
        }

        return stockQuoteHistory;
    }

    /**
     * Buy a share from public stock quote.
     *
     * @param customer for which customer
     * @param what shares to be bought
     * @param count of shares to be bought
     * @throws StockExchangeUnreachableException
     * @throws BuySharesVolumeException
     * @throws BuySharesNotEnoughException
     */
    @Override
    public void buy(Customer customer, Share what, int count)
            throws StockExchangeUnreachableException, BuySharesVolumeException, BuySharesNotEnoughException {

        BigDecimal buyShares;
        boolean created = false;

        try {

            BigDecimal wert = proxy.findStockQuotesByCompanyName(what.getCompanyName()).get(0).getLastTradePrice();

            /* Variablen für die Berechnung des Depotwertes */
            BigDecimal buyCount = new BigDecimal(count);
            BigDecimal buyValue;
            buyValue = buyCount.multiply(wert);

            /* fetch managed instance to adapt investment volume */
            Volume volume = em.find(Volume.class, new Long(1));
            BigDecimal newVolume = volume.getInvestVolume();
            newVolume = newVolume.subtract(buyValue);

            if (newVolume.compareTo(BigDecimal.ZERO) == -1) {
                throw new BuySharesVolumeException();
            }

            double val = proxy.buy(what.getSymbol(), count);
            buyShares = new BigDecimal(val);

            System.out.println("Bought shares" + buyShares);

            Share bought = new Share(what.getSymbol(), what.getCompanyName(),
                    count, buyShares);

            volume.setInvestVolume(newVolume);
            em.persist(volume);

            Depot sdepot = null;

            if (customer.getDepot() == null) {
                System.out.println("No customer depot found");
                try {
                    sdepot = createDepot(customer);
                    customer.setDepot(sdepot);

                    created = true;
                    System.out.println("Depot was added to customer " + customer.getId() + "with id:" + customer.getDepot().getId());

                } catch (DepotCreationFailedException e) {
                    System.out.println("Depot could not be created");
                    // TODO AM hier darf es nicht normal weitergehen,
                    // wenn depot nicht angelegt werden kann braucht man 
                    // eine Fehlerbehandlung.
                }
            } else {

                sdepot = customer.getDepot();

                em.merge(sdepot);
            }
            bought.setDepot(sdepot);
            em.persist(bought);
            sdepot.getShares().add(bought);
            //sdepot.add(what);
            em.persist(bought);
            if (created) {
                em.persist(sdepot);

            } else {
                em.merge(sdepot);
            }

        } catch (TradingWSException_Exception e) {
            if (e instanceof TradingWSException_Exception) {
                String detail = e.getMessage();
                if (detail != null) {
                    String expected = "Not enough shares available";
                    if (detail.contains(expected)) {
                        throw new BuySharesNotEnoughException();
                    }
                }
            }
            throw new StockExchangeUnreachableException(e);
        }
    }

    /**
     * Sell a share to public stock quote.
     *
     * @param customer for which customer.
     * @param what shares to be sold.
     * @param count of shares to be sold.
     * @throws StockExchangeUnreachableException
     * @throws SellSharesAmountException
     */
    @Override
    public void sell(Customer customer, Share what, int count) throws StockExchangeUnreachableException,
            SellSharesAmountException {
        BigDecimal sellShares;
        if (count < 1) {
            throw new SellSharesAmountException();
        }
        if (what.getFloatCount() < count) {
            throw new SellSharesAmountException();
        }

        try {

            sellShares = new BigDecimal(proxy.sell(what.getSymbol(), count));
            System.out.println("Sold shares " + count);

            BigDecimal wert = proxy.findStockQuotesByCompanyName(what.getCompanyName()).get(0).getLastTradePrice();

            /* Variablen für die Berechnung des Depotwertes */
            BigDecimal sellCount = new BigDecimal(count);
            BigDecimal sellValue;
            sellValue = sellCount.multiply(wert);

            /* fetch managed instance to adapt investment volume */
            Volume volume = em.find(Volume.class, new Long(1));
            BigDecimal newVolume = volume.getInvestVolume();
            newVolume = newVolume.add(sellValue);

            volume.setInvestVolume(newVolume);
            em.persist(volume);

            if (count < what.getFloatCount()) {
                what.setFloatCount(what.getFloatCount() - count);
                em.merge(what);
            } else {
                customer.getDepot().getShares().remove(what);
                em.remove(em.merge(what));

                em.merge(customer.getDepot());
            }

        } catch (TradingWSException_Exception e) {
            throw new StockExchangeUnreachableException(e);
        }
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
            throw new StockExchangeUnreachableException(e);
        }
        found = new ArrayList<>();
        if (companyShares != null) {
            for (PublicStockQuote temp : companyShares) {
                found.add(new Share(temp.getSymbol(), temp.getCompanyName(),
                        temp.getFloatShares(), temp.getLastTradePrice()));
            }
        }
        return found;
    }


}
