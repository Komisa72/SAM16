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
public class BuySharesException extends Exception {
    
    public BuySharesException() {
    }
    
     /**
     * Constructs an instance of <code>TradingServiceException</code> with the
     * specified detail message.
     *
     * @param message the detail message.
     * 
     */
    public BuySharesException(String message) {
        super(message);
    }
    
     /**
     * Constructs an instance of <code>TradingServiceException</code> with the
     * specified detail message.
     *
     * @param message the detail message.
     * @param cause the inner cause of the exception.
     */
    public BuySharesException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
