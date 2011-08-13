package eu.grigis.gaetan.rcweb.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import eu.grigis.gaetan.rcweb.shared.UserInfo;


public interface AuthServiceAsync {

	void getAuthUser(String url, AsyncCallback<UserInfo> callback);

	void getAuthToken(String sender, String password,
			AsyncCallback<String> callback);

}
