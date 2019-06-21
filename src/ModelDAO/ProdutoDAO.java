/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelDAO;

import JDBC.ConnectionFactoryFirebird;
import JDBC.ConnectionFactoryMySQL;
import ModelBean.Produto;
import funcoes.CDbl;
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
public class ProdutoDAO {

    private Connection con = null;
    private Connection con2 = null;

    public ProdutoDAO() {
        con = ConnectionFactoryMySQL.getConnection();
    }
    
    public boolean addVencimento(String cd,String vencimento) {
        String sql = "INSERT INTO vencimento (cd,vencimento) VALUES (?,?)";
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, cd);
            stmt.setString(2, vencimento);
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.err.println("ERRO no addVencimento: " + ex);
            return false;
        } finally {
            ConnectionFactoryMySQL.closeConnection(con, stmt);
        }
    }
    
    public boolean removeVencimento(int id) {
        String sql = "DELETE FROM vencimento WHERE id = ?";
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.err.println("Erro no remove: " + ex);
            return false;
        } finally {
            ConnectionFactoryMySQL.closeConnection(con, stmt);
        }
    }
    
    public List<Produto> findAllVencimento(){
        List<Produto> produtosVencList = new ArrayList<>();
        String sql = "SELECT * FROM vencimento";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()){
                Produto p = new Produto();
                p.setId(rs.getInt("id"));
                p.setCodigo(rs.getString("cd"));
                p.setVencimento(rs.getString("vencimento"));
                produtosVencList.add(p);
            }
        } catch (SQLException ex) {
            System.err.println("ERRO no findAllVencimento: "+ex);
        }
        finally{
            ConnectionFactoryMySQL.closeConnection(con, stmt, rs);
        }
        return produtosVencList;
    }
    
    public boolean add(Produto produto) {
        String sql = "INSERT INTO uso (ref,qtd,tp) VALUES (?,?,?)";
        PreparedStatement stmt = null;
        
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, produto.getCodigo());
            stmt.setDouble(2, CDbl.CDblTresCasas(produto.getQtd()));
            stmt.setInt(3, produto.getTp());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.err.println("ERRO no add: " + ex);
            return false;
        } finally {
            ConnectionFactoryMySQL.closeConnection(con, stmt);
        }
    }

    public boolean remove(Produto produto) {
        String sql = "DELETE FROM uso WHERE seq = ?";
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, produto.getId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.err.println("Erro no remove: " + ex);
            return false;
        } finally {
            ConnectionFactoryMySQL.closeConnection(con, stmt);
        }
    }
    public boolean remove(int seq) {
        String sql = "DELETE FROM uso WHERE seq = ?";
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, seq);
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.err.println("Erro no remove: " + ex);
            return false;
        } finally {
            ConnectionFactoryMySQL.closeConnection(con, stmt);
        }
    }
    
    public boolean removeProdutosCondicao(int condicao) {
        String sql = "DELETE FROM uso WHERE tp = ?";
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, condicao);
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.err.println("Erro no remove: " + ex);
            return false;
        } finally {
            ConnectionFactoryMySQL.closeConnection(con, stmt);
        }
    }

    public boolean updateQTD(Produto produto) {
        String sql = "UPDATE uso SET qtd = ? WHERE seq = ?";
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(sql);
            stmt.setDouble(1, CDbl.CDblTresCasas(produto.getQtd()));
            stmt.setInt(2, produto.getId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.err.println("Erro no update: " + ex);
            return false;
        } finally {
            ConnectionFactoryMySQL.closeConnection(con, stmt);
        }
    }
    public boolean updateTIPO(int seq,int tp) {
        String sql = "UPDATE uso SET tp = ? WHERE seq = ?";
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, tp);
            stmt.setInt(2, seq);
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.err.println("Erro no update: " + ex);
            return false;
        } finally {
            ConnectionFactoryMySQL.closeConnection(con, stmt);
        }
    }

    public List<Produto> getProdutosFirebird() {
        String sql = "SELECT PRODUTO.CD_REF,PRODUTO.AB_UNIDADE, PRODUTO.DS_PROD, PRECOS_PDA.VL_VENDA "
                + "FROM PRECOS_PDA, PRODUTO "
                + "WHERE PRECOS_PDA.CD_PROD = PRODUTO.CD_PROD ";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        con2 = ConnectionFactoryFirebird.getConnection();
        List<Produto> produtos = new ArrayList<>();
        try {
            stmt = con2.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Produto p = new Produto();
                p.setCodigo(rs.getString("CD_REF"));
                p.setUnidade(rs.getString("AB_UNIDADE"));
                p.setDescricao(rs.getString("DS_PROD"));
                p.setValor(rs.getDouble("VL_VENDA"));
                produtos.add(p);
            }
        } catch (SQLException ex) {
            System.err.println("Erro no getprodutosFirebird: " + ex);
            return null;
        } finally {
            ConnectionFactoryFirebird.closeConnection(con2, stmt, rs);
            ConnectionFactoryMySQL.closeConnection(con);
        }
        return produtos;
    }
    
    public List<Produto> obterCusto(List<Produto> produto){
        String sql = "SELECT CD_REF, VL_CUSTO FROM PRODUTOS_PDA ORDER BY CD_REF";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con2= ConnectionFactoryFirebird.getConnection();
            stmt= con2.prepareStatement(sql);
            rs = stmt.executeQuery();
            String cd_temp = null;
            while (rs.next()){
                cd_temp = rs.getString("CD_REF");
                for (Produto p:produto){
                    if (cd_temp.equals(p.getCodigo())){
                        p.setCusto(rs.getDouble("VL_CUSTO"));
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            ConnectionFactoryFirebird.closeConnection(con2, stmt, rs);
            ConnectionFactoryMySQL.closeConnection(con);
        }
        return produto;
    }
    public List<Produto> obterCusto2(List<Produto> produto){
        String sql = "SELECT CD_REF, VL_CUSTO FROM SUB_TAB_PRECO ORDER BY CD_REF";
        PreparedStatement stmt2 = null;
        ResultSet rs2 = null;
        try {
            con2= ConnectionFactoryFirebird.getConnection();
            stmt2= con2.prepareStatement(sql);
            rs2 = stmt2.executeQuery();
            while (rs2.next()){
                for (Produto p:produto){
                    if (rs2.getString("CD_REF").equals(p.getCodigo())){
                        p.setCusto(rs2.getDouble("VL_CUSTO"));
                        break;
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            ConnectionFactoryFirebird.closeConnection(con2, stmt2, rs2);
            ConnectionFactoryMySQL.closeConnection(con);
        }
        return produto;
    }
    
    public List<Produto> obterEstoque(List<Produto> produto){
        String sql = "SELECT CD_REF,QT_PROD FROM ESTOQUE ORDER BY CD_REF";
        PreparedStatement stmt2 = null;
        ResultSet rs2 = null;
        try {
            con2 = ConnectionFactoryFirebird.getConnection();
            stmt2 = con2.prepareStatement(sql);
            rs2 = stmt2.executeQuery();
            while (rs2.next()){
                for (Produto p:produto){
                    if (p.getCodigo().equals(rs2.getString("CD_REF"))){
                        p.setQtd(rs2.getDouble("QT_PROD"));
                        break;
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            ConnectionFactoryFirebird.closeConnection(con2, stmt2, rs2);
            ConnectionFactoryMySQL.closeConnection(con);
        }
        return produto;
    }
    
    public List<Produto> getAllProtudosFirebird(){
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT CD_REF,CD_GRUPO,DS_PROD,AB_UNIDADE FROM PRODUTO ORDER BY CD_REF";
        PreparedStatement stmt2 = null;
        ResultSet rs2 = null;
        con2  = null;
        try{
            con2 = ConnectionFactoryFirebird.getConnection();
            stmt2 = con2.prepareStatement(sql);
            rs2 = stmt2.executeQuery();
            while (rs2.next()){
                Produto p = new Produto();
                p.setCodigo(rs2.getString("CD_REF"));
                p.setGrupo(rs2.getInt("CD_GRUPO"));
                p.setDescricao(rs2.getString("DS_PROD"));
                p.setUnidade(rs2.getString("AB_UNIDADE"));
                produtos.add(p);
            }
            produtos = obterCusto2(produtos);
            produtos = obterEstoque(produtos);
        }catch(SQLException ex){
            System.err.println("ERRO ao tentar capturar os produtos, custos e quantidades do firebird: "+ex);
        }finally{
            ConnectionFactoryFirebird.closeConnection(con2, stmt2, rs2);
            ConnectionFactoryMySQL.closeConnection(con);
        }
        return produtos;
    }
    
    public List<Produto> findALL() {
        String sql = "SELECT * FROM uso";
        String sql2 = "SELECT PRODUTO.CD_REF, PRODUTO.DS_PROD, PRECOS_PDA.VL_VENDA "
                + "FROM PRECOS_PDA, PRODUTO "
                + "WHERE PRECOS_PDA.CD_PROD = PRODUTO.CD_PROD "
                + "ORDER BY PRODUTO.CD_REF ";
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        List<Produto> produtos = new ArrayList<>();
        try {
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            con2 = ConnectionFactoryFirebird.getConnection();
            stmt2 = con2.prepareStatement(sql2);
            rs2 = stmt2.executeQuery();
            while (rs.next()) {
                Produto p = new Produto();
                p.setId(rs.getInt("seq"));
                p.setCodigo(rs.getString("ref"));
                p.setQtd(rs.getDouble("qtd"));
                p.setTp(rs.getInt("tp"));
                produtos.add(p);
            }
            while (rs2.next()) {
                for (int x = 0; x < produtos.size(); x++) {
                    if (rs2.getString("CD_REF").equals(produtos.get(x).getCodigo())) {
                        produtos.get(x).setDescricao(rs2.getString("DS_PROD"));
                        produtos.get(x).setValor(rs2.getDouble("VL_VENDA"));
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Erro no findALL: " + ex);
        } finally {
            ConnectionFactoryFirebird.closeConnection(con2, stmt2, rs2);
            ConnectionFactoryMySQL.closeConnection(con, stmt, rs);
        }
        return produtos;
    }

    public String getDescricao(String codigo) throws Exception {
        String sql = "SELECT PRODUTO.DS_PROD,PRODUTO.CD_REF FROM PRODUTO";
        PreparedStatement stmt2 = null;
        ResultSet rs2 = null;
        con2 = null;
        try {
            con2 = ConnectionFactoryFirebird.getConnection();
            stmt2 = con2.prepareStatement(sql);
            rs2 = stmt2.executeQuery();
            while (rs2.next()) {
                if (rs2.getString("CD_REF").equals(codigo)) {
                    return rs2.getString("DS_PROD");
                }
            }
        } catch (SQLException ex) {
            throw new Exception("Erro ao tentar obter a descrição no banco de dados Firebird.");
        } finally {
            ConnectionFactoryFirebird.closeConnection(con2, stmt2, rs2);
            ConnectionFactoryMySQL.closeConnection(con);
        }
        return null;
    }
    
    
}
