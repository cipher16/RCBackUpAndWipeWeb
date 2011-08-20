package eu.grigis.gaetan.rcweb.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class FAQ extends Composite {

	private static FAQUiBinder uiBinder = GWT.create(FAQUiBinder.class);

	interface FAQUiBinder extends UiBinder<Widget, FAQ> {
	}

	public FAQ() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
