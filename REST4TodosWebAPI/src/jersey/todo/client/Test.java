package jersey.todo.client;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.client.ClientConfig;
//import javax.ws.rs.client.config.DefaultClientConfig;
//import javax.ws.rs.representation.Form;

import jersey.todo.model.Todo;
import jersey.todo.model.TodoList;

import java.util.Iterator;

public class Test {
  public static void main(String[] args) {

	    ClientConfig config = new ClientConfig();
	    Client client = ClientBuilder.newClient(config);
	    WebTarget target = client.target(getBaseURI());
	    // crea una tarea
	    Todo todo = new Todo("5", "Blabla");
	    todo.setDescripcion("bla-bla-bla-bla");
	    Response response = target.path("rest").path("todos")
	    	.path(todo.getId()).request().accept(MediaType.APPLICATION_XML)
	    	.put(Entity.xml(todo),Response.class);
	    // Se comprueba que el código http devuelto es 201 == created
	    System.out.println(response.getStatus());
		if(response.getHeaders().containsKey("Location")) {
			Object location = response.getHeaders().get("Location").get(0);
			System.out.println("Location: " + location.toString());
		}  
	    // Obtiene los Todos en xml
	    System.out.println(target.path("rest").path("todos").request()
	        .accept(MediaType.TEXT_XML).get(String.class));
	  /*  // Obtiene los Todos en JSON para una aplicación
	    System.out.println(target.path("rest").path("todos").request()
	        .accept(MediaType.APPLICATION_JSON).get(String.class));*/
	    // Obtiene los Todos en JSON para una aplicación
	    System.out.println(target.path("rest").path("todos").request()
	        .accept(MediaType.APPLICATION_XML).get(String.class));

	    //Obtiene los Todos en XML para una aplicación
	    TodoList lista = target.path("rest").path("todos").request()
	        .accept(MediaType.APPLICATION_XML).get(TodoList.class);
	    Iterator<Todo> i  = lista.getL().iterator();
	    while (i.hasNext()) {
	    	System.out.println(i.next().getTarea());
	    }
	    
	    // Obtiene el Todo con id 5
	    System.out.println(target.path("rest").path("todos/5").request()
	        .accept(MediaType.APPLICATION_XML).get(String.class));

	    // Obtiene el Todo con id 5 como objeto de Todo.class
	    System.out.println(target.path("rest").path("todos/5").request()
	        .accept(MediaType.APPLICATION_XML).get(Todo.class).getTarea());
	    
	    // Elimina el Todo con id 5
	    response = target.path("rest").path("todos/5").request().delete();
	    System.out.println(response.getStatus());
	    // Obtiene el Todo con id 5
	    response = target.path("rest").path("todos")
	        	.path("5").request().accept(MediaType.APPLICATION_XML)
	        	.get(Response.class);
	    // Se comprueba que el código http devuelto es 404 == not found
	    System.out.println(response.getStatus());	  
	  
// create a Todo
//    Form form = new Form();
//    form.add("id", "4");
//    form.add("summary", "Demonstration of the client lib for forms");
//    response = service.path("rest").path("todos")
//        .type(MediaType.APPLICATION_FORM_URLENCODED)
//        .post(ClientResponse.class, form);
//    System.out.println("Form response " + response.getEntity(String.class));
    // Get the all todos, id 4 should be created
//    System.out.println(service.path("rest").path("todos")
//        .accept(MediaType.APPLICATION_XML).get(String.class));

  }

  private static URI getBaseURI() {
    return UriBuilder.fromUri("http://localhost:8080/REST4TodosWebAPI/").build();
  }
} 