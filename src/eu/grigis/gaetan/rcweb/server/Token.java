package eu.grigis.gaetan.rcweb.server;

import java.io.Serializable;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Token implements Serializable {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String mail;
	
	@Persistent
	private String token;
	
	public Token(String m, String t) {
		mail=m;
		token=t;
	}

	/**
	 * There should be only one token ... but for test we can use several sender adress so ....
	 * @param mail
	 * @return
	 */
	public static String getTokenForMail(String mail)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Token t = pm.getObjectById(Token.class,mail);
		if(t==null)
			return "";
		return t.token;
	}

	public static void saveTokenForMail(String mail,String token)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Token t = new Token(mail,token);
		pm.makePersistent(t);
	}
}
