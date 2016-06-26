package Herramientas;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.File;
import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author user1
 */
public class ParserGPX {
    //Herramientas para parseado
    private Namespace ns;
    private SAXBuilder parser = new SAXBuilder(); 
    
    //Datos de interés
    private double minlat,
                   minlon,
                   maxlat,
                   maxlon;
    
    private Vector<Double> latitudes,
                           longitudes;
    
    public ParserGPX(File file) {
        minlat = Double.POSITIVE_INFINITY;
        minlon = Double.POSITIVE_INFINITY;
        maxlat = Double.NEGATIVE_INFINITY;
        maxlon = Double.NEGATIVE_INFINITY;
        this.latitudes = new Vector<>();
        this.longitudes = new Vector<>();
        try {
            Document doc = parser.build(file);
            Element rootNode = doc.getRootElement();
            ns = rootNode.getNamespace();
            
            List<Element> tracks = rootNode.getChildren("trk", ns);
            for (Element track : tracks) {
                List<Element> segments = track.getChildren("trkseg", ns);
                for (Element segment: segments) {
                    List<Element> waypoints = segment.getChildren("trkpt", ns);
                    for (int i = 0; i < waypoints.size(); i++) {
                        Element pointXML = waypoints.get(i);
                        double latitud = 0.0;
                        double longitud = 0.0;
                        // double elevation = 0.0;
                        
                        try {
                            latitud = pointXML.getAttribute("lat").getDoubleValue();
                            longitud = pointXML.getAttribute("lon").getDoubleValue();
                            if(latitud > maxlat)
                                maxlat = latitud;
                            else if(latitud < minlat) 
                                minlat = latitud;
                            if(longitud > maxlon)
                                maxlon = longitud;
                            else if(longitud < minlon){
                                minlon = longitud;
                            }
                            this.latitudes.add(latitud);                           
                            this.longitudes.add(longitud);
                        } catch (DataConversionException e) {
                        }
                        /*
                        if (pointXML.getChild("ele", ns) != null) {
                                elevation = new Double(pointXML.getChildText("ele", ns));
                        }

                        if (pointXML.getChild("time", ns) != null) {
                                try {
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                                        Date time = dateFormat.parse(pointXML.getChildText("time", ns));			
                                } catch (ParseException e) {
                                }				
                        }*/
                    }
                }
            }
        } catch (JDOMException | IOException ex) {
            Logger.getLogger(ParserGPX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public JsonArray gpxToJson() {
        JsonArray path = new JsonArray();
        for (int i = 0; i < latitudes.size(); i++) {
            JsonObject coord = new JsonObject();
            coord.add("lat", new JsonPrimitive(this.latitudes.get(i)));
            coord.add("lng", new JsonPrimitive(this.longitudes.get(i)));
            path.add(coord);
        }
        return path;
    }
    
    public double getMinlat() {
        return minlat;
    }

    public double getMinlon() {
        return minlon;
    }

    public double getMaxlat() {
        return maxlat;
    }

    public double getMaxlon() {
        return maxlon;
    }

    public Vector<Double> getLatitudes() {
        return latitudes;
    }

    public Vector<Double> getLongitudes() {
        return longitudes;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //File archivoGPX = new File("huelva-extrema-2015.gpx");
        //File archivoGPX = new File("track.gpx");
        File archivoGPX = new File("transpirinenca-cap-de-creus-hondarribia-la-transpirenaica.gpx");
        ParserGPX GPXparser = new ParserGPX(archivoGPX);
        System.out.println(GPXparser.gpxToJson());
    }   
}