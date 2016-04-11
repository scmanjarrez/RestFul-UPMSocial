package jersey.todo.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

import jersey.todo.dao.TodoDao;
import jersey.todo.model.Todo;

@Path("todos")
public class TodoResource {
  @Context
  UriInfo uriInfo;
  @Context
  Request request;
  String id;
  
  // Web API     
  @Path("{todo}")
  @GET
  @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  public Response getTodo(@PathParam("todo") String id) {
	  Response res;
	  Todo todo;
	  if(TodoDao.getInstance().getModel().containsKey(id)) {
		  todo = TodoDao.getInstance().getModel().get(id);
	      res = Response.ok(todo).build();
	  } else {
		  //throw new RuntimeException("Get: Tarea con id " + id +  " no encontrada");
	      res = Response.status(Response.Status.NOT_FOUND).build();
	  }
	  return res;
  }
  
  // Para testing (Navegador como cliente)
  @Path("{todo}")
  @GET
  @Produces(MediaType.TEXT_XML)
  public Response getTodoHTML(@PathParam("todo") String id) {
	  Response res;
	  Todo todo;
	  if(TodoDao.getInstance().getModel().containsKey(id)) {
		  todo = TodoDao.getInstance().getModel().get(id);
	      res = Response.ok(todo).build();
	  } else {
		  //throw new RuntimeException("Get: Tarea con id " + id +  " no encontrada");
	      res = Response.status(Response.Status.NOT_FOUND).build();
	  }
	  return res;
  }
  
  @PUT
  @Path("{todo}")
  @Consumes(MediaType.APPLICATION_XML)
  public Response putTodo(JAXBElement<Todo> todo) {
    Todo c = todo.getValue();
    return putAndGetResponse(c);
  }
  
  @DELETE
  @Path("{todo}")
  public Response deleteTodo(@PathParam("todo") String id) {
	  Response res;
	  if(TodoDao.getInstance().getModel().containsKey(id)) {
		  TodoDao.getInstance().getModel().remove(id);
	      res = Response.ok().build();
	  } else {
		  //throw new RuntimeException("Delete: Tarea con id " + id +  " no encontrada");
	      res = Response.noContent().build();
	  }
	  return res;
  }
  
  private Response putAndGetResponse(Todo todo) {
    Response res;
    if(TodoDao.getInstance().getModel().containsKey(todo.getId())) {
      res = Response.noContent().build();
    } else {
      TodoDao.getInstance().getModel().put(todo.getId(), todo);
      res = Response.created(uriInfo.getAbsolutePath()).header("Location", uriInfo.getAbsolutePath().toString()).build();
    }
    return res;
  }
} 
