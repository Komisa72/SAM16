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
import javax.persistence.FetchType;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

/**
 * Entity Share
 *
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

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "DEPOT_FK", updatable = false)
    private Depot depot;

    @Column(precision = 19, scale = 2)
    private BigDecimal stockPrice;

    /**
     * Default constructor needed by persistance framework.
     */
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
        this.stockPrice = price;
        // set the time as of now
        time = new SimpleDateFormat(TIME_FORMAT).format(
                new Timestamp(System.currentTimeMillis()));
    }

    /**
     * Getter for symbol.
     *
     * @return symbol.
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Setter for symbol.
     *
     * @param symbol
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Getter for buy price.
     * @return price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Setter for buy price.
     * @param price
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     *
     * @return price at stock.
     */
    public BigDecimal getstockPrice() {
        return this.stockPrice;
    }

    /**
     *
     * @param stockPrice
     */
    public void setstockPrice(BigDecimal stockPrice) {
        this.stockPrice = stockPrice;
    }

    /**
     *
     * @return stockPrice
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
     * @return the companyName.
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Setter of company name.
     *
     * @param companyName the companyName to set.
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
     * Setter of count.
     *
     * @param floatCount the floatCount to set
     */
    public void setFloatCount(long floatCount) {
        this.floatCount = floatCount;
    }

    /**
     * Getter of depot.
     * @return depot
     */
    public Depot getDepot() {
        return depot;
    }

    /**
     * Setter of depot.
     * @param depot
     */
    public void setDepot(Depot depot) {
        this.depot = depot;
    }

}
