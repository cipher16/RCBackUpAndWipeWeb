package eu.grigis.gaetan.rcweb.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Logger;

import com.google.android.c2dm.server.C2DMessaging;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import eu.grigis.gaetan.rcweb.shared.DataTransfer;
import eu.grigis.gaetan.rcweb.client.services.TransformData;

public class TransformDataImpl extends RemoteServiceServlet implements TransformData{
	
	private static final long serialVersionUID = 1L;
	private static Logger log=Logger.getLogger(TransformDataImpl.class.getName());
	
	@Override
	public String sendMessage(String message) {
		
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		Gson g = new Gson();
		
		String sender = "loupzeur@gmail.com";
		String recipient = "nobody";
		
		if (user != null) {
			recipient = user.getEmail();
		}
		
		log.warning("sendMessage: sender = " + sender);
		log.warning("sendMessage: recipient = " + recipient);
		log.warning("sendMessage: message = " + message);
		
		String collapseKey = "" + message.hashCode();
		
		try {
			sendMessage(Token.getTokenForMail(sender), collapseKey, DataTransferQuery.getLastAuthForMail(recipient), message);
		} catch (Exception e) {
			log.warning("Got an error "+e.getMessage());
		}
		return "";
	}

	@Override
	public DataTransfer getLastTypeForMail(String mail, String type) {
		return DataTransferQuery.getLastTypeForMail(mail, type);
	}
	@Override
	public DataTransfer getLastTypeForCurrentRegUser(String type) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if(user==null)
			return null;
		return DataTransferQuery.getLastTypeForMail(user.getEmail(), type);
	}
	
	private void sendMessage(String authToken, String collapseKey, DataTransfer registrationId, String message)
    throws Exception
    {
        log.warning("sending message...");
        if(authToken==null||authToken.isEmpty())
        	return;
        if(registrationId==null)
        {
        	log.warning("no device found");
        	return;
        }
        URL url = new URL("https://android.apis.google.com/c2dm/send");
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.setDoOutput(true);
        request.setDoInput(true);

        StringBuilder buf = new StringBuilder();
        buf.append("registration_id").append("=").append((URLEncoder.encode(registrationId.getId(), "UTF-8")));
        buf.append("&collapse_key").append("=").append((URLEncoder.encode(collapseKey, "UTF-8")));
        buf.append("&data.message").append("=").append((URLEncoder.encode(message, "UTF-8")));
        
        request.setRequestMethod("POST");
        request.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        request.setRequestProperty("Content-Length", buf.toString().getBytes().length+"");
        request.setRequestProperty("Authorization", "GoogleLogin auth=" + authToken);
        
        OutputStreamWriter post = new OutputStreamWriter(request.getOutputStream());
        post.write(buf.toString());
        post.flush();
        
        BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
        buf = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            buf.append(inputLine);
        }
        post.close();
        in.close();

        log.warning("post url : "+buf.toString());
        log.warning("response from C2DM server:\n" + buf.toString());
        
        int code = request.getResponseCode();
        log.warning("response code: " + request.getResponseCode());
        log.warning("response message: " + request.getResponseMessage());
        if (code == 200) {
            //TODO: check for an error and if so, handle
            
        } else if (code == 503) {
            //TODO: check for Retry-After header; use exponential backoff and try again
            
        } else if (code == 401) {
            //TODO: get a new auth token
        }
    }
}
