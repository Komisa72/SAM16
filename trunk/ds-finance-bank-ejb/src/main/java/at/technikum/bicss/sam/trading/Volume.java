/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * This entity contains the invest volume, helper class for perstisting
 * the invest volume of the bank.
 */
@Entity
public class Volume implements Serializable {

    @Id
    private Long Id;

    private BigDecimal investVolume;

    /**
     * Getter of id.
     *
     * @return the id.
     */
    public Long getId() {
        return Id;
    }

    /**
     * Setter of id.
     *
     * @param Id to set.
     */
    public void setId(Long Id) {
        this.Id = Id;
    }

    /**
     * Getter of invest volume.
     *
     * @return the investVolume,.
     */
    public BigDecimal getInvestVolume() {
        return investVolume;
    }

    /**
     * Setter of inveset volume.
     * @param investVolume the investVolume to set
     */
    public void setInvestVolume(BigDecimal investVolume) {
        this.investVolume = investVolume;
    }

}
