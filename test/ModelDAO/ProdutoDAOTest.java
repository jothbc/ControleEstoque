/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelDAO;

import ModelBean.Produto;
import funcoes.CDbl;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.junit.Test;
import org.junit.Ignore;

/**
 *
 * @author User
 */
public class ProdutoDAOTest {
    
    public ProdutoDAOTest() {
    }

    @Test
    @Ignore
    public void exibir() {
        ProdutoDAO dao = new ProdutoDAO();
        List<Produto> produtos = dao.findALL();
        JOptionPane.showMessageDialog(null, produtos.get(0).getDescricao());
    }
    @Test
    @Ignore
    public void obterref(){
        String t = "78935785";
        ProdutoDAO dao = new ProdutoDAO();
        try {
            JOptionPane.showMessageDialog(null, dao.getDescricao(t));
        } catch (Exception ex) {
            Logger.getLogger(ProdutoDAOTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void arrumarQTD(){
        List<Produto> produtos = new ProdutoDAO().findALL();
        for (Produto p:produtos){
            p.setQtd(CDbl.CDblTresCasas(p.getQtd()));
            if (!new ProdutoDAO().updateQTD(p)){
                JOptionPane.showMessageDialog(null, "erro em algo");
            }
        }
    }
    
}
