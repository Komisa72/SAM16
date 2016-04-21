/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

/**
 *  DepotCreationFailedException thrown on depot creation failure.
 */
public class DepotCreationFailedException extends TradingServiceException {


    /**
     * Constructs an instance of <code>DepotCreationFailedException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public DepotCreationFailedException(String msg) {
        super(msg);
    }
    
    
        /**
     * Constructs an instance of <code>DepotCreationFailedException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     * @param cause the inner cause of the exception.
     */
    public DepotCreationFailedException(String msg, Throwable cause)
    {
        super(msg, cause);
    }

}
