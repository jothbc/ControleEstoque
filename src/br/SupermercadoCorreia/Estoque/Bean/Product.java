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
public class Product {

    private int id_db;
    private String code;
    private double amount;
    private double amount_db;
    private int type;
    private String description;
    private double sale_value;
    private double cost_value;
    private String expiration_date;
    private String provider;
    private int group_db;
    private String unity;
    
    public Product() {
    }

    /**
     * @return the id_db
     */
    public int getId_db() {
        return id_db;
    }

    /**
     * @param id_db the id_db to set
     */
    public void setId_db(int id_db) {
        this.id_db = id_db;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
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

    /**
     * @return the sale_value
     */
    public double getSale_value() {
        return sale_value;
    }

    /**
     * @param sale_value the sale_value to set
     */
    public void setSale_value(double sale_value) {
        this.sale_value = sale_value;
    }

    /**
     * @return the cost_value
     */
    public double getCost_value() {
        return cost_value;
    }

    /**
     * @param cost_value the cost_value to set
     */
    public void setCost_value(double cost_value) {
        this.cost_value = cost_value;
    }

    /**
     * @return the expiration_date
     */
    public String getExpiration_date() {
        return expiration_date;
    }

    /**
     * @param expiration_date the expiration_date to set
     */
    public void setExpiration_date(String expiration_date) {
        this.expiration_date = expiration_date;
    }

    /**
     * @return the provider
     */
    public String getProvider() {
        return provider;
    }

    /**
     * @param provider the provider to set
     */
    public void setProvider(String provider) {
        this.provider = provider;
    }

    /**
     * @return the group_db
     */
    public int getGroup_db() {
        return group_db;
    }

    /**
     * @param group_db the group_db to set
     */
    public void setGroup_db(int group_db) {
        this.group_db = group_db;
    }

    /**
     * @return the unity
     */
    public String getUnity() {
        return unity;
    }

    /**
     * @param unity the unity to set
     */
    public void setUnity(String unity) {
        this.unity = unity;
    }

    /**
     * @return the amount_db
     */
    public double getAmount_db() {
        return amount_db;
    }

    /**
     * @param amount_db the amount_db to set
     */
    public void setAmount_db(double amount_db) {
        this.amount_db = amount_db;
    }
    
}
