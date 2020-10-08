package XmlToJson.Converter;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class Main
{

    public static void main (String[] args)
    {
        try
        {
            System.out.println (XmlToJsonConverter.convert (new File("patients.xml")));
        } catch (ParserConfigurationException e)
        {
            e.printStackTrace ();
        } catch (IOException e)
        {
            e.printStackTrace ();
        } catch (SAXException e)
        {
            e.printStackTrace ();
        }
    }
}
