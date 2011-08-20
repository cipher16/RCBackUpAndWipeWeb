package eu.grigis.gaetan.rcweb.server;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import eu.grigis.gaetan.rcweb.shared.DataTransfer;

/**
 * Used to save several type of object :
 * 	-C2DM    : Token of google for C2DM (only the admin)
 *  -CHANNEL : Token of google for Channel (every user, except the admin)
 *  --> JDO doesn't support composed key (as for now ...)
 * @author kikoolol
 *
 */
@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Token implements Serializable {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String id;//concatenate mail:type (give better performance than a request using mail and type)
	
	@Persistent
	private String mail;
	
	@Persistent
	private String token;
	
	@Persistent
	private String type;
	
	@Persistent
	private Date date;
	
	public Token(String m, String t,String ty) {
		id=m+":"+ty;
		mail=m;
		token=t;
		type=ty;
		date= Calendar.getInstance().getTime();//just to get an info about the age of token, may be not used
	}

	public static String getTokenForMail(String mail,String type)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Token t=null;
		try{
			t = pm.getObjectById(Token.class,mail+":"+type);
		}catch(Exception e){}
		pm.close();
		if(t==null)
			return "";
		return t.token;
	}

	public static void saveTokenForMail(String mail,String token,String type)
	{
		Logger.getLogger("Token").warning("Saving object : "+mail+":"+type);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Token t = new Token(mail,token,type);
		pm.makePersistent(t);
		pm.close();
	}
	
	public static void deleteToken(String mail,String type)
	{
		Logger.getLogger("Token").warning("Removing object : "+mail+":"+type);
		//not sure if we can delete object when disconnected so get it back ...
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Token t = pm.getObjectById(Token.class,mail+":"+type);
		if(t!=null)
			pm.deletePersistent(t);
		pm.close();
	}
}
