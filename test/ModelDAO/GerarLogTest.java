/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelDAO;

import ModelBean.Condicao;
import ModelBean.Produto;
import java.util.List;
import javax.swing.JOptionPane;
import org.junit.Test;

/**
 *
 * @author User
 */
public class GerarLogTest {
    
    public GerarLogTest() {
    }

    @Test
    public void testSomeMethod() {
        Produto p = new Produto();
        p.setCodigo("500");
        p.setQtd(10);
        List<Condicao> cond = null;
        if (new GerarLog().lancarLogBaixa(p,20.0, "jonathan",cond)){
            JOptionPane.showMessageDialog(null, "lançado com sucesso");
        }else{
            JOptionPane.showMessageDialog(null, "Erro ao tentar lançar");
        }
    }
    
}
