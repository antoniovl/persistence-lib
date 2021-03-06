/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unixlibre.persistence;

/**
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public enum TransactionTypesEnum {
    /**
     * The transaction it's handled externally, (i.e. container managed).
     */
    CONTAINER,
    /**
     * UserTransaction.
     */
    USER,
    /**
     * Local transaction (JPA with JavaSE or JDBC).
     */
    LOCAL;
}
