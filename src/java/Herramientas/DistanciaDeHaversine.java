/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Herramientas;

import java.util.Vector;

/**
 *
 * @author Je¡ZZ¡
 */
public abstract class DistanciaDeHaversine {
    private static final double radioTierra = 6371;
    
    public static double getDistancia(Vector<Double> latitudes, Vector<Double> longitudes) {
        double distancia = 0;
        if((latitudes.size() > 1 && longitudes.size() > 1) && (latitudes.size() == longitudes.size()))
            for(int i = 0; i < (latitudes.size() - 1); i++) {
                double distLatitud = aRadianes(latitudes.get(i + 1) - latitudes.get(i)), 
                       distLongitud  = aRadianes(longitudes.get(i + 1) - longitudes.get(i));
                double a = Math.sin(distLatitud / 2) * Math.sin(distLatitud / 2) + 
                           Math.cos(aRadianes(latitudes.get(i))) * Math.cos(aRadianes(latitudes.get(i + 1))) * 
                           Math.sin(distLongitud / 2) * Math.sin(distLongitud / 2);
                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
                distancia += radioTierra * c;
            }
        return distancia;
    }
    
    private static double aRadianes(double valor) {
        return valor * Math.PI / 180;
    }
}
