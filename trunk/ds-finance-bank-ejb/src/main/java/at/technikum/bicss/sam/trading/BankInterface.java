/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

import java.util.List;
import javax.ejb.Local;
import net.froihofer.dsfinance.ws.trading.PublicStockQuote;

/** BankInterface
 * Local Interface consumed by web applicaton.
 */
@Local
public interface BankInterface {
    
    
    public void createCustomer(Customer customer, String password) throws CustomerCreationFailedException;
    
    public List<Customer> listCustomer();

    public List<PublicStockQuote>  companyShares();
    
    public double volume();


}
