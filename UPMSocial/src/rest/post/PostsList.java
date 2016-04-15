package rest.post;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PostsList {
	private ArrayList<Post> posts;

	public PostsList(){

	}

	public PostsList(ArrayList<Post> posts) {
		super();
		this.posts = posts;
	}

	public ArrayList<Post> getP_list() {
		return posts;
	}

	public void setP_list(ArrayList<Post> posts) {
		this.posts = posts;
	}
	
}