/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelDAO;

import JDBC.ConnectionFactoryFirebird;
import JDBC.ConnectionFactoryMySQL;
import ModelBean.Estoque;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class EstoqueDAO {
    Connection con = null;
    Connection con2 = null;
    public EstoqueDAO() {
        con = ConnectionFactoryFirebird.getConnection();
        con2=ConnectionFactoryMySQL.getConnection();
    }

    public List<Estoque> getEstoque(){
        List<Estoque> estoque = new ArrayList<>();
        List<Estoque> estoque2 = new ArrayList<>();
        String sql = "SELECT CD_REF,QT_PROD FROM ESTOQUE ORDER BY CD_REF";
        PreparedStatement stmt= null;
        ResultSet rs = null;
        try {
            stmt =con.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()){
                Estoque e = new Estoque();
                e.setCd(rs.getString("CD_REF"));
                e.setQtd_estoque(rs.getDouble("QT_PROD"));
                e.setQtd_baixa(0);
                estoque.add(e);
            }
            ConnectionFactoryFirebird.closeConnection(con, stmt, rs);
            sql = "SELECT * FROM uso";
            stmt = con2.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()){
                Estoque e = new Estoque();
                e.setCd(rs.getString("ref"));
                e.setQtd_baixa(rs.getDouble("qtd"));
                estoque2.add(e);
            }
            ConnectionFactoryMySQL.closeConnection(con2, stmt, rs);
            for (int x=0;x<estoque.size();x++){
                for (int i=0;i<estoque2.size();i++){
                    if (estoque.get(x).getCd().equals(estoque2.get(i).getCd())){
                        estoque.get(x).addQtd_baixa(estoque2.get(i).getQtd_baixa());
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Erro ao tentar capturar o estado do estoque: "+ex);
        }finally{
            ConnectionFactoryFirebird.closeConnection(con, stmt, rs);
            ConnectionFactoryMySQL.closeConnection(con2, stmt, rs);
        }
        return estoque;
    }
    
    
}
