/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.SupermercadoCorreia.Estoque.DAO;

import JDBC.ConnectionFactoryFirebird;
import JDBC.ConnectionFactoryMySQL;
import br.SupermercadoCorreia.Estoque.Bean.Product;
import funcoes.CDate;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

/**
 *
 * @author User
 */
public class ProductDAO {

    private Connection con = null;
    private PreparedStatement stmt = null;
    private ResultSet rs = null;
    private String sql = null;

    public final static int CONSUMPTION = 1;
    public final static int CONSUMO = CONSUMPTION;
    public final static int BAKERY = 2;
    public final static int PADARIA = BAKERY;
    public final static int EXCHANGE = 3;
    public final static int TROCA = EXCHANGE;
    public final static int BREAK = 4;
    public final static int QUEBRA = BREAK;
    public final static String CONSUMPTION_TABLE = "consumption";
    public final static String BAKERY_TABLE = "bakery";
    public final static String EXCHANGE_TABLE = "exchange";
    public final static String BREAK_TABLE = "break";

    public List<Product> getAllFB(JProgressBar j) throws Exception {
        j.setMinimum(0);
        j.setValue(0);
        con = ConnectionFactoryFirebird.getConnection();
        List<Product> array = new ArrayList<>();
        //sql = "SELECT PRODUTO.CD_REF,CD_GRUPO,DS_PROD,AB_UNIDADE,QT_PROD FROM PRODUTO, ESTOQUE WHERE PRODUTO.CD_REF = ESTOQUE.CD_REF AND PRODUTO.FG_ATIVO = 1 ORDER BY CD_REF";
        //buscar inativos também
        sql = "SELECT PRODUTO.CD_REF,CD_GRUPO,DS_PROD,AB_UNIDADE,QT_PROD FROM PRODUTO, ESTOQUE WHERE PRODUTO.CD_REF = ESTOQUE.CD_REF ORDER BY CD_REF";
        try {
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            j.setMaximum(rs.getFetchSize() + 1);
            while (rs.next()) {
                Product p = new Product();
                p.setCode(rs.getString("CD_REF"));
                p.setGroup_db(rs.getInt("CD_GRUPO"));
                p.setDescription(rs.getString("DS_PROD").replaceAll("'", " "));
                p.setUnity(rs.getString("AB_UNIDADE"));
                p.setAmount_db(rs.getDouble("QT_PROD"));
                array.add(p);
                j.setValue(j.getValue() + 1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception("Não foi possível obter os protudos da base de dados da IBS.");
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

    public List<Product> getAllFB_Provider(List<Product> array, JProgressBar j) {
        j.setMinimum(0);
        j.setValue(0);
        sql = "SELECT PRODUTO_FORNECEDOR.CD_REF, PRODUTO_FORNECEDOR.CD_FORNECEDOR, PRODUTO_FORNECEDOR.DT_ALT, PESSOA.NM_PESSOA FROM PESSOA, PRODUTO_FORNECEDOR WHERE PESSOA.CD_PESSOA = PRODUTO_FORNECEDOR.CD_FORNECEDOR ORDER BY PRODUTO_FORNECEDOR.CD_REF";
        con = ConnectionFactoryFirebird.getConnection();
        try {
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            String codigo_atual = null, fornecedor_atual = null;
            Date data_atual = null;
            j.setMaximum(rs.getFetchSize() + 1);
            while (rs.next()) {
                if (codigo_atual == null) {
                    fornecedor_atual = rs.getString("NM_PESSOA");
                    codigo_atual = rs.getString("CD_REF");
                    data_atual = rs.getDate("DT_ALT");
                } else if (codigo_atual.equals(rs.getString("CD_REF"))) {
                    if (data_atual.before(rs.getDate("DT_ALT"))) {
                        data_atual = rs.getDate("DT_ALT");
                        fornecedor_atual = rs.getString("NM_PESSOA");
                    }
                } else if (!codigo_atual.equals(rs.getString("CD_REF"))) {
                    for (Product p : array) {
                        if (p.getCode().equals(codigo_atual)) {
                            p.setProvider(fornecedor_atual.replaceAll("'", " "));
                            break;
                        }
                    }
                    fornecedor_atual = rs.getString("NM_PESSOA");
                    codigo_atual = rs.getString("CD_REF");
                    data_atual = rs.getDate("DT_ALT");
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
        sql = "TRUNCATE TABLE product";
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
    public boolean refreshAllProduct(List<Product> products, boolean provider, JProgressBar progress) {
        con = ConnectionFactoryMySQL.getConnection();
        progress.setMinimum(0);
        progress.setMaximum(products.size());
        progress.setValue(0);
        if (provider) {
            sql = "INSERT INTO product (code,description,sale,cost,un,amount,ID_GROUP_FIREBIRD,provider) VALUES";
        } else {
            sql = "INSERT INTO product (code,description,sale,cost,un,amount,ID_GROUP_FIREBIRD) VALUES";
        }
        try {
            String temp = "";
            String concatenation;
            for (int x = 0; x < products.size(); x++) {
                concatenation = "(";
                concatenation += products.get(x).getCode();
                concatenation += ",";
                concatenation += "'" + products.get(x).getDescription() + "'"; //descricao com ' acaba estragando o insert, entao remove-se isso
                concatenation += ",";
                concatenation += products.get(x).getSale_value();
                concatenation += ",";
                concatenation += products.get(x).getCost_value();
                concatenation += ",";
                concatenation += "'" + products.get(x).getUnity() + "'";
                concatenation += ",";
                concatenation += products.get(x).getAmount_db();
                concatenation += ",";
                concatenation += products.get(x).getGroup_db();
                if (provider) {
                    concatenation += ",";
                    if (products.get(x).getProvider() != null) {
                        concatenation += "'" + products.get(x).getProvider() + "'";
                    } else {
                        concatenation += "'não encontrado'";
                    }
                }
                concatenation += ")";
                if (x + 1 == products.size()) {
                    concatenation += ";";
                } else {
                    concatenation += ",";
                }
                temp += concatenation;
                progress.setValue(progress.getValue() + 1);
            }
            sql += temp;
            stmt = con.prepareStatement(sql);
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Erro ao tentar atualizar Produtos na base de dados.\nTentando novamente...");
            return refreshAllProductAux(products, provider, progress);
        } finally {
            ConnectionFactoryMySQL.closeConnection(con, stmt);
        }
    }

    private boolean refreshAllProductAux(List<Product> products, boolean provider, JProgressBar progress) {
        con = ConnectionFactoryMySQL.getConnection();
        progress.setMinimum(0);
        progress.setMaximum(products.size());
        progress.setValue(0);
        if (provider) {
            sql = "INSERT INTO product (code,description,sale,cost,un,amount,ID_GROUP_FIREBIRD,provider) VALUES (?,?,?,?,?,?,?,?)";
        } else {
            sql = "INSERT INTO product (code,description,sale,cost,un,amount,ID_GROUP_FIREBIRD) VALUES (?,?,?,?,?,?,?)";
        }
        try {
            stmt = con.prepareStatement(sql);
            for (Product p : products) {
                stmt.setBigDecimal(1, BigDecimal.valueOf(Long.parseLong(p.getCode())));
                stmt.setString(2, p.getDescription());
                stmt.setDouble(3, p.getSale_value());
                stmt.setDouble(4, p.getCost_value());
                stmt.setString(5, p.getUnity());
                stmt.setDouble(6, p.getAmount_db());
                stmt.setInt(7, p.getGroup_db());
                if (provider) {
                    stmt.setString(8, p.getProvider());
                }
                stmt.execute();
                progress.setValue(progress.getValue() + 1);
            }
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Segunda tentativa de inserção no banco de dados falhado.");
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
            if (rs.first()) {
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
        } finally {
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
            while (rs.next()) {
                Product p = new Product();
                p.setCode(rs.getString("code"));
                p.setDescription(rs.getString("description"));
                p.setAmount_db(rs.getDouble("amount"));
                p.setSale_value(rs.getDouble("sale"));
                p.setCost_value(rs.getDouble("cost"));
                p.setUnity(rs.getString("un"));
                p.setProvider(rs.getString("provider"));
                p.setGroup_db(rs.getInt("ID_GROUP_FIREBIRD"));
                products.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactoryMySQL.closeConnection(con, stmt, rs);
        }
        return products;
    }

    public boolean already(String cd) {
        con = ConnectionFactoryMySQL.getConnection();
        sql = "SELECT * FROM type_use WHERE code = " + cd;
        try {
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            return rs.first();
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactoryMySQL.closeConnection(con, stmt, rs);
        }
        return false;
    }

    public boolean insert_type_use(String cd, String desc, double amount, String column) {
        con = ConnectionFactoryMySQL.getConnection();
        sql = "INSERT INTO type_use(code,description," + column + ") VALUES (?,?,?)";
        try {
            stmt = con.prepareStatement(sql);
            stmt.setBigDecimal(1, BigDecimal.valueOf(Long.parseLong(cd)));
            stmt.setString(2, desc);
            stmt.setDouble(3, amount);
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            ConnectionFactoryMySQL.closeConnection(con, stmt);
        }
    }

    public boolean update_type_use(String code, double amount, String column) {
        con = ConnectionFactoryMySQL.getConnection();
        sql = "UPDATE type_use SET " + column + "=" + column + " + ? WHERE code = ?";
        try {
            stmt = con.prepareStatement(sql);
            stmt.setDouble(1, amount);
            stmt.setBigDecimal(2, BigDecimal.valueOf(Long.parseLong(code)));
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            ConnectionFactoryMySQL.closeConnection(con, stmt);
        }
    }

    public List<Product> findAll_Type_Use(String table) {
        List<Product> product = new ArrayList<>();
        sql = "SELECT * FROM type_use WHERE " + table + " !=0";
        con = ConnectionFactoryMySQL.getConnection();
        try {
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setCode(rs.getString("code"));
                p.setDescription(rs.getString("description"));
                p.setAmount(rs.getDouble(table));
                product.add(p);
            }
            if (!product.isEmpty()) {
                for (Product p : product) {
                    sql = "SELECT code,amount,cost,un FROM product WHERE code = ?";
                    stmt = con.prepareStatement(sql);
                    stmt.setBigDecimal(1, BigDecimal.valueOf(Long.parseLong(p.getCode())));
                    rs = stmt.executeQuery();
                    rs.first();
                    p.setAmount_db(rs.getDouble("amount"));
                    p.setCost_value(rs.getDouble("cost"));
                    p.setUnity(rs.getString("un"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactoryMySQL.closeConnection(con, stmt, rs);
        }
        return product;
    }

    public boolean setAmount_type_use(long code, double amount, String column) {
        con = ConnectionFactoryMySQL.getConnection();
        sql = "UPDATE type_use SET " + column + " = ? WHERE code = ?";
        try {
            stmt = con.prepareStatement(sql);
            stmt.setDouble(1, amount);
            stmt.setBigDecimal(2, BigDecimal.valueOf(code));
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            ConnectionFactoryMySQL.closeConnection(con, stmt);
        }
    }

    public void insert_log_type_use(String usuario, String code, String description, double amount, String column) {
        sql = "INSERT INTO log_type_use(user,code,description,amount,local,hour) VALUES (?,?,?,?,?,?)";
        con = ConnectionFactoryMySQL.getConnection();
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, usuario);
            stmt.setBigDecimal(2, BigDecimal.valueOf(Long.parseLong(code)));
            stmt.setString(3, description);
            stmt.setDouble(4, amount);
            stmt.setString(5, column);
            stmt.setString(6, (CDate.getHoraAtualPTBR() + " " + CDate.getHojePTBR().replaceAll("/", "-")));
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex);
        } finally {
            ConnectionFactoryMySQL.closeConnection(con, stmt);
        }
    }
}
