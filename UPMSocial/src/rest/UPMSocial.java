package rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import rest.user.*;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
//import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author Sergio Chica Manjarrez
 * 
 * @version 0.1
 * 
 */

@Path("/users")
public class UPMSocial {

	private static String host = "sql8.freemysqlhosting.net";
	private static String user = "sql8115240";
	private static String pass = "wEShxsCLq1";
	private static String db = "sql8115240";
	//private static String host = "localhost";
//	private static String user = "upmsocial";
//	private static String pass = "upmsocial";
//	private static String db = "";
	
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	// Devuelve la lista de todos los usuarios (para uso desde navegador)
	@GET
	@Produces(MediaType.TEXT_XML)
	public UsersList getUsersBrowser() {
		ArrayList<User> users = null;
		Connection nConexion = null;
		try {
			nConexion = connectToDB();
			users = obtenerUsuarios(nConexion);
			System.out.println(users.toString());
		} catch (ClassNotFoundException | SQLException e) {
			System.err.println("Fallo al conectar a la DB.");
		}finally{
			try {
				nConexion.close();
			} catch (Exception e) {
				System.err.println("Fallo al cerrar la conexión con la DB.");
			}
		}

		return new UsersList(users);
	}

	//	  // Devuelve a las aplicaciones cliente la lista de todos
	//	  @GET
	//	  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	//	  public TodoList getTodos() {
	//	    List<Todo> todos = new ArrayList<Todo>();
	//	    todos.addAll(TodoDao.getInstance().getModel().values());
	//	    return new TodoList(todos);
	//	  }
	//
	//	  // Devuelve el nÃºmero de tareas registradas
	//	  @GET
	//	  @Path("count")
	//	  @Produces(MediaType.TEXT_PLAIN)
	//	  public String getCount() {
	//	    int count = TodoDao.getInstance().getModel().size();
	//	    return String.valueOf(count);
	//	  }
	//
	//	  @POST
	//	  @Produces(MediaType.TEXT_HTML)
	//	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//	  public void newTodo(@FormParam("id") String id,
	//	      @FormParam("todo") String summary,
	//	      @FormParam("descripcion") String description,
	//	      @Context HttpServletResponse servletResponse) throws IOException {
	//	    Todo todo = new Todo(id, summary);
	//	    if (description != null) {
	//	      todo.setDescripcion(description);
	//	    }
	//	    TodoDao.getInstance().getModel().put(id, todo);
	//
	//	    servletResponse.sendRedirect("../create_todo.html");
	//	  }
	//	
	//	






//	// Este método se invoca si se solicita TEXT_PLAIN
//	@GET
//	@Produces(MediaType.TEXT_PLAIN)
//	public String saludoPlainText() {
//		return "Hola JAX-RS";
//	}
//
//	@GET
//	@Produces(MediaType.TEXT_XML)
//	public String saludoXML() {
//		return "<?xml version=\"1.0\"?>" + "<hola>Hola JAX-RS" + "</hola>";
//	}
//
//	@GET
//	@Produces(MediaType.TEXT_HTML)
//	public String saludoHtml() {
//		return "<html>" + "<title>" + "Hola JAX-RS" + "</title>"
//				+ "<body><h1>" + "Hola JAX-RS" + "</h1></body>" + "</html> ";
//	}



	public static Connection connectToDB()
			throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		String url= "jdbc:mysql://"+host+":3306/"+db;
		Connection conexion= DriverManager.getConnection(url, user, pass);
		return conexion;	
	}

	public static ArrayList<User> obtenerUsuarios(Connection conexion)
			throws SQLException {
		User user;	
		ArrayList<User> u_list = new ArrayList<User>();
		Statement sentencia = conexion.createStatement();
		ResultSet rsTabla = sentencia.executeQuery("select * from USERS");

		while(rsTabla.next()){
			user = new User();
			user.setUsername(rsTabla.getString("username"));
			user.setFirst_name(rsTabla.getString("first_name"));
			user.setLast_name(rsTabla.getString("last_name"));
			user.setPhone((Integer)rsTabla.getObject("phone"));
			user.setEmail(rsTabla.getString("email"));
			user.setAddress(rsTabla.getString("address"));
			user.setRegister_date(rsTabla.getDate("register_date"));
			u_list.add(user);
		}
		return u_list;
	}

}

