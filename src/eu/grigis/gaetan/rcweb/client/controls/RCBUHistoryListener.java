package eu.grigis.gaetan.rcweb.client.controls;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

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
        	{
        		PhoneControl pc = PhoneControl.getInstance();
        		RootPanel.get("content").add(pc);
        		pc.displayType(event.getValue().replace("phone.", ""));
        	}
        }
        else if(event.getValue().equals("admin"))
        {
        	final FlexTable fx = new FlexTable();
        	Button btn = new Button("Save Auth");
        	final TextBox tbSender = new TextBox();
        	final PasswordTextBox tbpass = new PasswordTextBox();
        	int row=0;
        	fx.setText(row++, 0, "To send C2DM notification we need to get a token from the 'sender' account that you configured during the C2DM registration (only the token will be stored in database).");
        	fx.getFlexCellFormatter().setColSpan(0, 0, 2);
        	fx.setText(row, 0, "Login du compte sender");
        	fx.setWidget(row++, 1, tbSender);
        	fx.setText(row, 0,"Le mot de passe");
        	fx.setWidget(row++, 1,tbpass);
        	fx.setWidget(row, 0,btn);
        	RootPanel.get("content").add(fx);
        	
        	btn.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					
					((AuthServiceAsync) GWT.create(AuthService.class)).getAuthToken(tbSender.getText(), tbpass.getText(), new AsyncCallback<String>() {
						
						@Override
						public void onSuccess(String result) {
							fx.setWidget(fx.getRowCount(),0,new Label("Retrieved token "+result));
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