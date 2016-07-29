/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Herramientas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Je¡ZZ¡
 */
public class PruebaSeguimiento {
    public static void main(String[] args) {
        String db = "seguimiento_trayectorio",
               url = "jdbc:mysql://localhost:3306/" + db,
               user = "root",
               pass = "TFG_Seguimiento_Trayectoria";
        try {    
            Connection con = DriverManager.getConnection(url, user, pass);
            
        } catch (SQLException ex) {
            Logger.getLogger(PruebaSeguimiento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
