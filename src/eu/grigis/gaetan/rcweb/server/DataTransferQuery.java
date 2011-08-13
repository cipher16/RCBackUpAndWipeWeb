package eu.grigis.gaetan.rcweb.server;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import eu.grigis.gaetan.rcweb.shared.DataTransfer;

public class DataTransferQuery {
	@SuppressWarnings("unchecked")
	public static DataTransfer getLastAuthForMail(String mail)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery("select from " + DataTransfer.class.getName()
	            + " where mail=='" + mail + "' && type=='AUTH' ORDER BY date desc LIMIT 1");
		List<DataTransfer> list = (List<DataTransfer>) query.execute();
		if(list==null||(list!=null&&list.isEmpty()))
			return null;
		return list.get(0);//return first found
	}
	
	public static DataTransfer getLastTypeForMail(String mail,String type)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Query query = pm.newQuery("select from " + DataTransfer.class.getName()
	            + " where mail=='" + mail + "' && type=='"+type.toUpperCase()+"' ORDER BY date desc LIMIT 1");
		List<DataTransfer> list = (List<DataTransfer>) query.execute();
		if(list==null||(list!=null&&list.isEmpty()))
			return null;
		return list.get(0);//return first found
	}
}
