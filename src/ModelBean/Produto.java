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
public class Produto {

    private int id;
    private String codigo;
    private double qtd;
    private int tp;
    private String descricao;
    private double valor;
    private double custo;
    private String vencimento;
    private String fornecedor;
    private int grupo;
    private String unidade;
    
    public Produto() {
    }

    public Produto(String codigo, double qtd, int tp, String descricao) {
        this.codigo = codigo;
        this.qtd = qtd;
        this.tp = tp;
        this.descricao = descricao;
    }

    public String getVencimento() {
        return this.vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
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
     * @return the qtd
     */
    public double getQtd() {
        return qtd;
    }

    /**
     * @param qtd the qtd to set
     */
    public void setQtd(double qtd) {
        this.qtd = qtd;
    }

    /**
     * @return the tp
     */
    public int getTp() {
        return tp;
    }

    /**
     * @param tp the tp to set
     */
    public void setTp(int tp) {
        this.tp = tp;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setCusto(double custo) {
        this.custo = custo;
    }

    public double getCusto() {
        return custo;
    }

    public void setGrupo(int grupo) {
        this.grupo = grupo;
    }

    public int getGrupo() {
        return grupo;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }
    
    
    

}
