/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Column;

/**
 *
 * @author amaierhofer
 */
@Entity
public class Depot implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private Long customerID;
    
    @Column(name="DEPOT_VALUE")
    private double value;
    
    /**
     * 
     * Getter & Setter Methods 
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getCustomerID() {
        return customerID;
    }
    
    public void setCustomerID(Long customerID) {
        this.customerID = customerID;
    }
    
    public double getValue() {
        return value;
    }

    public void setValue(Long value) {
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
    
    public double getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }
    
}
