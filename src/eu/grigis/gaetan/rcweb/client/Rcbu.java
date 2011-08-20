package eu.grigis.gaetan.rcweb.client;

import com.google.gwt.appengine.channel.client.Channel;
import com.google.gwt.appengine.channel.client.ChannelFactory;
import com.google.gwt.appengine.channel.client.ChannelFactory.ChannelCreatedCallback;
import com.google.gwt.appengine.channel.client.SocketError;
import com.google.gwt.appengine.channel.client.SocketListener;
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
import eu.grigis.gaetan.rcweb.client.ui.PhoneControl;
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
					if(user.isAdmin())
						RootPanel.get("tabbar").add(new Hyperlink("Admin","admin"));
					RootPanel.get("tabbar").add(new Hyperlink("Phone","phone.status"));
					RootPanel.get("auth").getElement().setInnerHTML("<a href='"+user.getUrl()+"'>Deconnexion ("+user.getName()+")</a>");

					/*Channel API, used to retrieve real time info*/
					ChannelFactory.createChannel(user.getTokenChan(), new ChannelCreatedCallback() {
						  @Override
						  public void onChannelCreated(Channel channel) {
						    channel.open(new SocketListener() {
						      @Override
						      public void onOpen() {/*Do nothing it's OK*/}
						      @Override
						      public void onMessage(String message) {
						    	message=message.toLowerCase().trim();
						        if(message.matches("^(ring|status|geoloc)$"))
						        {
						        	PhoneControl.getInstance().displayType(message);
						        }
						      }
						      @Override
						      public void onError(SocketError error) {
						        Window.alert("Error: " + error.getDescription());
						      }
						      @Override
						      public void onClose() {/*Do nothing it's OK*/}
						    });
						  }
						});
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
		RootPanel.get("tabbar").add(new Hyperlink("Home","home"));
		RootPanel.get("tabbar").add(new Hyperlink("FAQ","faq"));
		RootPanel.get("tabbar").add(new Hyperlink("TOS","tos"));
		RootPanel.get("tabbar").add(new Hyperlink("About","about"));
	}
}
