package hello;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity //This tells Hibernate to make a table out of this class
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;

	/**
	 * Acts as a primary key
	 */
	@NotNull
	@Column(unique=true)
	private String username;
	
	/**
	 * Password is hashed, and then sent to server.
	 * Stored encrypted with ServerPublicKey in db
	 */
	@NotNull
	@Column
	@Lob
	private String passHash;
	
	/**
	 * Not encrypted when stored on server
	 */
	@NotNull
	@Column
	@Lob
	private String userPublicKey;
	
	/**
	 * Encrypted with user password
	 */
	@NotNull
	@Column
	@Lob
	private String encryptedUserPrivateKey;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getUsername(){
		return username;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getPassHash(){
		return passHash;
	}

	public void setPassHash(String passHash){
		this.passHash = passHash;
	}
	
	public String getUserPublicKey() {
		return userPublicKey;
	}
	
	public void setUserPublicKey(String userPublicKey) {
		this.userPublicKey = userPublicKey;
	}
	
	public String getEncryptedUserPrivateKey() {
		return encryptedUserPrivateKey;
	}
	
	public void setEncryptedUserPrivateKey(String encryptedUserPrivateKey) {
		this.encryptedUserPrivateKey = encryptedUserPrivateKey;
	}
}
//Above, is the entity class which Hibernate will automatically translate into a table