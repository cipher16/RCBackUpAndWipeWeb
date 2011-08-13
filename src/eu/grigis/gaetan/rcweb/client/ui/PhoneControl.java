package eu.grigis.gaetan.rcweb.client.ui;


import java.util.HashMap;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import eu.grigis.gaetan.rcweb.client.services.TransformData;
import eu.grigis.gaetan.rcweb.client.services.TransformDataAsync;
import eu.grigis.gaetan.rcweb.shared.DataTransfer;
import eu.grigis.gaetan.rcweb.shared.UserInfo;

public class PhoneControl extends HorizontalPanel{

	private FlexTable fxInfo;
	private static Logger log= Logger.getLogger("phoneControl");
	public PhoneControl() {
		fxInfo= new FlexTable();
		Button btnStatus = new Button("Get Status");
		Button btnLastReg = new Button("Get Last Reg");
		VerticalPanel vp = new VerticalPanel();
		
		vp.add(btnStatus);
		vp.add(btnLastReg);
		this.add(vp);
		this.add(fxInfo);
		
		/*Button handler*/
		btnStatus.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				((TransformDataAsync) GWT.create(TransformData.class)).sendMessage("status", new AsyncCallback<String>() {
					@Override
					public void onSuccess(String result) {
						fxInfo.clear();
						displayLastStatus();
					}
					@Override
					public void onFailure(Throwable caught) {}
				});
			}
		});
		btnLastReg.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fxInfo.clear();
				displayLastRegId();
			}
		});
	}
	
	public void displayLastStatus()
	{
		((TransformDataAsync) GWT.create(TransformData.class)).getLastTypeForCurrentRegUser("status", new AsyncCallback<DataTransfer>() {
			
			@Override
			public void onSuccess(DataTransfer result) {
				log.warning("Getting data : "+(result==null) );
				if(result==null)
					return;
				log.warning("Some debug "+result.getId()+" "+result.getMail());
				HashMap<String,String> map=result.getData();
				int i=0;
				if(map==null)
					return;
				for (String key : map.keySet()) {
					fxInfo.setText(i, 0, key);
					fxInfo.setText(i, 1, map.get(key));
					i++;
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {}
		});
	}
	public void displayLastRegId()
	{
		((TransformDataAsync) GWT.create(TransformData.class)).getLastTypeForCurrentRegUser("auth", new AsyncCallback<DataTransfer>() {
			
			@Override
			public void onSuccess(DataTransfer result) {
				if(result==null)
					return;
				fxInfo.setText(0, 0, "regId");
				fxInfo.setText(0, 1, result.getId());
				fxInfo.setText(1, 0, "mail");
				fxInfo.setText(1, 1, result.getMail());
			}
			
			@Override
			public void onFailure(Throwable caught) {}
		});
	}
}
