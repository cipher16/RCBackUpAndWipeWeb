package eu.grigis.gaetan.rcweb.client;

import com.google.gwt.i18n.client.Constants;

public interface RCBUConstants extends Constants {

	/*Home parts*/
	@DefaultStringValue("Home")
	String home();
	
	@DefaultStringValue("FAQ")
	String faq();
	
	@DefaultStringValue("TOS")
	String tos();
	
	@DefaultStringValue("About")
	String about();

	@DefaultStringValue("Administration")
	String administration();

	@DefaultStringValue("Connection")
	String connection();

	@DefaultStringValue("Disconnection")
	String disconnection();

	@DefaultStringValue("Phone")
	String phone();
	
	@DefaultStringValue("Close")
	String close();

	/*Phone Parts*/
	
	@DefaultStringValue("Status")
	String status();
	
	@DefaultStringValue("Geoloc")
	String geoloc();

	@DefaultStringValue("Ring")
	String ring();
	
	@DefaultStringValue("Lock")
	String lock();

	@DefaultStringValue("Send notification to refresh data")
	String sendNotifToRefresh();

	@DefaultStringValue("The phone is ringing now")
	String phoneIsRinging();

	@DefaultStringValue("Loading Data")
	String loadingData();

	@DefaultStringValue("No data found")
	String notDataFound();

	@DefaultStringValue("Last known position")
	String lastKnownPosition();

	@DefaultStringValue("Sending notification ...")
	String sendingNotification();

	@DefaultStringValue("Waiting phone's response")
	String waitingPhoneResponse();

	@DefaultStringValue("Set Password")
	String setPassword();

	@DefaultStringValue("Password")
	String password();

	@DefaultStringValue("Are you sure ??")
	String areYouSure();
	
	@DefaultStringValue("To lock your phone set a password, to unlock it set an empty password")
	String toLockYourPhone();
	
	@DefaultStringValue("Lock has been send to your phone")
	String lockSentToYourPhone();
	
	/*Administration Parts*/
	@DefaultStringValue("To send C2DM notification we need to get a token from the 'sender' account that you configured during the C2DM registration (only the token will be stored in database).")
	String sendC2DMAuth();
	
	@DefaultStringValue("Save Authentication Header")
	String saveAuth();
	
	@DefaultStringValue("Login of sender account")
	String adminLogin();
	
	@DefaultStringValue("password (will not be stored)")
	String adminPass();
	
	@DefaultStringValue("Retrieved token and stored in database ")
	String adminGotToken();
	
	/*Server Parts : TransformData*/
	
	@DefaultStringValue("No registered phone found for your account")
	String noRegisteredPhoneFound();
	
	@DefaultStringValue("You were not found in our database")
	String userNotFound();
	
	@DefaultStringValue("We were unable to send the notification")
	String unableToSendMsg();
}
