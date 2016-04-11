package rest1;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

// Plain Old Java Object (POJO)
// La clase registra sus métodos para la petición HTTP GET utilizando la anotación @GET. 
// Mediante la anotación @Produces, define que puede ofrecer varias representaciones asociadas
// a diferentes tipos MIME: text, XML y HTML. 

// Por omisión el navegador solicita el tipo MIME HTML.

//Establece el path a la URL base + /holamundo
@Path("/holamundo")
public class HolaMundo {

  // Este método se invoca si se solicita TEXT_PLAIN
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String saludoPlainText() {
    return "Hola JAX-RS";
  }
	
  @GET
  @Produces(MediaType.TEXT_XML)
  public String saludoXML() {
    return "<?xml version=\"1.0\"?>" + "<hola>Hola JAX-RS" + "</hola>";
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public String saludoHtml() {
    return "<html>" + "<title>" + "Hola JAX-RS" + "</title>"
        + "<body><h1>" + "Hola JAX-RS" + "</h1></body>" + "</html> ";
  }

} 
