package com.amex.intellify.webservices;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

import com.amex.intellify.backend.DbHelper;
import com.amex.intellify.backend.EmailService;
import com.amex.intellify.model.RegisterUser;
import com.amex.intellify.model.SwipeInfo;
import com.amex.intellify.model.SwipeInfoResponse;
import com.google.gson.Gson;
import com.sun.jersey.api.json.JSONJAXBContext;

@Path("/")
public class Service {

	@Provider
	public static class JAXBContextResolver implements
			ContextResolver<JAXBContext> {

		@SuppressWarnings("unused")
		private JAXBContext context;
		@SuppressWarnings("unchecked")
		private Class[] types = {};

		@SuppressWarnings("deprecation")
		public JAXBContextResolver() throws Exception {
			Map<String, Object> props = new HashMap<String, Object>();
			props.put(JSONJAXBContext.JSON_NOTATION, "MAPPED_JETTISON");
			props.put(JSONJAXBContext.JSON_ROOT_UNWRAPPING, Boolean.FALSE);
			this.context = new JSONJAXBContext(types, props);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.ws.rs.ext.ContextResolver#getContext(java.lang.Class)
		 */
		@SuppressWarnings("deprecation")
		public JAXBContext getContext(Class<?> objectType) {

			JAXBContext jaxb = null;
			for (int i = 0; i < types.length; i++) {
				if (types[i].equals(objectType)) {
					Map<String, Object> props = new HashMap<String, Object>();
					props.put(JSONJAXBContext.JSON_NOTATION, "MAPPED_JETTISON");
					props.put(JSONJAXBContext.JSON_ROOT_UNWRAPPING,
							Boolean.FALSE);
					try {
						jaxb = new JSONJAXBContext(types, props);

					} catch (JAXBException e) {

					}
					break;
				}
			}
			return jaxb;
		}
	}

	private final static Logger logger = Logger.getLogger(Service.class);

	/**
	 * Home Page of the Web Services
	 */
	@GET
	@Path("/home")
	@Produces(MediaType.APPLICATION_JSON)
	public String home() {
		// First Web-Service to display the Welcome message
		// of the application.
		// TODO - Home

		System.out.println("WebService Working");
		logger.info("WebService Working");
		return " Welcome to Carpool Middleware Web services - Ver 1 ";
	}
	
	/**
	 * Home Page of the Web Services
	 */
	@GET
	@Path("/cardUsage")
	@Produces(MediaType.APPLICATION_JSON)
	public String getCardUsage() {
		
		Gson gson = new Gson();
		DbHelper helper = new DbHelper();
		System.out.println("data requested");
		
		try{
			SwipeInfoResponse re = helper.getIntellifyStats();
			String res = gson.toJson(re);
			System.out.println(res);
			return res;
			
		}catch(Exception ex){
			return "Failure";
		}
		/*return "{\"cards\": [{\"Discover\": {\"restaurants\": \"9,989\",\"electronics\": \"20,099\",\"Clothing\": \"00\",\"retail\": \"88,674\",\"gas\": \"99,887\"}},"
				+ "{\"Citi\": {\"restaurants\": \"20,899\",\"electronics\": \"11,998\",\"Clothing\": \"88,977\",\"retail\": \"76,674\",\"gas\": \"10,887\"}}, "
				+ "{\"Amex\": {\"restaurants\": \"32,854\",\"electronics\": \"12,990\",\"Clothing\": \"88,977\",\"retail\": \"88,674\",\"gas\": \"99,887\"}}, "
				+ "{\"Chase\": {\"restaurants\": \"97,123\",\"electronics\": \"17,098\",\"Clothing\": \"48,498\",\"retail\": \"88,674\",\"gas\": \"99,887\"}},"
				+ " {\"Bofa\": {\"restaurants\": \"20,899\",\"electronics\": \"13,900\",\"Clothing\": \"88,977\",\"retail\": \"88,674\",\"gas\": \"99,887\"}}]}";
		*/}

	@POST
	@Path("/cardType")
	@Consumes(MediaType.WILDCARD)
	@Produces(MediaType.APPLICATION_JSON)
	public String getSwipeInfo(String request) throws Exception {
		
		logger.info("Request:"+request);
		System.out.println("Request:"+request);

		try {
			Gson gson = new Gson();
			SwipeInfo info = gson.fromJson(request, SwipeInfo.class);
			
			/*if(info.getCardType().equalsIgnoreCase("chase")){
				info.setEmv(123);
				info.setCardYear(2016);
				info.setCustomerId(10);
				info.setMonth("02");
			}
			else if(info.getCardType().equalsIgnoreCase("amex")){
				info.setEmv(121);
				info.setCardYear(2016);
				info.setCustomerId(10);
				info.setMonth("09");
			}else if(info.getCardType().equalsIgnoreCase("citi")){
				info.setEmv(231);
				info.setCardYear(2016);
				info.setCustomerId(10);
				info.setMonth("08");
			}else if(info.getCardType().equalsIgnoreCase("discover")){
				info.setEmv(182);
				info.setCardYear(2016);
				info.setCustomerId(10);
				info.setMonth("07");
			}else if(info.getCardType().equalsIgnoreCase("bofa")){
				info.setEmv(321);
				info.setCardYear(2016);
				info.setCustomerId(10);
				info.setMonth("06");
			}*/
			
			DbHelper helper = new DbHelper();
			String response  = helper.setSwipeInfo(info);
			System.out.println("Response: "+response);
			
			return response;
			
		} catch (Exception ex) {

			logger.info("setUser:Service.java have errors: " + ex.getMessage());
			return "Failure";
		}
	}
	
	@POST
	@Path("/getoffers")
	@Consumes(MediaType.WILDCARD)
	@Produces(MediaType.APPLICATION_JSON)
	public String getoffers(String request) throws Exception {
		
		logger.info("Request:"+request);
		System.out.println("Request:"+request);
		EmailService service = new EmailService();

		try {
			service.sendOffers(request);
		
			return "Success";

		} catch (Exception ex) {

			logger.info("setUser:Service.java have errors: " + ex.getMessage());
			return "Failure";
		}
	}
	
	@POST
	@Path("/loginUser")
	@Consumes(MediaType.WILDCARD)
	@Produces(MediaType.APPLICATION_JSON)
	public String loginUser(String request) throws Exception {
		
		logger.info("Request:"+request);
		System.out.println("Request:"+request);

		return "{\"response\":\"Success\"}";
	
	}
	
	public static void main(String[] args) {
		Service service = new Service();
		try {
			System.out.println(service.loginUser("{\"userName\":\"nishant\",\"password\":\"123\",\"intellifyKey\":\"321\"}"));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@POST
	@Path("/registerUser")
	@Consumes(MediaType.WILDCARD)
	@Produces(MediaType.APPLICATION_JSON)
	public String registerUser(String request) throws Exception {
		
		System.out.println("MyRequest:"+request);
		Gson gson = new Gson();
		DbHelper helper = new DbHelper();
		
		/*try{
			
			RegisterUser registerUser = gson.fromJson(request, RegisterUser.class);
			
			String validCustomerId = helper.validateUser(registerUser);
			
			if(!validCustomerId.equalsIgnoreCase("Failure")){
				String createResponse = helper.createUser(registerUser,validCustomerId);
				return createResponse;
			}
			
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}*/
		return request;
	}

	@POST
	@Path("/insertAdd")
	@Consumes(MediaType.WILDCARD)
	@Produces(MediaType.APPLICATION_JSON)
	public String setAdd(String request) throws Exception {
		
		logger.info("Request:"+request);
		System.out.println("Request:"+request);

		try {
			Gson gson = new Gson();
			return "Success";
			

		} catch (Exception ex) {
			System.out.println("setUser:Service.java have errors: " + ex.getMessage());
			logger.info("setUser:Service.java have errors: " + ex.getMessage());
			return "Failure";
		}
	}

}
