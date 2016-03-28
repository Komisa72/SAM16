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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 *
 * @author cbaierl
 */
@Entity
public class Share implements Serializable {

    private static final long serialVersionUID = 2L;
    
    // TODO symbol ist kein automatisch generierter Key, wird aus webservicee 
    // ausgelesen. d. h. symbol muss richtig initialisiert werden.
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String symbol;

    private String companyName;
    
    // TODO: AM use BigDecimal as given in webservice
    //@Column(name="price", precision=19, scale=2)
    //private BigDecimal price;
    private double price;
    
    @Column(name="BUY_TIME")
    private String time;
    

    // number of shares, maybe also used for showing floats of stock exchange.
    private long floatCount;

    // TODO: AM die Klasse Buy ist eine generierte Klasse! daher kein Schieben möglich!
    // wofür brauchen wir eine Methode?
    //TODO: Diese 2 Zeilen in eine Methode umwandeln und die Klasse Buy schieben
    //TODO: AM see hint of netbeans date is a reserved SQL-99 keyword  
    private Date date = new Date();
    // TODO: AM warum nur Tag? Kann ich an einem Tag nicht mehrere Aktien von 
    // der gleichen Firma kaufen?
    // wofür brauchen wir eine Variable newstring? was ist ihre Aufgabe? 
    private String newstring = new SimpleDateFormat("yyyy-MM-dd").format(date);
  
    /**
     * Constructor for shares.
     * @param symbol unique identifier for the share, delivered by stock exchange..
     * @param companyName name of company.
     * @param count of shares of the given symbol.
     * @param price of this share.
     */
    public Share(String symbol, String companyName, long count, BigDecimal price)
    {
        this.symbol = symbol;
        this.companyName = companyName;
        // TODO AM set correct price
        this.price = 47.11;
        this.floatCount = count;
        
        // set the time as of now
        time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
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
    public Double getPrice() {
        return price;
    }
    
    /**
     *
     * @param price
     */
    public void setPrice(double price) {
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
    public int hashCode() {
        int hash = 0;
        hash += (symbol != null ? symbol.hashCode() : 0);
        return hash;
    }


    @Override
    public String toString() {
        return "at.technikum.bicss.sam.trading.Share[ symbol=" + symbol + " ]";
    }

    /**
     * Getter of company name.
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Setter of company name.
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * Getter.
     * @return the floatCount
     */
    public long getFloatCount() {
        return floatCount;
    }

    /**
     * Setter.
     * @param floatCount the floatCount to set
     */
    public void setFloatCount(long floatCount) {
        this.floatCount = floatCount;
    }
    

}
