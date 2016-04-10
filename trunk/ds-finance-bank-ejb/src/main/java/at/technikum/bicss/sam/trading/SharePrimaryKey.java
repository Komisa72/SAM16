/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

import java.io.Serializable;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 */
public class SharePrimaryKey implements Serializable {

    private String time;
    private String symbol;

    public SharePrimaryKey() {
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
                append(symbol).
                append(time).
                toHashCode();
    }

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
        return pk.time.equals(time) && pk.symbol.equals(symbol);
    }
}
