package rest1;

import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

//Plain Old Java Object (POJO)
//La clase registra sus métodos para la petición HTTP GET utilizando la anotación @GET. 
//Mediante la anotación @Produces, define que puede ofrecer varias representaciones asociadas
//a diferentes tipos MIME: text, XML y HTML. 

//Por omisión el navegador solicita el tipo MIME HTML.

//Establece el path a la URL base + /holamundo
@Path("/holamundo")
public class HolaMundo {

  @Context
  private UriInfo uriInfo;
	
  // Este método se invoca si se solicita TEXT_PLAIN
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public Response saludoPlainText() {
	  String respuesta = "Hola JAX-RS";
	  return Response.status(Response.Status.OK).entity(respuesta).header("Location", 
			  uriInfo.getAbsolutePath().toString()+"kk").build();
  }

  @GET
  @Produces(MediaType.TEXT_XML)
  public String saludoXML() {
    return "<?xml version=\"1.0\"?>" + "<hola>Hola JAX-RS" + "</hola>";
  }

  /*http://localhost:8080/REST1bHolaMundo/rest/holamundo/saluda/sergio/chica?&apellido2=manjarrez*/
  @GET
  @Path("saluda/{nombre}/{apellido}")
  @Produces(MediaType.TEXT_HTML)
  public String saludoHtml(@PathParam("nombre") String n, @PathParam("apellido") String a, 
		  @QueryParam("apellido2") String a2) {
    return "<html>" + "<title>" + "Hola JAX-RS" + "</title>"
        + "<body><h1>" + "Hola " + n +" "+ a + " "+ a2 + "</body></h1>" + "</html> ";
  }

} 














