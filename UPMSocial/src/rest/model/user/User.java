package rest.model.user;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType (propOrder={"username","first_name","last_name","phone","email","address","register_date"})
public class User {
	
	private String username;
	private String first_name;
	private String last_name;
	private Integer phone;
	private String email;
	private String address;
	@XmlJavaTypeAdapter(DateFormatterAdapter.class)
	private Date register_date;

	public User(){
		
	}

	public User(String username, String first_name, String last_name, Integer phone, String email, String address,
			Date register_date) {
		super();
		this.username = username;
		this.first_name = first_name;
		this.last_name = last_name;
		this.phone = phone;
		this.email = email;
		this.address = address;
		this.register_date = register_date;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public Integer getPhone() {
		return phone;
	}

	public void setPhone(Integer phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getRegister_date() {
		return register_date;
	}

	public void setRegister_date(Date register_date) {
		this.register_date = register_date;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", first_name=" + first_name + ", last_name=" + last_name + ", phone="
				+ phone + ", email=" + email + ", address=" + address + ", register_date=" + register_date + "]";
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
