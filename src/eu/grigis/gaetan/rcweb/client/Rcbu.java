package eu.grigis.gaetan.rcweb.client;

import com.google.gwt.appengine.channel.client.Channel;
import com.google.gwt.appengine.channel.client.ChannelFactory;
import com.google.gwt.appengine.channel.client.ChannelFactory.ChannelCreatedCallback;
import com.google.gwt.appengine.channel.client.SocketError;
import com.google.gwt.appengine.channel.client.SocketListener;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;

import eu.grigis.gaetan.rcweb.client.controls.RCBUHistoryListener;
import eu.grigis.gaetan.rcweb.client.services.AuthService;
import eu.grigis.gaetan.rcweb.client.services.AuthServiceAsync;
import eu.grigis.gaetan.rcweb.client.ui.PhoneControl;
import eu.grigis.gaetan.rcweb.shared.UserInfo;

public class Rcbu implements EntryPoint {
	private RCBUHistoryListener historyListener;
	public static RCBUConstants constants = GWT.create(RCBUConstants.class);
	public void onModuleLoad() {
		historyListener = new RCBUHistoryListener();
		addMenu();
		getAuth();
		addLang();
		if(History.getToken().isEmpty())
			History.newItem("home", false);//loading home
        History.addValueChangeHandler(historyListener);
        History.fireCurrentHistoryState();
	}
	
	public static void displayError(String message)
	{
		Anchor link = new Anchor(Rcbu.constants.close());
		RootPanel.get("error").clear();
		RootPanel.get("error").getElement().setAttribute("style", "display: block;");
		RootPanel.get("error").add(new HTML(message));
		RootPanel.get("error").add(link);
		link.addClickHandler(new ClickHandler() {
			@Override public void onClick(ClickEvent event) {
				RootPanel.get("error").clear();
				RootPanel.get("error").getElement().setAttribute("style", "display: none;");
			}
		});
	}
	public void addLang()
	{
		Image en = new Image("lang_en.png");
		Image fr = new Image("lang_fr.png");
		RootPanel.get("flags").add(en);
		RootPanel.get("flags").add(fr);
		en.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.Location.replace(Window.Location.createUrlBuilder().setParameter("locale", "en").buildString());
			}
		});
		fr.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.Location.replace(Window.Location.createUrlBuilder().setParameter("locale", "fr").buildString());
			}
		});
	}
	
	public void getAuth()
	{
		((AuthServiceAsync) GWT.create(AuthService.class)).getAuthUser(Window.Location.getHref(),new AsyncCallback<UserInfo>() {
			@Override
			public void onSuccess(UserInfo user) {
				if(user.isLoggedIn())
				{
					if(user.isAdmin())
						RootPanel.get("tabbar").add(new Hyperlink(constants.administration(),"admin"));
					RootPanel.get("tabbar").add(new Hyperlink(constants.phone(),"phone.status"));
					RootPanel.get("auth").getElement().setInnerHTML("<a href='"+user.getUrl()+"'>"+constants.disconnection()+" ("+user.getName()+")</a>");

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
					RootPanel.get("auth").getElement().setInnerHTML("<a href='"+user.getUrl()+"'>"+constants.connection()+"</a>");
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
	}
	
	public void addMenu()
	{
		RootPanel.get("tabbar").add(new Hyperlink(constants.home(),"home"));
		RootPanel.get("tabbar").add(new Hyperlink(constants.faq(),"faq"));
		RootPanel.get("tabbar").add(new Hyperlink(constants.tos(),"tos"));
		RootPanel.get("tabbar").add(new Hyperlink(constants.about(),"about"));
	}
}
