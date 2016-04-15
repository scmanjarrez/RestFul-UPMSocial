package rest.user;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UsersList {
	private ArrayList<User> users;

	public UsersList(){

	}

	public UsersList(ArrayList<User> users) {
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