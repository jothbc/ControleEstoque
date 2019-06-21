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
public class Condicao {
    private int tp;
    private String decricao;

    public Condicao() {
    }

    public Condicao(String decricao) {
        this.decricao = decricao;
    }

    public Condicao(int tp, String decricao) {
        this.tp = tp;
        this.decricao = decricao;
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
     * @return the decricao
     */
    public String getDecricao() {
        return decricao;
    }

    /**
     * @param decricao the decricao to set
     */
    public void setDecricao(String decricao) {
        this.decricao = decricao;
    }
    
}
