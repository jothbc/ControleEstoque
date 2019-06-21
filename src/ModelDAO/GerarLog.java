/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelDAO;

import JDBC.ConnectionFactoryMySQL;
import ModelBean.Condicao;
import ModelBean.LogProduto;
import ModelBean.Produto;
import funcoes.CDate;
import funcoes.CDbl;
import java.sql.Connection;
import java.sql.Date;
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
public class GerarLog {

    Connection con = null;

    public GerarLog() {
        con = ConnectionFactoryMySQL.getConnection();
    }

    public boolean limparLog() {
        String sql = "DELETE FROM log_troca WHERE id>0";
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(sql);
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(GerarLog.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            ConnectionFactoryMySQL.closeConnection(con, stmt);
        }
    }

    public boolean gerarLog(List<Produto> produtos) {
        if (limparLog()) {
            con = ConnectionFactoryMySQL.getConnection();
            String sql = "INSERT INTO log_troca(id,cd,nome,qtd,fornecedor,valor_und,valor_total) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement stmt=null;
            try {
                stmt = con.prepareStatement(sql);
                for (Produto p : produtos) {
                    stmt.setInt(1, p.getId());
                    stmt.setString(2, p.getCodigo());
                    stmt.setString(3, p.getDescricao());
                    stmt.setDouble(4, p.getQtd());
                    stmt.setString(5, p.getFornecedor());
                    stmt.setDouble(6, p.getValor());
                    stmt.setDouble(7, CDbl.CDblDuasCasas(p.getQtd()*p.getValor()));
                    stmt.executeUpdate();
                }
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(GerarLog.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            } finally {
                ConnectionFactoryMySQL.closeConnection(con, stmt);
            }
        }return false;
    }
    public boolean lancarLogBaixa(Produto produto,Double qtd, String usuario,List<Condicao> cond){
        String sql = "INSERT INTO log_lancamento (cd,qtd,data_lancado,usuario,tipo) VALUES (?,?,?,?,?)";
        PreparedStatement stmt = null;
        String tipo = "";
        for (Condicao c:cond){
            if (c.getTp()==produto.getTp()){
                tipo = c.getDecricao();
            }
        }
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, produto.getCodigo());
            stmt.setDouble(2, CDbl.CDblTresCasas(qtd));
            stmt.setString(3, new CDate().DataPTBRtoDataMySQL(new CDate().DataPTBRAtual()));
            stmt.setString(4, usuario);
            stmt.setString(5, tipo);
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.err.println("Erro ao tentar lançar log de baixas: "+ex);
        }finally{
            ConnectionFactoryMySQL.closeConnection(con, stmt);
        }
        return false;
    }
    public List<LogProduto> findAllLogBaixa(){
        List<LogProduto> log = new ArrayList<>();
        String sql = "SELECT * FROM log_lancamento";
        PreparedStatement stmt = null;
        ResultSet rs= null;
        try {
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()){
                LogProduto p = new LogProduto();
                Condicao c = new Condicao();
                p.setId(rs.getInt("id"));
                p.setCodigo(rs.getString("cd"));
                p.setQtdAtual(rs.getDouble("qtd"));
                p.setData(new CDate().DataMySQLtoDataStringPT(rs.getString("data_lancado")));
                p.setUser(rs.getString("usuario"));
                c.setDecricao(rs.getString("tipo"));
                p.setCondicao(c);
                log.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GerarLog.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            ConnectionFactoryMySQL.closeConnection(con, stmt, rs);
        }
        return log;
    }
    
}
