package eu.grigis.gaetan.rcweb.client.ui;

import java.util.HashMap;

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

public class PhoneControl extends HorizontalPanel{

	private FlexTable fxInfo;
	public PhoneControl() {
		fxInfo= new FlexTable();
		Button btnStatus = new Button("Status");
		Button btnGeoloc = new Button("Geoloc");
		Button btnRing = new Button("Ring");
		VerticalPanel vp = new VerticalPanel();
		
		vp.add(btnStatus);
		vp.add(btnGeoloc);
		vp.add(btnRing);
		this.add(vp);
		this.add(fxInfo);
		
		//sending message to device through C2DM
		btnStatus.addClickHandler(new ControlHandler("status"));
		btnGeoloc.addClickHandler(new ControlHandler("geoloc"));
		btnRing.addClickHandler(new ControlHandler("ring"));
	}
	
	public void displayLastType(String type)
	{
		((TransformDataAsync) GWT.create(TransformData.class)).getLastTypeForCurrentRegUser(type, new AsyncCallback<DataTransfer>() {
			@Override
			public void onSuccess(DataTransfer result) {
				if(result==null)
					return;
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

	class ControlHandler implements ClickHandler{

		private String type;
		public ControlHandler(String t) {
			type=t;
		}
		
		@Override
		public void onClick(ClickEvent event) {
			fxInfo.removeAllRows();
			fxInfo.setText(0,0,"Loading data ...");
			((TransformDataAsync) GWT.create(TransformData.class)).sendMessage(type, new AsyncCallback<String>() {
				@Override
				public void onSuccess(String result) {
					fxInfo.removeAllRows();
					displayLastType(type);
				}
				@Override
				public void onFailure(Throwable caught) {}
			});
		}
		
	}
}
