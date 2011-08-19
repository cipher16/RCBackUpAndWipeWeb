package eu.grigis.gaetan.rcweb.client.ui;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import eu.grigis.gaetan.rcweb.client.services.TransformData;
import eu.grigis.gaetan.rcweb.client.services.TransformDataAsync;
import eu.grigis.gaetan.rcweb.shared.DataTransfer;

public class PhoneControl extends Composite{
	
	private static PhoneControlUiBinder uiBinder = GWT.create(PhoneControlUiBinder.class);
	interface PhoneControlUiBinder extends UiBinder<Widget, PhoneControl> {}

	@UiField FlexTable fxInfo;
	@UiField VerticalPanel menu;
	public PhoneControl(String type) {
		initWidget(uiBinder.createAndBindUi(this));
		
		menu.setStyleName("links-left");
		menu.add(new Hyperlink("Status","phone.status"));
		menu.add(new Hyperlink("Geoloc","phone.geoloc"));
		menu.add(new Hyperlink("Ring","phone.ring"));
		
		if(type.equals("ring"))
			sendMessage("ring");
		displayLastType(type);
	}
	
	public void displayLastType(final String type)
	{
		if(type.equals("ring"))
		{
			fxInfo.setText(0, 0, "The phone is ringing now");
			return;
		}
		fxInfo.setText(0, 0, "Loading Data");
		((TransformDataAsync) GWT.create(TransformData.class)).getLastTypeForCurrentRegUser(type, new AsyncCallback<DataTransfer>() {
			@Override
			public void onSuccess(DataTransfer result) {
				Button btnRefresh = new Button("Send notification to refresh data");
				btnRefresh.addClickHandler(new ClickHandler() {
					@Override public void onClick(ClickEvent event) {
						sendMessage(type);
					}
				});
				fxInfo.setWidget(0, 0, btnRefresh);
				fxInfo.getFlexCellFormatter().setColSpan(0, 0, 2);
				if(result==null)
				{
					fxInfo.setText(1, 0, "No data found");
					fxInfo.getFlexCellFormatter().setColSpan(1, 0, 2);
					return;
				}
				final HashMap<String,String> map=result.getData();
				int i=1;
				if(map==null)
					return;
				for (String key : map.keySet()) {
					fxInfo.setText(i, 0, key);
					fxInfo.setText(i, 1, map.get(key));
					i++;
				}
				//display geoloc info
				if(result.getType().equals("GEOLOC")&&i>0)
				{
					String ApiKey = "ABQIAAAAXpEB7Go1TVzVyQBm4VXr7BT2E6_VxY2Ak13-OcHSQevEFnxe1xRmwlx8Scb6CVGYiwOvPWgNQh_eoA";
					System.out.println("Proto : "+Window.Location.getProtocol());
					if(Window.Location.getProtocol().startsWith("https"))
						ApiKey="ABQIAAAAYXiByvugTLk9egeRM901lxTTqgRoTvkyqtnpNyMv68yzun4bLBRWHuXHxezjMlB74rbct142iIaMIg";
					Maps.loadMapsApi(ApiKey, "2", false, new Runnable() {@Override public void run() {
						LatLng lat = LatLng.newInstance(Double.valueOf(map.get("lat")), Double.valueOf(map.get("long")));
						MapWidget gmap=new MapWidget(lat,15);
						
						gmap.setStyleName("gmap");
						gmap.addOverlay(new Marker(lat));
					    gmap.getInfoWindow().open(lat,new InfoWindowContent("Last known position"));
						gmap.addControl(new LargeMapControl());
						gmap.setSize("400px", "300px");
						fxInfo.setWidget(0, 3, gmap);
						fxInfo.getFlexCellFormatter().setRowSpan(0, 3, map.size()+2);
					}});
				}
			}
			@Override
			public void onFailure(Throwable caught) {}
		});
	}
	
	public void sendMessage(final String type)
	{
		fxInfo.removeAllRows();
		fxInfo.setText(0,0,"Sending notification ...");
		((TransformDataAsync) GWT.create(TransformData.class)).sendMessage(type, new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				if(type.equals("ring"))
					return;//do nothing when phone is ringing ... no data to display
				fxInfo.removeAllRows();
				displayLastType(type);
			}
			@Override
			public void onFailure(Throwable caught) {}
		});
	}
}
