package eu.grigis.gaetan.rcweb.shared;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


@PersistenceCapable
public class DataTransfer implements Serializable {
	
	private static final long serialVersionUID = 1L;
	

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long key;
	
	@Persistent(serialized="true")
	private HashMap<String,String> data;
	
	@Persistent
	private String mail;
	
	@Persistent
	private String sender;
	
	@Persistent
	private String id;
	
	@Persistent
	private String type;
	
	@Persistent
	private Date date;
	
	public DataTransfer()
	{
		setMail("");
		setId("");
		setType("");
		setSender("");
	}
	
	public DataTransfer(String m,String s,String i,String t) {
		setMail(m);
		setId(i);
		setType(t);
		setSender(s);
	}
	
	public void setData(HashMap<String,String> data) {
		this.data = data;
	}

	public HashMap<String,String> getData() {
		return data;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getMail() {
		return mail;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSender() {
		return sender;
	}
}
