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
public class thRobot2 implements Runnable {

    private final DefaultTableModel tb;
    private String custoMinimo;
    private String qtdMais;
    private final int delay;

    public thRobot2(DefaultTableModel tb, String custoMinimo_, String qtdMais, int delay) {
        this.tb = tb;
        this.custoMinimo = custoMinimo_;
        this.qtdMais = qtdMais;
        this.delay = delay;
    }

    @Override
    public void run() {
        String cd;
        double qtd;
        double custo;
        double custoMinimoDbl = 1;
        double qtdAdd = 0;
        if (qtdMais != null) {
            qtdAdd = Double.parseDouble(qtdMais.replaceAll(",", "\\."));
        }
        if (this.custoMinimo != null) {
            custoMinimoDbl = Double.parseDouble(this.custoMinimo.replaceAll(",", "\\."));
        }
        for (int x = 0; x < tb.getRowCount(); x++) {
            //verificação negativo (double) tb.getValueAt(x, 2) < 0
            //verificação positivo (double) tb.getValueAt(x, 2) > 0
            if (tb.getValueAt(x, 5) == "X") {
                cd = obterCodigo(tb, x);
                qtd = obterQuantidade(tb, x);
                qtd += qtdAdd;
                custo = obterCusto(tb, x, custoMinimoDbl);

                RobotJCR.executar(cd, delay);
                RobotJCR.executar(KeyEvent.VK_ENTER, delay);

                if (!"KG".equals((String) tb.getValueAt(x, 4))) {
                    RobotJCR.executar(Integer.toString((int)qtd), delay);
                } else {
                    RobotJCR.executar(Double.toString(qtd), delay);
                }
                RobotJCR.executar(KeyEvent.VK_ENTER, delay);
                RobotJCR.executar(Double.toString(custo), delay);
                RobotJCR.executar(KeyEvent.VK_ENTER, delay);
            }
        }
    }

    private String obterCodigo(DefaultTableModel tb, int linha) {
        return (String) tb.getValueAt(linha, 0);
    }

    private double obterQuantidade(DefaultTableModel tb, int x) {
        double qtd;
        if ((double) tb.getValueAt(x, 2) < 0) {
            qtd = ((double) tb.getValueAt(x, 2)) * (-1.0);
        } else {
            qtd = ((double) tb.getValueAt(x, 2));
        }
        return qtd;
    }

    private double obterCusto(DefaultTableModel tb, int x, double custoMinimoDbl) {
        double custo;
        if (tb.getValueAt(x, 3) != null) {
            custo = (double) tb.getValueAt(x, 3);
        } else if (custoMinimoDbl != 1) {
            custo = custoMinimoDbl;
        } else {
            custo = 1;
        }
        return custo;
    }
}
