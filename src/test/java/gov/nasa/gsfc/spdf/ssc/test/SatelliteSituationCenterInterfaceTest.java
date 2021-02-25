/**
 * MIT License
 * Copyright (c) 2017 kkdt
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package gov.nasa.gsfc.spdf.ssc.test;

import gov.nasa.gsfc.spdf.ssc.CoordinateSystem;
import gov.nasa.gsfc.spdf.ssc.DataResult;
import gov.nasa.gsfc.spdf.ssc.FileResult;
import gov.nasa.gsfc.spdf.ssc.KmlRequest;
import gov.nasa.gsfc.spdf.ssc.ResultStatusCode;
import gov.nasa.gsfc.spdf.ssc.SSCExternalException_Exception;
import gov.nasa.gsfc.spdf.ssc.SatelliteDescription;
import gov.nasa.gsfc.spdf.ssc.SatelliteSpecification;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * <p>
 * Unit test for Satellite Situation Center interface specifications, mainly used
 * to inspect what the data looks like coming from the web service.
 * </p>
 * 
 * @author thinh
 *
 */
public class SatelliteSituationCenterInterfaceTest extends WsExampleUtil {
   @Test
   public void testGetAllSatellites() throws SSCExternalException_Exception {
      List<SatelliteDescription> activeSatellites = new ArrayList<>();
      List<SatelliteDescription> inactiveSatellites = new ArrayList<>();
      List<SatelliteDescription> satellites = ssc.getAllSatellites();
      
      for(SatelliteDescription s : satellites) {
         Calendar endTime = s.getEndTime().toGregorianCalendar();
         if(now.before(endTime)) {
            activeSatellites.add(s);
         } else {
            inactiveSatellites.add(s);
         }
         
      }
      
      System.out.println("Total satellites: " + satellites.size());
      System.out.println("Active  : " + activeSatellites.size());
      System.out.println("Inactive: " + inactiveSatellites.size());
      
      assertTrue(activeSatellites.size() > 0);
      assertTrue(inactiveSatellites.size() > 0);
   }
   
   @Test
   public void testGetKmlRequest() throws Exception {
      Calendar now = Calendar.getInstance(UTC_TIME_ZONE);
      Calendar endDate = (Calendar) now.clone();
      endDate.add(Calendar.HOUR, 3);
      
      KmlRequest request = getKmlRequest(now, endDate);
      FileResult result = ssc.getKmlFiles(request);
      
      System.out.println("Status: " + result.getStatusCode());
      System.out.println("Subcode: " + result.getStatusSubCode());
      System.out.println("Texts: " + result.getStatusText());
      System.out.println("URLs: " + result.getUrls());
      
      assertTrue("Expects request to fail because of empty satellite list", 
         result != null && result.getStatusCode() == ResultStatusCode.ERROR);
      
      SatelliteSpecification s = new SatelliteSpecification();
      s.setId("iss");
      s.setResolutionFactor(1);
      request.getSatellites().add(s);
      
      result = ssc.getKmlFiles(request);
      
      System.out.println("Status: " + result.getStatusCode());
      System.out.println("Subcode: " + result.getStatusSubCode());
      System.out.println("Texts: " + result.getStatusText());
      System.out.println("URLs: " + result.getUrls());
      
      assertTrue("Expects request to be success", result != null && result.getStatusCode() == ResultStatusCode.SUCCESS);
   }
   
   /**
    * <p>
    * Query SSCWeb for a time range from the current time to a day ahead.
    * </p>
    * 
    * @throws Exception 
    */
   @Test
   public void testGetDataResultFutureTime() throws Exception {
      Calendar calendar = GregorianCalendar.getInstance(UTC_TIME_ZONE);
      Calendar startDate = (Calendar) calendar.clone();
      Calendar endDate = (Calendar) calendar.clone();
      endDate.add(Calendar.DATE, 1);
      
      DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
      final XMLGregorianCalendar xmlStart = datatypeFactory
         .newXMLGregorianCalendar(startDate.get(Calendar.YEAR),
            startDate.get(Calendar.MONTH) + 1,
            startDate.get(Calendar.DAY_OF_MONTH),
            startDate.get(Calendar.HOUR_OF_DAY),
            startDate.get(Calendar.MINUTE),
            startDate.get(Calendar.SECOND),
            startDate.get(Calendar.MILLISECOND), 0);
      final XMLGregorianCalendar xmlEnd = datatypeFactory
         .newXMLGregorianCalendar(endDate.get(Calendar.YEAR),
            endDate.get(Calendar.MONTH) + 1,
            endDate.get(Calendar.DAY_OF_MONTH),
            endDate.get(Calendar.HOUR_OF_DAY),
            endDate.get(Calendar.MINUTE),
            endDate.get(Calendar.SECOND),
            endDate.get(Calendar.MILLISECOND), 0);
      
      CoordinateSystem coordinateSystem = CoordinateSystem.GEO; // ECEF?
      DataResult dataResult = ssc.getData(getDataFileRequest(xmlStart, xmlEnd, coordinateSystem));
      printDataResult(dataResult);
      
      assertTrue(dataResult != null);
   }


}
