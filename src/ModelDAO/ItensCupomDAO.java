/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelDAO;

import JDBC.ConnectionFactoryFirebird;
import ModelBean.Produto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class ItensCupomDAO {

    Connection con = null;

    public ItensCupomDAO() {
        con = ConnectionFactoryFirebird.getConnection();
    }

    public String findCD_MOV(String cd_cupom) {
        String sql = "SELECT CD_MOVIMENTO, CD_CUPOM FROM DOC_FATURA_DETALHADO WHERE CD_CUPOM = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String cd = "";
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, cd_cupom);
            rs = stmt.executeQuery();
            while (rs.next()) {
                cd = rs.getString("CD_MOVIMENTO");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItensCupomDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactoryFirebird.closeConnection(con, stmt, rs);
        }
        return cd;
    }

    public List<Produto> getItensCupom(String cdMov) {
        String sql = "SELECT CD_MOVIMENTO, CD_REFER_PRO, QT_ITE_PRO FROM ITENS_DOC WHERE ITENS_DOC.CD_MOVIMENTO = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Produto> produtos = new ArrayList<>();
        try {
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(cdMov));
            rs = stmt.executeQuery();
            while (rs.next()) {
                Produto prod = new Produto();
                prod.setCodigo(rs.getString("CD_REFER_PRO"));
                prod.setQtd(rs.getDouble("QT_ITE_PRO"));
                produtos.add(prod);
            }
        } catch (SQLException ex) {
            System.err.println("ERRO ao tentar capturar itens do cupom no firebird: " + ex);
            return null;
        } finally {
            ConnectionFactoryFirebird.closeConnection(con, stmt, rs);
        }
        return produtos;
    }

}
