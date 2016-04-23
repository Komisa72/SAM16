/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

import java.io.Serializable;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Class representing the combined key of share {symbol, time}
 */
public class SharePrimaryKey implements Serializable {

    private String time;
    private String symbol;

    /**
     * Getter for symbol name.
     * @return symbol name.
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Setter for symbol.
     * @param symbol to be set.
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Getter for time of buy.
     * @return time of buy.
     */
    public String getTime() {
        return time;
    }

    /**
     * Setter for buy time.
     * @param time of buy.
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * hashCode used by persistenc framework.
     * @return the hashcode of this key. 
     */
    @Override
    public int hashCode() {
        // HashCodeBuilder enables a good hashCode for this class.
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
                append(symbol).
                append(time).
                toHashCode();
    }

    /**
     * equals used by persistance framework.
     * @obj object to be compared to this.
    */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SharePrimaryKey)) {
            return false;
        }
        if (obj == null) {
            return false;
        }
        SharePrimaryKey pk = (SharePrimaryKey) obj;
        // both time and symbols build the primary key
        return pk.time.equals(time) && pk.symbol.equals(symbol);
    }
}
