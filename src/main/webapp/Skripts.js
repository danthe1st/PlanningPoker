/**
 * Protect label/issue input from deleting while Refreshing
 * 
 * Problems: last pressed key(in input) will be deleted
 * 	You are kicked out of the input field while Refreshing
 */

/*
 * set Label to saved label
 */
function customReset(){
    if(typeof(sessionStorage.getItem('last_entry'))!="undefined"){
        var element = document.getElementById("labelIn");
        element.value = sessionStorage.getItem('last_entry');
    }
//    for (var i = 0; i < sessionStorage.length; i++){
//       var item=sessionStorage.getItem(localStorage.key(i));
//        if(typeof(item)!="undefined"&&item!=null){
//            var element = document.getElementById(item.toString());
//            element.value = sessionStorage.getItem(item.toString());
//        }
//    }
}
//function customReset(name, id){
//    if(sessionStorage.getItem(name)){
//        var element = document.getElementById(id);
//        element.value = sessionStorage.getItem(name);
//    }
//}
/*
 * Speichere Inhalt von Input
 */
function setStorage(element){
	if(element.value==undefined||element.value==null){return;}
    sessionStorage.setItem('last_entry',element.value);
}
//function setStorage(element, name){
//	if(element.value==undefined||element.value==null){return;}
//    sessionStorage.setItem(name,element.value);
//}

/*
 * Führe customReset bei laden des Fensters aus
 */
window.onload = function() {
	customReset();
}


//auto-reload
var secs;
var timerID = null;
var timerRunning = false;
var delay = 1000;
/*
 * Startet den Timer(neu)
 */
function InitializeTimer(seconds) {
    // Set the length of the timer, in seconds
    secs = seconds;
    StopTheClock();
    StartTheTimer();
}
/*
 * Stoppt den Timer
 */
function StopTheClock() {
    if (timerRunning)
        clearTimeout(timerID);
    timerRunning = false;
}
/*
 * Startet den Timer
 */
function StartTheTimer() {
    if (secs == 0) {
        StopTheClock();
        // Here's where you put something useful that's
        // supposed to happen after the allotted time.
        // For example, you could display a message:
        window.location.href = window.location.href;
    }
    else {
        //self.status = 'Remaining: ' + secs;
        document.getElementById("lbltime").innerText = secs + " ";
        secs = secs - 1;
        timerRunning = true;
        timerID = self.setTimeout("StartTheTimer()", delay);
    }
}