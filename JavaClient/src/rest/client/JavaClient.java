package rest.client;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.client.ClientConfig;

import rest.model.post.Post;
import rest.model.post.PostList;
import rest.model.user.User;
import rest.model.user.UserList;

import java.util.Iterator;

public class JavaClient {
	public static void main(String[] args) {

		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		WebTarget target = client.target(getBaseURI());

		Response response;
		User user = new User();
		Post post = new Post();
		UserList u_list;
		Iterator<User> it_u_list;
		PostList p_list;
		Iterator<Post> it_p_list; 
		String postID="";

		/* Pedimos todos los usuarios actuales */
		System.out.println("\n----------GET /users");
		
		u_list = target.path("api").path("users").request()
				.accept(MediaType.APPLICATION_XML).get(UserList.class);
		
		it_u_list  = u_list.getUser().iterator();
		System.out.println("Lista de usuarios");
		while (it_u_list.hasNext()) {
			System.out.println(it_u_list.next());
		}

		/* Creamos un usuario*/
		user.setUsername("usuarioJava");
		user.setFirst_name("JavaUserFirtsName");
		user.setPhone(910214412);
		user.setEmail("javaclient@gmail.com");

		System.out.println("\n----------POST /users + xml");
		
		response = target.path("api").path("users").request()
				.post(Entity.xml(user), Response.class);
		System.out.println("Status: "+response.getStatus());
		
		if(response.getHeaders().containsKey("Location")) {
			Object location = response.getHeaders().get("Location").get(0);
			System.out.println("Location: " + location.toString());
		}

		/* Volvemos a pedir los usuarios para comprobar que sí se ha creado */
		System.out.println("\n----------GET /users");
		
		u_list = target.path("api").path("users").request()
				.accept(MediaType.APPLICATION_XML).get(UserList.class);
		it_u_list  = u_list.getUser().iterator();
		System.out.println("Lista de usuarios");
		while (it_u_list.hasNext()) {
			System.out.println(it_u_list.next());
		}

		/* Pedimos la información de usuarioJava */
		System.out.println("\n----------GET /users/usuarioJava");
		
		user = target.path("api").path("users").path("usuarioJava").request()
				.accept(MediaType.APPLICATION_XML).get(User.class);
		System.out.println("Información de usuario");
		System.out.println(user);

		/* Modificamos su perfil */
		user.setFirst_name("nombreModificado");
		user.setLast_name("apellidoModificado");
		user.setPhone(0);
		
		System.out.println("\n----------PUT /users/usuarioJava + xml");
		
		response = target.path("api").path("users").path("usuarioJava").request()
				.put(Entity.xml(user), Response.class);
		System.out.println("Status: "+response.getStatus());
		
		if(response.getHeaders().containsKey("Location")) {
			Object location = response.getHeaders().get("Location").get(0);
			System.out.println("Location: " + location.toString());
		}

		/* Crea un post */
		post.setContent("Este post ha sido creado desde un cliente Java");

		/* Y lo publicamos */
		System.out.println("\n----------POST /users/usuarioJava/posts + xml");
		
		response = target.path("api").path("users").path("usuarioJava").path("posts").request()
				.post(Entity.xml(post), Response.class);
		System.out.println("Status: "+response.getStatus());
		
		if(response.getHeaders().containsKey("Location")) {
			Object location = response.getHeaders().get("Location").get(0);
			System.out.println("Location: " + location.toString());
		}

		/* Pedimos la lista de posts de usuarioJava */	
		System.out.println("\n----------GET /users/usuarioJava/posts");
		
		p_list = target.path("api").path("users").path("usuarioJava").path("posts").request()
				.accept(MediaType.APPLICATION_XML).get(PostList.class);
		
		it_p_list  = p_list.getPost().iterator();
		System.out.println("Lista de posts de usuarioJava");
		while (it_p_list.hasNext()) {
			post = it_p_list.next();
			postID = String.valueOf(post.getId());
			System.out.println(post);
		}

		/* Obtenemos el número total de post publicado por usuarioJava */
		System.out.println("\n----------GET /users/usuarioJava/posts/count");
		
		System.out.println("Total de posts publicados por usuarioJava: "
				+target.path("api").path("users").path("usuarioJava").path("posts").path("count")
				.request().accept(MediaType.TEXT_PLAIN).get(String.class));

		/* Obtenemos la información del post que hemos creado */
		System.out.println("\n----------GET /users/usuarioJava/posts/id");
		
		post = target.path("api").path("users").path("usuarioJava").path("posts").path(postID).request()
				.accept(MediaType.APPLICATION_XML).get(Post.class);
		System.out.println("Información de post");
		System.out.println(post);


		/* Editamos el post */
		System.out.println("\n----------PUT /users/usuarioJava/posts/id + xml");
		
		post.setContent("Este post ha sido editado desde el cliente Java");
		response = target.path("api").path("users").path("usuarioJava").path("posts").path(postID).request()
				.put(Entity.xml(post), Response.class);
		System.out.println("Status: "+response.getStatus());

		if(response.getHeaders().containsKey("Location")) {
			Object location = response.getHeaders().get("Location").get(0);
			System.out.println("Location: " + location.toString());
		}

		/* Comprobamos que se editó correctamente */
		System.out.println("\n----------GET /users/usuarioJava/posts/id");
		
		post = target.path("api").path("users").path("usuarioJava").path("posts").path(postID).request()
				.accept(MediaType.APPLICATION_XML).get(Post.class);
		System.out.println("Información del post editado");
		System.out.println(post);

		/* Obtenemos la lista de post publicado por v130111 en un período determinado, filtrado por contenido */
		System.out.println("\n----------GET /users/usuarioJava/posts?content=mancha&fechaInicio=20-03-2008&fechaFin=29-03-2016&from=0&to=2");
		
		System.out.println("Total de posts publicados por v130111, filtrado por fecha, contenido y total: ");
		p_list = target.path("api").path("users").path("v130111").path("posts").queryParam("content", "mancha")
				.queryParam("fechaInicio", "20-03-2008").queryParam("fechaFin", "29-03-2016").queryParam("from", "0").queryParam("to", "2")
				.request().accept(MediaType.APPLICATION_XML).get(PostList.class);
		
		it_p_list  = p_list.getPost().iterator();
		System.out.println("Lista de posts de v130111");
		while (it_p_list.hasNext()) {
			System.out.println(it_p_list.next());
		}

		/* Eliminamos el post de usuarioJava */		
		System.out.println("\n----------DELETE /users/usuarioJava/posts/id");
		
		response = target.path("api").path("users").path("usuarioJava").path("posts").path(postID).request()
				.delete(Response.class);
		System.out.println("Status: "+response.getStatus());

		/* Pedimos lista de usuarios filtrada por nombre */
		System.out.println("\n----------GET /users?nombre=sergio");
		
		u_list = target.path("api").path("users").queryParam("nombre", "sergio").request()
				.accept(MediaType.APPLICATION_XML).get(UserList.class);
		it_u_list  = u_list.getUser().iterator();
		System.out.println("Lista de usuarios con nombre Sergio");
		while (it_u_list.hasNext()) {
			System.out.println(it_u_list.next());
		}

		/* Añadimos v130111 a nuestra lista de amigos */	
		user.setUsername("v130111");
		
		System.out.println("\n----------POST /users/usuarioJava/friends?añadir + xml");
		
		response = target.path("api").path("users").path("usuarioJava").path("friends")
				.queryParam("añadir", "").request()
				.post(Entity.xml(user), Response.class);
		System.out.println("Status: "+response.getStatus());
		
		if(response.getHeaders().containsKey("Location")) {
			Object location = response.getHeaders().get("Location").get(0);
			System.out.println("Location: " + location.toString());
		}

		/* Pedimos lista de amigos de usuarioJava */
		System.out.println("\n----------GET /users/usuarioJava/friends");
		
		u_list = target.path("api").path("users").path("usuarioJava").path("friends").request()
				.accept(MediaType.APPLICATION_XML).get(UserList.class);
		
		it_u_list  = u_list.getUser().iterator();
		System.out.println("Lista de amigos de usuarioJava");
		while (it_u_list.hasNext()) {
			System.out.println(it_u_list.next());
		}

		/* Eliminamos v130111 de nuestra lista de amigos */	
		user.setUsername("v130111");
		
		System.out.println("\n----------POST /users/usuarioJava/friends?eliminar + xml");
		
		response = target.path("api").path("users").path("usuarioJava").path("friends")
				.queryParam("eliminar", "").request()
				.post(Entity.xml(user), Response.class);
		System.out.println("Status: "+response.getStatus());
		
		if(response.getHeaders().containsKey("Location")) {
			Object location = response.getHeaders().get("Location").get(0);
			System.out.println("Location: " + location.toString());
		}	

		/* Pedimos lista de amigos de v130111 filtrada por nombre */
		System.out.println("\n----------GET /users/usuarioJava/friends?nombre=david");
		
		u_list = target.path("api").path("users").path("v130111").path("friends").queryParam("nombre", "david").request()
				.accept(MediaType.APPLICATION_XML).get(UserList.class);
		
		it_u_list  = u_list.getUser().iterator();
		System.out.println("Lista de amigos de v130111 con nombre David");
		while (it_u_list.hasNext()) {
			System.out.println(it_u_list.next());
		}

		/* Pedimos lista de posts publicados por los amigos de v130111 */
		System.out.println("\n----------GET /users/usuarioJava/friends?posts");
		
		System.out.println("Lista de posts publicados por los amigos de v130111: ");
		p_list = target.path("api").path("users").path("v130111").path("friends").queryParam("posts", "")
				.request().accept(MediaType.APPLICATION_XML).get(PostList.class);
		
		it_p_list  = p_list.getPost().iterator();
		System.out.println("Lista de posts de amigos de v130111");
		while (it_p_list.hasNext()) {
			System.out.println(it_p_list.next());
		}

		/* Pedimos la lista de posts publicados por los amigos de v130111, limitado por cantidad */
		System.out.println("\n----------GET /users/usuarioJava/friends?from=3&to=5");
		
		System.out.println("Lista de posts publicados por los amigos de v130111, limitado por cantidad: ");
		p_list = target.path("api").path("users").path("v130111").path("friends").queryParam("posts", "")
				.queryParam("from", "3").queryParam("to", "5")
				.request().accept(MediaType.APPLICATION_XML).get(PostList.class);
		
		it_p_list  = p_list.getPost().iterator();
		System.out.println("Lista de posts de amigos de v130111");
		while (it_p_list.hasNext()) {
			System.out.println(it_p_list.next());
		}

		/* Eliminamos el perfil de usuarioJava */
		System.out.println("\n----------DELETE /users/usuarioJava");
		
		response = target.path("api").path("users").path("usuarioJava").request()
				.delete(Response.class);
		System.out.println("Status: "+response.getStatus());

		/* Comprobamos que no exista usuarioJava una vez eliminado */
		System.out.println("\n----------GET /users/usuarioJava");
		
		response = target.path("api").path("users").path("usuarioJava").request()
				.get(Response.class);
		System.out.println("Status: "+response.getStatus());


	}

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost:8080/UPMSocial/").build();
	}
}