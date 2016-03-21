/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

/**
 * Exception used when webservice of stock exchange is unreachable.
 */
public class StockExchangeUnreachableException extends TradingServiceException {


    /**
     * Constructs an instance of <code>CustomerCreationFailedException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public StockExchangeUnreachableException(String msg) {
        super(msg);
    }
    
    
        /**
     * Constructs an instance of <code>CustomerCreationFailedException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     * @param cause the inner cause of the exception.
     */
    public StockExchangeUnreachableException(String msg, Throwable cause)
    {
        super(msg, cause);
    }

}
