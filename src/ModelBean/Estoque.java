/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelBean;

/**
 *
 * @author User
 */
public class Estoque {
    private String cd;
    private String desc;
    private double qtd_estoque;
    private double qtd_baixa;

    public Estoque() {
    }

    /**
     * @return the cd
     */
    public String getCd() {
        return cd;
    }

    /**
     * @param cd the cd to set
     */
    public void setCd(String cd) {
        this.cd = cd;
    }

    /**
     * @return the qtd_estoque
     */
    public double getQtd_estoque() {
        return qtd_estoque;
    }

    /**
     * @param qtd_estoque the qtd_estoque to set
     */
    public void setQtd_estoque(double qtd_estoque) {
        this.qtd_estoque = qtd_estoque;
    }

    /**
     * @return the qtd_baixa
     */
    public double getQtd_baixa() {
        return qtd_baixa;
    }

    /**
     * @param qtd_baixa the qtd_baixa to set
     */
    public void setQtd_baixa(double qtd_baixa) {
        this.qtd_baixa = qtd_baixa;
    }
    public void addQtd_baixa(double qtd_baixa){
        this.qtd_baixa += qtd_baixa;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
    
    
}
