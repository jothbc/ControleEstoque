/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EstoqueFrms.Interacao;

import funcoes.RobotJCR;
import java.awt.event.KeyEvent;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author User
 */
public class thRobot implements Runnable {

    DefaultTableModel tb;

    public thRobot(DefaultTableModel tb) {
        this.tb = tb;
    }

    @Override
    public void run() {
        for (int x = 0; x < tb.getRowCount(); x++) {
            if (tb.getValueAt(x, 6) == "X") {
                double valor = (double) tb.getValueAt(x, 3);        //custo
                double qtd = (double) tb.getValueAt(x, 1);          //quantidade
                String cd = (String) tb.getValueAt(x, 0);
                if (valor == 0) {                                   //se valor = 0 defini como 1
                    valor = 1.0;
                }
                RobotJCR.executar(cd, 50);                          //Digita o CD
                RobotJCR.executar(KeyEvent.VK_ENTER, 50);
                int qtd2 = (int) qtd;
                if (!"KG".equals((String)tb.getValueAt(x, 5))) {
                    RobotJCR.executar(Integer.toString(qtd2), 50);  //digita qtd em formato int
                    System.out.println(qtd2);
                } else {
                    RobotJCR.executar(Double.toString(qtd), 50);    //digita qtd em formato double
                }
                RobotJCR.executar(KeyEvent.VK_ENTER, 50);
                RobotJCR.executar(Double.toString(valor), 50);      //digita o valor de custo
                RobotJCR.executar(KeyEvent.VK_ENTER, 50);
            }
        }
    }
}
