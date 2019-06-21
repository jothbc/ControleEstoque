/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelDAO;

import JDBC.ConnectionFactoryFirebird;
import JDBC.ConnectionFactoryMySQL;
import ModelBean.Condicao;
import ModelBean.LogProduto;
import funcoes.CDate;
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
public class LogEstoque {

    Connection con = null;

    public LogEstoque() {
        con = ConnectionFactoryFirebird.getConnection();
    }

    public List<LogProduto> getLogProduto(String cd) {
        List<LogProduto> logprodutos = new ArrayList<>();
        String sql = "SELECT * FROM log_estoque WHERE log_estoque.cd_ref =? AND log_estoque.dt_data>='01.05.2018'";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, cd);
            rs = stmt.executeQuery();
            while (rs.next()) {
                LogProduto log = new LogProduto();
                log.setCodigo(cd);
                log.setData(rs.getString("DT_DATA"));
                log.setMovimento(rs.getString("CD_DOCUMENTO"));
                log.setQtdAnterior(rs.getDouble("QT_ANT"));
                log.setQtdAtual(rs.getDouble("QT_ATUAL"));
                log.setQtdEntrada(rs.getDouble("QT_QTDE"));
                logprodutos.add(log);
            }
        } catch (SQLException ex) {
            System.err.println("ERRO ao tentar gerar o log de movimento do protudo:" + ex);
        } finally {
            ConnectionFactoryFirebird.closeConnection(con, stmt, rs);
        }
        return logprodutos;
    }

    public List<LogProduto> getLogLancamento() {
        List<LogProduto> log = new ArrayList<>();
        String sql = "SELECT * FROM log_lancamento";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        //###########
        ConnectionFactoryFirebird.closeConnection(con);
        Connection con2 = ConnectionFactoryMySQL.getConnection();
        //##############
        try {
            stmt = con2.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                LogProduto p = new LogProduto();
                Condicao c = new Condicao();
                p.setId(rs.getInt("id"));
                p.setCodigo(rs.getString("cd"));
                p.setQtdAtual(rs.getDouble("qtd"));
                p.setData(rs.getString("data_lancado"));
                p.setUser(rs.getString("usuario"));
                c.setDecricao(rs.getString("tipo"));
                p.setCondicao(c);
                log.add(p);
            }
        } catch (SQLException ex) {
            System.err.println("ERRO ao tentar gerar o log de movimento do protudo:" + ex);
        } finally {
            ConnectionFactoryMySQL.closeConnection(con2, stmt, rs);
        }
        return log;
    }

}
