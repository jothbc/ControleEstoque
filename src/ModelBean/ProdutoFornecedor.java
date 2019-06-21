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
public class ProdutoFornecedor {
    private String cd;
    private String data;
    private String fornecedor;

    public ProdutoFornecedor() {
    }

    public ProdutoFornecedor(String cd, String data, String fornecedor) {
        this.cd = cd;
        this.data = data;
        this.fornecedor = fornecedor;
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
     * @return the fornecedor
     */
    public String getFornecedor() {
        return fornecedor;
    }

    /**
     * @param fornecedor the fornecedor to set
     */
    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }
    
}
