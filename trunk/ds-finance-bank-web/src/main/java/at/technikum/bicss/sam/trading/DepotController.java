/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

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

    private int buyCount = 1;

    private int sellCount = 1;

    /**
     * Creates a new instance of DepotController
     */
    public DepotController() {
    }

    /**
     * checkBuyCount.
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

    /**
     * checkSellCount.
     *
     * @param context faces context.
     * @param component from ui which requests the check.
     * @param value the string input to be checked.
     */
    public void checkSellCount(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            throw new ValidatorException(new FacesMessage("Anzahl ist 0."));
        }

        // TODO max. soviele shares verkaufen wie zur zeit als verfügbar.
    }

    /**
     * Buy shares for the selected customer.
     *
     * @throws StockExchangeUnreachableException
     */
    public void buyShare() throws StockExchangeUnreachableException {
        try {
            bank.buy(model.getSelectedCustomer(), model.getSelectedShare(), getBuyCount());
            model.findShares();
            
             FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Transaction: Buy Shares OK",
                    String.format("(%s) shares have been added to your depot.", getBuyCount()));
            RequestContext.getCurrentInstance().showMessageInDialog(message);
            buyCount = 1;

        } catch (StockExchangeUnreachableException ex) {
            // in this case show error page, because we can not buy shares.
            throw ex;
        } catch (BuySharesNotEnoughException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Buy failed",
                    String.format("Not enough shares to buy %s shares.", getBuyCount()));
            RequestContext.getCurrentInstance().showMessageInDialog(message);

        } catch (BuySharesVolumeException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Buy failed", "Bank volume exceeded. Please try again later.");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    /**
     * Sell Shares for the selected customer.
     *
     * @throws StockExchangeUnreachableException
     */
    public void sellShare() throws StockExchangeUnreachableException {
        try {
            bank.sell(model.getSelectedCustomer(), model.getSelectedShare(), getSellCount());
            
              FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Transaction: Sell Shares OK",
                    String.format("(%s) shares removed from depot.", getSellCount()));
            RequestContext.getCurrentInstance().showMessageInDialog(message);
            sellCount = 1;
        } catch (StockExchangeUnreachableException ex) {
            // in this case show error page, because we can not sell shares.
            throw ex;
        } catch (SellSharesAmountException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sell failed",
                    String.format("Can not sell %s shares.", getSellCount()));
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    /**
     * Getter buy count.
     *
     * @return the buyCount
     */
    public int getBuyCount() {
        return buyCount;
    }

    /**
     * Setter buy count.
     *
     * @param buyCount the buyCount to set
     */
    public void setBuyCount(int buyCount) {
        this.buyCount = buyCount;
    }

    /**
     * Getter sell count.
     *
     * @return the buyCount.
     */
    public int getSellCount() {
        return sellCount;
    }

    /**
     * Setter sell count.
     *
     * @param sellCount count.
     */
    public void setSellCount(int sellCount) {
        this.sellCount = sellCount;
    }

}
