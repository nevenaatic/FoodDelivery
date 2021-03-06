package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Artical;
import beans.Restaurant;
import beans.User;
import dao.ArticalDAO;
import dao.RestaurantDAO;
import dto.ArticalChangeDTO;
import dto.ArticalDTO;

@Path("/artical")
public class ArticalService {
	
	@Context
	HttpServletRequest request;
	@Context
	ServletContext context;
	
	public ArticalService() {
		
	}
	
	private ArticalDAO getArticalDAO() {
		ArticalDAO articals = (ArticalDAO)context.getAttribute("articals");
		
		if (articals == null) {
			articals = new ArticalDAO();
			context.setAttribute("articals", articals);
		}
	
		return articals;
	}
	
	@GET
	@Path("/getAllArticalsManager")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Artical> getAll() {
		ArticalDAO articalDAO = getArticalDAO();
		ArrayList<Artical> ret= new ArrayList<Artical>(); 
		
		User user = (User)request.getSession().getAttribute("loginUser");
		
		for (Artical artical : articalDAO.getValues()) {
			if(!artical.getDeleted() && artical.getIdRestaurant() == user.getIdRestaurant()) {
				ret.add(artical);
			}
			
		}
		return ret;
	}
	
	@POST
	@Path("/getAllArticals")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Artical> getAllArticals(String idRestaurant) {
		ArticalDAO articalDAO = getArticalDAO();
		ArrayList<Artical> ret= new ArrayList<Artical>(); 
		RestaurantDAO restaurantDAO = getRestaurantsDAO();
		Restaurant restaurant = restaurantDAO.getByID(Integer.parseInt(idRestaurant));
		
		for (Artical artical : articalDAO.getValues()) {
			if(!artical.getDeleted() && artical.getIdRestaurant() == Integer.parseInt(idRestaurant) && !restaurant.getDeleted()) {
				ret.add(artical);
			}
			
		}
		return ret;
	}
	
	@POST
	@Path("/getArtical")
	@Produces(MediaType.APPLICATION_JSON)
	public Artical getArtical(String id) {
		ArticalDAO articalDAO = getArticalDAO();
		
		for (Artical artical : articalDAO.getValues()) {
			if(!artical.getDeleted() && artical.getId() == Integer.parseInt(id)) {
				return artical;
			}
			
		}
		return null;
	}
	
	@POST
	@Path("/addArtical")
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add( ArticalDTO artical) {
		ArticalDAO articalDAO = getArticalDAO();
		if( articalDAO.addArtical(artical) != null) 
			return  Response.status(Response.Status.ACCEPTED).build();
		else  return Response.status(Response.Status.BAD_REQUEST).entity("Vec postoji artikal sa datim imenom!").build();
	}
	
	@POST
	@Path("/changeArtical")
	@Consumes(MediaType.APPLICATION_JSON)
	public void add(ArticalChangeDTO artical) {
		ArticalDAO articalDAO = getArticalDAO();
		articalDAO.changeArtical(artical);
	}
	
	
	@POST
	@Path("/deleteArtical")
	public  void delete(String articalId) {
		ArticalDAO articalDAO = getArticalDAO();
		articalDAO.deleteArticalById(Integer.parseInt(articalId));
	}
	
	private RestaurantDAO getRestaurantsDAO() {
		RestaurantDAO restaurants = (RestaurantDAO)context.getAttribute("restaurants");
		
		if (restaurants == null) {
			restaurants = new RestaurantDAO();
			context.setAttribute("restaurants", restaurants);
		}
	
		return restaurants;
	}
	
	
}
