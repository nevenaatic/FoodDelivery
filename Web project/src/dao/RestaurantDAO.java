package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import beans.Restaurant;



public class RestaurantDAO {
	private HashMap<Integer,Restaurant> restaurants;

	public HashMap<Integer, Restaurant> getRestaurants() {
		return restaurants;
	}

	public void setRestaurants(HashMap<Integer, Restaurant> restaurants) {
		this.restaurants = restaurants;
	}
	
	
	public RestaurantDAO() {
		this.setRestaurants(new HashMap<Integer, Restaurant>());
		
		loadRestaurants();
	}
	
	//ucitavanje restorana iz fajla
	@SuppressWarnings("unchecked")
	private void loadRestaurants() {
		FileWriter fileWriter = null;
		BufferedReader in = null;
		File file = null;
		try {
			file = new File("WebContent/data/restaurants.txt");
			in = new BufferedReader(new FileReader(file));

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setVisibilityChecker(
					VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
			TypeFactory factory = TypeFactory.defaultInstance();
			MapType type = factory.constructMapType(HashMap.class, Integer.class, Restaurant.class);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			this.restaurants = ((HashMap<Integer, Restaurant>) objectMapper.readValue(file, type));
		} catch (FileNotFoundException fnfe) {
			try {
				file.createNewFile();
				fileWriter = new FileWriter(file);
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
				String string = objectMapper.writeValueAsString(restaurants);
				fileWriter.write(string);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fileWriter != null) {
					try {
						fileWriter.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//ucitavanje restorana u fajl
	private void saveComments() {
		File f = new File("WebContent/data/restaurants.txt");
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(f);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			String string= objectMapper.writeValueAsString(this.restaurants);
			fileWriter.write(string);
			fileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Collection<Restaurant> getValues() {
		return this.restaurants.values();
	}

	public Restaurant getByID(int restaurantId) {
		System.out.println("hejjjj");
		System.out.println(this.restaurants.values().size());
		for (Restaurant restaurant : this.restaurants.values()) {
			if(restaurant.getId() == restaurantId) {
				System.out.println("hej");
				return restaurant;
			}
		}
		return null;
	}
}
