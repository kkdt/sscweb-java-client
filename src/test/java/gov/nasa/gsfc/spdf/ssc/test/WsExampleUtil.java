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

import gov.nasa.gsfc.spdf.ssc.BFieldTraceOptions;
import gov.nasa.gsfc.spdf.ssc.BTraceData;
import gov.nasa.gsfc.spdf.ssc.CoordinateComponent;
import gov.nasa.gsfc.spdf.ssc.CoordinateData;
import gov.nasa.gsfc.spdf.ssc.CoordinateSystem;
import gov.nasa.gsfc.spdf.ssc.DataFileRequest;
import gov.nasa.gsfc.spdf.ssc.DataResult;
import gov.nasa.gsfc.spdf.ssc.DateFormat;
import gov.nasa.gsfc.spdf.ssc.DegreeFormat;
import gov.nasa.gsfc.spdf.ssc.DistanceUnits;
import gov.nasa.gsfc.spdf.ssc.FilteredCoordinateOptions;
import gov.nasa.gsfc.spdf.ssc.FormatOptions;
import gov.nasa.gsfc.spdf.ssc.Hemisphere;
import gov.nasa.gsfc.spdf.ssc.KmlRequest;
import gov.nasa.gsfc.spdf.ssc.LatLonFormat;
import gov.nasa.gsfc.spdf.ssc.OutputOptions;
import gov.nasa.gsfc.spdf.ssc.SatelliteData;
import gov.nasa.gsfc.spdf.ssc.SatelliteSituationCenterInterface;
import gov.nasa.gsfc.spdf.ssc.SatelliteSituationCenterService;
import gov.nasa.gsfc.spdf.ssc.SatelliteSpecification;
import gov.nasa.gsfc.spdf.ssc.TimeFormat;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.SimpleTimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

/**
 * <p>
 * Utility used for test cases and is a subset of the parsing logic used in the 
 * original {@code WsExample} developed by SSC.
 * </p>
 * 
 * @author thinh
 *
 */
public class WsExampleUtil {
   protected static final SimpleTimeZone UTC_TIME_ZONE = new SimpleTimeZone(0, "UTC");
   protected static final SimpleDateFormat DATE_FORMATTER;
   static {
       DATE_FORMATTER = new SimpleDateFormat("yyyy/DDD HH:mm:ss");
       DATE_FORMATTER.setTimeZone(UTC_TIME_ZONE);
   }
   
   public static SatelliteSituationCenterInterface getSSC(String wsdlLocation) throws MalformedURLException {
      System.setProperty("http.agent", "WsExample (" + 
         System.getProperty("os.name") + " " + 
         System.getProperty("os.arch") + ")");
      
      SatelliteSituationCenterService service =
         new SatelliteSituationCenterService(
             new URL(wsdlLocation),
             new QName("http://ssc.spdf.gsfc.nasa.gov/", "SatelliteSituationCenterService"));

     SatelliteSituationCenterInterface ssc = service.getSatelliteSituationCenterPort();
     return ssc;
   }
   
   public static XMLGregorianCalendar getXMLGregorianCalendar(Calendar calendar) throws DatatypeConfigurationException {
      DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
      final XMLGregorianCalendar xmlStart = datatypeFactory
         .newXMLGregorianCalendar(calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            calendar.get(Calendar.SECOND),
            calendar.get(Calendar.MILLISECOND), 0);
      return xmlStart;
   }
   
   public static List<BFieldTraceOptions> getBFieldTraceOptions() {
      BFieldTraceOptions geoNorthBFieldTrace = new BFieldTraceOptions();
      // GEO north B field trace
      // options
      geoNorthBFieldTrace.setCoordinateSystem(CoordinateSystem.GEO);
      geoNorthBFieldTrace.setFieldLineLength(true);
      geoNorthBFieldTrace.setFootpointLatitude(true);
      geoNorthBFieldTrace.setFootpointLongitude(true);
      geoNorthBFieldTrace.setHemisphere(Hemisphere.NORTH);

      BFieldTraceOptions geoSouthBFieldTrace = new BFieldTraceOptions();
      // GEO south B field trace
      // options
      geoSouthBFieldTrace.setCoordinateSystem(CoordinateSystem.GEO);
      geoSouthBFieldTrace.setFieldLineLength(true);
      geoSouthBFieldTrace.setFootpointLatitude(true);
      geoSouthBFieldTrace.setFootpointLongitude(true);
      geoSouthBFieldTrace.setHemisphere(Hemisphere.SOUTH);

      BFieldTraceOptions gmNorthBFieldTrace = new BFieldTraceOptions();
      // GM north B field trace options
      gmNorthBFieldTrace.setCoordinateSystem(CoordinateSystem.GM);
      gmNorthBFieldTrace.setFieldLineLength(true);
      gmNorthBFieldTrace.setFootpointLatitude(true);
      gmNorthBFieldTrace.setFootpointLongitude(true);
      gmNorthBFieldTrace.setHemisphere(Hemisphere.NORTH);

      BFieldTraceOptions gmSouthBFieldTrace = new BFieldTraceOptions();
      // GM south B field trace options
      gmSouthBFieldTrace.setCoordinateSystem(CoordinateSystem.GM);
      gmSouthBFieldTrace.setFieldLineLength(true);
      gmSouthBFieldTrace.setFootpointLatitude(true);
      gmSouthBFieldTrace.setFootpointLongitude(true);
      gmSouthBFieldTrace.setHemisphere(Hemisphere.SOUTH);
      
      List<BFieldTraceOptions> options = new ArrayList<>();
      options.add(gmSouthBFieldTrace);
      options.add(gmNorthBFieldTrace);
      options.add(geoSouthBFieldTrace);
      options.add(geoNorthBFieldTrace);
      return options;
   }
   
   public static KmlRequest getKmlRequest(Calendar start, Calendar end) throws Exception {
      KmlRequest request = new KmlRequest();
      request.setBeginTime(getXMLGregorianCalendar(start));
      request.setEndTime(getXMLGregorianCalendar(end));
      request.setNorthBFieldTraceFootpoint(true);
      request.setSouthBFieldTraceFootpoint(true);
      request.setTrajectory(true);
      return request;
   }
   
   public static DataFileRequest getDataFileRequest(XMLGregorianCalendar startDate, 
      XMLGregorianCalendar endDate, CoordinateSystem coordinateSystem) 
   {
      SatelliteSpecification fastSat = new SatelliteSpecification();
      fastSat.setId("fast");
      fastSat.setResolutionFactor(2);

      DataFileRequest dataRequest = new DataFileRequest();
      dataRequest.setBeginTime(startDate);
      dataRequest.setEndTime(endDate);
      // dataRequest.setFormatOptions(getFormatOptions());
      dataRequest.setOutputOptions(getOutputOptions(coordinateSystem));
      dataRequest.getSatellites().add(fastSat);
      return dataRequest;
   }

   public static FormatOptions getFormatOptions() {
      FormatOptions formatOptions = new FormatOptions();
      formatOptions.setDateFormat(DateFormat.YYYY_DDD);
      formatOptions.setTimeFormat(TimeFormat.HH_MM);
      formatOptions.setDistanceUnits(DistanceUnits.RE);
      formatOptions.setDistancePrecision((short) 4);
      formatOptions.setDegreeFormat(DegreeFormat.DECIMAL);
      formatOptions.setDegreePrecision((short) 2);
      formatOptions.setLatLonFormat(LatLonFormat.LAT_90_LON_180);
      return formatOptions;
   }

   public static OutputOptions getOutputOptions(CoordinateSystem coordinateSystem) {
      List<FilteredCoordinateOptions> options = new ArrayList<FilteredCoordinateOptions>();
      for (CoordinateComponent component : CoordinateComponent.values()) {
         boolean bypassLocalTime = false;
         switch (coordinateSystem) {
         case GEI_J_2000:
         case GEI_TOD:
            bypassLocalTime = true;
         default:
            if (component == CoordinateComponent.LOCAL_TIME && bypassLocalTime) {
               continue;
            } else {
               FilteredCoordinateOptions option = new FilteredCoordinateOptions();
               option.setCoordinateSystem(coordinateSystem);
               option.setComponent(component);
               option.setFilter(null);
               options.add(option);
            }
            break;
         }
      }

      OutputOptions outputOptions = new OutputOptions();
      outputOptions.setAllLocationFilters(true);
      outputOptions.getCoordinateOptions().addAll(options);
      outputOptions.getBFieldTraceOptions().addAll(getBFieldTraceOptions());
      return outputOptions;
   }

   public static void printDataResult(DataResult result) {
      List<SatelliteData> data = result.getData();
      if (data == null || data.size() == 0) {
         System.out.println("No satellite data");
         return;
      } else {
         System.out.println("data.length = " + data.size());
         System.out.println("Satellite Data:");
      }
      for (SatelliteData datum : data) {
         print(datum);
      }
   }

   public static void print(SatelliteData data) {
      System.out.println("  " + data.getId());
      List<XMLGregorianCalendar> time = data.getTime();
      List<CoordinateData> coords = data.getCoordinates();
      List<BTraceData> bTrace = data.getBTraceData();
      print(time, coords, bTrace);
   }

   public static void print(List<XMLGregorianCalendar> time, List<CoordinateData> coords, 
      List<BTraceData> bTrace) 
   {
      for (int i = 0; i < time.size(); i++) {
         System.out.println("  " + time.get(i) + ", " + time.get(i).toGregorianCalendar().getTime());
         for (int j = 0; j < coords.size(); j++) {
            List<Double> x = coords.get(j).getX(); // km
            List<Double> y = coords.get(j).getY(); // km
            List<Double> z = coords.get(j).getZ(); // km
            List<Float> lat = coords.get(j).getLatitude();
            List<Float> lon = coords.get(j).getLongitude();
            List<Double> lt = coords.get(j).getLocalTime();

            if (x != null && i < x.size()) {
               System.out.printf("  %10.2f", x.get(i));
            }
            if (y != null && i < y.size()) {
               System.out.printf("  %10.2f", y.get(i));
            }
            if (z != null && i < z.size()) {
               System.out.printf("  %10.2f", z.get(i));
            }
            if (lat != null && i < lat.size()) {
               System.out.printf("  %10.2f", lat.get(i));
            }
            if (lon != null && i < lon.size()) {
               System.out.printf("  %10.2f", lon.get(i));
            }
            if (lt != null && i < lt.size()) {
               System.out.printf("  %10.2f", lt.get(i));
            }
         }

         for (BTraceData bTraceData : bTrace) {
            List<Double> arcLength = bTraceData.getArcLength();
            List<Float> lat = bTraceData.getLatitude();
            List<Float> lon = bTraceData.getLongitude();
            CoordinateSystem coordinateSystem = bTraceData.getCoordinateSystem();
            Hemisphere hemisphere = bTraceData.getHemisphere();

            System.out.printf("  %s/%s", coordinateSystem.name(), hemisphere.name());
            if (arcLength != null && i < arcLength.size()) {
               System.out.printf("  %10.2f", arcLength.get(i));
            }
            if (lat != null && i < lat.size()) {
               System.out.printf("  %10.2f", lat.get(i));
            }
            if (lon != null && i < lon.size()) {
               System.out.printf("  %10.2f", lon.get(i));
            }
         }
         System.out.println();
      }
   }
}
