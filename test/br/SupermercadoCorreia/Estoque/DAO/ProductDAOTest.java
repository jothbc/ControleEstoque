/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.SupermercadoCorreia.Estoque.DAO;

import br.SupermercadoCorreia.Estoque.Bean.Product;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author User
 */
public class ProductDAOTest {
    
    public ProductDAOTest() {
    }

    @Test
    public void testSomeMethod() {
        //new ProductDAO().refresh_now();
        System.out.println(new ProductDAO().refresh_is_needed(ProductDAO.HOUR_MILES));
        
    }
    
}
