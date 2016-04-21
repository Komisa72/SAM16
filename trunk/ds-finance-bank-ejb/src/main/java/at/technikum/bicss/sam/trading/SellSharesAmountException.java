/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

/**
 * SellSharesAmountException thron on sell when underflow or nothing to sell.
 */
public class SellSharesAmountException extends TradingServiceException {
    
    /**
     * Constructor.
     */
    public SellSharesAmountException() {
        super("Can not sell this amount, underflow of amount or 0.");
    }
    

}
