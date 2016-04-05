/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Column;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 */
@Entity
@NamedQueries({  
@NamedQuery(name="getDepot",
    query="SELECT SUM(d.value) FROM Depot d"),

@NamedQuery(name="getDepotById",
    query="SELECT d.id FROM Depot d WHERE d.id=:depotId"),
})

public class Depot implements Serializable {

    private static final long serialVersionUID = 1L;

    // TODO AM this list must be persisted later, therefore remove 
    // @Transient annotation
    @Transient
    private List<Share> shareList;

    /**
     *
     * @param share
     */
    public void add(Share share) {
        shareList.add(share);
    }
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long customerID;
    // TODO AM: depot value is a calculated value from the shareList
    // mark this as @Transient and do not persist in database
    @Column(name = "DEPOT_VALUE")
    private double value;
    
    public Depot(Long customerID, double value) {
        this.customerID = customerID;
        this.value = value;
        
    }

    /**
     *
     * Getter & Setter Methods
     *
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public Long getCustomerID() {
        return customerID;
    }

    /**
     *
     * @param customerID
     */
    public void setCustomerID(Long customerID) {
        this.customerID = customerID;
    }

    /**
     *
     * @return
     */
    public double getValue() {
        return value;
    }
    /**
     *
     * @param value
     */
    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Depot)) {
            return false;
        }
        Depot other = (Depot) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "at.technikum.bicss.sam.trading.Depot[ id=" + id + " ]";
    }

    @Transient
    /* rating must be calculated and not persisted */
    private double rating;

    /**
     *
     * @return
     */
    public double getRating() {
        return rating;
    }

    /**
     *
     * @param rating
     */
    public void setRating(Long rating) {
        this.rating = rating;
    }

    /**
     * listShares list shareList for this depot.
     *
     * @return list of found shares or empty list.
     */
    public List<Share> listShares() {
        return shareList;

    }
    

    // TODO AM: subject to change, do not initialise with dummy data
    @PostConstruct
    private void init() {
           
        shareList = new ArrayList<>();
        Share dummy = new Share("471147114711", "Dummy 4711 Koelnisch Wasser",
                4711, new BigDecimal(47.11d, new MathContext(2)));
        shareList.add(dummy);
    }

}
