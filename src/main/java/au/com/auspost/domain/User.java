package au.com.auspost.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

@Entity
@Table(name="USERS")
public class User {

	@Id
	@GeneratedValue
	@Column(name="USER_ID")
	private Long id;
	
	@Column(name="USERNAME")
	@NotEmpty
	private String username;
	
	@Column(name="PASSWORD")
	@NotEmpty
	@JsonIgnore
	private String password;
	
	@Column(name="FIRST_NAME")
	@NotEmpty
	private String firstName;
	
	@Column(name="LAST_NAME")
	@NotEmpty
	private String lastName;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return getId() + ", " + getUsername() + ", " + getFirstName() + ", " + getLastName();
	}
	
}
