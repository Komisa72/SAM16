/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 *
 * @author Claudia
 */
@Entity
public class Volume implements Serializable{
    
    @Id
    private Long Id;
    
    private BigDecimal investVolume;
    
    
    

    public Long getId() {
        return Id;
    }
    
    public void setId(Long Id) {
        this.Id = Id;
    }
    /**
     * @return the investVolume
     */
    public BigDecimal getInvestVolume() {
        return investVolume;
    }

    /**
     * @param investVolume the investVolume to set
     */
    public void setInvestVolume(BigDecimal investVolume) {
        this.investVolume = investVolume;
    }
    
    
    
}
