package com.TobyMellor.TrainRouteFinder.routes;

import com.TobyMellor.TrainRouteFinder.App;
import com.TobyMellor.TrainRouteFinder.Manager;
import com.TobyMellor.TrainRouteFinder.stations.StationManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.util.List;

/**
 * Manager containing Lists containing the <code>basicRoutes</code>
 * Used to perform operations on the whole collection of objects
 *
 * @author <a href="mailto:T.Mellor-16@student.lboro.ac.uk">Toby Mellor (B619693)</a>
 *
 * @version $Revision: 1.0.0 $, $Date: 2017/03/27 21:43:00 $
 */

public class BasicRouteManager extends Manager {
    private List<BasicRoute> basicRoutes;

    public BasicRouteManager(List<BasicRoute> basicRoutes) {
        setBasicRoutes(basicRoutes);
    }

    /**
     * Retrieves the whole collection of loaded <code>BasicRoute</code>s
     *
     * @return List<BasicRoute>
     */
    public List<BasicRoute> getBasicRoutes() {
        return basicRoutes;
    }

    /**
     * Sets the whole collection of <code>BasicRoute</code>s
     *
     * @param basicRoutes the collection of <code>BasicRoute</code>s
     *
     * @return List<BasicRoute>
     */
    public void setBasicRoutes(final List<BasicRoute> basicRoutes) {
        this.basicRoutes = basicRoutes;
    }

    /**
     * Retrieves the <code>BasicRoute</code> where the involved <code>DestinationStation</code>s
     * have the corresponding names <code>departingStationName</code> and <code>destinationStationName</code>
     *
     * Returns null if the <code>BasicRoute</code> cannot be found
     *
     * @param departingStationName the name of the station the train departs from
     * @param destinationStationName the name of the station the train arrives at
     *
     * @return BasicRoute
     */
    public BasicRoute getBasicRoute(final String departingStationName, final String destinationStationName) {
        final StationManager stationManager = App.getStationManager();

        for (BasicRoute basicRoute : getBasicRoutes()) {
            if (stationManager.getDestinationStation(basicRoute.getDepartingStationId()).getName().equals(departingStationName) && stationManager.getDestinationStation(basicRoute.getDestinationStationId()).getName().equals(destinationStationName)) {
                return basicRoute;
            }
        }

        return null;
    }

    /**
     * Retrieves the <code>BasicRoute</code> where the <code>id</code> is <code>basicRouteId</code>
     *
     * Returns null if the <code>BasicRoute</code> cannot be found
     *
     * @param basicRouteId the ID of the BasicRoute to return
     *
     * @return List<BasicRoute>
     */
    public BasicRoute getBasicRoute(final String basicRouteId) {
        final List<BasicRoute> basicRoutes = getBasicRoutes();

        for (BasicRoute basicRoute : basicRoutes) {
            if (basicRoute.getId().equals(basicRouteId)) {
                return basicRoute;
            }
        }

        return null;
    }

    /**
     * Converts the collection of <code>BasicRoute</code>s
     * into XML and writes it to the corresponding XML file
     *
     * Any errors are thrown and caught at the lower level
     */
    public void save() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element rootElement = document.createElement("basicRoutes");

            for (BasicRoute basicRoute : getBasicRoutes()) {
                Element basicRouteElement = document.createElement("basicRoute");
                basicRouteElement.setAttribute("id", basicRoute.getId());

                basicRouteElement.appendChild(createXMLElement(document, "departingStationId", basicRoute.getDepartingStationId()));
                basicRouteElement.appendChild(createXMLElement(document, "destinationStationId", basicRoute.getDestinationStationId()));
                basicRouteElement.appendChild(createXMLElement(document, "singlePrice", String.valueOf(basicRoute.getSinglePrice())));
                basicRouteElement.appendChild(createXMLElement(document, "returnPrice", String.valueOf(basicRoute.getReturnPrice())));
                basicRouteElement.appendChild(createXMLElement(document, "duration", String.valueOf(basicRoute.getDuration())));

                rootElement.appendChild(basicRouteElement);
            }

            document.appendChild(rootElement);

            writeXML(document, new File("resources/xml/basic_routes.xml"));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}
