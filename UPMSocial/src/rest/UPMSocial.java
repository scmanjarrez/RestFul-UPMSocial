package rest;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import rest.posts.Post;
import rest.user.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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

	// Devuelve a las aplicaciones cliente la lista de todos los usuarios
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public UsersList getUsersClient() {
		ArrayList<User> users = null;
		Connection nConexion = null;
		try {
			nConexion = connectToDB();
			users = obtenerUsuarios(nConexion);
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
	
	// Devuelve información de un usuario en concreto (para uso desde navegador)
	@GET
	@Path("{username}")
	@Produces(MediaType.TEXT_XML)
	public Response getUserBrowser(@PathParam("username") String username) {
		Response resp;
		User user = null;
		Connection nConexion = null;
		try {
			nConexion = connectToDB();
			user = obtenerUsuario(nConexion, username);
			if(user != null)
				resp = Response.ok(user).build();
			else
				resp = Response.status(Response.Status.NOT_FOUND).build();
		} catch (ClassNotFoundException | SQLException e) {
			System.err.println("Fallo al conectar a la DB.");
			resp = Response.status(Response.Status.NOT_FOUND).build();
		}finally{
			try {
				nConexion.close();
			} catch (Exception e) {
				System.err.println("Fallo al cerrar la conexión con la DB.");
			}
		}
		System.err.println(resp.toString());
		return resp;
	}
	
	// Devuelve información de un usuario en concreto (para uso desde cliente)
	@GET
	@Path("{username}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getUserClient(@PathParam("username") String username) {
		Response resp;
		User user = null;
		Connection nConexion = null;
		try {
			nConexion = connectToDB();
			user = obtenerUsuario(nConexion, username);
			if(user != null)
				resp = Response.ok(user).build();
			else
				resp = Response.status(Response.Status.NOT_FOUND).build();
		} catch (ClassNotFoundException | SQLException e) {
			System.err.println("Fallo al conectar a la DB.");
			resp = Response.status(Response.Status.NOT_FOUND).build();
		}finally{
			try {
				nConexion.close();
			} catch (Exception e) {
				System.err.println("Fallo al cerrar la conexión con la DB.");
			}
		}
		System.err.println(resp.toString());
		return resp;
	}
	/*
	// Devuelve la lista de posts publicados por un usuario (para uso desde navegador)
	//queryParams = fechaInicio, fechaFin, from & to (cantidad+offset)
	@GET
	@Path("{username}/posts")
	@Produces(MediaType.TEXT_XML)
	public Response getUserPostsBrowser(@PathParam("username") String username, 
			@DefaultValue("") @QueryParam("fechaInicio") String fInicio, 
			@DefaultValue("") @QueryParam("fechaFin") String fFin,
			@DefaultValue("0") @QueryParam("from") int from, 
			@DefaultValue("1000") @QueryParam("to") int to) {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		String fechaFin;// = getDateFromString(fFin);
		if(fFin.equals(""))
			fechaFin = dateFormat.format(date);
		
		Response resp;
		ArrayList<Post> posts = null;
		Connection nConexion = null;
		
		try {
			nConexion = connectToDB();
			posts = obtenerPostsUsuario(nConexion, username, fInicio, fechaFin, from, to);
			if(user != null)
				resp = Response.ok(user).build();
			else
				resp = Response.status(Response.Status.NOT_FOUND).build();
		} catch (ClassNotFoundException | SQLException e) {
			System.err.println("Fallo al conectar a la DB.");
			resp = Response.status(Response.Status.NOT_FOUND).build();
		}finally{
			try {
				nConexion.close();
			} catch (Exception e) {
				System.err.println("Fallo al cerrar la conexión con la DB.");
			}
		}
		System.err.println(resp.toString());
		return resp;
	}
	*/

	
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
			user.setRegister_date((Date)rsTabla.getDate("register_date"));
			u_list.add(user);
		}
		return u_list;
	}
	
	public static User obtenerUsuario(Connection conexion, String username)
			throws SQLException {
		User user = null;
		Statement sentencia = conexion.createStatement();
		ResultSet rsTabla = sentencia.executeQuery("select * from USERS where username="+"\'"+username+"\'");

		while(rsTabla.next()){
			user = new User();
			user.setUsername(rsTabla.getString("username"));
			user.setFirst_name(rsTabla.getString("first_name"));
			user.setLast_name(rsTabla.getString("last_name"));
			user.setPhone((Integer)rsTabla.getObject("phone"));
			user.setEmail(rsTabla.getString("email"));
			user.setAddress(rsTabla.getString("address"));
			user.setRegister_date((Date)rsTabla.getDate("register_date"));
		}
		System.err.println("Información obtenida");
		return user;
	}
	
	public static ArrayList<Post> obtenerPostsUsuario(Connection conexion, String username,
			Date fInicio, Date fFin, int from, int to)
			throws SQLException {
		Post post;	
		int aux = to-from;
		ArrayList<Post> p_list = new ArrayList<Post>();
		Statement sentencia = conexion.createStatement();
		ResultSet rsTabla = sentencia.executeQuery("select * from POSTS "
				+ "where author_username="+"\'"+username+"\' and creation_date between "
						+ "str_to_date(\'"+fInicio+"\',\'%d-%m-%Y\') and "
						+ "str_to_date(\'"+fFin+"\',\'%d-%m-%Y\') limit "+from+","+aux);

		while(rsTabla.next()){
			post = new Post();
			post.setId(rsTabla.getInt("id"));
			post.setContent(rsTabla.getString("content"));
			post.setCreation_date((Date)rsTabla.getDate("creation_date"));
			p_list.add(post);
		}
		return p_list;
	}
	
	
	private Date getDateFromString(String dateString) {
		Date date;
	    try {
	        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        date = df.parse(dateString);
	    } catch (ParseException e) {
	    	return null;
	        //WebApplicationException ...("Date format should be yyyy-MM-dd'T'HH:mm:ss", Status.BAD_REQUEST);
	    }
	    return date;
	}

}

