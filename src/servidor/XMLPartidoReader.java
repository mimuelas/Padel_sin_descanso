package servidor;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import clase.Equipo;
import clase.Partido;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class XMLPartidoReader {

    public static List<Partido> leerPartidosDesdeXML(String filePath) {
        List<Partido> partidos = new ArrayList<>();
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("partido");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Element eElement = (Element) nList.item(temp);

                String fechaHora = eElement.getAttribute("fechaHora");
                String nivel = eElement.getAttribute("nivel");
                String pista = eElement.getAttribute("pista");
                String resultado = eElement.getElementsByTagName("resultado").item(0).getTextContent();

                // Leer equipos y jugadores
                NodeList equipos = eElement.getElementsByTagName("equipo");
                Equipo equipo1 = leerJugadores((Element) equipos.item(0));
                Equipo equipo2 = leerJugadores((Element) equipos.item(1));

                Partido partido = new Partido(fechaHora, nivel, pista, equipo1, equipo2, resultado);
                partidos.add(partido);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return partidos;
    }

    private static Equipo leerJugadores(Element equipo) {
        List<String> jugadores = new ArrayList<>();
        NodeList nList = equipo.getElementsByTagName("jugador");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            jugadores.add(nList.item(temp).getTextContent());
        }

        Equipo equipoS = new Equipo(jugadores.get(0), jugadores.get(1));
        
        return equipoS;
    }

}

