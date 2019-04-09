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
public class UserRelationships {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;

	@NotNull
	@Column
	private String fromUsername;
	
	@NotNull
	@Column
	private String toUsername;
	
	/** Can be either "requested", "needsResponse", "rejected", "friends", or "notFriends"
	 * 
	 */
	@NotNull
	@Column
	private String relationshipType;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getFromUsername(){
		return fromUsername;
	}

	public void setFromUsername(String fromusername){
		this.fromUsername = fromusername;
	}

	public String getToUsername(){
		return toUsername;
	}

	public void setToUsername(String tousername){
		this.toUsername = tousername;
	}
	
	public String getRelationshipType(){
		return relationshipType;
	}

	public void setRelationshipType(String relationshiptype){
		this.relationshipType = relationshiptype;
	}
}
//Above, is the entity class which Hibernate will automatically translate into a table