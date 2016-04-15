/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

/**
 *
 */
public class BuySharesNotEnoughException extends TradingServiceException {
    
    /**
     * Constructor.
     */
    public BuySharesNotEnoughException() {
        super("Not enough shares available.");
    }
    

}
