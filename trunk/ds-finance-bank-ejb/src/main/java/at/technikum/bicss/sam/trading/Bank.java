/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.froihofer.util.jboss.WildflyAuthDBHelper;
import java.io.File;
import java.io.IOException;
import javax.annotation.PostConstruct;

/**
 * Bank class responsible for persting customer and its shares in depots. Also
 * check the maximum amount shares to hold at this bank.
 */
@Stateless(name = "BankEJB")
@Local(BankInterface.class)
public class Bank implements BankInterface {

    private WildflyAuthDBHelper authHelper;

    /**
     * Constructor.
     */
    public Bank() {

    }

    @Override
    public String sayHello() {
        // TODO subject to change, remove unused code.
        return "55";
    }

    @PostConstruct
    private void SetupBankRole() {
        // setup initial default bank user with default credentials and role.
        try {
            String password = "bank";
            /*
            TODO can not read out password defined in ejb-jar.xml
             InitialContext iniCtx = new InitialContext();
            Context envCtx = (Context) iniCtx.lookup("java:comp/env");
            password = (String) envCtx.lookup("password");
             */
            String homeDir = System.getProperty("jboss.home.dir");
            authHelper = new WildflyAuthDBHelper(new File(homeDir));
            String[] bankRoles = new String[1];
            bankRoles[0] = "bank";

            authHelper.addUser("bank", password, bankRoles);
            System.out.println("Bank user prepared.");

            //} 
            //catch (NamingException ex) {
            //    System.out.println("Naming exception.");
        } catch (IOException ex) {
            System.out.println("Bank user setup bank role io exception.");
        }

    }
}
