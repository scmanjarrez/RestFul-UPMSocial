package rest;

import java.util.ArrayList;
import java.util.Date;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import rest.post.Post;
import rest.post.PostList;
import rest.user.User;
import rest.user.UserList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
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

	// Devuelve la lista de todos los usuarios
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getUsers() {
		Response resp;
		ArrayList<User> users = null;
		Connection nConexion = null;
		try {
			nConexion = connectToDB();
			users = obtenerUsuarios(nConexion);
			if(users != null)
				resp = Response.ok(new UserList(users)).build();
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
		return resp;
	}

	// Devuelve información de un usuario en concreto
	@GET
	@Path("{username}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getUserInfo(@PathParam("username") String username) {
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
		} finally{
			try {
				nConexion.close();
			} catch (Exception e) {
				System.err.println("Fallo al cerrar la conexión con la DB.");
			}
		}
		return resp;
	}

	// Devuelve la lista de posts publicados por un usuario
	// queryParams = fechaInicio(dd-mm-yyyy), fechaFin(dd-mm-yyyy), from(x) & to(x) (cantidad+offset)
	@GET
	@Path("{username}/posts")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getUserPosts(@PathParam("username") String username, 
			@DefaultValue("") @QueryParam("fechaInicio") String fInicio, 
			@DefaultValue("") @QueryParam("fechaFin") String fFin,
			@DefaultValue("0") @QueryParam("from") int from, 
			@DefaultValue("1000") @QueryParam("to") int to,
			@DefaultValue("%") @QueryParam("content") String content) {
		Response resp;
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		String fechaFin="";
		if(fFin.equals(""))
			fechaFin = df.format(date);
		else
			fechaFin = fFin;

		ArrayList<Post> posts = null;
		Connection nConexion = null;

		try {
			nConexion = connectToDB();
			posts = obtenerPostsUsuario(nConexion, username, fInicio, fechaFin, from, to, content);
			if (posts != null){
				if(posts.isEmpty()){
					resp = Response.status(Response.Status.BAD_REQUEST).build();
				} else 
					resp = Response.ok(new PostList(posts)).build();
			} else
				resp = Response.status(Response.Status.NOT_FOUND).build();

		} catch (ClassNotFoundException | SQLException e) {
			System.err.println("Fallo al conectar a la DB.");
			resp = Response.status(Response.Status.NOT_FOUND).build();
		}finally{
			try {
				nConexion.close();
			} catch (Exception e) {
				System.err.println("Fallo al cerrar la conexión con la DB.");
				resp = Response.status(Response.Status.NOT_FOUND).build();
			}
		}
		return resp;
	}

	@GET
	@Path("/prueba")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getPrueba(@QueryParam("info")String info,@QueryParam("amigos") String amigos){
		Response resp = Response.status(Response.Status.ACCEPTED).build();;
		System.out.println("info="+info+" amigos="+amigos);
		return resp;
	}

	// Devuelve la lista de amigos de un usuario
	//queryParams = posts, amigos, id, fechaInicio, fechaFin, from, to, content
	@GET
	@Path("{username}/friends")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getUserFriends(@PathParam("username") String username, 
			@QueryParam("posts") String posts,
			@QueryParam("amigos") String amigos,
			@QueryParam("id") String id,
			@DefaultValue("0") @QueryParam("from") int from,
			@DefaultValue("1000") @QueryParam("to") int to,
			@DefaultValue("%") @QueryParam("nombre") String nombre,
			@DefaultValue("") @QueryParam("fechaInicio") String fInicio,
			@DefaultValue("") @QueryParam("fechaFin") String fFin,
			@DefaultValue("%") @QueryParam("content") String content) {

		/*
		 * Dar amigos de username /friends
		 * Dar info de amigo de username /friends?id=v130007
		 * Dar posts de amigos de username /friends?posts
		 * Dar post de amigo X de username /friends?posts=v130007
		 * Dar amigos de amigos de username /friends?amigos
		 * Dar amigos de amigo X de username /friends?amigos=v130007
		 */
		Response resp=null;
		if(posts!=null){
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			Date date = new Date();
			String fechaFin="";
			if(fFin.equals(""))
				fechaFin = df.format(date);
			else
				fechaFin = fFin;
			if(posts.equals(""))
				posts="%";
			System.out.println(posts);
			ArrayList<Post> p_list = null;
			Connection nConexion = null;

			try {
				nConexion = connectToDB();
				p_list = obtenerPostsAmigosUsuario(nConexion, username, posts, fInicio, fechaFin, from, to, content);
				System.out.println(p_list.toString());
				if (posts != null){
					resp = Response.ok(new PostList(p_list)).build();
				}
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
			return resp;
		}else if(amigos!=null){
			if(amigos.equals(""))
				amigos="%";
			ArrayList<User> friends = null;
			Connection nConexion = null;

			try {
				nConexion = connectToDB();
				friends = obtenerAmigosAmigoUsuario(nConexion, username, amigos, nombre, from, to);
				if (friends != null){
					resp = Response.ok(new UserList(friends)).build();
				} else
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
			return resp;


		}else if(id != null){
			if(id.equals(""))
				return resp = Response.status(Response.Status.BAD_REQUEST).build();

			User user = null;
			Connection nConexion = null;
			try {
				nConexion = connectToDB();
				user = obtenerInfoAmigoUsuario(nConexion, username, id);
				if(user != null)
					resp = Response.ok(user).build();
				else
					resp = Response.status(Response.Status.NOT_FOUND).build();
			} catch (ClassNotFoundException | SQLException e) {
				System.err.println("Fallo al conectar a la DB.");
				resp = Response.status(Response.Status.NOT_FOUND).build();
			} finally{
				try {
					nConexion.close();
				} catch (Exception e) {
					System.err.println("Fallo al cerrar la conexión con la DB.");
				}
			}
			return resp;
		} else {

			ArrayList<User> friends = null;
			Connection nConexion = null;

			try {
				nConexion = connectToDB();
				friends = obtenerAmigosUsuario(nConexion, username, nombre, from, to);
				if (friends != null){
					resp = Response.ok(new UserList(friends)).build();
				} else
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
			return resp;
		}
	}
	
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
		ResultSet rsTabla = sentencia.executeQuery("select * from USERS where username=\'"+username+"\'");

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
		return user;
	}

	public static ArrayList<Post> obtenerPostsUsuario(Connection conexion, String username,
			String fInicio, String fFin, int from, int to, String content)
					throws SQLException {
		Post post;	
		int aux = to-from;
		ArrayList<Post> p_list = new ArrayList<Post>();
		Statement sentencia = conexion.createStatement();
		ResultSet rsTabla = sentencia.executeQuery("select * from POSTS "
				+ "where author_username=\'"+username+"\' and creation_date between "
				+ "str_to_date(\'"+fInicio+"\',\'%d-%m-%Y\') and "
				+ "str_to_date(\'"+fFin+"\',\'%d-%m-%Y\') and "
				+"content like \'%"+content+"%\' "
				+ "order by creation_date asc limit "+from+","+aux);

		while(rsTabla.next()){
			post = new Post();
			post.setId(rsTabla.getInt("id"));
			post.setAuthor_username(rsTabla.getString("author_username"));
			post.setContent(rsTabla.getString("content"));
			post.setCreation_date((Date)rsTabla.getDate("creation_date"));
			p_list.add(post);
		}
		return p_list;
	}

	public static ArrayList<User> obtenerAmigosUsuario(Connection conexion, String username,
			String nombre, int from, int to)
					throws SQLException {
		User user;	
		int aux = to-from;
		ArrayList<User> u_list = new ArrayList<User>();
		Statement sentencia = conexion.createStatement();
		ResultSet rsTabla = sentencia.executeQuery("select * from USERS "
				+ "join FRIENDS on USERS.username = FRIENDS.friend "
				+ "where FRIENDS.username=\'"+username+"\' "
				+ "and USERS.first_name like \'"+nombre+"\' "
				+ "limit "+from+","+aux);

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

	public static ArrayList<Post> obtenerPostsAmigosUsuario(Connection conexion, String username,
			String author, String fInicio, String fFin, int from, int to, String content)
					throws SQLException {

		Post post;	
		int aux = to-from;
		ArrayList<Post> p_list = new ArrayList<Post>();
		Statement sentencia = conexion.createStatement();
		ResultSet rsTabla = sentencia.executeQuery("select * from POSTS "
				+ "join FRIENDS on POSTS.author_username = FRIENDS.friend "
				+ "where username=\'"+username+"\' and author_username like \'"+author+"\' "
				+ "and creation_date between "
				+ "str_to_date(\'"+fInicio+"\',\'%d-%m-%Y\') and "
				+ "str_to_date(\'"+fFin+"\',\'%d-%m-%Y\') and "
				+ "content like \'%"+content+"%\' "
				+ "order by creation_date asc limit "+from+","+aux);

		while(rsTabla.next()){
			post = new Post();
			post.setId(rsTabla.getInt("id"));
			post.setAuthor_username(rsTabla.getString("author_username"));
			post.setContent(rsTabla.getString("content"));
			post.setCreation_date((Date)rsTabla.getDate("creation_date"));
			p_list.add(post);
		}
		return p_list;
	}

	public static ArrayList<User> obtenerAmigosAmigoUsuario(Connection conexion, String username,
			String amigos, String nombre, int from, int to)
					throws SQLException {

		User user;	
		int aux = to-from;
		ArrayList<User> u_list = new ArrayList<User>();
		Statement sentencia = conexion.createStatement();

		ResultSet rsTabla = sentencia.executeQuery("select * from USERS "
				+ "join FRIENDS on USERS.username = FRIENDS.friend "
				+ "where FRIENDS.username in (select USERS.username from USERS "
				+ "join FRIENDS on USERS.username = FRIENDS.friend "
				+ "where FRIENDS.username=\'"+username+"\' "
				+ "and FRIENDS.friend like \'"+amigos+"\') "
				+ "and first_name like \'"+nombre+"\' "
				+ "limit "+from+","+aux);

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

	public static User obtenerInfoAmigoUsuario(Connection conexion, String username, String id)
			throws SQLException {
		User user = null;
		Statement sentencia = conexion.createStatement();
		ResultSet rsTabla = sentencia.executeQuery("select * from USERS "
				+ "where username in (select USERS.username from USERS "
				+ "join FRIENDS on USERS.username = FRIENDS.friend "
				+ "where FRIENDS.username=\'"+username+"\') "
				+ "and username like \'"+id+"\'");

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
		return user;
	}
}

