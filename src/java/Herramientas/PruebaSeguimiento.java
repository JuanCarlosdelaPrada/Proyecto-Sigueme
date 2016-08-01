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
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Je¡ZZ¡
 */
public class PruebaSeguimiento implements Runnable { 
    private final String prueba_id,
                         usuario_id;
    private final Random nAleatorios;
    
    public PruebaSeguimiento(String prueba_id, String usuario_id, long semilla) {
        this.prueba_id = prueba_id;
        this.usuario_id = usuario_id;
        this.nAleatorios = new Random(semilla);
    }
    
    @Override
    public void run() {
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
                stmt.setString(1, this.prueba_id);
                stmt.setString(2, this.usuario_id);
                stmt.setDouble(3, latitudes.get(i) + this.nAleatorios.nextDouble() * 0.001 - 0.0005);
                stmt.setDouble(4, longitudes.get(i) + this.nAleatorios.nextDouble() * 0.001 - 0.0005);
                stmt.execute();
                con.close(); 
                Thread.sleep(5000);
            }
        } catch (SQLException | InterruptedException ex) {
            Logger.getLogger(PruebaSeguimiento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        ExecutorService hilos = Executors.newFixedThreadPool(5);
        
        Random nAleatorios = new Random(System.currentTimeMillis());
        String[] idUsuarios = {"prh_19_666arq@hotmail.com", "a@g.es", "b@g.es", "c@g.es", "d@g.es"};
        
        for (int i = 0; i < 5; i++) {
            Runnable persona = new PruebaSeguimiento("camboya", idUsuarios[i], nAleatorios.nextLong());
            hilos.execute(persona);
            Thread.sleep(5000);
        }
        
        hilos.shutdown();
        while (!hilos.isTerminated()) {
        }
        
        System.out.println("FIN DE LA CARRERA");
    }
}
