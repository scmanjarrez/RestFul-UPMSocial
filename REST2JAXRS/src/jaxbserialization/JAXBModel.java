package jaxbserialization;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement()
// JAX-RS soporta el mapping automático de una clase anotada con JAXB a XML y JSON

public class JAXBModel {
	private String atrib1;
	private String atrib2;
	@XmlElement(name="atribA")
	public String getAtrib1() {
	    return atrib1;
	}
	public void setAtrib1(String atrib1) {
	    this.atrib1 = atrib1;
	}
	@XmlElement(name="atribB")
	//@XmlTransient()
	public String getAtrib2() {
	    return atrib2;
	}
	public void setAtrib2(String atrib2) {
	    this.atrib2 = atrib2;
	}  
}