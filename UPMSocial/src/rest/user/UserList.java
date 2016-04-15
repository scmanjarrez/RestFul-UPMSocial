package rest.user;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserList {
	private ArrayList<User> users;

	public UserList(){

	}

	public UserList(ArrayList<User> users) {
		super();
		this.users = users;
	}

	public ArrayList<User> getUser() {
		return users;
	}

	public void setUser(ArrayList<User> users) {
		this.users = users;
	}

}