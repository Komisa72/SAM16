/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Customer of bank.
 * TODO: persist the customer to database.
 */
@Entity
public class Customer implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * @return the MAX_LENGTH_NAME
     */
    public static int getMaxLengthName() {
        return MAX_LENGTH_NAME;
    }
    
    @Id @GeneratedValue
    private int id;
    
    
    // limit lenght of customer name to this size
    private final static int MAX_LENGTH_NAME = 200;
    

    private String name;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the identifier
     */
    public int getId() {
        return id;
    }

    
}
