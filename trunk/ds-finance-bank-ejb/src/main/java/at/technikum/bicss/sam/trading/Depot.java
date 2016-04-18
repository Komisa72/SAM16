/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 *
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "getDepot",
            query = "SELECT SUM(d.value) FROM Depot d"),
    
    @NamedQuery(name = "getDepotById",
            query = "SELECT d FROM Depot d WHERE d.id =:depotId"),

    @NamedQuery(name = "getDepotShares",
            query = "SELECT s FROM Share s WHERE s.depot.id =:depotId"),

    @NamedQuery(name = "getCustomerDepot",
            query = "SELECT d FROM Depot d WHERE d.customer.id =:customerId")

})

public class Depot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne() 
    @JoinColumn(name="CUSTOMER_FK", updatable=false)
    private Customer customer;
    
    @Column(name = "Shares")
    @OneToMany(mappedBy = "depot", fetch = FetchType.EAGER)
    private List<Share> shares  = new ArrayList<>();

    // TODO depot value could be calculated from the shares
    @Column(name = "DEPOT_VALUE")
    private BigDecimal value;

    public Depot() {
    }
    
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    
    /**
     *
     * Getter id.
     *
     * @return id of depot.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter for id.
     * @param id of depot.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     *
     * @param value
     */
    public void setValue(BigDecimal value) {
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
    private BigDecimal rating = new BigDecimal(0);

    /**
     *
     * @return
     */
    public BigDecimal getRating() {
        return rating;
    }

    /**
     *
     * @param rating
     */
    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    /**
     * Getter for share list.
     * @return the shares
     */
    public List<Share> getShares() {
        return shares;
    }

    /**
     * @param shares the shares to set
     */
    public void setShares(List<Share> shares) {
        this.shares = shares;
    }

}
