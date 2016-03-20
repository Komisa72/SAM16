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

import net.froihofer.util.jboss.WildflyAuthDBHelper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

/**
 * Bank class responsible for persting customer and its shares in depots. Also
 * check the maximum amount shares to hold at this bank.
 */
@Stateless(name = "BankEJB")
@Local(BankInterface.class)
public class Bank implements BankInterface {

    private static WildflyAuthDBHelper authHelper;

    // TODO subject to change, customerList should be read out of database
    // TODO think about concurrency when customerList is static, now every Bank-Bean
    // has it's own customerList!
    private ArrayList<Customer> customerList;

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

    // Credentials Webservice Trading Service (group account)
    // user
    // bic4b16_06
    // password
    // Feim0Kah4
    
    
    @PostConstruct
    private void init() {
        // setup initial default bank user with default credentials and role.
        String homeDir = System.getProperty("jboss.home.dir");
        authHelper = new WildflyAuthDBHelper(new File(homeDir));

        // customerList = dao.customerList();
        // Actually, you should retrieve the customerList from DAO. This is just for demo.
        customerList = new ArrayList<>();

    }

    @Override
    public void createCustomer(Customer customer, String password) throws CustomerCreationFailedException {
        String[] customerRoles = new String[1];
        customerRoles[0] = "customer";

        try {
            authHelper.addUser(customer.getName(), password, customerRoles);
            // TODO persist customer
            //customer.persist();
            customer.setId(customerList.isEmpty() ? 1 : customerList.get(customerList.size() - 1).getId() + 1);
            customerList.add(customer);
        } catch (IOException ex) {
            System.out.println("Could not add customer to password database");
            throw new CustomerCreationFailedException("Could not add customer to password database.", ex);
        }

    }

    @Override
    public List<Customer> listCustomer() {
        return customerList;
    }
}
