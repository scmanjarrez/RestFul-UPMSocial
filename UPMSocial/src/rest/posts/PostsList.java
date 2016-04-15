package rest.posts;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PostsList {
	private ArrayList<Post> post;

	public PostsList(){

	}

	public PostsList(ArrayList<Post> post) {
		super();
		this.post = post;
	}

	public ArrayList<Post> getUser() {
		return post;
	}

	public void setUser(ArrayList<Post> post) {
		this.post = post;
	}

}