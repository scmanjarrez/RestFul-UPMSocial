package rest.posts;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
public class Post {
	private int id;
	private String author_username;
	private String content;
	private Date creation_date;

	public Post(){
		
	}

	public Post(int id, String author_username, String content, Date creation_date) {
		super();
		this.id = id;
		this.author_username = author_username;
		this.content = content;
		this.creation_date = creation_date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAuthor_username() {
		return author_username;
	}

	public void setAuthor_username(String author_username) {
		this.author_username = author_username;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreation_date() {
		return creation_date;
	}

	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
	}


} 
