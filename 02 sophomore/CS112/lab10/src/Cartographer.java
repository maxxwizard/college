package lab10;

import java.util.Random;

class CityInfo {
	public String city_name;
	public float lat, longi;
	public float x, y;
	
	CityInfo(String city_info_str) {
		String [] city_infos = city_info_str.split(",");
		city_name = city_infos[0];
		lat = Float.parseFloat(city_infos[1]) + Float.parseFloat(city_infos[2])/60;
		longi = Float.parseFloat(city_infos[3]) + Float.parseFloat(city_infos[4])/60;
		
		lat = (float)Math.toRadians(lat);
		longi = (float)Math.toRadians(longi);
	}
}

public class Cartographer {
	public static CityInfo [] cities_info;
	public static float [][] cities_distance;
	public int num_cities;
	float min_x, min_y, max_x, max_y;
	
	public Cartographer(int num_cities){
		num_cities = Math.min(num_cities, cities_info_str.length);
		this.num_cities = num_cities;
		cities_info = new CityInfo[num_cities];
		boolean [] chosen_cities = new boolean[cities_info_str.length];
		Random rand = new Random();
		int num_chosen_cities = 0;
		while( num_chosen_cities < num_cities ){
			int choose = rand.nextInt(cities_info_str.length);
			if( !chosen_cities[choose] ){
				chosen_cities[choose] = true;
				num_chosen_cities++;
			}
		}
		int j = 0;
		float mean_long = 0;
		for( int i = 0; i < chosen_cities.length; i++ ){
			if( chosen_cities[i] ) {
				cities_info[j] = new CityInfo(cities_info_str[i]);
				mean_long += cities_info[j].longi;
				j++;
			}
		}
		mean_long /= num_cities;
		min_x = min_y = 100000;	max_x = max_y = -100000; 
		for( int i = 0; i < num_cities; i++ )
			map_projection(cities_info[i], mean_long);
		
		cities_distance = new float[num_cities][num_cities];
		for( int i = 0; i < num_cities; i++ ){
			for( j = i + 1; j < num_cities; j++ ){
				cities_distance[i][j] = cities_distance[j][i] 
				        = haversine_distance(cities_info[i], cities_info[j]);
			}
		}
	}
	
	void map_projection(CityInfo city_info, float long0){
		float x = long0 - city_info.longi;
		float y = (float)5/4 * (float)Math.log(Math.tan(0.25*Math.PI + 0.4*city_info.lat));
		
		min_x = Math.min(min_x, x); min_y = Math.min(min_y, y);
		max_x = Math.max(max_x, x); max_y = Math.max(max_y, y);
		
		city_info.x = x; city_info.y = y;
	}
	
	float haversine_distance(CityInfo city1, CityInfo city2){
		float R = 6371; // km
		double d = Math.acos(Math.sin(city1.lat)*Math.sin(city2.lat) + 
		                  Math.cos(city1.lat)*Math.cos(city2.lat) *
		                  Math.cos(city2.longi - city1.longi)) * R;
		return (float)d;
	}
	
	static String [] cities_info_str = {
		"Albany,42,40,73,45",
		"Albuquerque,35,05,106,39",
		"Amarillo,35,11,101,50",
		"Atlanta,33,45,84,23",
		"Austin,30,16,97,44",
		"Baker,44,47,117,50",
		"Baltimore,39,18,76,38",
		"Bangor,44,48,68,47",
		"Birmingham,33,30,86,50",
		"Bismarck,46,48,100,47",
		"Boise,43,36,116,13",
		"Boston,42,21,71,5",
		"Buffalo,42,55,78,50",
		"Carlsbad,32,26,104,15",
		"Charleston,32,47,79,56",
		"Charleston,38,21,81,38",
		"Charlotte,35,14,80,50",
		"Cheyenne,41,9,104,52",
		"Chicago,41,50,87,37",
		"Cincinnati,39,8,84,30",
		"Cleveland,41,28,81,37",
		"Columbia,34,0,81,2",
		"Columbus,40,0,83,1",
		"Dallas,32,46,96,46",
		"Denver,39,45,105,0",
		"Des Moines,41,35,93,37",
		"Detroit,42,20,83,3",
		"Dubuque,42,31,90,40",
		"Duluth,46,49,92,5",
		"Eastport,44,54,67,0",
		"El Centro,32,38,115,33",
		"El Paso,31,46,106,29",
		"Eugene,44,3,123,5",
		"Fargo,46,52,96,48",
		"Flagstaff,35,13,111,41",
		"Fort Worth,32,43,97,19",
		"Fresno,36,44,119,48",
		"Grand Junction,39,5,108,33",
		"Grand Rapids,42,58,85,40",
		"Havre,48,33,109,43",
		"Helena,46,35,112,2",
		"Hot Springs,34,31,93,3",
		"Houston,29,45,95,21",
		"Idaho Falls,43,30,112,1",
		"Indianapolis,39,46,86,10",
		"Jackson,32,20,90,12",
		"Jacksonville,30,22,81,40",
		"Kansas City,39,6,94,35",
		"Key West,24,33,81,48",
		"Klamath Falls,42,10,121,44",
		"Knoxville,35,57,83,56",
		"Las Vegas,36,10,115,12",
		"Lewiston,46,24,117,2",
		"Lincoln,40,50,96,40",
		"Long Beach,33,46,118,11",
		"Los Angeles,34,3,118,15",
		"Louisville,38,15,85,46",
		"Manchester,43,0,71,30",
		"Memphis,35,9,90,3",
		"Miami,25,46,80,12",
		"Milwaukee,43,2,87,55",
		"Minneapolis,44,59,93,14",
		"Mobile,30,42,88,3",
		"Montgomery,32,21,86,18",
		"Montpelier,44,15,72,32",
		"Nashville,36,10,86,47",
		"Newark,40,44,74,10",
		"New Haven,41,19,72,55",
		"New Orleans,29,57,90,4",
		"New York,40,47,73,58",
		"Oakland,37,48,122,16",
		"Oklahoma City,35,26,97,28",
		"Omaha,41,15,95,56",
		"Philadelphia,39,57,75,10",
		"Phoenix,33,29,112,4",
		"Pierre,44,22,100,21",
		"Pittsburgh,40,27,79,57",
		"Portland,43,40,70,15",
		"Portland,45,31,122,41",
		"Providence,41,50,71,24",
		"Raleigh,35,46,78,39",
		"Reno,39,30,119,49",
		"Richfield,38,46,112,5",
		"Richmond,37,33,77,29",
		"Roanoke,37,17,79,57",
		"Sacramento,38,35,121,30",
		"St. Louis,38,35,90,12",
		"Salt Lake City,40,46,111,54",
		"San Antonio,29,23,98,33",
		"San Diego,32,42,117,10",
		"San Francisco,37,47,122,26",
		"San Jose,37,20,121,53",
		"Santa Fe,35,41,105,57",
		"Savannah,32,5,81,5",
		"Seattle,47,37,122,20",
		"Shreveport,32,28,93,42",
		"Sioux Falls,43,33,96,44",
		"Spokane,47,40,117,26",
		"Springfield,39,48,89,38",
		"Springfield,42,6,72,34",
		"Springfield,37,13,93,17",
		"Syracuse,43,2,76,8",
		"Tampa,27,57,82,27",
		"Toledo,41,39,83,33",
		"Tulsa,36,09,95,59",
		"Virginia Beach,36,51,75,58",
		"Washington,38,53,77,02",
		"Wichita,37,43,97,17",
		"Wilmington,34,14,77,57",
	};	
}