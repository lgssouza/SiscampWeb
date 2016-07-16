
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.utilitarios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Luiz Guilherme Souza
 */
public class ConnectionUtil {
    public Connection getConnection() throws ClassNotFoundException{
        try{
            //dar o nome da classe do driver
            Class.forName("com.mysql.jdbc.Driver");
            //retornar a conexão com o banco aberta
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/siscamp","root","");
                        
        }
        //caso houver algum erro , retorna o mesmo.
        catch (SQLException e){
            throw new RuntimeException(e);
        }
        
    }
    
}
