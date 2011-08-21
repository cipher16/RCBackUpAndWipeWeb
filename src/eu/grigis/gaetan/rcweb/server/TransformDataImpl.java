package eu.grigis.gaetan.rcweb.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Logger;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import eu.grigis.gaetan.rcweb.shared.DataTransfer;
import eu.grigis.gaetan.rcweb.client.Rcbu;
import eu.grigis.gaetan.rcweb.client.services.TransformData;
import eu.grigis.gaetan.rcweb.client.ui.PhoneControl;

public class TransformDataImpl extends RemoteServiceServlet implements TransformData{
	
	private static final long serialVersionUID = 1L;
	private static Logger log=Logger.getLogger(TransformDataImpl.class.getName());
	
	@Override
	public String sendMessage(String message) throws Exception {
		
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		String sender = "nobody";
		String recipient = "nobody";
		
		if (user != null) {
			recipient = user.getEmail();
		}
		
		DataTransfer dt = DataTransferQuery.getLastAuthForMail(recipient);
		if(dt==null)
		{
			log.severe("No regId found for : "+sender);
			throw new Exception(PhoneControl.errorMessage.phoneNotFound.toString());
		}
		sender = dt.getSender();
		
		if(recipient.equals("nobody")|sender.equals("nobody"))
		{
			log.severe("Cannot find recipient or sender. Data corruption ?");
			throw new Exception(PhoneControl.errorMessage.userNotFound.toString());
		}
		
		log.warning("sendMessage: sender = " + sender);
		log.warning("sendMessage: recipient = " + recipient);
		log.warning("sendMessage: message = " + message);
		
		String collapseKey = "" + message.hashCode();
		
		try {
			sendMessage(Token.getTokenForMail(sender,"C2DM"), collapseKey, dt, message);
		} catch (Exception e) {
			log.warning("Got an error "+e.getMessage());
			throw new Exception(PhoneControl.errorMessage.unableSendMessage.toString());
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
