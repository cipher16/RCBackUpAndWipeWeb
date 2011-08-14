package eu.grigis.gaetan.rcweb.client.controls;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import eu.grigis.gaetan.rcweb.client.services.AuthService;
import eu.grigis.gaetan.rcweb.client.services.AuthServiceAsync;
import eu.grigis.gaetan.rcweb.client.ui.Home;
import eu.grigis.gaetan.rcweb.client.ui.PhoneControl;

public class RCBUHistoryListener implements ValueChangeHandler<String> {

	@Override
    public void onValueChange(ValueChangeEvent<String> event) {

        RootPanel.get("content").clear();
        if (event.getValue().equals("home")){
            RootPanel.get("content").add(new Home());
        }
        else if (event.getValue().equals("faq")){
            RootPanel.get("content").add(new HTMLPanel("Here will be the faq ... one day ... may be"));
        }
        else if(event.getValue().startsWith("phone."))
        {
        	if(event.getValue().matches("phone.(status|geoloc|ring)"))
        		RootPanel.get("content").add(new PhoneControl(event.getValue().replace("phone.", "")));
        }
        else if(event.getValue().equals("admin"))
        {
        	final VerticalPanel vp = new VerticalPanel();
        	Button btn = new Button("Save Auth");
        	final TextBox tbSender = new TextBox();
        	final PasswordTextBox tbpass = new PasswordTextBox();
        	vp.add(tbSender);
        	vp.add(tbpass);
        	vp.add(btn);
        	RootPanel.get("content").add(vp);
        	
        	btn.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					
					((AuthServiceAsync) GWT.create(AuthService.class)).getAuthToken(tbSender.getText(), tbpass.getText(), new AsyncCallback<String>() {
						
						@Override
						public void onSuccess(String result) {
							vp.add(new Label("Retrieved token "+result));
						}
						
						@Override
						public void onFailure(Throwable caught) {
							
						}
					});
				}
			});
        }
    }

}