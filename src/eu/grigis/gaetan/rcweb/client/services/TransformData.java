package eu.grigis.gaetan.rcweb.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import eu.grigis.gaetan.rcweb.shared.DataTransfer;

@RemoteServiceRelativePath("json")
public interface TransformData extends RemoteService {
	String sendMessage(String status) throws Exception;
	DataTransfer getLastTypeForMail(String mail,String type);
	DataTransfer getLastTypeForCurrentRegUser(String type);
}
