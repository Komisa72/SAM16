/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import java.text.SimpleDateFormat;
import javax.persistence.CascadeType;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author cbaierl
 */
@Entity
@IdClass(SharePrimaryKey.class)
public class Share implements Serializable {

    private static final long serialVersionUID = 3L;

    private final static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Id
    private String symbol;

    private String companyName;

    @Column(precision = 19, scale = 2)
    private BigDecimal price;

    @Id
    @Column(name = "BUY_TIME")
    private String time;

  
    // number of shares, maybe also used for showing floats of stock exchange.
    private long floatCount;
    
    @ManyToOne(optional=false) 
    @JoinColumn(name="id", nullable=false, updatable=false)
    private Depot depot;
   
    public Share() {
    }

    /**
     * Constructor for shares.
     *
     * @param symbol unique identifier for the share, delivered by stock
     * exchange..
     * @param companyName name of company.
     * @param count of shares of the given symbol.
     * @param price of this share.
     */
    public Share(String symbol, String companyName, long count, BigDecimal price) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.price = price;
        this.floatCount = count;

        // set the time as of now
        time = new SimpleDateFormat(TIME_FORMAT).format(
                new Timestamp(System.currentTimeMillis()));
    }

    /*
     * 
     * Getter & Setter Methods 
     */
    /**
     *
     * @return
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     *
     * @param symbol
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     *
     * @return
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     *
     * @param price
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     *
     * @return
     */
    public String getTime() {
        return time;
    }

    /**
     *
     * @param time
     */
    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "at.technikum.bicss.sam.trading.Share[ symbol=" + symbol + " "
                + time + " ]";
    }

    /**
     * Getter of company name.
     *
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Setter of company name.
     *
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * Getter.
     *
     * @return the floatCount
     */
    public long getFloatCount() {
        return floatCount;
    }

    /**
     * Setter.
     *
     * @param floatCount the floatCount to set
     */
    public void setFloatCount(long floatCount) {
        this.floatCount = floatCount;
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
