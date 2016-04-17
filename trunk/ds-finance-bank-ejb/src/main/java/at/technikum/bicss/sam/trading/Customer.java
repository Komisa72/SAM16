/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Customer of bank.
 */
@Entity
@NamedQueries({  
@NamedQuery(name="allCustomers",
    query="SELECT c FROM Customer c ORDER BY c.id"),
@NamedQuery(name="singleCustomer",
    query="SELECT c FROM Customer c WHERE c.name IS :customerName")
})

@Table(uniqueConstraints={@UniqueConstraint(columnNames={"name"})})
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * @return the MAX_LENGTH_NAME
     */
    public static int getMaxLengthName() {
        return MAX_LENGTH_NAME;
    }
    
    @Id //@GeneratedValue
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
   
    
    @OneToOne(optional=true, cascade={CascadeType.MERGE}) 
    @PrimaryKeyJoinColumn
    private Depot depot;
    
    // limit lenght of customer name to this size
    private final static int MAX_LENGTH_NAME = 200;
    
    @Column(name = "name", unique= true, nullable = false, length = MAX_LENGTH_NAME)
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
    public Long getId() {
        return id;
    }

   
   public Depot getDepot()
   {
      return depot;
   }
      
   public void setDepot(Depot depot)
   {
      this.depot = depot;
   }
    

}
