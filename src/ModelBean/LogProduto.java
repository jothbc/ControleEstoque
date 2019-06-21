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
public class LogProduto {
    private int id;
    private String codigo;
    private String data;
    private String movimento;
    private double qtdAnterior;
    private double qtdAtual;
    private double qtdEntrada;
    private String user;
    private Condicao condicao;
    public LogProduto() {
    }

    
    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * @return the movimento
     */
    public String getMovimento() {
        return movimento;
    }

    /**
     * @param movimento the movimento to set
     */
    public void setMovimento(String movimento) {
        this.movimento = movimento;
    }

    /**
     * @return the qtdAnterior
     */
    public double getQtdAnterior() {
        return qtdAnterior;
    }

    /**
     * @param qtdAnterior the qtdAnterior to set
     */
    public void setQtdAnterior(double qtdAnterior) {
        this.qtdAnterior = qtdAnterior;
    }

    /**
     * @return the qtdAtual
     */
    public double getQtdAtual() {
        return qtdAtual;
    }

    /**
     * @param qtdAtual the qtdAtual to set
     */
    public void setQtdAtual(double qtdAtual) {
        this.qtdAtual = qtdAtual;
    }

    /**
     * @return the qtdEntrada
     */
    public double getQtdEntrada() {
        return qtdEntrada;
    }

    /**
     * @param qtdEntrada the qtdEntrada to set
     */
    public void setQtdEntrada(double qtdEntrada) {
        this.qtdEntrada = qtdEntrada;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setCondicao(Condicao condicao) {
        this.condicao = condicao;
    }

    public Condicao getCondicao() {
        return condicao;
    }
    
}
