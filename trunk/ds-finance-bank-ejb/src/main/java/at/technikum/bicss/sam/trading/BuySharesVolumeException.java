/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

/**
 *
 * @author Claudia
 */
public class BuySharesVolumeException extends TradingServiceException {
    
    public BuySharesVolumeException() {
        super("Could not buy shares, because volume would exceed allowed value.");
    }
    
}
