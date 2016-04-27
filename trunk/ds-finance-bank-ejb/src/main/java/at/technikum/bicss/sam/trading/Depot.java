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

/**
 * Depot related to one customer. Contains all the bought shares.
 */
@Entity
@NamedQueries({
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
    @JoinColumn(name = "CUSTOMER_FK", updatable = false)
    private Customer customer;

    @Column(name = "Shares")
    @OneToMany(mappedBy = "depot", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Share> shares = new ArrayList<>();

    @Transient
    /* rating must be calculated and not persisted */
    private BigDecimal rating = new BigDecimal(0);

    /**
     * Getter of customer.
     *
     * @return the customer assigned to this depot.
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Setter of customer.
     *
     * @param customer set the customer assigned to this depot.
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    /**
     * retrieves value of all shares on the day they were bought
     * @return sum of share value on the day bought
     */
    public BigDecimal getBuyValue() {
        
        BigDecimal sum = new BigDecimal(0);
        
        for (Share share : shares) {
            sum = sum.add(share.getPrice().multiply(new BigDecimal(share.getFloatCount())));
        }
            
        return sum;
    }
    
    
    /**
     * Getter id.
     *
     * @return id of depot.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter for id.
     *
     * @param id of depot.
     */
    public void setId(Long id) {
        this.id = id;
    }


    /**
     * hashCode of this instance, maybe used by persistence framework.
     * @return 
     */
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

    /**
     *
     * @return rating
     */
    public BigDecimal getRating() {
        BigDecimal sum = new BigDecimal(0);
        BigDecimal value = new BigDecimal(0);

        for (Share share : shares) {
            value=share.getstockPrice();
            if(value == null) {
                return new BigDecimal(0);
            }
            sum = sum.add(share.getstockPrice().multiply(new BigDecimal(share.getFloatCount())));
        }
            
        return sum;
        
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
     *
     * @return the shares.
     */
    public List<Share> getShares() {
        return shares;
    }

    /**
     * Setter for share list.
     *
     * @param shares the shares to set
     */
    public void setShares(List<Share> shares) {
        this.shares = shares;
    }

}
