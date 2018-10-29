package io.github.danthe1st.poker;

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
	 * Getter f�r Map dateiformat
	 */
	public static final Map<String, String> getDateiformat() {
		return dateiformat;
	}
}
