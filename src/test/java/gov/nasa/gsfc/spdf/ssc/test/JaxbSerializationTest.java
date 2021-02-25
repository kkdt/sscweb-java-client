package gov.nasa.gsfc.spdf.ssc.test;

import gov.nasa.gsfc.spdf.ssc.SSCExternalException_Exception;
import gov.nasa.gsfc.spdf.ssc.SatelliteDescription;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JaxbSerializationTest extends WsExampleUtil {
    public static List<SatelliteDescription> satellites = new ArrayList<>();

    @BeforeClass
    public static void beforeTest() throws SSCExternalException_Exception, JAXBException {
        satellites = ssc.getAllSatellites();
        assertTrue(satellites != null && satellites.size() > 0);
        System.out.println("Total satellites: " + satellites.size());
    }

    @Test
    public void jaxbMarshallTest() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance("gov.nasa.gsfc.spdf.ssc");

        SatelliteDescription description = satellites.stream()
            .findAny().orElse(null);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        JAXBElement<SatelliteDescription> jaxbElement =
            new JAXBElement<>( new QName("", "SatelliteDescription"),
                SatelliteDescription.class,
                description);

        StringWriter writer = new StringWriter();
        marshaller.marshal(jaxbElement, writer);
        String xml = writer.toString();
        assertNotNull(xml);
        System.out.print(xml);
    }
}
