package eu.grigis.gaetan.rcweb.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

public class ChannelPresence extends HttpServlet {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5742702861943321589L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		com.google.appengine.api.channel.ChannelPresence presence = channelService.parsePresence(req);

		if(req.getServletPath().equals("/_ah/channel/disconnected/"))
		{//delete token
			Token.deleteToken(presence.clientId(), "CHANNEL");
		}
	}
}
