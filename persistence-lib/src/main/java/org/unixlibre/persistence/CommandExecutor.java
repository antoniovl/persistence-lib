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
@FunctionalInterface
public interface CommandExecutor {
    public <T> void executeCommand(Command<T> cmd);
}
