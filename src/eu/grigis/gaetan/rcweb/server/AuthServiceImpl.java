package eu.grigis.gaetan.rcweb.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Logger;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import eu.grigis.gaetan.rcweb.client.services.AuthService;
import eu.grigis.gaetan.rcweb.shared.UserInfo;

public class AuthServiceImpl extends RemoteServiceServlet implements
		AuthService {
	private static Logger log = Logger.getLogger("AuthServiceImpl");
	private static final long serialVersionUID = 1L;
	@Override
	public UserInfo getAuthUser(String url) {
		UserService us = UserServiceFactory.getUserService();
		
		if(!us.isUserLoggedIn())
			return new UserInfo("", "", us.createLoginURL(url), false);
		return new UserInfo(us.getCurrentUser().getNickname(), us.getCurrentUser().getEmail(), us.createLogoutURL(url), true);
	}
	
	@Override
	public String getAuthToken(String sender,String password)
    {
		String authToken = null;
		try{
	        authToken = Token.getTokenForMail(sender);
	        if (authToken != null) {
	            log.info("retrieved auth token from db: " + authToken);
	            return authToken;
	        }	
		}catch (Exception e) {}
        
        log.info("asking C2DM server for auth token...");
        
        StringBuilder buf = new StringBuilder();
        HttpURLConnection request = null;
        OutputStreamWriter post = null;
        try {
            URL url = new URL("https://www.google.com/accounts/ClientLogin");
            request = (HttpURLConnection) url.openConnection();
            request.setDoOutput(true);
            request.setDoInput(true);

            buf.append("accountType").append("=").append((URLEncoder.encode("GOOGLE", "UTF-8")));
            buf.append("&Email").append("=").append((URLEncoder.encode(sender, "UTF-8")));
            buf.append("&Passwd").append("=").append((URLEncoder.encode(password, "UTF-8")));
            buf.append("&service").append("=").append((URLEncoder.encode("ac2dm", "UTF-8")));
            buf.append("&source").append("=").append((URLEncoder.encode("myco-pushapp-1.0", "UTF-8")));
            
            request.setRequestMethod("POST");
            request.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            request.setRequestProperty("Content-Length", buf.toString().getBytes().length+"");
            
            post = new OutputStreamWriter(request.getOutputStream());
            post.write(buf.toString());
            post.flush();
            
            int code = request.getResponseCode();
            log.info("response code: " + request.getResponseCode());
            log.info("response message: " + request.getResponseMessage());
            if (code == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
                buf = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.startsWith("Auth=")) {
                        authToken = inputLine.substring(5);
                    }
                    buf.append(inputLine);
                }
                post.close();
                in.close();
                log.info("response from C2DM server:\n" + buf.toString());
                
            } else if (code == 403) {
                //TODO: handle error conditions
            }
            
            if (authToken != null) {
                log.info("storing auth token: " + authToken);
                Token.saveTokenForMail(sender, authToken);
            }
            
            return authToken;
            
        } catch (Exception e) {
            log.severe("unable to make https post request to c2dm server"+e);
            return null;
        }
    }
}
