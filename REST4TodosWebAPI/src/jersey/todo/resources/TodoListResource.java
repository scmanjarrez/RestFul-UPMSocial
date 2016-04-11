package jersey.todo.resources;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import jersey.todo.dao.TodoDao;
import jersey.todo.model.Todo;
import jersey.todo.model.TodoList;

@Path("/todos")
public class TodoListResource {

  // Permite instertar objetos de contexto en la clase,
  // e.g. ServletContext, Request, Response, UriInfo
  @Context
  UriInfo uriInfo;
  @Context
  Request request;

  // Devuelve la lista de todos al usuario (para uso desde navegador)
  @GET
  @Produces(MediaType.TEXT_XML)
  public TodoList getTodosBrowser() {
    List<Todo> todos = new ArrayList<Todo>();
    todos.addAll(TodoDao.getInstance().getModel().values());
    return new TodoList(todos);
  }

  // Devuelve a las aplicaciones cliente la lista de todos
  @GET
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public TodoList getTodos() {
    List<Todo> todos = new ArrayList<Todo>();
    todos.addAll(TodoDao.getInstance().getModel().values());
    return new TodoList(todos);
  }

  // Devuelve el número de tareas registradas
  @GET
  @Path("count")
  @Produces(MediaType.TEXT_PLAIN)
  public String getCount() {
    int count = TodoDao.getInstance().getModel().size();
    return String.valueOf(count);
  }

  @POST
  @Produces(MediaType.TEXT_HTML)
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public void newTodo(@FormParam("id") String id,
      @FormParam("todo") String summary,
      @FormParam("descripcion") String description,
      @Context HttpServletResponse servletResponse) throws IOException {
    Todo todo = new Todo(id, summary);
    if (description != null) {
      todo.setDescripcion(description);
    }
    TodoDao.getInstance().getModel().put(id, todo);

    servletResponse.sendRedirect("../create_todo.html");
  }

} 