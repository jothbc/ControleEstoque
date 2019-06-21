/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelDAO;

import JDBC.ConnectionFactoryMySQL;
import ModelBean.Usuario;
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
public class UsuarioDAO {

    Connection con = null;

    public UsuarioDAO() {
        con = ConnectionFactoryMySQL.getConnection();
    }

    public boolean add(String nome, String senha,int permissao) {
        String sql = "INSERT INTO usuario (nome,senha,permissao) VALUES (?,?,?)";
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, senha);
            stmt.setInt(3, permissao);
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.err.println("ERRO ao tentar adicionar usuario: " + ex);
            return false;
        } finally {
            ConnectionFactoryMySQL.closeConnection(con, stmt);
        }
    }

    public List<Usuario> findAll() {
        String sql = "SELECT * FROM usuario";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Usuario> users = new ArrayList<>();
        try {
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setNome(rs.getString("nome"));
                u.setSenha(rs.getString("senha"));
                u.setSeq(rs.getInt("seq"));
                u.setPermissao(rs.getInt("permissao"));
                users.add(u);
            }
        } catch (SQLException ex) {
            System.err.println("ERRO no findAll: " + ex);
        } finally {
            ConnectionFactoryMySQL.closeConnection(con, stmt, rs);
        }
        return users;
    }

    public boolean verificar(String nome, String senha) {
        List<Usuario> users = findAll();
        for (int x = 0; x < users.size(); x++) {
            if (nome.equals(users.get(x).getNome()) && senha.equals(users.get(x).getSenha())) {
                return true;
            }
        }
        return false;
    }
    public boolean verificarPermissao(String senha){
        List<Usuario> users = findAll();
        for (int x = 0; x < users.size(); x++) {
            if (senha.equals(users.get(x).getSenha()) && users.get(x).getPermissao()==1) {
                return true;
            }
        }
        return false;
    }
    public boolean remover(int id){
        String sql = "DELETE FROM usuario WHERE seq = ?";
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            System.err.println("Erro ao tentar remover usuario do banco de dados: "+ex);
            return false;
        }finally{
            ConnectionFactoryMySQL.closeConnection(con, stmt);
        }
    }
}
