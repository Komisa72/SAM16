/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

/**
 * TradingServiceException base exception of the application.
 */
public class TradingServiceException extends Exception {

    /**
     * Creates a new instance of <code>TradingServiceException</code> without
     * detail message.
     */
    public TradingServiceException() {
    }

    /**
     * Constructs an instance of <code>TradingServiceException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public TradingServiceException(String msg) {
        super(msg);
    }
    
    
    /**
     * Constructs an instance of <code>TradingServiceException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     * @param cause the inner cause of the exception.
     */
    public TradingServiceException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
