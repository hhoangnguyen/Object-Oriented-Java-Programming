package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import de.fhpotsdam.unfolding.providers.Microsoft;
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Huy Hoang-Nguyen
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;
	
	// Less than this threshold is a light earthquake
	private static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	private static final float THRESHOLD_LIGHT = 4;

	// starting x position for UnfoldingMap
	private static final int START_X_POSITION_MAP = 200;

	/** This is where to find the local tiles, for working without an Internet connection */
	private static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// The map
	private UnfoldingMap map;
	
	// feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	private List<MagnitudeKey> magnitudeKeys;
	
	public void setup() {
		size(950, 600, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, START_X_POSITION_MAP, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			// Google blocked me for multiple attempts. I'm using Microsoft's one instead
			map = new UnfoldingMap(this, START_X_POSITION_MAP, 50, 700, 500, new Microsoft.AerialProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    // The List you will populate with new SimplePointMarkers
	    List<Marker> markers = new ArrayList<Marker>();

	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
	    // Add a loop here that calls createMarker (see below)
	    // to create a new SimplePointMarker for each PointFeature in 
	    // earthquakes.  Then add each new SimplePointMarker to the 
	    // List markers (so that it will be added to the map in the line below)
		SimplePointMarker simplePointMarker;
		for (PointFeature pointFeature : earthquakes) {
			simplePointMarker = this.createMarker(pointFeature);
			markers.add(simplePointMarker);
		}

	    // Add the markers to the map so that they are displayed
	    map.addMarkers(markers);
	}

	/**
	 * Helper method that takes in an earthquake
	 * feature and returns a SimplePointMarker for that earthquake
	 * @param feature a PointFeature
	 * @return SinglePointMarker
	 */
	private SimplePointMarker createMarker(PointFeature feature)
	{  
		// To print all of the features in a PointFeature (so you can see what they are)
		// uncomment the line below.  Note this will only print if you call createMarker 
		// from setup
		// System.out.println(feature.getProperties());
		
		// Create a new SimplePointMarker at the location given by the PointFeature
		SimplePointMarker marker = new SimplePointMarker(feature.getLocation());
		
		Object magObj = feature.getProperty("magnitude");
		float mag = Float.parseFloat(magObj.toString());

		// array of MagnitudeKey objects
		this.magnitudeKeys = this.getMagnitudeKeys();

		MagnitudeKey magnitudeKey;
		if (mag < THRESHOLD_LIGHT) { // small
			magnitudeKey = this.getMagnitudeKeyforKey(MagnitudeKey.MAGNITUDE_KEY_SMALL);
		}
		else if (mag >= THRESHOLD_LIGHT && mag < THRESHOLD_MODERATE) { // light
			magnitudeKey = this.getMagnitudeKeyforKey(MagnitudeKey.MAGNITUDE_KEY_LIGHT);
		}
		else { // moderate
			magnitudeKey = this.getMagnitudeKeyforKey(MagnitudeKey.MAGNITUDE_KEY_MODERATE);
		}
		// update marker's radius and color
		marker.setRadius(magnitudeKey.getRadius());
		marker.setColor(magnitudeKey.getColor());
	    
	    // Finally return the marker
	    return marker;
	}

	/**
	 * Helper method creating a list of MagnitudeKey
	 * @return List
	 */
	private List<MagnitudeKey> getMagnitudeKeys() {
		List<MagnitudeKey> magnitudeKeys = new ArrayList<>();

		MagnitudeKey moderate = new MagnitudeKey(MagnitudeKey.MAGNITUDE_KEY_MODERATE, color(225, 0, 0), THRESHOLD_MODERATE * 3, "5.0+ Magnitude");
		magnitudeKeys.add(moderate);
		MagnitudeKey light = new MagnitudeKey(MagnitudeKey.MAGNITUDE_KEY_LIGHT, color(225, 225, 0), THRESHOLD_MODERATE * 2, "4.0+ Magnitude");
		magnitudeKeys.add(light);
		MagnitudeKey small = new MagnitudeKey(MagnitudeKey.MAGNITUDE_KEY_SMALL, color(0, 0, 225), THRESHOLD_MODERATE, "Below 4.0");
		magnitudeKeys.add(small);

		return magnitudeKeys;
	}

	/**
	 * Helper method to find MagnitudeKey given key
	 * @param key a String
	 * @return MagnitudeKey
	 */
	private MagnitudeKey getMagnitudeKeyforKey(String key) {
		for (MagnitudeKey magnitudeKey : this.magnitudeKeys) {
			if (magnitudeKey.getKey().equals(key)) {
				return magnitudeKey;
			}
		}
		return null;
	}
	
	public void draw() {
	    background(10);
	    map.draw();
	    addKey();
	}


	/**
	 * Helper method to draw keys in GUI
	 */
	private void addKey() 
	{
		// rectangle container
		int leftMargin = 10;
		int cornerRadius = 5;
		int keyHeight = 170;
		int keyWidth = START_X_POSITION_MAP - leftMargin * 2;
		int topLeftY = (height - keyHeight) / 2;
		fill(255.0f);
		rect(leftMargin, topLeftY, keyWidth, keyHeight, cornerRadius);

		// label
		int textSize = 17;
		textAlign(CENTER);
		textSize(15);
		fill(0.0f);
		text("Earthquake Key", leftMargin + keyWidth/2, topLeftY + textSize * 2);

		// keys
		float spacingY = THRESHOLD_MODERATE * 6;

		int count = 1;
		for (MagnitudeKey magnitudeKey : this.magnitudeKeys) {
			float curHeight = topLeftY + spacingY + spacingY * count;

			// draw marker
			fill(magnitudeKey.getColor());
			ellipse(leftMargin + spacingY, curHeight, magnitudeKey.getRadius(), magnitudeKey.getRadius());

			// draw text
			textSize(12);
			textAlign(LEFT);
			fill(0.0f);
			text(magnitudeKey.getDescription(), leftMargin + spacingY * 2, curHeight);

			count++;
		}
	}
}
