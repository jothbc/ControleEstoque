/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.SupermercadoCorreia.Estoque.DAO;

import JDBC.ConnectionFactoryFirebird;
import JDBC.ConnectionFactoryMySQL;
import br.SupermercadoCorreia.Estoque.Bean.Product;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;

/**
 *
 * @author User
 */
public class ProductDAO {

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sql = null;
    
    public final static int CONSUMPTION = 1;
    public final static int COMSUMO = 1;
    public final static int BAKERY = 2;
    public final static int PADARIA = 2;
    public final static int EXCHANGE = 3;
    public final static int TROCA = 3;
    public final static int BREAK = 4;
    public final static int QUEBRA = 4;

    public List<Product> getAllFB(JProgressBar j) {
        j.setMinimum(0);
        j.setValue(0);
        con = ConnectionFactoryFirebird.getConnection();
        List<Product> array = new ArrayList<>();
        sql = "SELECT PRODUTO.CD_REF,CD_GRUPO,DS_PROD,AB_UNIDADE,QT_PROD FROM PRODUTO, ESTOQUE WHERE PRODUTO.CD_REF = ESTOQUE.CD_REF AND PRODUTO.FG_ATIVO = 1 ORDER BY CD_REF";
        try {
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            j.setMaximum(rs.getFetchSize() + 1);
            while (rs.next()) {
                Product p = new Product();
                p.setCode(rs.getString("CD_REF"));
                p.setGroup_db(rs.getInt("CD_GRUPO"));
                p.setDescription(rs.getString("DS_PROD"));
                p.setUnity(rs.getString("AB_UNIDADE"));
                p.setAmount_db(rs.getDouble("QT_PROD"));
                array.add(p);
                j.setValue(j.getValue() + 1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactoryFirebird.closeConnection(con, stmt, rs);
        }
        return array;
    }

    public List<Product> getAllFB_Sale(List<Product> array, JProgressBar j) {
        j.setMinimum(0);
        j.setValue(0);
        con = ConnectionFactoryFirebird.getConnection();
        sql = "SELECT PRODUTO.CD_REF, PRODUTO.DS_PROD, PRECOS_PDA.VL_VENDA "
                + "FROM PRECOS_PDA, PRODUTO "
                + "WHERE PRECOS_PDA.CD_PROD = PRODUTO.CD_PROD "
                + "ORDER BY PRODUTO.CD_REF";
        try {
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            j.setMaximum(rs.getFetchSize() + 1);
            while (rs.next()) {
                for (Product p : array) {
                    if (p.getCode().equals(rs.getString("CD_REF"))) {
                        p.setSale_value(rs.getDouble("VL_VENDA"));
                        break;
                    }
                }
                j.setValue(j.getValue() + 1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactoryFirebird.closeConnection(con, stmt, rs);
        }
        return array;
    }

    public List<Product> getAllFB_Cost(List<Product> array, JProgressBar j) {
        j.setMinimum(0);
        j.setValue(0);
        con = ConnectionFactoryFirebird.getConnection();
        sql = "SELECT CD_REF, VL_CUSTO FROM SUB_TAB_PRECO ORDER BY CD_REF";
        try {
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            j.setMaximum(rs.getFetchSize() + 1);
            while (rs.next()) {
                for (Product p : array) {
                    if (p.getCode().equals(rs.getString("CD_REF"))) {
                        p.setCost_value(rs.getDouble("VL_CUSTO"));
                        break;
                    }
                }
                j.setValue(j.getValue() + 1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactoryFirebird.closeConnection(con, stmt, rs);
        }
        return array;
    }

    public boolean removeAll() {
        con = ConnectionFactoryMySQL.getConnection();
        sql = "DELETE FROM product";
        try {
            stmt = con.prepareStatement(sql);
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            ConnectionFactoryMySQL.closeConnection(con, stmt);
        }
    }

    /*
    * Função otimizada para ter uma melhor performance na hora de inserir os 
    * registros no mysql fazendo tudo de uma só vez..
    */
    public boolean refreshAllProduct(List<Product> products, boolean provider, JProgressBar j) {
        con = ConnectionFactoryMySQL.getConnection();
        j.setMinimum(0);
        j.setMaximum(products.size());
        j.setValue(0);
        if (provider) {
            sql = "INSERT INTO product (code,description,sale,cost,un,amount,provider) VALUES";
        } else {
            sql = "INSERT INTO product (code,description,sale,cost,un,amount) VALUES";
        }
        try {
            String temp="";
            String concatenation;
            for (int x = 0; x < products.size(); x++) {
                concatenation ="";
                concatenation += "(";
                concatenation += products.get(x).getCode();
                concatenation += ",";
                concatenation += "'" + products.get(x).getDescription().replaceAll("'", " ") + "'";
                concatenation += ",";
                concatenation += products.get(x).getSale_value();
                concatenation += ",";
                concatenation += products.get(x).getCost_value();
                concatenation += ",";
                concatenation += "'" + products.get(x).getUnity() + "'";
                concatenation += ",";
                concatenation += products.get(x).getAmount_db();
                if (provider) {
                    concatenation += ",";
                    concatenation += products.get(x).getProvider();
                }
                concatenation += ")";
                if (x + 1 == products.size()) {
                    concatenation += ";";
                } else {
                    concatenation += ",";
                }
                temp += concatenation;
                j.setValue(j.getValue()+1);
            }
            sql += temp;
            stmt = con.prepareStatement(sql);
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            ConnectionFactoryMySQL.closeConnection(con, stmt);
        }
    }

    public Product getProduct(String text) {
        con = ConnectionFactoryMySQL.getConnection();
        sql = "SELECT * FROM product WHERE code = ?";
        Product p = null;
        try {
            stmt = con.prepareStatement(sql);
            stmt.setBigDecimal(1, BigDecimal.valueOf(Long.parseLong(text)));
            rs = stmt.executeQuery();
            rs.first();
            if (rs.first()){
                p = new Product();
                p.setCode(text);
                p.setDescription(rs.getString("description"));
                p.setAmount_db(rs.getDouble("amount"));
                p.setSale_value(rs.getDouble("sale"));
                p.setCost_value(rs.getDouble("cost"));
                p.setUnity(rs.getString("un"));
                p.setProvider(rs.getString("provider"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            ConnectionFactoryMySQL.closeConnection(con, stmt, rs);
        }
        return p;
    }
    
    public List<Product> findAll() {
        con = ConnectionFactoryMySQL.getConnection();
        sql = "SELECT * FROM product ORDER BY code";
        List<Product> products = new ArrayList<>();
        try {
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            while(rs.next()){
                Product p = new Product();
                p.setCode(rs.getString("code"));
                p.setDescription(rs.getString("description"));
                p.setAmount_db(rs.getDouble("amount"));
                p.setSale_value(rs.getDouble("sale"));
                p.setCost_value(rs.getDouble("cost"));
                p.setUnity(rs.getString("un"));
                p.setProvider(rs.getString("provider"));
                products.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            ConnectionFactoryMySQL.closeConnection(con, stmt, rs);
        }
        return products;
    }
    
    public boolean already(String cd){
        con = ConnectionFactoryMySQL.getConnection();
        sql = "SELECT * FROM type_use WHERE code = "+cd;
        try {
            stmt = con.prepareStatement(sql);
            rs=  stmt.executeQuery();
            return rs.first();
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            ConnectionFactoryMySQL.closeConnection(con, stmt, rs);
        }
        return false;
    }
    
    public boolean insert_type_use(String cd,String desc,double q,int type){
        con = ConnectionFactoryMySQL.getConnection();
        sql = "INSERT INTO type_use(code,description,";
        String temp = "";
        switch (type) {
            case CONSUMPTION:
                temp="consumption) VALUES (?,?,?)";
                break;
            case BAKERY:
                temp="bakery) VALUES (?,?,?)";
                break;
            case EXCHANGE:
                temp="exchange) VALUES (?,?,?)";
                break;
            case BREAK:
                temp="break) VALUES (?,?,?)";
                break;
            default:
                break;
        }
        sql+=temp;
        try {
            stmt = con.prepareStatement(sql);
            stmt.setBigDecimal(1, BigDecimal.valueOf(Long.parseLong(cd)));
            stmt.setString(2, desc);
            stmt.setDouble(3, q);
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }finally{
            ConnectionFactoryMySQL.closeConnection(con, stmt);
        }
    }
    
    public boolean update_type_use(String cd,double q,int type){
        con = ConnectionFactoryMySQL.getConnection();
        sql = "UPDATE type_use SET ";
        String temp = "";
        switch (type) {
            case CONSUMPTION:
                temp="consumption = consumption + ? WHERE code = ?";
                break;
            case BAKERY:
                temp="bakery = bakery + ? WHERE code = ?";
                break;
            case EXCHANGE:
                temp="exchange = exchange + ? WHERE code = ?";
                break;
            case BREAK:
                temp="break = break + ? WHERE code = ?";
                break;
            default:
                break;
        }
        sql+=temp;
        try {
            stmt = con.prepareStatement(sql);
            stmt.setDouble(1, q);
            stmt.setBigDecimal(2, BigDecimal.valueOf(Long.parseLong(cd)));
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }finally{
            ConnectionFactoryMySQL.closeConnection(con, stmt);
        }
    }
}
