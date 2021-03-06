package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import beans.Address;
import beans.Comment;
import beans.Restaurant;
import beans.User;
import dto.RestaurantChangeDTO;
import dto.RestaurantNewDTO;
import dto.RestaurantSearchMixDTO;
import enums.CustomerType;
import enums.RestaurantType;
import enums.Role;
import enums.Status;



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
	

	public Collection<Restaurant> getValues() {
		this.loadRestaurants();
		return this.restaurants.values();
	}

	public Restaurant getByID(int restaurantId) {
		System.out.println("U RESTORAN METODI SAM");
		for (Restaurant restaurant : this.restaurants.values()) {
			if(restaurant.getId() == restaurantId) {
				System.out.println(restaurant);
				return restaurant;
			}
		}
		return null;
	}
	
	
	
	public void deleteRestaurantById(int id) {
		Restaurant restaurant = getByID(id);
		if(restaurant !=null) {
			restaurant.setDeleted(true);
			saveRestaurants();
			
		}
	}
	
	
	//ucitavanje restorana u fajl
	private void saveRestaurants() {
		File f = new File("WebContent/data/restaurants.txt");
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(f);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			String stringRestaurants = objectMapper.writeValueAsString(this.restaurants);
			fileWriter.write(stringRestaurants);
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

	public Restaurant addRestaurant(RestaurantNewDTO restaurant) {
		Restaurant newRestaurant = new Restaurant(generateIdRestaurant(),restaurant.managerId, restaurant.name, restaurant.type, new ArrayList<Integer>(), Status.OTVOREN, new Address(restaurant.street, restaurant.number, restaurant.city, restaurant.zipCode, restaurant.latitude, restaurant.longitude), generateLink(restaurant.link), false, 0.0);
		this.restaurants.put(newRestaurant.getId(), newRestaurant);
		saveRestaurants();
		return newRestaurant;
	}

	private String generateLink(String link) {
		String ret="";
		//C:\fakepath\20180717_155517.jpg
		String path[] = link.split("fakepath");
		ret = path[1].substring(1);
		System.out.println(path);
		System.out.println(ret);
		return ret;
	}

	private Integer generateIdRestaurant() {
		int ret = 0;
        for (Restaurant restaurantBig : this.getValues())
        {
            for (Restaurant restaurant : this.getValues())
            {
                if (ret == restaurant.getId())
                {
                    ++ret;
                    break;
                }
            }
        }
        return ret;
	}

	public void changeRestaurant(RestaurantChangeDTO restaurant) {
		Restaurant restaurantChange = getByID(restaurant.id);
		restaurantChange.setName(restaurant.name);
		restaurantChange.setType(restaurant.type);
		restaurantChange.setAddress(new Address(restaurant.street, restaurant.number, restaurant.city, restaurant.zipCode));
		restaurantChange.setLink(restaurant.link);
		restaurantChange.setManagerId(restaurant.managerId);
		saveRestaurants();
	}
	
	
	private RestaurantType checkTypeRestaurant(String type)
	{
		RestaurantType ret =null;
		if(type.equals("ITALIAN")) {
			ret=RestaurantType.ITALIJANSKI;
		}
		if(type.equals("CHINESE")) {
			ret=RestaurantType.KINESKI;
		}
		 if(type.equals("PIZZA")) {
			 ret=RestaurantType.PIZZA;
		} 
		 if(type.equals("BARBECUE")) {
			ret=RestaurantType.ROSTILJ;
		}
		if(type.equals("FISH")) {
			ret=RestaurantType.RIBLJI;
		}
		if(type.equals("VEGE")) {
			ret=RestaurantType.VEGE;
		} 
		
		return ret;
	}

	public Collection<Restaurant> filterUsersByType(String type) {
		RestaurantType typeRestaurant = checkTypeRestaurant(type);
		ArrayList<Restaurant> ret = new ArrayList<Restaurant>();
		for(Restaurant restaurant : this.restaurants.values()) {
			if(restaurant.getType().equals(typeRestaurant)) {
				ret.add(restaurant);
			}
		}
		return null;
	}

	public Collection<Restaurant> searchMix(RestaurantSearchMixDTO parameters) {
		ArrayList<Restaurant> ret = new ArrayList<Restaurant>();
		
			for(Restaurant restaurant : this.restaurants.values()) {
				if (restaurant.getName().equals(parameters.name)) {
					System.out.println(restaurant.getName().toLowerCase().equals(parameters.name.toLowerCase()));
				System.out.println((restaurant.getAddress().getCity().toLowerCase().equals(parameters.location.toLowerCase()) || restaurant.getAddress().getStreet().toLowerCase().equals(parameters.location.toLowerCase())));
				System.out.println(parameters.grade.equals(""));
				System.out.println(parameters.type.equals(""));
				}
				
			//	if (restaurant.getStatus() == Status.OPEN) {
					// samo tip
					if(restaurant.getType().equals(parameters.type) && parameters.name.equals("") && parameters.location.equals("") && parameters.grade.equals("")) 
					{
						ret.add(restaurant);
						//break;
						
					}
					
					//samo naziv
					if(parameters.type.equals(RestaurantType.PRETRAGA) && restaurant.getName().toLowerCase().equals(parameters.name.toLowerCase())  && parameters.location.equals("") && parameters.grade.equals("")) 
					{
						ret.add(restaurant);
						//break;
					
					}
					
					//ocena
					else if( !parameters.grade.equals("") && parameters.type.equals(RestaurantType.PRETRAGA) && parameters.name.equals("")  && parameters.location.equals("") && (checkGrade(restaurant.getGrade())) == Integer.parseInt(parameters.grade)) 
					{
						ret.add(restaurant);
						System.out.println("PARAMETAR GRADE:" + parameters.grade);
						System.out.println("RESTORAN GRADE:" + restaurant.getGrade());
	
					}
					
					//lokacija
					else if(parameters.type.equals(RestaurantType.PRETRAGA) && parameters.name.equals("")  && (restaurant.getAddress().getCity().toLowerCase().equals(parameters.location.toLowerCase()) || restaurant.getAddress().getStreet().toLowerCase().equals(parameters.location.toLowerCase()) && parameters.grade.equals("")))
					{
						ret.add(restaurant);
						
					}
					
					//naziv i lokacija
					else if(restaurant.getName().toLowerCase().equals(parameters.name.toLowerCase())  && (restaurant.getAddress().getCity().toLowerCase().equals(parameters.location.toLowerCase()) || restaurant.getAddress().getStreet().toLowerCase().equals(parameters.location.toLowerCase())) && parameters.grade.equals("") && parameters.type.equals(RestaurantType.PRETRAGA))
					{
						ret.add(restaurant);
						
					}
					
					//naziv i tip
					else if( restaurant.getType().equals(parameters.type) && restaurant.getName().toLowerCase().equals(parameters.name.toLowerCase()) && parameters.location.equals("") && parameters.grade.equals("")) 
					{
						ret.add(restaurant);
						
					}
					
					//naziv i ocena samo
					else if (!parameters.grade.equals("") && parameters.type.equals(RestaurantType.PRETRAGA) && restaurant.getName().toLowerCase().equals(parameters.name.toLowerCase())  && parameters.location.equals("") && checkGrade(restaurant.getGrade()) == Integer.parseInt(parameters.grade)) 
					{
						ret.add(restaurant);
						
					}
				
					
					//lokacija i tip
					else if(restaurant.getType().equals(parameters.type)  && (restaurant.getAddress().getCity().toLowerCase().equals(parameters.location.toLowerCase()) || restaurant.getAddress().getStreet().toLowerCase().equals(parameters.location.toLowerCase())) && parameters.grade.equals("") && parameters.name.equals(""))
					{
						ret.add(restaurant);
					
					}
					
					//lokacija i ocena
					else 	if(!parameters.grade.equals("") && parameters.name.equals("")  && (restaurant.getAddress().getCity().toLowerCase().equals(parameters.location.toLowerCase()) || restaurant.getAddress().getStreet().toLowerCase().equals(parameters.location.toLowerCase())) && (checkGrade(restaurant.getGrade()) == Integer.parseInt(parameters.grade)) && parameters.type.equals(RestaurantType.PRETRAGA))
					{
						ret.add(restaurant);
						
					}
					
					//tip i ocena
					else 	if(!parameters.grade.equals("") && restaurant.getType().equals(parameters.type) && parameters.name.equals("") && parameters.location.equals("")  && (checkGrade(restaurant.getGrade()) == Integer.parseInt(parameters.grade))) 
					{
						ret.add(restaurant);
						
					}
					
					//naziv, lokacija, tip
					else if(restaurant.getType().equals(parameters.type) && restaurant.getName().toLowerCase().equals(parameters.name.toLowerCase()) && parameters.grade.equals("") && (restaurant.getAddress().getCity().toLowerCase().equals(parameters.location.toLowerCase()) || restaurant.getAddress().getStreet().toLowerCase().equals(parameters.location.toLowerCase()) )) 
					{
						ret.add(restaurant);
						
					}
					
					//naziv, lokacija, ocena
					else if(!parameters.grade.equals("") && parameters.type.equals(RestaurantType.PRETRAGA) && restaurant.getName().toLowerCase().equals(parameters.name.toLowerCase()) && (checkGrade(restaurant.getGrade()) == Integer.parseInt(parameters.grade)) && (restaurant.getAddress().getCity().toLowerCase().equals(parameters.location.toLowerCase()) || restaurant.getAddress().getStreet().toLowerCase().equals(parameters.location.toLowerCase()) )) 
					{
						ret.add(restaurant);
						
					}
					
					//lokacija, tip, ocena
					else	if( !parameters.grade.equals("") && restaurant.getType().equals(parameters.type) && parameters.name.equals("") && (checkGrade(restaurant.getGrade()) == Integer.parseInt(parameters.grade)) && (restaurant.getAddress().getCity().toLowerCase().equals(parameters.location.toLowerCase()) || restaurant.getAddress().getStreet().toLowerCase().equals(parameters.location.toLowerCase()) )) 
					{
						ret.add(restaurant);
						
					}
					
					//sve
					else if (!parameters.grade.equals("") && restaurant.getType().equals(parameters.type) && restaurant.getName().toLowerCase().equals(parameters.name.toLowerCase()) && (checkGrade(restaurant.getGrade()) == Integer.parseInt(parameters.grade)) && (restaurant.getAddress().getCity().toLowerCase().equals(parameters.location.toLowerCase()) || restaurant.getAddress().getStreet().toLowerCase().equals(parameters.location.toLowerCase()) )) 
					{
						ret.add(restaurant);
					
					}
				}
			//}	
		
		return ret;
	}
	
	private double checkGrade(double grade) {
		int ret= 0;
		if( grade < 1.50) {
			ret = 1;
			
		}
		else if(grade >= 1.50 && grade < 2.50) {
			ret = 2;
			
		}
		else if( grade >= 2.50 && grade < 3.50) {
			ret = 3;
		}
		else if( grade >= 3.50 && grade < 4.50) {
			ret = 4;
		}
		else {
			ret = 5;
		}
		return ret;
	}

	private static final DecimalFormat df = new DecimalFormat("0.00");
	
	public void updateGrade(int idRestaurant, double grade, Collection<Comment> comments) {
		Restaurant restaurant = this.getByID(idRestaurant);
		double sum = 0;
		double br = 0;
		
		for (Comment comment : comments) {
			if (comment.getRestaurantId() == restaurant.getId()) {
				sum = sum + comment.getGrade();
				br += 1;
			}
		}
		
		restaurant.setGrade(Double.parseDouble(df.format(sum/br)));
		this.saveRestaurants();
	}

}
