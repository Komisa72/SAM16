/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
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

    private int buyCount = 2;


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
    /**
     * Creates a new instance of DepotController
     */
    public DepotController() {
    }
    
        /**
     * checkUserName if it is not empty and not too long.
     *
     * @param context faces context.
     * @param component from ui which request the check.
     * @param value the string input to be ckecked.
     */
    public void checkBuyCount(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            throw new ValidatorException(new FacesMessage("Anzahl ist 0."));
        }

       // TODO max. soviele shares kaufen wie zur zeit als floatShares verfügbar

    }
    
    
    
    public void buyShare() throws StockExchangeUnreachableException
    {
        try {
            bank.buy(model.getSelectedCustomer(), model.getSelectedShare(), getBuyCount());
        } catch (StockExchangeUnreachableException ex) {
            // TODO AM add error handling
            throw ex; 
        } catch (BuySharesException ex) {
            // TODO AM what if it exceeds volume
        }
    }

    /**
     * @return the buyCount
     */
    public int getBuyCount() {
        return buyCount;
    }

    /**
     * @param buyCount the buyCount to set
     */
    public void setBuyCount(int buyCount) {
        this.buyCount = buyCount;
    }
    


}
