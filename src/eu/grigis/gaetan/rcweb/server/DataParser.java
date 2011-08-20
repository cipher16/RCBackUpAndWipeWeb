package eu.grigis.gaetan.rcweb.server;

import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.gson.Gson;

import eu.grigis.gaetan.rcweb.shared.DataTransfer;

public class DataParser extends HttpServlet {
	private static Logger log = Logger.getLogger("DataParser");
	private static final long serialVersionUID = 4749016443092546607L;

//	@Override
//	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
//			throws ServletException, IOException {
//		resp.setContentType("text/html");
//		String r = "<html><body><form method='post'><textarea name='data'></textarea><input type='submit' /></form></body></html>";
//		ServletOutputStream out = resp.getOutputStream();
//		out.println(r);
//		return;
//	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String data = req.getParameter("data");
		log.warning("Getting data : "+data);
		if(data.isEmpty())
			return;
		
		Gson g = new Gson();
		DataTransfer dt = g.fromJson(data, DataTransfer.class);
		
		/*Sending message to Channel, the service check if user is connected or not*/
			ChannelService channelService = ChannelServiceFactory.getChannelService();	
			channelService.sendMessage(new ChannelMessage(dt.getMail(), dt.getType()));

		dt.setDate(Calendar.getInstance().getTime());
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {pm.makePersistent(dt);} finally {pm.close();}
	}	
}
