package eu.grigis.gaetan.rcweb.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.RootPanel;

import eu.grigis.gaetan.rcweb.client.controls.RCBUHistoryListener;
import eu.grigis.gaetan.rcweb.client.services.AuthService;
import eu.grigis.gaetan.rcweb.client.services.AuthServiceAsync;
import eu.grigis.gaetan.rcweb.shared.UserInfo;

public class Rcbu implements EntryPoint {
	private RCBUHistoryListener historyListener;
	public void onModuleLoad() {
		historyListener = new RCBUHistoryListener();
		addMenu();
		getAuth();
		if(History.getToken().isEmpty())
			History.newItem("home", false);//loading home
        History.addValueChangeHandler(historyListener);
        History.fireCurrentHistoryState();
	}
	
	public void getAuth()
	{
		((AuthServiceAsync) GWT.create(AuthService.class)).getAuthUser(Window.Location.getHref(),new AsyncCallback<UserInfo>() {
			@Override
			public void onSuccess(UserInfo user) {
				if(user.isLoggedIn())
				{
					RootPanel.get("tabbar").add(new Hyperlink("Phone","phone"));
					RootPanel.get("auth").getElement().setInnerHTML("<a href='"+user.getUrl()+"'>Deconnexion ("+user.getName()+")</a>");
				}
				else
				{
					RootPanel.get("auth").getElement().setInnerHTML("<a href='"+user.getUrl()+"'>Connexion</a>");
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
	}
	
	public void addMenu()
	{
		Hyperlink home = new Hyperlink("Home","home");
		Hyperlink faq = new Hyperlink("FAQ","faq");
		Hyperlink admin = new Hyperlink("Admin","admin");
		RootPanel.get("tabbar").add(home);
		RootPanel.get("tabbar").add(faq);
		RootPanel.get("tabbar").add(admin);
	}
}
