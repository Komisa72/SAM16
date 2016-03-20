/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

/**
 * Customer of bank.
 * TODO: persist the customer to database.
 */
public class Customer {
    // TODO add annotations for persisting the id
    private int id;
    
    // TODO add annotations for persisting the name
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

    /**
     * @param id the identifier to set
     */
    public void setId(int id) {
        this.id = id;
    }
    
    
}
