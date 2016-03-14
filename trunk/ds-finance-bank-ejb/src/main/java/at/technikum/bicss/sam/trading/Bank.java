/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

import javax.ejb.Local;
import javax.ejb.Stateless;

/**
 * @author amaierhofer
 */
@Stateless(name = "BankEJB")
@Local(BankInterface.class)
public class Bank implements BankInterface {

    /** sayHello
     * TODO to be replaced by real methods from BankInterface
     *
     */
    @Override
    public void sayHello() {
        // TODO subject to change, remove unused code.
        int i = 0;
        i = 5;
    }
}
