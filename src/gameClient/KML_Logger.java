package gameClient;

import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 * this class create 24 kml files that can be loaded to google earth and view the game
 * in a specific level
 * this is only for auto game.
 */


class KML_Logger {

    private int level_game;
    private StringBuffer s;

    /**
     * constructor
     * @param level is the level in the game
     */
    KML_Logger(int level) {
        this.level_game = level;
        s = new StringBuffer();
        //KML_Play();
        start_KML();
    }

    /**
     * this function initialize kml to the game
     */
    private void start_KML(){
        s.append(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
                        "<kml xmlns=\"http://earth.google.com/kml/2.2\">\r\n" +
                        "  <Document>\r\n" +
                        "    <name>" + "Game stage :"+ level_game + "</name>" +"\r\n"
        );
        kmlN();
    }

    /**
     * This function initialize the node to KML
     */
    private void kmlN(){
        s.append(" <Style id=\"node\">\r\n" +
                "      <IconStyle>\r\n" +
                "        <Icon>\r\n" +
                "          <href>http://maps.google.com/mapfiles/kml/pal3/icon35.png</href>\r\n" +
                "        </Icon>\r\n" +
                "        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
                "      </IconStyle>\r\n" +
                "    </Style>"
        );

        kmlF();
    }

    /**
     * this function initialize the fruits to KML
     */
    private void kmlF(){
        s.append(
                " <Style id=\"fruit_-1\">\r\n" +
                        "      <IconStyle>\r\n" +
                        "        <Icon>\r\n" +
                        "          <href>http://maps.google.com/mapfiles/kml/paddle/purple-stars.png</href>\r\n" +
                        "        </Icon>\r\n" +
                        "        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
                        "      </IconStyle>\r\n" +
                        "    </Style>" +
                        " <Style id=\"fruit_1\">\r\n" +
                        "      <IconStyle>\r\n" +
                        "        <Icon>\r\n" +
                        "          <href>http://maps.google.com/mapfiles/kml/paddle/red-stars.png</href>\r\n" +
                        "        </Icon>\r\n" +
                        "        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
                        "      </IconStyle>\r\n" +
                        "    </Style>"
        );

        kmlR();
    }

    /**
     * this function initialise the Robots icon to KML
     */
    private void kmlR(){
        s.append(
                " <Style id=\"robot\">\r\n" +
                        "      <IconStyle>\r\n" +
                        "        <Icon>\r\n" +
                        "          <href>http://maps.google.com/mapfiles/kml/shapes/motorcycling.png></href>\r\n" +
                        "        </Icon>\r\n" +
                        "        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
                        "      </IconStyle>\r\n" +
                        "    </Style>"
        );
    }

    /**
     * this function is used in "paint"
     * after painting each element
     * the function enters the kml the location of each element
     * @param id
     * @param location
     */
    void placeMark(String id, String location){
        LocalDateTime Present_time = LocalDateTime.now();
        s.append(
                "    <Placemark>\r\n" +
                        "      <TimeStamp>\r\n" +
                        "        <when>" + Present_time+ "</when>\r\n" +
                        "      </TimeStamp>\r\n" +
                        "      <styleUrl>#" + id + "</styleUrl>\r\n" +
                        "      <Point>\r\n" +
                        "        <coordinates>" + location + "</coordinates>\r\n" +
                        "      </Point>\r\n" +
                        "    </Placemark>\r\n"
        );
    }


    void kmlPause(){
        s.append("  \r\n</Document>\r\n" +
                "</kml>");
        saveKML();
    }


    private void saveKML(){
        try {
            File file=new File("data/"+ level_game +".kml");
            PrintWriter pw=new PrintWriter(file);
            pw.write(s.toString());
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}