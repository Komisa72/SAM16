/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

import java.util.List;
import javax.ejb.Local;

/** BankInterface
 * Local Interface consumed by web applicaton.
 */
@Local
public interface BankInterface {
    
    /** sayHello
     * TODO add the real interface methods used by the web application.
     * @return hello message.
     */
    public String sayHello();
    
    public void createCustomer(Customer customer, String password) throws CustomerCreationFailedException;
    
    public List<Customer> listCustomer();


}
