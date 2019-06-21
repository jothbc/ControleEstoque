/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelDAO;

import JDBC.ConnectionFactoryFirebird;
import ModelBean.ProdutoFornecedor;
import funcoes.CDate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author User
 */
public class ProdutoFornecedorDAO {

    Connection con = null;

    public ProdutoFornecedorDAO() {
        con = ConnectionFactoryFirebird.getConnection();
    }

    public List<ProdutoFornecedor> findAll() {
        String sql = "SELECT PRODUTO_FORNECEDOR.CD_REF, PRODUTO_FORNECEDOR.CD_FORNECEDOR, PRODUTO_FORNECEDOR.DT_ALT, PESSOA.NM_PESSOA FROM PESSOA, PRODUTO_FORNECEDOR WHERE PESSOA.CD_PESSOA = PRODUTO_FORNECEDOR.CD_FORNECEDOR AND ((PRODUTO_FORNECEDOR.CD_REF>99999)) ORDER BY PRODUTO_FORNECEDOR.CD_REF";
        List<ProdutoFornecedor> pf = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        CDate conv = new CDate();
        try {
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            String ultimoCd = null, ultimoFornecedor = null;
            Date ultimaData = null;
            while (rs.next()) {
                if (ultimoCd == null) {
                    ultimoFornecedor = rs.getString("NM_PESSOA");
                    ultimoCd = rs.getString("CD_REF");
                    ultimaData = rs.getDate("DT_ALT");
                } else if (ultimoCd.equals(rs.getString("CD_REF"))) {
                    if (ultimaData.before(rs.getDate("DT_ALT"))) {
                        ultimaData = rs.getDate("DT_ALT");
                        ultimoFornecedor = rs.getString("NM_PESSOA");
                    }
                } else if (!ultimoCd.equals(rs.getString("CD_REF"))) {
                    String temp = conv.DataMySQLtoDataStringPT(ultimaData.toString());
                    ProdutoFornecedor prod = new ProdutoFornecedor(ultimoCd, temp, ultimoFornecedor);
                    pf.add(prod);
                    ultimoFornecedor = rs.getString("NM_PESSOA");
                    ultimoCd = rs.getString("CD_REF");
                    ultimaData = rs.getDate("DT_ALT");
                }
            }
        } catch (SQLException ex) {
            System.err.println("ERRO ao tentar pesquisar os fornecedores no firebird: " + ex);
        } finally {
            ConnectionFactoryFirebird.closeConnection(con, stmt, rs);
        }
        return pf;
    }

}
