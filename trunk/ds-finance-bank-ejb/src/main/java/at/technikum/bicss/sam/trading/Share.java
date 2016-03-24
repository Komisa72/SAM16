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
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String symbol;

    private String companyName;
    
    private double price;
    
    @Column(name="BUY_TIME")
    private String time;
    
    //TODO: Diese 2 Zeilen in eine Methode umwandeln und die Klasse Buy schieben
    Date date = new Date();
    String newstring = new SimpleDateFormat("yyyy-MM-dd").format(date);
  
    /*
     * 
     * Getter & Setter Methods 
     */
    
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    
    public String getName() {
        return companyName;
    }
    
    public void setName(String companyName) {
        this.companyName = companyName;
    }
    
    public Double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public String getTime() {
        return time;
    }
    
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
    

}
