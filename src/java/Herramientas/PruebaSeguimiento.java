/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Herramientas;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Je¡ZZ¡
 */
public class PruebaSeguimiento {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost/seguimiento_trayectoria",
               user = "root",
               pass = "TFG_Seguimiento_Trayectoria";
        ParserGPX GPXparser = new ParserGPX(new File("huelva-extrema-2015.gpx"));
        Vector<Double> latitudes = GPXparser.getLatitudes();
        Vector<Double> longitudes = GPXparser.getLongitudes();
        try {    
            for (int i = 0; i < latitudes.size(); i++) {
                Connection con = DriverManager.getConnection(url, user, pass);
                PreparedStatement stmt = con.prepareStatement("INSERT INTO posicion VALUES (?, ?, CURDATE(), CURTIME(), ?, ?)");
                stmt.setString(1, "camboya");
                stmt.setString(2, "prh_19_666arq@hotmail.com");
                stmt.setDouble(3, latitudes.get(i));
                stmt.setDouble(4, longitudes.get(i));
                stmt.execute();
                con.close(); 
                Thread.sleep(5000);
            }
        } catch (SQLException | InterruptedException ex) {
            Logger.getLogger(PruebaSeguimiento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
