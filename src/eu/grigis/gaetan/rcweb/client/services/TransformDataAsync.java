package eu.grigis.gaetan.rcweb.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import eu.grigis.gaetan.rcweb.shared.DataTransfer;

public interface TransformDataAsync {

	void sendMessage(String status, AsyncCallback<String> callback);
	void getLastTypeForMail(String mail, String type,
			AsyncCallback<DataTransfer> callback);
	void getLastTypeForCurrentRegUser(String type,
			AsyncCallback<DataTransfer> callback);
}
