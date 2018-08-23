package com.ifco.development.poker;

import java.util.HashMap;
import java.util.Map;

public class Constants {
	public static final String SESSION_USER = "user";// Sitzungsattribut f�r den Benutzernamen
	public static final String REQUEST_CARD = "card";// Requestattribut f�r die Karte
	public static final String MSG_ERR = "errmsg";// Konstante f�r anzuzeigende Fehlernachrichten
	public static final String REQ_LABEL = "label";// Konstante f�r label/issue
	public static final String ALLOWED_IN = "^[A-Za-z0-9_.~`?/@#%^*&_+=<>.,;:\\|(){}\\[\\]\\- ]+$";
	private static final Map<String, String> dateiformat = new HashMap<String, String>();// Map f�r gespeicherte Dateiformate
	/*
	 * static-Methode: wird bei ersten Aufruf der Klasse aufgerufen 
	 * Map dateiformat wird gesetzt
	 */
	static {
		dateiformat.clear();
		dateiformat.put("Idle", ".gif");
		dateiformat.put("Unknown", ".jpg");
		dateiformat.put("Coffee", ".jpg");
		dateiformat.put("0", ".jpg");
		dateiformat.put("1", ".jpg");
		dateiformat.put("2", ".jpg");
		dateiformat.put("3", ".jpg");
		dateiformat.put("5", ".jpg");
		dateiformat.put("8", ".jpg");
		dateiformat.put("20", ".jpg");
		dateiformat.put("40", ".jpg");
		dateiformat.put("100", ".jpg");
		dateiformat.put("Infinity", ".jpg");
		dateiformat.put("Back", ".jpg");
		dateiformat.put("DEFAULT", ".jpg");
	}

	/*
	 * Getter f�r Map dateiformat
	 */
	public static final Map<String, String> getDateiformat() {
		return dateiformat;
	}
}
