package rest.user;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UsersList {
	private ArrayList<User> user;

	public UsersList(){

	}

	public UsersList(ArrayList<User> user) {
		super();
		this.user = user;
	}

	public ArrayList<User> getUser() {
		return user;
	}

	public void setUser(ArrayList<User> user) {
		this.user = user;
	}

}