package eu.grigis.gaetan.rcweb.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import eu.grigis.gaetan.rcweb.shared.UserInfo;

@RemoteServiceRelativePath("auth")
public interface AuthService extends RemoteService {
	UserInfo getAuthUser(String url);
	String getAuthToken(String sender,String password);
}
