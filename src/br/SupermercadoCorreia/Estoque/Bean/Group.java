/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.SupermercadoCorreia.Estoque.Bean;

/**
 *
 * @author User
 */
public class Group {

    private boolean fromFirebird;
    private int id;
    private String description;
    private String table_mysql;

    public Group() {
        fromFirebird = false;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return this.getDescription();
    }

    /**
     * @return the fromFirebird
     */
    public boolean isFromFirebird() {
        return fromFirebird;
    }

    /**
     * @param fromFirebird the fromFirebird to set
     */
    public void setFromFirebird(boolean fromFirebird) {
        this.fromFirebird = fromFirebird;
    }

    /**
     * @return the table_mysql
     */
    public String getTable_mysql() {
        return table_mysql;
    }

    /**
     * @param table_mysql the table_mysql to set
     */
    public void setTable_mysql(String table_mysql) {
        this.table_mysql = table_mysql;
    }

}
