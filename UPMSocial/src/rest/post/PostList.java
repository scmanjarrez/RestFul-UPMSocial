package rest.post;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PostList {
	private ArrayList<Post> posts;

	public PostList(){

	}

	public PostList(ArrayList<Post> posts) {
		super();
		this.posts = posts;
	}

	public ArrayList<Post> getPost() {
		return posts;
	}

	public void setPost(ArrayList<Post> posts) {
		this.posts = posts;
	}
	
}