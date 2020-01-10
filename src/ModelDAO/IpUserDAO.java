/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelDAO;

import JDBC.ConnectionFactoryMySQL;
import ModelBean.IpUser;
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
public class IpUserDAO {
    Connection con =null;

    public IpUserDAO() {
        con = ConnectionFactoryMySQL.getConnection();
    }
    public boolean addIp(IpUser ip){
        String sql = "INSERT INTO iproot (ip,usuario) VALUES (?,?)";
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, ip.getIp());
            stmt.setString(2, ip.getUser());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.err.println("ERRO ao tentar salvar o ip: "+ex);
            return false;
        }finally{
            ConnectionFactoryMySQL.closeConnection(con, stmt);
        }
    }
    public List<IpUser> findAll(){
        List<IpUser> ips = new ArrayList<>();
        String sql = "SELECT * FROM iproot";
        PreparedStatement stmt =null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()){
                IpUser i = new IpUser();
                i.setId(rs.getInt("id"));
                i.setIp(rs.getString("ip"));
                i.setUser(rs.getString("usuario"));
                ips.add(i);
            }
        } catch (SQLException ex) {
            System.err.println("Erro ao tentar buscar os ips cadastrados no banco de dados: "+ex);
        }finally{
            ConnectionFactoryMySQL.closeConnection(con, stmt, rs);
        }
        return ips;
    }
    public boolean delete(int id){
        String sql = "DELETE FROM iproot WHERE id = ?";
        PreparedStatement stmt =null;
        try {
            stmt= con.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            System.err.println("ERRO ao tentar deletar um ip do iproot: "+ex);
            return false;
        }finally{
            ConnectionFactoryMySQL.closeConnection(con, stmt);
        }
    }
    
}
