package rest.post;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType (propOrder={"id","author_username","content","creation_date"})
public class Post {

	private int id;
	@XmlElement (name = "author")
	private String author_username;
	private String content;
	@XmlJavaTypeAdapter(DateFormatterAdapter.class)
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
	
	@Override
	public String toString() {
		return "Post [id=" + id + ", author_username=" + author_username + ", content=" + content + ", creation_date="
				+ creation_date + "]";
	}

    private static class DateFormatterAdapter extends XmlAdapter<String, Date> {
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        @Override
        public Date unmarshal(final String v) throws Exception {
            return dateFormat.parse(v);
        }

        @Override
        public String marshal(final Date v) throws Exception {
            return dateFormat.format(v);
        }
    }
} 
