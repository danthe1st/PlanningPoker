package com.ifco.development.poker.web;

import com.ifco.development.poker.Constants;
import com.ifco.development.poker.Game;

public class CardImageResolver {
	/*
	 * gibt den Namen des Bildes der angegebenen Karte zurück

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
	
	/*
	 * gibt das anzuzeigende Bild zurück

	 * Wenn der eigene Benutzer, der ausgewählte Benutzer oder das Spiel null sind wird null zurückgegeben
	 * Wenn der ausgewählte Benutzer nochkeine Karte ausgewählt hat wird das Idle-Bild zurückgegeben
	 * Wenn der ausgewählte Benutzer gleich dem eigenen ist wird die dazugehörige Karte zurückgegeben
	 * Sonst wird der Rückgabewert von getUserDisplayedCard(siehe Game) zurückgegeben
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
