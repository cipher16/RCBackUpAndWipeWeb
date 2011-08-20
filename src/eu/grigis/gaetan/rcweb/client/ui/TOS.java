package eu.grigis.gaetan.rcweb.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class TOS extends Composite {

	private static AboutUiBinder uiBinder = GWT.create(AboutUiBinder.class);

	interface AboutUiBinder extends UiBinder<Widget, TOS> {
	}

	public TOS() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
