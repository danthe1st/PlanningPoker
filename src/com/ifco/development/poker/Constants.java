package com.ifco.development.poker;

import java.util.HashMap;
import java.util.Map;

public class Constants {
	public static final String SESSION_USER = "user";// Sitzungsattribut für den Benutzernamen
	public static final String REQUEST_CARD = "card";// Requestattribut für die Karte
	public static final String MSG_ERR = "errmsg";// Konstante für anzuzeigende Fehlernachrichten
	public static final String REQ_LABEL = "label";// Konstante für label/issue
	public static final String ALLOWED_IN = "^[A-Za-z0-9_.~`?/@#%^*&_+=<>.,;:\\|(){}\\[\\]\\- ]+$";
	private static final Map<String, String> dateiformat = new HashMap<String, String>();// Map für gespeicherte Dateiformate
	/*
	 * static-Methode: wird bei ersten Aufruf der Klasse aufgerufen 
	 * Map dateiformat wird gesetzt
	 */
	static {
		dateiformat.clear();
		dateiformat.put("Idle", ".jpg");
		dateiformat.put("Unknown", ".png");
		dateiformat.put("Coffee", ".png");
		dateiformat.put("0", ".png");
		dateiformat.put("1", ".png");
		dateiformat.put("2", ".png");
		dateiformat.put("3", ".png");
		dateiformat.put("5", ".png");
		dateiformat.put("8", ".png");
		dateiformat.put("13", ".png");
		dateiformat.put("20", ".png");
		dateiformat.put("40", ".png");
		dateiformat.put("100", ".png");
		dateiformat.put("Infinity", ".png");
		dateiformat.put("Back", ".png");
		dateiformat.put("DEFAULT", ".jpg");
	}

	/*
	 * Getter für Map dateiformat
	 */
	public static final Map<String, String> getDateiformat() {
		return dateiformat;
	}
}
