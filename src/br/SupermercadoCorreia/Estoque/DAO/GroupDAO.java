/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.SupermercadoCorreia.Estoque.DAO;

import JDBC.ConnectionFactoryFirebird;
import br.SupermercadoCorreia.Estoque.Bean.Group;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class GroupDAO {

    Connection con = null;
    PreparedStatement stmt;
    ResultSet rs;
    String sql;

    public List<Group> getGroupFirebird() {
        List<Group> group = new ArrayList<>();
        sql = "SELECT CD_GRUPO,DS_GRUPO FROM GRUPO";
        con = ConnectionFactoryFirebird.getConnection();
        try {
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Group g = new Group();
                g.setFromFirebird(true);
                g.setId(rs.getInt("CD_GRUPO"));
                g.setDescription(rs.getString("DS_GRUPO"));
                group.add(g);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GroupDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactoryFirebird.closeConnection(con, stmt, rs);
        }
        return group;
    }

}
