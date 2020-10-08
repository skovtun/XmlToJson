package XmlToJson.Converter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringJoiner;

public class XmlToJsonConverter
{
    public static Map<String, String> STATES;

    static
    {
        STATES = new HashMap<>();
        STATES.put ("Alabama", "AL");
        STATES.put ("Alaska", "AK");
        STATES.put ("Alberta", "AB");
        STATES.put ("American Samoa", "AS");
        STATES.put ("Arizona", "AZ");
        STATES.put ("Arkansas", "AR");
        STATES.put ("Armed Forces (AE)", "AE");
        STATES.put ("Armed Forces Americas", "AA");
        STATES.put ("Armed Forces Pacific", "AP");
        STATES.put ("British Columbia", "BC");
        STATES.put ("California", "CA");
        STATES.put ("Colorado", "CO");
        STATES.put ("Connecticut", "CT");
        STATES.put ("Delaware", "DE");
        STATES.put ("District Of Columbia", "DC");
        STATES.put ("Florida", "FL");
        STATES.put ("Georgia", "GA");
        STATES.put ("Guam", "GU");
        STATES.put ("Hawaii", "HI");
        STATES.put ("Idaho", "ID");
        STATES.put ("Illinois", "IL");
        STATES.put ("Indiana", "IN");
        STATES.put ("Iowa", "IA");
        STATES.put ("Kansas", "KS");
        STATES.put ("Kentucky", "KY");
        STATES.put ("Louisiana", "LA");
        STATES.put ("Maine", "ME");
        STATES.put ("Manitoba", "MB");
        STATES.put ("Maryland", "MD");
        STATES.put ("Massachusetts", "MA");
        STATES.put ("Michigan", "MI");
        STATES.put ("Minnesota", "MN");
        STATES.put ("Mississippi", "MS");
        STATES.put ("Missouri", "MO");
        STATES.put ("Montana", "MT");
        STATES.put ("Nebraska", "NE");
        STATES.put ("Nevada", "NV");
        STATES.put ("New Brunswick", "NB");
        STATES.put ("New Hampshire", "NH");
        STATES.put ("New Jersey", "NJ");
        STATES.put ("New Mexico", "NM");
        STATES.put ("New York", "NY");
        STATES.put ("Newfoundland", "NF");
        STATES.put ("North Carolina", "NC");
        STATES.put ("North Dakota", "ND");
        STATES.put ("Northwest Territories", "NT");
        STATES.put ("Nova Scotia", "NS");
        STATES.put ("Nunavut", "NU");
        STATES.put ("Ohio", "OH");
        STATES.put ("Oklahoma", "OK");
        STATES.put ("Ontario", "ON");
        STATES.put ("Oregon", "OR");
        STATES.put ("Pennsylvania", "PA");
        STATES.put ("Prince Edward Island", "PE");
        STATES.put ("Puerto Rico", "PR");
        STATES.put ("Quebec", "QC");
        STATES.put ("Rhode Island", "RI");
        STATES.put ("Saskatchewan", "SK");
        STATES.put ("South Carolina", "SC");
        STATES.put ("South Dakota", "SD");
        STATES.put ("Tennessee", "TN");
        STATES.put ("Texas", "TX");
        STATES.put ("Utah", "UT");
        STATES.put ("Vermont", "VT");
        STATES.put ("Virgin Islands", "VI");
        STATES.put ("Virginia", "VA");
        STATES.put ("Washington", "WA");
        STATES.put ("West Virginia", "WV");
        STATES.put ("Wisconsin", "WI");
        STATES.put ("Wyoming", "WY");
        STATES.put ("Yukon Territory", "YT");
    }

    public static String convert (String data) throws ParserConfigurationException, IOException, SAXException
    {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance ();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder ();
        Document doc = dBuilder.parse (new InputSource(new StringReader(data)));

        doc.getDocumentElement ().normalize ();

        StringJoiner result = new StringJoiner ("\n");
        result.add ("[");

        NodeList patients = doc.getElementsByTagName ("patient");
        for (int i = 0; i < patients.getLength (); i++)
        {
            Node node = patients.item (i);
            if (node.getNodeType () == Node.ELEMENT_NODE)
            {
                result.add ("       {");
                Element patient = (Element) node;
                NodeList child = patient.getChildNodes ();
                for (int j = 0; j < child.getLength (); j++)
                {
                    Node pnode = child.item (j);
                    if (pnode.getNodeType () == Node.ELEMENT_NODE)
                    {
                        Element field = (Element) pnode;
                        String fieldName = field.getTagName ();
                        String fieldValue = field.getTextContent ();

                        switch (fieldName)
                        {
                            case "id":
                                fieldName = "patientid";
                                break;

                            case "dateOfBirth":
                                fieldName = "age";
                                fieldValue = "" + _convertToAge (fieldValue);
                                break;

                            case "gender":
                                fieldName = "sex";
                                fieldValue = ((fieldValue.equals ("m")) ? "\"male\"" : "\"female\"");
                                break;

                            case "state":
                                fieldValue = "\"" + STATES.get (fieldValue) + "\"";
                                break;

                            case "name":
                                fieldValue = "\"" + fieldValue + "\"";
                                break;
                        }

                        result.add ("              \"" + fieldName + "\":" + fieldValue + ",");
                    }
                }
                result.add ("       },");
            }
        }

        result.add ("]");

        return result.toString ();
    }

    public static String convert (File xmlFile) throws IOException, SAXException, ParserConfigurationException
    {
        Scanner scanner = new Scanner (xmlFile);
        String data = scanner.useDelimiter ("\\A").next ();
        scanner.close ();

        return convert (data);
    }

    private static int _convertToAge (String date)
    {
        return Period.between (LocalDate.parse (date, DateTimeFormatter.ofPattern ("MM/d/yyyy")), LocalDate.now ()).getYears ();
    }
}