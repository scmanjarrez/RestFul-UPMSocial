package jersey.todo.model;

import javax.xml.bind.annotation.XmlRootElement;

/** Por cada objeto que viaje, debe haber un bin que permita serializar/deserializar
 * <todo>
 * 		<id></id>
 *  	<tarea></tarea>
 *  	<descripcion></descripcion>
 * </todo>
 * */


@XmlRootElement
public class Todo {
  private String id;
  private String tarea;
  private String descripcion;
  
  // Obligatorio el constructor vacio
  public Todo(){
    
  }
  public Todo (String id, String tarea){
    this.id = id;
    this.tarea = tarea;
  }
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getTarea() {
    return tarea;
  }
  public void setTarea(String tarea) {
    this.tarea = tarea;
  }
  public String getDescripcion() {
    return descripcion;
  }
  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }
  
  
} 
