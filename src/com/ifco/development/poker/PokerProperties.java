package com.ifco.development.poker;


public class PokerProperties {
	//default-Properties
	private static final boolean ADMIN_ALLOWED=false;
	private static final boolean CHAT_ENABLED=false;
	private static final boolean TIMEOUT_ENABLED=false;
	private static final boolean STOP_GAME_WITHOUT_CARD_ALLOWED=true;
	private static final int REFRESH_TIME=10;
	private static final int MAX_CHAT_MSGS = 10;
	private static final int MAX_CHARS_ALLOWED_IN=46;
	private static final int MAX_USERS=Integer.MAX_VALUE;
	private static final long IDLE_TIME=Long.MAX_VALUE;

	/*
	 * Bei gültiger Property:
	 * 	gebe den Wert der Propertie zurück
	 * Bei ungültiger Property:
	 * 	gebe standardwert zurück
	 * Bei nicht vorhandener Property
	 * 	setze Propertie auf Standardwert und gebe diesen zurück
	 */
	public static boolean getAdminAllowed() {
		final String AdminAllowed=System.getProperty("com.ifco.development.poker.admin_allowed");
		if(AdminAllowed==null) {
			System.setProperty("com.ifco.development.poker.admin_allowed", String.valueOf(ADMIN_ALLOWED));
			return ADMIN_ALLOWED;
		}
		if(AdminAllowed.equals("true")) {
			return true;
		}
		if(AdminAllowed.equals("false")) {
			return false;
		}
		return ADMIN_ALLOWED;
	}
	public static boolean getChatEnabled() {
		final String ChatEnabled=System.getProperty("com.ifco.development.poker.chat_enabled");
		if(ChatEnabled==null) {
			System.setProperty("com.ifco.development.poker.chat_enabled", String.valueOf(CHAT_ENABLED));
			return CHAT_ENABLED;
		}
		if(ChatEnabled.equals("true")) {
			return true;
		}
		if(ChatEnabled.equals("false")) {
			return false;
		}
		return CHAT_ENABLED;
	}
	public static boolean getTimeoutEnabled() {
		final String timeoutEnabled=System.getProperty("com.ifco.development.poker.timeout_enabled");
		if(timeoutEnabled==null) {
			System.setProperty("com.ifco.development.poker.timeout_enabled", String.valueOf(CHAT_ENABLED));
			return TIMEOUT_ENABLED;
		}
		try {
			return Boolean.parseBoolean(timeoutEnabled);
		} catch (Exception e) {
			return TIMEOUT_ENABLED;
		}
	}
	public static boolean getStopGameWithoutCardAllowed() {
		final String stop=System.getProperty("com.ifco.development.poker.StopGameWithoutCardAllowed");
		if(stop==null) {
			System.setProperty("com.ifco.development.poker.StopGameWithoutCardAllowed", String.valueOf(STOP_GAME_WITHOUT_CARD_ALLOWED));
			return STOP_GAME_WITHOUT_CARD_ALLOWED;
		}
		try {
			return Boolean.parseBoolean(stop);
		} catch (Exception e) {
			return STOP_GAME_WITHOUT_CARD_ALLOWED;
		}
	}
	public static int getRefreshTime() {
		final String refreshTime=System.getProperty("com.ifco.development.poker.refreshTime");
		
		if(refreshTime==null) {
			System.setProperty("com.ifco.development.poker.refreshTime", String.valueOf(REFRESH_TIME));
			return REFRESH_TIME;
			
		}
		try {
			if(Integer.parseInt(refreshTime)>=0) {
				return Integer.parseInt(refreshTime);
			}
			return REFRESH_TIME;
		} catch (final NumberFormatException e) {
			return REFRESH_TIME;
		}
	}
	public static int getMaxChatMsgs() {
		final String maxMsgs=System.getProperty("com.ifco.development.poker.maxChatMsgs");
		if(maxMsgs==null) {
			System.setProperty("com.ifco.development.poker.maxChatMsgs", String.valueOf(MAX_CHAT_MSGS));
			return MAX_CHAT_MSGS;
		}
		try {
			if(Integer.parseInt(maxMsgs)<=0) {
				return MAX_CHAT_MSGS;
			}
			return Integer.parseInt(maxMsgs);
		} catch (final NumberFormatException e) {
			return MAX_CHAT_MSGS;
		}
	}
	public static int getMaxCharactarsAllowedIn() {
		final String maxChars=System.getProperty("com.ifco.development.poker.maxCharsAllowedIn");
		if(maxChars==null) {
			System.setProperty("com.ifco.development.poker.maxCharsAllowedIn", String.valueOf(MAX_CHARS_ALLOWED_IN));
			return MAX_CHARS_ALLOWED_IN;
		}
		try {
			if(Integer.parseInt(maxChars)<=0) {
				return MAX_CHARS_ALLOWED_IN;
			}
			return Integer.parseInt(maxChars);
		} catch (final NumberFormatException e) {
			return MAX_CHARS_ALLOWED_IN;
		}
	}
	public static int getMaxUsers() {
		final String maxUsers=System.getProperty("com.ifco.development.poker.maxUsers");
		if(maxUsers==null) {
			System.setProperty("com.ifco.development.poker.maxUsers", String.valueOf(MAX_USERS));
			return MAX_USERS;
		}
		try {
			if(Integer.parseInt(maxUsers)==0) {
				return Integer.MAX_VALUE;
			}
			if(Integer.parseInt(maxUsers)<0) {
				return MAX_USERS;
			}
			return Integer.parseInt(maxUsers);
		} catch (final NumberFormatException e) {
			return MAX_USERS;
		}
	}
	public static long getIdleTime() {
		final String IdleTime=System.getProperty("com.ifco.development.poker.idleTime");
		if(IdleTime==null) {
			System.setProperty("com.ifco.development.poker.idleTime", String.valueOf(IDLE_TIME));
			return IDLE_TIME;
		}
		try {
			if(Integer.parseInt(IdleTime)==0) {
				return Long.MAX_VALUE;
			}
			if(Integer.parseInt(IdleTime)<0) {
				return IDLE_TIME;
			}
			return Integer.parseInt(IdleTime)*1000*60;
		} catch (final NumberFormatException e) {
			return IDLE_TIME;
		}
	}
}
