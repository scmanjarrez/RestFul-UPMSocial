package jersey.todo.dao;

import java.util.HashMap;
import java.util.Map;

import jersey.todo.model.Todo;

public class TodoDao {

  private Map<String, Todo> contentProvider = new HashMap<>();
  
  private static TodoDao instance = null;
  
  private TodoDao() {
    
    Todo todo = new Todo("1", "Aprender XML y Java");
    todo.setDescripcion("Repasar JAX-B");
    contentProvider.put("1", todo);
    todo = new Todo("2", "Repasar Jersey");
    todo.setDescripcion("Repasar JAX-RS");
    contentProvider.put("2", todo);
    
  }
  public Map<String, Todo> getModel(){
    return contentProvider;
  }
  
  
  public static TodoDao getInstance() {
	  if (instance==null)
		  instance = new TodoDao();
	  return instance;
  }
} 

/*
public enum TodoDao {
  instance;
  
  private Map<String, Todo> contentProvider = new HashMap<String, Todo>();
  
  private TodoDao() {
    
    Todo todo = new Todo("1", "Aprender XML y Java");
    todo.setDescripcion("Repasar JAX-B");
    contentProvider.put("1", todo);
    todo = new Todo("2", "Repasar Jersey");
    todo.setDescripcion("Repasar JAX-RS");
    contentProvider.put("2", todo);
    
  }
  public Map<String, Todo> getModel(){
    return contentProvider;
  }
  
} 
*/
