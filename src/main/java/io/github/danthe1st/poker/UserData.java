package io.github.danthe1st.poker;

public class UserData {
	private String name;//String f�r Benutzername
	private String card;//String f�r Karte
	private long lastAction=System.currentTimeMillis();//Variable f�r Zeit der letzten gespielten Karte in ms
	private boolean Admin=false;
	private String defaultCard=null;
	private boolean recentlyRegistered=false;
	public String getDefaultCard() {
		return defaultCard;
	}
	public void setDefaultCard(final String defaultCard) {
		this.defaultCard = defaultCard;
	}
	public boolean isAdmin() {
		if(!PokerProperties.getAdminAllowed()){
			return false;
		}
		return Admin;
	}
	public void setAdmin(final boolean Admin) {
		this.Admin = Admin;
	}
	public String getName() {
		return name;
	}
	public void setName(final String name) {
		this.name = name;
	}
	public String getCard() {
		return card;
	}
	public void setCard(final String card) {
		this.card = card;
	}
	public long getLastAction() {
		return lastAction;
	}
	public void setLastAction(final long lastPlayed) {
		this.lastAction = lastPlayed;
	}
	public void reset(){
		this.setCard(null);
		this.setLastAction(System.currentTimeMillis());
	}
	public void restart(){
		if(card==null){
			return;
		}
		card=defaultCard;
	}
	public boolean isCardPlayed(){
		return card!=null;		
	}
	public boolean isRecentlyRegistered() {
		return recentlyRegistered;
	}
	public void setRecentlyRegistered(final boolean recentlyRegistered) {
		this.recentlyRegistered = recentlyRegistered;
	}

}
