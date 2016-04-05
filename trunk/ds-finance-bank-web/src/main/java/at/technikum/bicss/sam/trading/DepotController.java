/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author zsayici
 */
@Named("depotController")
@SessionScoped

public class DepotController implements Serializable {

    private static final long serialVersionUID = 1L;

     @EJB(name = "BankEJB")
    private BankInterface bank;
    
    
    @Inject
    @Named("tradingModel") 
    private TradingModel model;

    private List<Share> depotShares = new ArrayList<>();
    private Depot depot;
 
    
    public Depot getDepot()
    {
        return depot;
    }
    
    public void setDepot(Depot dep)
    {
        depot = dep;
    }
    
    public List<Share> getDepotShares()
    {
        return depotShares;
    }

    public void setDepotShares(List<Share> shares)
    {
        depotShares = shares;
    }
    
    
    /**
     *
     */
    public enum Role {

        /**
         * Not logged in.
         */
        NONE,
        /**
         * Logged in as customer.
         */
        CUSTOMER,
        /**
         * Logged in as bank.
         */
        BANK;

        /**
         *
         * @return
         */
        public boolean isBank() {
            return Role.BANK.equals(this);

        }
        
        public boolean isCustomer() {
            return Role.CUSTOMER.equals(this);

        }
    }

    /**
     *
     * @param customer
     * @throws DepotCreationFailedException
     */
    /*public void createDepot(Customer customer) throws DepotCreationFailedException
    {
        
       
        try {
            bank.createDepot(customer);           
            model.updateModel();
        } catch (DepotCreationFailedException ex) {
            throw ex;
        }
        
    } */
  
    public List<Share> getShares(long depotID) throws StockExchangeUnreachableException {
        if (depotID != 0) 
        {
         
               depot = getDepotById(depotID);
               depotShares = getDepotShares(depot);
            
        } else {
            depotShares.clear();
        }
        return depotShares;
    }
    
    private List<Share> getDepotShares(Depot depot)
    {
        List<Share> shares = null;
        
        return shares;
    }
    
    
   
    private Depot getDepotById(long depotID)
    {
        return depot;
    }

    

   

 

    /**
     * Creates a new instance of DepotController
     */
    public DepotController() {
    }

    /**
     * Init this bean.
     */
    //@PostConstruct
    /*
    public void init() {
        // TODO remove unused code
        // aufgrund der session feststellen, in welcher rolle wir uns gerade
        // befinden
        Role who = getRole();
        if (who == Role.CUSTOMER) {
            FacesContext context = FacesContext.getCurrentInstance();

            //context.redirect(context.getRequestContextPath() + "/showcustomer.xhtml");
        }

        customerList = bank.listCustomer();

    }*/

}
