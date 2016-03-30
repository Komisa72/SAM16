/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

import java.io.Serializable;
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
    public void createDepot(Customer customer) throws DepotCreationFailedException
    {
     
       
        try {
            bank.createDepot(customer);           
            model.updateModel();
           
     
        } catch (DepotCreationFailedException ex) {
            throw ex;
        }
        
    }
  
    /**
     * Creates a new instance of DepotController
     */
    public DepotController() {
        
    }

    /**
     * Init this bean.
     */

}
