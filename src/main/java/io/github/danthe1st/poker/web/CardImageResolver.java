package io.github.danthe1st.poker.web;

import io.github.danthe1st.poker.Constants;
import io.github.danthe1st.poker.Game;

public class CardImageResolver {
	/*
	 * gibt den Namen des Bildes der angegebenen Karte zur�ck

	 * Name des Bildes: c<karte><Dateiformat der Karte>
	 * 	Wenn kein Dateiformat bekannt ist wird das DEFAULT-Dateiformat verwendet
	 */
	public String getCardImage(final String card){
		String datei= "c"+card;
		if(Constants.getDateiformat().containsKey(card)){
			datei+=Constants.getDateiformat().get(card);
		}
		else{
			datei+=Constants.getDateiformat().get("DEFAULT");
		}
		return datei;
	}
	public String getCardImage(final int card){
		
		return getCardImage(String.valueOf(card));
	}
	/*
	 * gibt das anzuzeigende Bild zur�ck

	 * Wenn der eigene Benutzer, der ausgew�hlte Benutzer oder das Spiel null sind wird null zur�ckgegeben
	 * Wenn der ausgew�hlte Benutzer nochkeine Karte ausgew�hlt hat wird das Idle-Bild zur�ckgegeben
	 * Wenn der ausgew�hlte Benutzer gleich dem eigenen ist wird die dazugeh�rige Karte zur�ckgegeben
	 * Sonst wird der R�ckgabewert von getUserDisplayedCard(siehe Game) zur�ckgegeben
	 * 
	 */
	public String getUserCardImage(final String user,final String myUser,final Game g){
		if(user==null||myUser==null||g==null){
			return "null";
		}
		final String myCard = g.getCard(user);
		final String card=g.getUserDisplayedCard(user);
		if(card.equals("Idle")||card.equals("Coffee")){
			return getCardImage(card);
		}
		if(user.equals(myUser)){
			return this.getCardImage(myCard);
		}
		return this.getCardImage(card);
	}
}
