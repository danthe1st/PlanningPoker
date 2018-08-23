package com.ifco.development.poker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Game {	

	private final Map<String, UserData> users = new HashMap<String, UserData>();// Map in der für jeden User Daten gesetzt werden
	private static final String[] ALL_CARDS = new String[] { "0", "1", "2", "3", "5", "8", "13", "20", "40", "100", "Infinity", "Unknown", "Coffee" };// private Konstantenarrey für  alle Karten
	private boolean started = false;
	private boolean completed = false;
	private String label = "";
	private String[] chat = new String[PokerProperties.getMaxChatMsgs()];
	private int msgCount = 0;
	private int msgNum=0;

	public int getMsgCount() {
		return msgCount;
	}

	/*
	 * Logischer Getter für den Chat.
	 * 
	 * Wenn der Chat aktiviert ist(Konstante):
	 * 	Falls Chat vorhanden:
	 * 		gebe String[] mit dem Chat zurück.
	 *	 sonst
	 *	 	erstelle neuen, leeren Chat und gebe diesen zurück.
	 * sonst:
	 * 	gebe null zurück
	 */
	public String[] getChat() {
		if(PokerProperties.getChatEnabled()) {
			if(this.chat==null||this.chat.length!=PokerProperties.getMaxChatMsgs()) {
				this.chat=new String[PokerProperties.getMaxChatMsgs()];
			}
			final String[] chat=new String[PokerProperties.getMaxChatMsgs()];
			int i=0;
			for(int j=msgNum;j<PokerProperties.getMaxChatMsgs();j++) {
				chat[i]=this.chat[j];
				i++;
			}
			i=0;
			for(int j=0;j<msgNum;j++) {
				chat[i]=this.chat[j];
				i++;
			}

			return chat;
		}
		return null;
	}

	/*
	 * Wenn der Chat aktiviert ist(Konstante):
	 * 	Füge dem Chat eine Nachricht hinzu
	 */
	public void addChatMsg(final String msg) {
		if(PokerProperties.getChatEnabled()) {
			if(this.chat==null||this.chat.length!=PokerProperties.getMaxChatMsgs()){
				this.chat=new String[PokerProperties.getMaxChatMsgs()];
			}
			chat[msgNum]=msg;
			if(msgNum<PokerProperties.getMaxChatMsgs()-1) {
				msgNum++;
			}
			else {
				msgNum=0;
			}
		}
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(final String label) {
		this.label = label;
	}

	/*
	 * prüft ob Benutzer registriert ist
	 */
	public boolean isUserRegistered(final String username) {
		if(username==null){
			return false;
		}
		return this.users.containsKey(username);
	}

	/*
	 * prüft ob ein Benutzer Admin ist
	 */
	public boolean isAdmin(final String user) {
		if (isUserRegistered(user)) {
			return users.get(user).isAdmin();
		} else {
			return false;
		}
	}

	/*
	 * prüft ob eine Karte Infinity ist
	 */
	public boolean isInfinity(final int value) {
		if (value == Integer.MAX_VALUE) {
			return true;
		}
		// else
		return false;
	}

	/*
	 * Setzt oder löscht Admin-Berechtigungen eines Benutzers(true: Admin, false: kein Admin)
	 */
	public void setAdmin(final String user, final boolean status) {
		if (PokerProperties.getAdminAllowed()) {
			users.get(user).setAdmin(status);
		}
	}

	/*
	 * Main-Methode zum testen
	 */
	public static void main(String[] args) {
		final Game g = new Game();

		@SuppressWarnings("resource")
		final Scanner scan = new Scanner(System.in);
		String userS;
		String cardS;

//Eingabe von Benutzernamen
		while (true) {
			System.out.println("select Username to register or tip in '_'):");
			userS = scan.next();
			if (userS.equals("_")) {
				break;
			}
			g.registerUser(userS);
		}
//Eingabe der Karten der Benutzernamen(beenden mit stop/stop)
//Buenutzerliste: list/list
		while (true) {
			if (g.isReady()||!g.hasUsers()) {
				g.stop();
				break;
			}
			System.out.println("Bitte Benutzername und datzgehörige Karte eingeben:");

			userS = scan.next();
			cardS = scan.next();
//jader Benutzer hat eine Karte-->Stoppe das Spiel
			if (g.isReady()) {
				g.stop();
				break;
			}
//Eingabe von stop-->stoppe das Spiel
			if (userS.equals("stop") && ((g.getCardValue(cardS) == -1) || !g.isUserRegistered(userS))) {
				g.stop();
				break;
//Eingabe von list, userlist oder users-->gibt Benutzer aus
			} else if (((userS.equals("list")) || (userS.equals("userlist")) || (userS.equals("users")))
					&& ((g.getCardValue(cardS) == -1) || !g.isUserRegistered(userS))) {
				for (final String user : g.getUsernames()) {
					System.out.println("user: \"" + user + "\"");
				}
//gültige Karte-->Spiele diese Karte
			} else if (g.getCardValue(cardS) != -1) {
				g.playCard(userS, cardS);
			} else {
				continue;
			}
		}
		System.out.println(g.toString());
	}
	/*
	 * gibt Zahl zu angegebener Karte zurück

	 * Ungültige Karte:null 
	 * ?:1000
	 * Kaffee: 1001 
	 * Idle: 1002 
	 * Infinity: Maximaler int-Wert 
	 * sonst: Zahl der Karte
	 */
	public int getCardValue(final String card) {

		if (card == null) {
			return -1;
		}
		if (card.equals("Unknown")) {
			return 1000;
		}
		if (card.equals("Coffee")) {
			return 1001;
		}
		if (card.equals("Idle")) {
			return 1002;
		}
		if (card.equals("Infinity")) {
			return Integer.MAX_VALUE;
		}
		try {
			return Integer.parseInt(card);
		} catch (final NumberFormatException e) {
			return -1;
		}
	}
	public String getCardString(final int value){
		String cardString=new String();

		if(value<0){
			return null;
		}
		else if(value>100){
			switch(value){// (where return:) no break nessecary(won't be executed)
				case (Integer.MAX_VALUE):{
					return "Infinity";
				}
				case (1000):{
					return "Unknown";
				}
				case (1001):{
					return "Coffee";
				}
				case (1002):{
					return "Idle";
				}
				default:{
					return null;
				}
			}
		}
		else{
			cardString=""+value;
			if(isCardValid(cardString)){
				return cardString;
			}
			//else|
			//	  |
		}	//	 \|/
		return null;
	}
	/*
	 * Prüft ob Karte gültig(möglich zu spielen) ist

	 * gültig: gebe true zurück ungültig: gebe false zurück
	 */
	public boolean isCardValid(final String card) {
		for (int i = 0; i < ALL_CARDS.length; i++) {
			if (card.equals(ALL_CARDS[i])) {
				return true;
			}
		}
		return false;
	}
	/*
	 * Prüft ob eine Spielbare Karte gespielt ist(Zahl, Unendlich oder ?
	 */
	private boolean isPlayableCardPlayed(){
		for (final String user : users.keySet()){
			final int value=getCardValue(getCard(user));
			if((value>=0&&value<=100)||value==Integer.MAX_VALUE||value==1000){
				return true;
			}
		}
		return false;
	}
	/*
	 * Prüft ob ein Benutzer mit gültiger Karte(Zahl oder Unendlich) vorhanden ist.

	 * vorhanden: true 
	 * nicht vorhanden: false
	 */
	public boolean hasUserWithValidCard() {
		for (final String user : users.keySet()) {
			final int value = getCardValue(getCard(user));
			if (((value >= 0) && (value <= 100)) || (value == Integer.MAX_VALUE)) {
				return true;
			}
		}
		// else
		return false;
	}
	/*
	 * Prüft ob es Benutzer gibt, welche eine Karte ausgewählt haben
	 */
	public boolean hasUserWithCard(final int value){
		for (final String user : users.keySet()){
			if(value==getCardValue(getCard(user))){
				return true;
			}
		}
		
		return false;
	}

	/*
	 * gibt alle Benutzernamen in einem Set<String> zurück
	 */
	public Set<String> getUsernames() {
		final Set<String> userNamesSet = new HashSet<String>();
		userNamesSet.addAll(users.keySet());
		return userNamesSet;
	}

	/*
	 * gibt alle Benutzer mit angegebener Kartennummer zurück(durch , getrennt)
	 */
	public String getUsersWithCard(final int cardValue) {
		return getUsersWithCard(cardValue, ", ");
	}
	/*
	 * gibt alle Benutzer mit angegebener Kartennummer zurück(durch übergegebenes Zeichen getrennt)
	 */
	public String getUsersWithCard(final int cardValue, final String trennung) {
		final StringBuilder sb = new StringBuilder();
		boolean loopAlreadyExecuted = false;
		for (final String user : users.keySet()) {

			if ((getCardValue(users.get(user).getCard()) == (cardValue))) {
				if (loopAlreadyExecuted) {
					sb.append(trennung);
				}
				sb.append(user);
				loopAlreadyExecuted = true;
			}
		}
		return sb.toString();
	}
	/*
	 * gibt die Anzahl der Benutzer mit einer übergegebenen Karte zurück(int-Value)
	 */
	public int getNumUsersWithCard(final int cardValue) {
		int num=0;
		for (final String user : users.keySet()) {

			if ((getCardValue(users.get(user).getCard()) == (cardValue))) {
				num++;
			}
		}
		return num;
	}

	/*
	 * gibt einen Set<String> mit allen Benutzer mit angegebenem Kartenstring
	 * zurück(durch , getrennt)
	 */
	public Set<String> getUsersWithCard(final String card) {
		final Set<String> userNamesSet = new HashSet<String>();
		// Collection<UserData> values = users.values();
		// Iterator<UserData> iterator = values.iterator();
		// while(iterator.hasNext()){
		// UserData next = iterator.next();
		// if(next.isCardPlayed()&&next.getCard().equals(card)){
		// userNamesSet.add(next.getName());
		// }
		// }
		for (final UserData userData : users.values()) {
			if (userData.isCardPlayed() && userData.getCard().equals(card)) {
				userNamesSet.add(userData.getName());
			}
		}
		return userNamesSet;
	}

	/*
	 * gibt ein String-Arrey mit allen Karten zurück(Getter für ALL_CARDS)
	 */
	public final String[] getAllCards() {
		return ALL_CARDS;
	}

	/*
	 * gibt die zu dem angegebenen Benutzer dazugehörige Karte an(Getter für users.<Username>.card)
	 */
	public String getCard(final String username) {
		return users.get(username).getCard();
	}

	/*
	 * Setzt zu dem angegebenen Benutzer die angegebene Karte (Setter für users.<Username>.card)
	 */
	public void setCard(final String username, final String card) {
		final UserData userData = users.get(username);
		userData.setCard(card);
	}

	/*
	 * prüft ob ein der angegebene Benutzer eine Karte hat
	 */
	public boolean hasUserCard(final String username) {
		return getCardValue(users.get(username).getCard()) != -1;
	}

	/*
	 * prüft welche Karte angezeigt werden soll

	 * Wenn die Karte null ist gebe Idle zurück
	 *  Wenn das Spiel fertig ist gebe Karte des Benutzers zurück 
	 *  Sonst gebe Rückseite zurück
	 */
	public String getUserDisplayedCard(final String user) {
		final String card = this.getCard(user);
		if (card == null) {
			return ("Idle");
		}
		if (card.equals("Coffee")) {
			return ("Coffee");
		} else if (this.isCompleted()) {
			return (card);
		} else {
			return ("Back");
		}
	}

	/*
	 * (non-Javadoc)

	 * @see java.lang.Object#toString()

	 * Schreibt Spieldaten in einen String 

	 * ------------GAME------------
	 * <jeder user> <Benutzername> <Karte> 
	 * min: <min> 
	 * max: <max> 
	 * average: <Durchschnitt> 
	 * ?: <?Num> 
	 * Users drinking Coffee: <CoffeeNum> 
	 * Users doing nothing: <idleNum>
	 * Errors: <AnzErr>
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("------------GAME------------\n");
		int min = 0;
		int max = 100;
		// int sum=0;
		int avg = 0;
		int qCount = 0;// ?
		int cCount = 0;// Coffee
		int iCount = 0;// Idle
		int errCount = 0;
//Schreibe jeden Benutzer an
		for (final String user : users.keySet()) {
			final UserData userData = users.get(user);
			if (userData.getCard() != null) {
				final int value = getCardValue(userData.getCard());
				if (value < 0) {// Fehler
					errCount++;
				} else {
					if (value <= 100) {// Zahl
						// sum+=value;
						if (min > value)
							min = value;
						if (max < value)
							max = value;
					} else if (value == 1000) {// ?
						qCount++;
					} else if (value == 1001) {// Coffee
						cCount++;
					} else if (value == 1002) {// Idle
						iCount++;
					} else if (value == Integer.MAX_VALUE) {// Infinity
						max = value;
					}
				}

			}

			sb.append(user);
			sb.append(":");
			sb.append(String.valueOf(userData.getCard()));
			sb.append("\n");
		}
//Durchschnitt
		if (users.size() == 0) {
			avg = 0;
		} else {
			avg = getResultAvg();
		}

		max = getResultMax();
		min = getResultMin();
//Ausgabe
		sb.append("min: ");
		sb.append(min);
		sb.append("\n");
		sb.append("max: ");
		if (max == Integer.MAX_VALUE) {
			sb.append("Infinity");
		} else {
			sb.append(max);
		}
		sb.append("\n");

		sb.append("average: ");
		if (avg == Integer.MAX_VALUE) {
			sb.append("The average of Infinity and other numbers is Infinity!");
		} else {
			sb.append(avg);
		}

		sb.append("\n");

		sb.append("?: ");
		sb.append(qCount);
		sb.append("\n");

		sb.append("Users drinking Coffee: ");
		sb.append(cCount);
		sb.append("\n");

		sb.append("Users doing nothing: ");
		sb.append(iCount);
		sb.append("\n");

		sb.append("Errors: ");
		sb.append(errCount);
		sb.append("\n");

		return sb.toString();
	}

	/*
	 * Für Welcome bei Registrieren
	 */
	public boolean isRecentlyRegistered(final String username) {
		return users.get(username).isRecentlyRegistered();
	}
	public void setRecentlyRegistered(final String username, final boolean registered) {
		users.get(username).setRecentlyRegistered(registered);
	}
	/*
	 * registriert und speichert Benutzer
	 */
	public void registerUser(final String username) {
		if (!this.hasUsers()) {
			this.start(username);
		}
		final UserData usrData = new UserData();
		usrData.setName(username);
		usrData.setRecentlyRegistered(true);
		// usrData.setCard(null);
		users.put(username, usrData);
	}
	/*
	 * unregistriert(löscht) Benutzer
	 */
	public void unregisterUser(final String username) {
		unregisterUser(username, false);
	}

	public void unregisterUser(final String username, final boolean silent) {
		/* final UserData remove = */users.remove(username);
		if (!silent) {
			System.out.println("user unregistered: \"" + String.valueOf(username) + "\"");
		}
		if (users.isEmpty()) {
			reset();
		}
	}
	/*
	 * Unregister all outdated Users
	 */
	public void kickOutdatedUsers(){
		if(PokerProperties.getTimeoutEnabled()) {
			for (final String user : users.keySet()) {
				final UserData usrData=users.get(user);
				if((System.currentTimeMillis()-usrData.getLastAction())>=PokerProperties.getIdleTime()) {
					unregisterUser(user);
				}
			}
		}
	}
	public void setLastActionTime(final String user) {
		final UserData usrData=users.get(user);
		if(usrData==null) {
			return;
		}
		usrData.setLastAction(System.currentTimeMillis());
	}
	/*
	 * Startet das Spiel

	 * Alle Karten werden zurückgesetzt 
	 * variable started-->true 
	 * variable completed-->false
	 */
	public void start() {
		if (!isRunning()) {
			for (final String user : users.keySet()) {
				final UserData usrData = users.get(user);
				usrData.restart();
			}
			this.started = true;
			this.completed = false;
			System.out.println("Game started");
			return;
		}
		//else
		System.err.println("Cannot start Game becouse it is already running");
	}
	public void start(final String username) {
		if (!isRunning()) {
			for (final String user : users.keySet()) {
				final UserData usrData = users.get(user);
				if(username.equals(user)){
					setCard(username, null);
					usrData.setDefaultCard(null);//testen: Caffee bleibt?...
					usrData.setLastAction(System.currentTimeMillis());
				}
				usrData.restart();
			}
			this.started = true;
			this.completed = false;
			System.out.println("Game started");
			return;
		}
		//else
		System.err.println("Cannot start Game becouse it is already running");
	}

	/*
	 * Stoppt das Spiel

	 * Alle nicht vorhandenen Karten werden auf 'Coffee'
	 * gesetzt variable started-->false
	 * variable completed-->true
	 */
	public void stop() {
		if (isRunning()) {
			for (final String user : users.keySet()) {
				final UserData usrData = users.get(user);
				if (!usrData.isCardPlayed()) {
					usrData.setCard("Unknown");
				}
			}
			this.started = false;
			this.completed = true;
			System.out.println("Game stopped");
			return;
		}
		//else
		System.err.println("Cannot stop Game becouse it is not running");
	}

	/*
	 * Das Spiel wird Resetted
	 * 
	 * variable started-->false 
	 * variable completed-->false 
	 * alle Benutzer werden gelöscht
	 */
	public void reset() {

		this.started = false;
		this.completed = false;
		chat = new String[PokerProperties.getMaxChatMsgs()];
		msgCount = 0;
		setLabel(null);
		if (!users.isEmpty()) {
			for (final String user : users.keySet()) {
				final UserData usrData = users.get(user);
				usrData.reset();
			}
		}
		System.out.println("Game resetted");
	}

	/*
	 * prüft ob Benutzer vorhanden sind
	 */
	public boolean hasUsers() {
		return !(users.isEmpty());
	}

	/*
	 * gibt die Anzahl der Benutzer zurück
	 */
	public int getNumUsers() {
		return users.size();
	}
	/*
	 * gibt die Anzahl der Benutzer mit einer Numerischen Karte(oder Infinity) zurück
	 */
	public int getNumUsersWithNumericCard() {
		int num=0;
		for (final String user : users.keySet()){
			final int value=getCardValue(getCard(user));
			if(value>=0&&value<=100||value==Integer.MAX_VALUE){
				num++;
			}
		}
		return num;
	}

	/*
	 * Spielt eine Karte

	 * Wenn das Spiel läuft Karte wird zu dem Benutzer gesetzt
	 *	 Wenn alle Beutzer gespielt haben
	 * 		Stoppe das Spiel
	 *	 gebe true zurück
	 * Sonst
	 * 	gebe falsch zurück
	 */
	public boolean playCard(final String user, final String card) {
		if (isRunning() && users.containsKey(user) && isCardValid(card)) {
			final UserData usrData = users.get(user);
			usrData.setCard(card);
			if(card.equals("Coffee")){
				usrData.setDefaultCard(card);
			}
			else{
				usrData.setDefaultCard(null);
			}
			System.out.println("\"" + user + "\" played the card \"" + card + "\"");
			usrData.setLastAction(System.currentTimeMillis());
			if (isAllUsersPlayed()&&isPlayableCardPlayed()&&getNumUsers()-getNumUsersWithCard(1001)>=2) {
				this.stop();
			}
			return true;
		} else {
			return false;
		}
	}

	/*
	 * prüft ob alle Benutzer gespielt haben
	 * 
	 * Wenn ein Benutzer keine Karte gespielt hat gebe false zurück 
	 * Sonst gebe true zurück
	 */
	private boolean isAllUsersPlayed() {
		for (final String user : users.keySet()) {
			final UserData usrData = users.get(user);
			if (usrData.getCard() == null) {
				return false;
			}
		}
		return true;
	}

	/*
	 * Prüft ob das Spiel nicht läuft(/gestartet werden kann)
	 */
	public boolean isReady() {
		return !isRunning();
	}

	/*
	 * gibt die kleinste gespielte Karte zurück

	 * Keine gültigen Karten verfügbar:
	 * 0 Wenn die kleinste(einzige) Karte Infinity ist-->Infinity
	 */
	public final int getResultMin() {
		if (!isReady()) {
			return 0;
		}

		int min = 101;
		// String minUser="";
		boolean hasInfinity = false;
		for (final String user : users.keySet()) {
			final UserData usrData = users.get(user);
			if ((getCardValue(usrData.getCard()) < min) && getCardValue(usrData.getCard()) >= 0) {
				// minUser=user;
				min = getCardValue(usrData.getCard());
			} else if (getCardValue(usrData.getCard()) == Integer.MAX_VALUE) {
				hasInfinity = true;
			}
		}
		if (min == 101 && hasInfinity) {
			min = Integer.MAX_VALUE;
		}

		if (min != 101)
			return min;
		return 0;
		// getCardValue(users.get(minUser).getCard());
		// users.get(minUser).getCard();

	}

	/*
	 * gibt die größte gespielte Karte zurück Keine gültigen Karten verfügbar: 0
	 */
	public final int getResultMax() {

		if (!isReady()) {
			return 0;
		}

		int max = 0;
		// String maxUser="";

		for (final String user : users.keySet()) {
			final UserData usrData = users.get(user);
			if (((getCardValue(usrData.getCard()) > max) && getCardValue(usrData.getCard()) <= 100)
					|| getCardValue(usrData.getCard()) == Integer.MAX_VALUE) {
				// maxUser=user;
				max = getCardValue(usrData.getCard());
			}
		}

		return max;
		// getCardValue(users.get(maxUser).getCard());
		// users.get(maxUser).getCard();
	}

	/*
	 * gibt den Durchschnitt der aller gültigen Karten zurück
	 * 
	 * Keine gültigen Karten verfügbar: 0
	 */
	public final int getResultAvg() {

		if (!isReady()) {
			return 0;
		}

		int anz = 0;
		int sum = 0;
		boolean infinityFound=false;

		for (final String user : users.keySet()) {
			final UserData usrData = users.get(user);
			if(getCardValue(usrData.getCard())==Integer.MAX_VALUE){
				infinityFound=true;
			}
			if ((getCardValue(usrData.getCard()) >= 0) && (getCardValue(usrData.getCard()) <= 100)) {
				sum += getCardValue(usrData.getCard());
				anz++;
			}
		}
		if (anz == 0) {
			if(infinityFound){
				return Integer.MAX_VALUE;
			}
			return 0;
		}
		return sum / anz;

	}

	/*
	 * prüft ob das Spiel fertig gespielt wurde
	 */
	public boolean isCompleted() {
		if (this.completed&&!this.isRunning()) {
			this.completed=true;
		} else {
			this.completed=false;
		}
		return this.completed;
	}

	/*
	 * prüft ob gerade gespielt wird
	 */
	public boolean isRunning() {
		if (this.started && !this.completed) {
			return true;
		} else {
			return false;
		}
	}
}