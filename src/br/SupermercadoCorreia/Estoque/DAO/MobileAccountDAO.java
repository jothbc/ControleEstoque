/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.SupermercadoCorreia.Estoque.DAO;

import JDBC.ConnectionFactoryMySQL;
import br.SupermercadoCorreia.Estoque.Bean.MobileAccount;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class MobileAccountDAO extends DAO {

    public MobileAccountDAO() {
        con = ConnectionFactoryMySQL.getConnection();
    }

    public boolean addAcount(MobileAccount account) {
        sql = "INSERT INTO user_mobile(nick,mac_wifi) VALUES (?,?)";
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, account.getNick());
            stmt.setString(2, account.getMac_wifi());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(MobileAccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            ConnectionFactoryMySQL.closeConnection(con, stmt);
        }
    }

    public boolean removeAccount(MobileAccount account) {
        sql = "DELETE FROM user_mobile WHERE mac_wifi = ?";
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, account.getMac_wifi());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(MobileAccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            ConnectionFactoryMySQL.closeConnection(con, stmt);
        }
    }

    public List<MobileAccount> findAll() {
        List<MobileAccount> accounts = new ArrayList<>();
        sql = "SELECT * FROM user_mobile";
        try {
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                accounts.add(new MobileAccount(rs.getString("mac_wifi"), rs.getString("nick")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MobileAccountDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            ConnectionFactoryMySQL.closeConnection(con, stmt, rs);
        }
        return accounts;
    }

}
