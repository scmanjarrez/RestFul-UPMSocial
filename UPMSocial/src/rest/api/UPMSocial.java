package rest.api;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBElement;

import rest.model.post.Post;
import rest.model.post.PostList;
import rest.model.user.User;
import rest.model.user.UserList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/*
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<post>
<content>El más allá es un lugar lúgubre...</content>
</post>
 */

/*
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<user>
<username>idDePrueba</username>
<first_name>Manolo</first_name>
<phone>910245654</phone>
</user>

 */


//@Path("/")
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

	/***
	 * @LlamadaHTTP
	 * GET http://localhost:8080/UPMSocial/api/users
	 * @return Lista de usuarios de UPMSocial
	 * **/
	// Devuelve la lista de todos los usuarios
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getUsers(@DefaultValue("%") @QueryParam("nombre") String nombre) {
		Response resp;
		ArrayList<User> users = null;
		Connection nConexion = null;
		try {
			nConexion = connectToDB();
			users = obtenerUsuarios(nConexion, nombre);
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

	/***
	 * @LlamadaHTTP
	 * POST http://localhost:8080/UPMSocial/api/users + User_xml
	 * @return OK, en suscripción correcta, de usuario a UPMSocial.
	 * **/
	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response postAddUser(JAXBElement<User> user) {
		Response resp;
		User info = user.getValue();
		User aux;

		Connection nConexion = null;
		try {
			nConexion = connectToDB();
			try {
				aux = obtenerUsuario(nConexion, info.getUsername());
				if(aux != null)
					resp = Response.noContent().build();
				else {
					nuevoUsuario(nConexion, info.getUsername(), info);
					resp = Response.status(Status.CREATED).header("Location", uriInfo.getAbsolutePath().toString()+"/"+info.getUsername()).build();
				}
			} catch (SQLException e) {
				System.err.println("Fallo al ejecutar la query.");
				resp = Response.status(Response.Status.BAD_REQUEST).build();
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
	}

	/***
	 * @LlamadaHTTP
	 * GET http://localhost:8080/UPMSocial/api/users/{id}
	 * @return Devuelve información de usuario "id".
	 * **/
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
			else {
				resp = Response.status(Response.Status.NOT_FOUND).build();
			}
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

	/***
	 * @LlamadaHTTP
	 * PUT http://localhost:8080/UPMSocial/api/users/{id} + User_xml
	 * @return OK, en edición correcta, de información de usuario "id".
	 * **/
	@PUT
	@Path ("{username}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putEditUserData(JAXBElement<User> user, @PathParam("username") String username) {
		Response resp;
		User info = user.getValue();

		Connection nConexion = null;
		try {
			nConexion = connectToDB();
			try {
				int row = editarUsuario(nConexion, username, info);
				if (row == 0)
					resp = Response.status(Status.NOT_FOUND).build();
				else
					resp = Response.status(Status.OK).header("Location", uriInfo.getAbsolutePath().toString()).build();
			} catch (SQLException e) {
				System.err.println("Fallo al ejecutar la query.");
				resp = Response.status(Response.Status.BAD_REQUEST).build();
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
	}

	/***
	 * @LlamadaHTTP
	 * DELETE http://localhost:8080/UPMSocial/api/users/{id}
	 * @return OK, en eliminación correcta, de usuario "id".
	 * **/
	@DELETE
	@Path ("{username}")
	public Response deleteUser(@PathParam("username") String username) {
		Response resp;

		Connection nConexion = null;
		try {
			nConexion = connectToDB();
			try {
				int row = eliminarUsuario(nConexion, username);
				if (row == 0)
					resp = Response.status(Status.NOT_FOUND).build();
				else
					resp = Response.ok().build();
			} catch (SQLException e) {
				System.err.println("Fallo al ejecutar la query.");
				resp = Response.status(Response.Status.BAD_REQUEST).build();
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
	}
	
	/***
	 * @LlamadaHTTP
	 * GET http://localhost:8080/UPMSocial/api/users/{id}/posts
	 * @return Devuelve lista de posts publicados por usuario "id".
	 * 
	 * @QueryParams fechaInicio, fechaFin, from, to, content
	 * **/
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
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 1);
		String fechaFin="";
		if(fFin.equals(""))
			fechaFin = df.format(c.getTime());
		else {
			try {
				c.setTime(df.parse(fFin));
			} catch (ParseException e) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
			c.add(Calendar.DATE, 1);
			fechaFin = df.format(c.getTime());
		}

		ArrayList<Post> posts = null;
		Connection nConexion = null;

		try {
			nConexion = connectToDB();
			try{
			posts = obtenerPostsUsuario(nConexion, username, fInicio, fechaFin, from, to, content);
			if (posts != null){
				resp = Response.ok(new PostList(posts)).build();
			} else
				resp = Response.status(Response.Status.NOT_FOUND).build();
			} catch (SQLException e){
				resp = Response.status(Response.Status.BAD_REQUEST).build();
			}
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
	
	/***
	 * @LlamadaHTTP
	 * GET http://localhost:8080/UPMSocial/api/users/{id}/posts/count
	 * @return Devuelve total de posts publicados por usuario "id".
	 * 
	 * @QueryParams fechaInicio, fechaFin
	 * **/
	@GET
	@Path("{username}/posts/count")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getUserPostsTotal(@PathParam("username") String username, 
			@DefaultValue("") @QueryParam("fechaInicio") String fInicio, 
			@DefaultValue("") @QueryParam("fechaFin") String fFin) {
		Response resp;
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 1);
		String fechaFin="";
		if(fFin.equals(""))
			fechaFin = df.format(c.getTime());
		else {
			try {
				c.setTime(df.parse(fFin));
			} catch (ParseException e) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
			c.add(Calendar.DATE, 1);
			fechaFin = df.format(c.getTime());
		}

		Connection nConexion = null;

		try {
			nConexion = connectToDB();
			User user = obtenerUsuario(nConexion, username);
			if (user != null){
				Integer total = obtenerTotalPostUsuario(nConexion, username, fInicio, fechaFin);
				System.out.println(total.toString());
				resp = Response.ok(total.toString()).build();
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
	

	/***
	 * @LlamadaHTTP
	 * POST http://localhost:8080/UPMSocial/api/users/{id}/posts + Post_xml
	 * @return OK, en creación correcta, de post de usuario "id".
	 * **/
	@POST
	@Path("{username}/posts")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response postCreateUserPost(JAXBElement<Post> post, @PathParam("username") String username) {
		Response resp;
		Post info = post.getValue();

		Connection nConexion = null;
		try {
			nConexion = connectToDB();
			try {
				int id = nuevoPost(nConexion, username, info);
				resp = Response.status(Status.CREATED).header("Location", uriInfo.getAbsolutePath().toString()+"/"+id).build();
			} catch (SQLException e) {
				System.err.println("Fallo al ejecutar la query.");
				resp = Response.status(Response.Status.BAD_REQUEST).build();
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
	}
	
	/***
	 * @LlamadaHTTP
	 * GET http://localhost:8080/UPMSocial/api/users/{id}/posts/{postID}
	 * @return Devuelve información de post "postID" de usuario "id"
	 * **/
	@GET
	@Path("{username}/posts/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getPostUser(@PathParam("username") String username, @PathParam("id") String postID) {
		Response resp;
		Post post = null;
		Connection nConexion = null;
		try {
			nConexion = connectToDB();
			post = obtenerPostUsuario(nConexion, username, postID);
			if(post != null)
				resp = Response.ok(post).build();
			else {
				resp = Response.status(Response.Status.NOT_FOUND).build();
			}
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

	/***
	 * @LlamadaHTTP
	 * PUT http://localhost:8080/UPMSocial/api/users/{id}/posts/{postID} + Post_xml
	 * @return OK, en edición correcta, de post "postID" de usuario "id".
	 * **/
	@PUT
	@Path("{username}/posts/{id}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putEditUserPost(JAXBElement<Post> post, @PathParam("username") String username,
			@PathParam("id") int id) {
		Response resp;
		Post info = post.getValue();

		Connection nConexion = null;
		try {
			nConexion = connectToDB();
			try {
				int row = editarPost(nConexion, username, info, id);
				if (row == 0)
					resp = Response.status(Status.NOT_FOUND).build();
				else 
					resp = Response.status(Status.OK).header("Location", uriInfo.getAbsolutePath().toString()).build();
			} catch (SQLException e) {
				System.err.println("Fallo al ejecutar la query.");
				resp = Response.status(Response.Status.BAD_REQUEST).build();
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
	}

	/***
	 * @LlamadaHTTP
	 * DELETE http://localhost:8080/UPMSocial/api/users/{id}/posts/{postID}
	 * @return OK, en eliminación correcta, de post "postID" de usuario "id".
	 * **/
	@DELETE
	@Path("{username}/posts/{id}")
	public Response deleteUserPost(@PathParam("username") String username, @PathParam("id") int id) {
		Response resp;
		Connection nConexion = null;
		try {
			nConexion = connectToDB();
			try {
				int row = eliminarPost(nConexion, username, id);
				if (row == 0)
					resp = Response.status(Status.NOT_FOUND).build();
				else
					resp = Response.ok().build();
			} catch (SQLException e) {
				System.err.println("Fallo al ejecutar la query.");
				resp = Response.status(Response.Status.BAD_REQUEST).build();
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
	}

	/***
	 * @LlamadasHTTP
	 * GET http://localhost:8080/UPMSocial/api/users/{id}/friends
	 * @Devuelve Devuelve tu lista de amigos.
	 * @LlamadasHTTP
	 * GET http://localhost:8080/UPMSocial/api/users/{id}/friends?id={friendID}
	 * @Devuelve Devuelve información de un amigo "friendID".
	 * @LlamadasHTTP
	 * GET http://localhost:8080/UPMSocial/api/users/{id}/friends?posts
	 * @Devuelve Devuelve lista de posts publicados por tus amigos.
	 * @LlamadasHTTP
	 * GET http://localhost:8080/UPMSocial/api/users/{id}/friends?posts={friendID}
	 * @Devuelve Devuelve lista de posts publicados de un amigo "friendID".
	 * @LlamadasHTTP
	 * GET http://localhost:8080/UPMSocial/api/users/{id}/friends?amigos
	 * @Devuelve Devuelve la lista de amigos de tus amigos.
	 * @LlamadasHTTP
	 * GET http://localhost:8080/UPMSocial/api/users/{id}/friends?amigos={friendID}
	 * @Devuelve Devuelve lista de amigos de un amigo "friendID".
	 * 
	 * @QueryParams fechaInicio, fechaFin, from, to, content, nombre
	 * **/
	@GET
	@Path("{username}/friends")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getUserFriendsInfo(@PathParam("username") String username, 
			@QueryParam("posts") String posts,
			@QueryParam("amigos") String amigos,
			@QueryParam("id") String id,
			@DefaultValue("0") @QueryParam("from") int from,
			@DefaultValue("1000") @QueryParam("to") int to,
			@DefaultValue("%") @QueryParam("nombre") String nombre,
			@DefaultValue("") @QueryParam("fechaInicio") String fInicio,
			@DefaultValue("") @QueryParam("fechaFin") String fFin,
			@DefaultValue("%") @QueryParam("content") String content) {

		Response resp=null;
		if(posts!=null){
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, 1);
			String fechaFin="";
			if(fFin.equals(""))
				fechaFin = df.format(c.getTime());
			else {
				try {
					c.setTime(df.parse(fFin));
				} catch (ParseException e) {
					return Response.status(Response.Status.BAD_REQUEST).build();
				}
				c.add(Calendar.DATE, 1);
				fechaFin = df.format(c.getTime());
			}

			if(posts.equals(""))
				posts="%";

			ArrayList<Post> p_list = null;
			Connection nConexion = null;

			try {
				nConexion = connectToDB();
				p_list = obtenerPostsAmigosUsuario(nConexion, username, posts, fInicio, fechaFin, from, to, content);
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
	
	/***
	 * @LlamadaHTTP
	 * GET http://localhost:8080/UPMSocial/api/users/{id}/friends/count
	 * @return Devuelve total de amigos de usuario "id".
	 * 
	 * **/
	@GET
	@Path("{username}/friends/count")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getUserFriendsTotal(@PathParam("username") String username) {
		Response resp;

		Connection nConexion = null;

		try {
			nConexion = connectToDB();
			User user = obtenerUsuario(nConexion, username);
			if (user != null){
				Integer total = obtenerTotalAmigosUsuario(nConexion, username);
				resp = Response.ok(total.toString()).build();
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
	

	/***
	 * @LlamadaHTTP
	 * POST http://localhost:8080/UPMSocial/api/users/{id}/friends?operacion + user_xml
	 * @return OK, en actualización correcta, de tu lista de amigos
	 * @QueryParam añadir, eliminar
	 * **/
	@POST
	@Path("{username}/friends")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response postAddDeleteFriendUser(@PathParam("username") String username, JAXBElement<User> user, 
			@QueryParam("añadir") String add, @QueryParam("eliminar") String delete) {

		Response resp;
		User info = user.getValue();

		Connection nConexion = null;
		try {
			nConexion = connectToDB();
			try {
				if(add != null){
					int row = nuevoAmigo(nConexion, username, info.getUsername());
					if (row == 0)
						resp = Response.status(Status.NOT_FOUND).build();
					else
						resp = Response.status(Status.CREATED).header("Location", uriInfo.getAbsolutePath().toString()+"?id="+info.getUsername()).build();

				}else if(delete != null){
					int row = eliminarAmigo(nConexion, username, info.getUsername());
					if (row == 0)
						resp = Response.status(Status.NOT_FOUND).build();
					else
						resp = Response.ok().build();
				}else
					resp = Response.status(Status.BAD_REQUEST).build();
			} catch (SQLException e) {
				System.err.println("Fallo al ejecutar la query.");
				resp = Response.status(Response.Status.BAD_REQUEST).build();
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
	}

	public static Connection connectToDB()
			throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		String url= "jdbc:mysql://"+host+":3306/"+db;
		Connection conexion= DriverManager.getConnection(url, user, pass);
		return conexion;	
	}

	public static ArrayList<User> obtenerUsuarios(Connection conexion, String nombre)
			throws SQLException {
		User user;	
		ArrayList<User> u_list = new ArrayList<User>();

		PreparedStatement sentencia = conexion.prepareStatement("select * from USERS "
				+ "where first_name like ?");

		sentencia.setString(1, "%"+nombre+"");

		ResultSet rsTabla = sentencia.executeQuery();

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
		PreparedStatement sentencia = conexion.prepareStatement("select * from USERS where username = ?");
		sentencia.setString(1, username);

		ResultSet rsTabla = sentencia.executeQuery();

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
		PreparedStatement sentencia = conexion.prepareStatement("select * from POSTS "
				+ "where author_username = ? and creation_date between "
				+ "str_to_date(?,?) and str_to_date(?,?) and "
				+ "content like ? "
				+ "order by creation_date asc limit ?,?");
		sentencia.setString(1, username);
		sentencia.setString(2, fInicio);
		sentencia.setString(3, "%d-%m-%Y");
		sentencia.setString(4, fFin);
		sentencia.setString(5, "%d-%m-%Y");
		sentencia.setString(6, "%"+content+"%");
		sentencia.setInt(7, from);
		sentencia.setInt(8, aux);

		ResultSet rsTabla = sentencia.executeQuery();

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
	
	public static Post obtenerPostUsuario(Connection conexion, String username, String postID)
			throws SQLException {
		Post post = null;
		PreparedStatement sentencia = conexion.prepareStatement("select * from POSTS where author_username = ? "
				+ "and id = ?");
		sentencia.setString(1, username);
		sentencia.setString(2, postID);

		ResultSet rsTabla = sentencia.executeQuery();

		while(rsTabla.next()){
			post = new Post();
			post.setId(rsTabla.getInt("id"));
			post.setAuthor_username(rsTabla.getString("author_username"));
			post.setContent(rsTabla.getString("content"));
			post.setCreation_date((Date)rsTabla.getDate("creation_date"));
		}
		return post;
	}
	
	public static int obtenerTotalPostUsuario(Connection conexion, String username,
			String fInicio, String fFin)
					throws SQLException {
		
		PreparedStatement sentencia = conexion.prepareStatement("select count(*) as Total from POSTS "
				+ "where author_username = ? and creation_date between "
				+ "str_to_date(?,?) and str_to_date(?,?)");
		
		sentencia.setString(1, username);
		sentencia.setString(2, fInicio);
		sentencia.setString(3, "%d-%m-%Y");
		sentencia.setString(4, fFin);
		sentencia.setString(5, "%d-%m-%Y");

		ResultSet rsTabla = sentencia.executeQuery();

		rsTabla.next();
		return rsTabla.getInt("Total");
	}

	public static ArrayList<User> obtenerAmigosUsuario(Connection conexion, String username,
			String nombre, int from, int to)
					throws SQLException {

		User user;	
		int aux = to-from;
		ArrayList<User> u_list = new ArrayList<User>();

		PreparedStatement sentencia = conexion.prepareStatement("select * from USERS "
				+ "join FRIENDS on USERS.username = FRIENDS.friend "
				+ "where FRIENDS.username = ? "
				+ "and USERS.first_name like ? "
				+ "limit ?,?");

		sentencia.setString(1, username);
		sentencia.setString(2, "%"+nombre+"%");
		sentencia.setInt(3, from);
		sentencia.setInt(4, aux);

		ResultSet rsTabla = sentencia.executeQuery();

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
	
	public static int obtenerTotalAmigosUsuario(Connection conexion, String username)
					throws SQLException {
		
		PreparedStatement sentencia = conexion.prepareStatement("select count(*) as Total from FRIENDS "
				+ "where username = ?");
		
		sentencia.setString(1, username);

		ResultSet rsTabla = sentencia.executeQuery();

		rsTabla.next();
		return rsTabla.getInt("Total");
	}

	public static ArrayList<Post> obtenerPostsAmigosUsuario(Connection conexion, String username,
			String author, String fInicio, String fFin, int from, int to, String content)
					throws SQLException {

		Post post;	
		int aux = to-from;
		ArrayList<Post> p_list = new ArrayList<Post>();

		PreparedStatement sentencia = conexion.prepareStatement("select * from POSTS "
				+ "join FRIENDS on POSTS.author_username = FRIENDS.friend "
				+ "where username = ? and author_username like ? "
				+ "and creation_date between "
				+ "str_to_date(?,?) and str_to_date(?,?) and "
				+ "content like ? "
				+ "order by creation_date asc limit ?,?");

		sentencia.setString(1, username);
		sentencia.setString(2, author);
		sentencia.setString(3, fInicio);
		sentencia.setString(4, "%d-%m-%Y");
		sentencia.setString(5, fFin);
		sentencia.setString(6, "%d-%m-%Y");
		sentencia.setString(7, "%"+content+"%");
		sentencia.setInt(8, from);
		sentencia.setInt(9, aux);

		ResultSet rsTabla = sentencia.executeQuery();

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

		PreparedStatement sentencia = conexion.prepareStatement("select * from USERS "
				+ "join FRIENDS on USERS.username = FRIENDS.friend "
				+ "where FRIENDS.username in (select USERS.username from USERS "
				+ "join FRIENDS on USERS.username = FRIENDS.friend "
				+ "where FRIENDS.username = ? "
				+ "and FRIENDS.friend like ?) "
				+ "and first_name like ? "
				+ "limit ?,?");

		sentencia.setString(1, username);
		sentencia.setString(2, amigos);
		sentencia.setString(3, "%"+nombre+"%");
		sentencia.setInt(4, from);
		sentencia.setInt(5, aux);

		ResultSet rsTabla = sentencia.executeQuery();

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

		PreparedStatement sentencia = conexion.prepareStatement("select * from USERS "
				+ "where username in (select USERS.username from USERS "
				+ "join FRIENDS on USERS.username = FRIENDS.friend "
				+ "where FRIENDS.username = ?) "
				+ "and username like ?");

		sentencia.setString(1, username);
		sentencia.setString(2, id);

		ResultSet rsTabla = sentencia.executeQuery();

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

	public static int nuevoUsuario(Connection conexion, String username, User user)
			throws SQLException {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String reg_date = df.format(date);

		PreparedStatement query = conexion.prepareStatement("insert into USERS values (?,?,?,?,?,?,?)");
		query.setString(1, user.getUsername());
		query.setString(2, user.getFirst_name());
		query.setString(3, user.getLast_name());
		if(user.getPhone() != null){
			query.setInt(4, user.getPhone());
		}else{ 
			query.setNull(4, Types.INTEGER);
		}
		query.setString(5, user.getEmail());
		query.setString(6, user.getAddress());
		query.setString(7, reg_date);

		return query.executeUpdate();
	}

	public static int editarUsuario(Connection conexion, String username, User user)
			throws SQLException {

		String sql = "update USERS set";
		if(user.getFirst_name() != null)
			sql = sql +" first_name = ?,";
		if(user.getLast_name() != null)
			sql = sql +" last_name = ?,";
		if(user.getPhone() != null)
			sql = sql +" phone = ?,";
		if(user.getEmail() != null)
			sql = sql +" email = ?,";
		if(user.getAddress() != null)
			sql = sql +" address = ?,";
		sql = sql.substring(0,sql.length()-1)+" ";
		sql = sql+"where username = ?";


		PreparedStatement query = conexion.prepareStatement(sql);

		int index = 1;

		if(user.getFirst_name() != null)
			query.setString(index++, user.getFirst_name());

		if(user.getLast_name() != null)
			query.setString(index++, user.getLast_name());

		if(user.getPhone() != null){
			if(user.getPhone() != 0)
				query.setInt(index++, user.getPhone());
			else 
				query.setNull(index++, Types.INTEGER);
		}

		if(user.getEmail() != null)
			query.setString(index++, user.getEmail());

		if(user.getAddress() != null)
			query.setString(index++, user.getAddress());

		query.setString(index, username);

		return query.executeUpdate();
	}

	public static int eliminarUsuario(Connection conexion, String username)
			throws SQLException {

		PreparedStatement query = conexion.prepareStatement("delete from USERS "
				+ "where username = ?");

		query.setString(1, username);

		return query.executeUpdate();
	}

	public static int nuevoPost(Connection conexion, String username, Post post)
			throws SQLException {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = new Date();
		String creat_date = df.format(date);

		PreparedStatement query = conexion.prepareStatement("insert into POSTS(author_username, content, creation_date) "
				+ "values (?,?,?)", Statement.RETURN_GENERATED_KEYS);
		query.setString(1, username);
		query.setString(2, post.getContent());
		query.setString(3, creat_date);

		query.executeUpdate();

		ResultSet generatedKeys = query.getGeneratedKeys();
		generatedKeys.next();

		return generatedKeys.getInt(1);
	}

	public static int editarPost(Connection conexion, String username, Post post, int id)
			throws SQLException {


		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = new Date();
		String edit_date = df.format(date);

		PreparedStatement query = conexion.prepareStatement("update POSTS set content = ?, "
				+ "last_edited = ? "
				+ "where author_username = ? and id = ?");

		query.setString(1, post.getContent());
		query.setString(2, edit_date);
		query.setString(3, username);
		query.setInt(4, id);

		return query.executeUpdate();
	}

	public static int eliminarPost(Connection conexion, String username, int id)
			throws SQLException {

		PreparedStatement query = conexion.prepareStatement("delete from POSTS "
				+ "where author_username = ? and id = ?");

		query.setString(1, username);
		query.setInt(2, id);

		return query.executeUpdate();
	}

	public static int nuevoAmigo(Connection conexion, String username, String amigo)
			throws SQLException {

		PreparedStatement query = conexion.prepareStatement("insert into FRIENDS "
				+ "values (?,?), (?,?)");
		query.setString(1, username);
		query.setString(2, amigo);
		query.setString(3, amigo);
		query.setString(4, username);

		return query.executeUpdate();
	}

	public static int eliminarAmigo(Connection conexion, String username, String amigo)
			throws SQLException {

		PreparedStatement query = conexion.prepareStatement("delete from FRIENDS "
				+ "where (username,friend) in ((?,?),(?,?))");
		query.setString(1, username);
		query.setString(2, amigo);
		query.setString(3, amigo);
		query.setString(4, username);

		return query.executeUpdate();
	}


}