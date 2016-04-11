package jaxbserialization;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/jaxb")
public class JAXBModelResource {
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public JAXBModel getXML() {
		JAXBModel serializableXML = new JAXBModel();
		serializableXML.setAtrib1("Mi primer objeto serializable");
		serializableXML.setAtrib2("En XML y JSON con JAXB");
		return serializableXML;
	}

	// Para poder testar el recurso desde el navegador
	@GET
	@Produces({ MediaType.TEXT_XML })
	public JAXBModel getHTML() {
		JAXBModel serializableXML = new JAXBModel();
		serializableXML.setAtrib1("Mi primer objeto serializable");
		serializableXML.setAtrib2("En XML y JSON con JAXB");
		return serializableXML;
	}

} 
