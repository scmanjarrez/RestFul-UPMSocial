//package client;
//
//
//import javax.ws.rs.client.Client;
//import javax.ws.rs.client.ClientBuilder;
//import javax.ws.rs.client.Entity;
//import javax.ws.rs.client.WebTarget;
//import javax.ws.rs.core.Form;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//public class RestClient {
//	private Client client = null;
//	
//	@Test
//	public void testPostTodo() {
//		//Setting the post method url to the client
//		WebTarget webTarget = client.target("http://localhost:8080/UPMSocial/api").path("users");
//
//		//Add key-value pair into the form object
//		Form form = new Form();
//		form.param("id", "5");
//		form.param("tarea", "Hacer un cliente Rest");
//		form.param("description", "By Sergio Chica");
//
//		//Send the form object along with the post call
//		Response response = webTarget.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
//		System.out.println("Respose code: " +  response.getStatus());
//		System.out.println("Respose value: " + response.readEntity(String.class));
//	}
//
//	@Before
//	public void setUp() {
//		client = ClientBuilder.newClient();
//	}
//
//	@After
//	public void tearDown() {
//		if (client != null) {
//			client.close();
//		}
//	}
//}

package client;

import rest.user.*;
import java.util.ArrayList;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;

/**
 * 
 * @author Sergio Chica Manjarrez
 * 
 * @version 0.1
 * 
 */

public class RestClient {
	private static Client client = null;
	public static void main(String[] args) {
		
		Client client = ClientBuilder.newClient( new ClientConfig().register( LoggingFilter.class ) );
		WebTarget webTarget = client.target("http://localhost:8080/UPMSocial/api").path("users");
		 
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_XML);
		Response response = invocationBuilder.get();
		 
		User user = response.readEntity(User.class);
		     
		System.out.println(response.getStatus());
		System.out.println(user);
		
//		
//		URL url = new URL("http://localhost:8080/REST1HolaMundo/rest/holamundo");
//		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//		WebTarget service = client.target("").path("holamundo");
//		System.out.println(service.accept(MediaType.TEXT_PLAIN).get(ClientResponse.class).toString());
//		System.out.println(service.accept( MediaType.TEXT_PLAIN).get(String.class));
//		System.out.println(service.accept( MediaType.TEXT_XML).get(String.class));
//		System.out.println(service.accept( MediaType.TEXT_HTML).get(String.class));
	}
}
