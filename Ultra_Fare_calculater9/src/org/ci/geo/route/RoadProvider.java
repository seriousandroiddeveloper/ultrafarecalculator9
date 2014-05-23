package org.ci.geo.route;



import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class RoadProvider {
	Queue<Double[]> queuemain;
	public static Road getRoute(InputStream is) {
		KMLHandler handler = new KMLHandler();
		try {
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			parser.parse(is, handler);
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return handler.mroad;
	}

	public static String getUrl(double fromLat, double fromLon, double toLat,
			double toLon) {// connect to map web service
		StringBuffer urlString = new StringBuffer();
		urlString.append("http://maps.googleapis.com/maps/api/directions/xml?");
		urlString.append("origin=");// from
		urlString.append(Double.toString(fromLat));
		urlString.append(",");
		urlString.append(Double.toString(fromLon));
		urlString.append("&destination=");// to
		urlString.append(Double.toString(toLat));
		urlString.append(",");
		urlString.append(Double.toString(toLon));
		urlString.append("&sensor=false");
		return urlString.toString();
	}
}

class KMLHandler extends DefaultHandler {
	Road mroad;
	boolean bfname = false;
	boolean lata = false;
	boolean lonb = false;
	boolean step = false;
	boolean fromname = false,text = false;
	
	boolean toname = false,duration=false,value=false,distance = false;
	
	static int count =0;
	Queue<Double[]> queue = new LinkedList<Double[]>();
	public KMLHandler() {
	mroad = new Road();
	
	mroad.mName="check";
	mroad.mColor = 0;
	mroad.mWidth=0;
	
	}
	
	
	
	public void startElement(String uri, String localName,String qName, 
                Attributes attributes) throws SAXException {
 
		
		//System.out.println("Start Element :" + qName);
 
		if (qName.equalsIgnoreCase("LEG")) {
			bfname = true;
		}
		
		if (qName.equalsIgnoreCase("start_location")) {
			fromname = true;
		}
		if (qName.equalsIgnoreCase("end_location")) {
			toname = true;
		}
 
		if (qName.equalsIgnoreCase("LAT")) {
			lata = true;
		}
 
		if (qName.equalsIgnoreCase("LNG")) {
			lonb = true;
		}
 
		if (qName.equalsIgnoreCase("step")) {
			step = true;
		}
		if (qName.equalsIgnoreCase("duration")) {
			duration = true;
		}
		if (qName.equalsIgnoreCase("value")) {
			value = true;
		}
		if (qName.equalsIgnoreCase("distance")) {
			distance = true;
		}
		
		if (qName.equalsIgnoreCase("text")) {
			text = true;
		}
		
 
	}
 
	public void endElement(String uri, String localName,
		String qName) throws SAXException {
 
		if (qName.equalsIgnoreCase("LEG")) {
			
			
			
			bfname = false;
			
		}
		
		if (qName.equalsIgnoreCase("start_location")) {
			fromname = false;
		}
		
		
		if (qName.equalsIgnoreCase("end_location")) {
			toname = false;
		}
 
		if (qName.equalsIgnoreCase("LAT")) {
			lata = false;
		}
 
		if (qName.equalsIgnoreCase("LNG")) {
			lonb = false;
			
		}
 
		if (qName.equalsIgnoreCase("step")) {
			step = false;
		}
		
		if (qName.equalsIgnoreCase("duration")) {
			duration = false;
		}
		if (qName.equalsIgnoreCase("value")) {
			value = false;
		}
		
		if (qName.equalsIgnoreCase("distance")) {
			distance = false;
		}
		if (qName.equalsIgnoreCase("text")) {
			text = false;
		}
		
 
	}
	
	
	String string1,string2;
	public void characters(char ch[], int start, int length) throws SAXException {
	/*if (bfname) {
			System.out.println("First Name : " + new String(ch, start, length));
			bfname = false;
		}*/
		
		
		
		
		if(bfname&&fromname&&lata&&step)
		{
			
			Log.i("road", new String(ch, start, length));
			string1 =new String(ch, start, length);
			//t[0]=Double.valueOf(new String(ch, start, length));
			
			lata = false;
			
		}
		if(bfname&&fromname&&lonb&&step)
		{
			Log.i("road", new String(ch, start, length));
			string2 = new String(ch, start, length);
		
			Double t[] = new Double[]{0.0,0.0};
			t[0]=Double.valueOf(string2);
			t[1]=Double.valueOf(string1);
			lonb = false;
			queue.add(t);
			
		}
	
		
		
		
		
		///
		if(bfname&&toname&&lata&&!step)
		{
			Log.i("road", new String(ch, start, length));
			string1 =new String(ch, start, length);
			//t[0]=Double.valueOf(new String(ch, start, length));
			lata = false;
			
		}
		if(!step&&bfname&&duration&&value)
		{
			Log.i("duration :", new String(ch, start, length));
			mroad.mduration=Integer.valueOf(new String(ch, start, length));
		}
		if(!step&&bfname&&distance&&value)
		{
			Log.i("distance :", new String(ch, start, length));
			mroad.mdistance=Integer.valueOf(new String(ch, start, length));
		}
		if(!step&&bfname&&distance&&text)
		{
		
			mroad.mDescription=new String(ch, start, length);
		}
		if(bfname&&toname&&lonb&&!step)
		{
			Log.i("road", new String(ch, start, length));
			string2 = new String(ch, start, length);
			Double t[] = new Double[]{0.0,0.0};
			t[0]=Double.valueOf(string2);
			t[1]=Double.valueOf(string1);
			queue.add(t);
			lonb = false;
			//queue.add(t);
			
			Double route[] = new Double[2];
			 double[][] tempdq = new double[queue.size()][2];
			int qcount=0;
			 while (!queue.isEmpty()) {
		        	route  = queue.remove();
		        	String lat = String.valueOf(route[0]);
		        	String lon = String.valueOf(route[1]);
		        	tempdq[qcount][0]=Double.valueOf(lat);
		        	tempdq[qcount][1]=Double.valueOf(lon);
		        	Log.i("s","i am workin");
		        	Log.i("Route", String.valueOf(tempdq[qcount][0])+","+String.valueOf(tempdq[qcount][1]));
		        	qcount++;
		        	
		        }	
			mroad.mRoute=tempdq;
			
		}
		
		
 
	}
	
	
}