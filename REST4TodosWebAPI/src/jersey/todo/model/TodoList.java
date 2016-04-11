package jersey.todo.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TodoList {
  private List<Todo> l;
  
  public TodoList(){
    
  }
  public TodoList (List<Todo> l){
    this.l = l;
  }
  public List<Todo> getL() {
    return l;
  }
  public void setL(List<Todo> l) {
    this.l = l;
  }
} 
