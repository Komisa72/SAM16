/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

/**
 * BuySharesVolumeException thrown when bank volume would be exceeded on buy.
 */
public class BuySharesVolumeException extends TradingServiceException {
    
    /**
     * Constructor.
     */
    public BuySharesVolumeException() {
        super("Could not buy shares, because volume would exceed allowed value.");
    }
    
}
