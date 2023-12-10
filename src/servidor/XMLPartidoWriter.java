package servidor;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;

public class XMLPartidoWriter {

	//añadir un partido a la hoja XML
    public static void addPartido(String filePath, String fechaHora, String nivel, String pista,String jugador1) {
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            // Crear nuevo partido
            Element nuevoPartido = doc.createElement("partido");
            nuevoPartido.setAttribute("fechaHora", fechaHora);
            nuevoPartido.setAttribute("nivael", nivel);
            nuevoPartido.setAttribute("pista", pista);

            // Crear equipos y jugadores
            Element equipos = doc.createElement("equipos");
            Element equipo1 = doc.createElement("equipo");
            equipo1.appendChild(crearJugadorElement(doc, jugador1));
            equipo1.appendChild(crearJugadorElement(doc, SimpleServer.vacio));
            Element equipo2 = doc.createElement("equipo");
            equipo2.appendChild(crearJugadorElement(doc, SimpleServer.vacio));
            equipo2.appendChild(crearJugadorElement(doc, SimpleServer.vacio));
            equipos.appendChild(equipo1);
            equipos.appendChild(equipo2);

            // Añadir resultado
            Element resultadoElement = doc.createElement("resultado");
            resultadoElement.appendChild(doc.createTextNode(SimpleServer.vacio));

            // Construir estructura
            nuevoPartido.appendChild(equipos);
            nuevoPartido.appendChild(resultadoElement);

            // Añadir al documento
            Node raiz = doc.getElementsByTagName("partidos").item(0);
            raiz.appendChild(nuevoPartido);

            // Guardar cambios en el archivo XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            
            // Configuración para una salida bien formateada
            //transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            //transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); // Configura la cantidad de espacios para la sangría

            
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    // Metodo que asigna resultado al partido (vale "-"-) (solo se ejecuta una vez por partido)
    public static void modificarResultadoPartido(String filePath, String fechaHora, String pista, String nuevoResultado) {
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList partidos = doc.getElementsByTagName("partido");
            for (int i = 0; i < partidos.getLength(); i++) {
                Node partido = partidos.item(i);
                if (partido.getNodeType() == Node.ELEMENT_NODE) {
                    Element elementoPartido = (Element) partido;
                    if (elementoPartido.getAttribute("fechaHora").equals(fechaHora) && 
                        elementoPartido.getAttribute("pista").equals(pista)) {
                        // Modificar resultado
                        Node resultadoNode = elementoPartido.getElementsByTagName("resultado").item(0);
                        resultadoNode.setTextContent(nuevoResultado);
                        break;
                    }
                }
            }

            // Guardar cambios en el archivo XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    // Método para reemplazar un jugador con "-" por un nombre específico
    public static void reemplazarJugador(String filePath, String fechaHora, String pista, String nuevoNombre) {
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList partidos = doc.getElementsByTagName("partido");
            for (int i = 0; i < partidos.getLength(); i++) {
                Node partido = partidos.item(i);
                if (partido.getNodeType() == Node.ELEMENT_NODE) {
                    Element elementoPartido = (Element) partido;
                    if (elementoPartido.getAttribute("fechaHora").equals(fechaHora) && 
                        elementoPartido.getAttribute("pista").equals(pista)) {
                        // Reemplazar jugador
                        if (reemplazarJugadorConGuion(elementoPartido, nuevoNombre)) {
                            break;
                        }
                    }
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //este metodo es usado por reemplazarJugador
    private static boolean reemplazarJugadorConGuion(Element partido, String nuevoNombre) {
        NodeList equipos = partido.getElementsByTagName("equipo");
        for (int i = 0; i < equipos.getLength(); i++) {
            NodeList jugadores = ((Element) equipos.item(i)).getElementsByTagName("jugador");
            for (int j = 0; j < jugadores.getLength(); j++) {
                Node jugador = jugadores.item(j);
                if (jugador.getTextContent().equals("-")) {
                    jugador.setTextContent(nuevoNombre);
                    return true; // Jugador reemplazado, salir del método
                }
            }
        }
        return false; // No se encontró jugador con "-", no deberiamos llegar aqui nunca
    }

    private static Node crearJugadorElement(Document doc, String nombre) {
        Element jugador = doc.createElement("jugador");
        jugador.appendChild(doc.createTextNode(nombre));
        return jugador;
    }
}
