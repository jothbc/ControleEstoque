/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelDAO;

import JDBC.ConnectionFactoryFirebird;
import JDBC.ConnectionFactoryMySQL;
import ModelBean.Condicao;
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
public class CondicaoDAO {

    private Connection con = null;

    public CondicaoDAO() {
        con = ConnectionFactoryMySQL.getConnection();
    }
    public void reconect(){
        con = ConnectionFactoryMySQL.getConnection();
    }

    public boolean save(Condicao c) {
        String sql = "INSERT INTO condicao (descricao) VALUES (?)";
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, c.getDecricao());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.err.println("ERRO: " + ex);
            return false;
        } finally {
            ConnectionFactoryMySQL.closeConnection(con, stmt);
        }
    }
    
    public boolean update(Condicao condicao){
        String sql = "UPDATE condicao SET descricao = ? WHERE tp = ?";
        PreparedStatement stmt = null;
        
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1,condicao.getDecricao());
            stmt.setInt(2, condicao.getTp());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.err.println("ModelDAO.CondicaoDAO.update() ERRO: "+ex);
            return false;
        }finally{
            ConnectionFactoryMySQL.closeConnection(con, stmt);
        }
    }

    public List<Condicao> findAll() {
        String sql = "SELECT * FROM condicao";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Condicao> condicoes = new ArrayList<>();
        try {
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Condicao condicao= new Condicao();
                condicao.setTp(rs.getInt("tp"));
                condicao.setDecricao(rs.getString("descricao"));
                condicoes.add(condicao);
            }
        } catch (SQLException ex) {
            System.err.println("ModelDAO.CondicaoDAO.findAll() ERRO: " + ex);
        } finally {
            ConnectionFactoryMySQL.closeConnection(con, stmt, rs);
        }
        return condicoes;
    }

    public int findTP(String descricao) {
        List<Condicao> condicoes = findAll();
        for (int x=0;x<condicoes.size();x++){
            if (condicoes.get(x).getDecricao().equals(descricao)){
                return condicoes.get(x).getTp();
            }
        }
        return -1;
    }
    
    public List<Condicao> findAllFirebird(){
        List<Condicao> grupos = new ArrayList<>();
        String sql = "SELECT CD_GRUPO,DS_GRUPO FROM GRUPO ORDER BY CD_GRUPO";
        PreparedStatement stmt2 = null;
        ResultSet rs2 = null;
        Connection con2 = null;
        try{
            con2= ConnectionFactoryFirebird.getConnection();
            stmt2 = con2.prepareStatement(sql);
            rs2 = stmt2.executeQuery();
            while (rs2.next()){
                Condicao c = new Condicao();
                c.setTp(rs2.getInt("CD_GRUPO"));
                c.setDecricao(rs2.getString("DS_GRUPO"));
                grupos.add(c);
            }
        }catch(Exception ex){
            System.err.println("ERRO ao tentar capturar os grupos no firebird:" +ex);
        }finally{
            ConnectionFactoryFirebird.closeConnection(con2, stmt2, rs2);
            ConnectionFactoryMySQL.closeConnection(con);
        }
        return grupos;
    }
}
