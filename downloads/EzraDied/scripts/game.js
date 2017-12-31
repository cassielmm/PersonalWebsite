/** With help from http://jsfiddle.net/F2es9/ **/

/** Variables **/
var currentNode; //current node
var game; //game data from JSON file
var gameSettings = [1, 1, 1, 1, 1]; //version status of multi-version nodes
    //[clinic, streetleft, item, home, homemetro]
var song = ""; //current song
var key; //passoword
var inventory = []; //inventory
var c = "short"; //how to display the text

/**
 * Setting up the game
 * Fetches and saved JSON data, builds "start" button 
 */
function load() {
    $(function() {
	$.getJSON('../data/data.json', function(data) {
        game = data;
	    document.getElementById("text").innerHTML = "<p><button id='path' onclick='setNode(game.START)'> Start Game </button></p>";
	     });
	 });
}

/**
 * Sets the new node and sends it off to be printed to the screen
 */
window.setNode = function(name) {
    currentNode = name;
    window.printCurrentNode();
}

/**
 * Prints the correct version of the node
 */
window.printCurrentNode = function(){
    //ver = 1 means there is only one version of it
    if (currentNode.ver == 1) {
        window.printCurrentNodeVer();
    //alternatley, ver = -1 and so there are multiple version of this room
    } else {
	//find the version that corresponds to the current setting
        var currentVersion = gameSettings[currentNode.index];
        for (var i = 0; i < currentNode.versions.length; i++) {
            if (currentNode.versions[i].ver == currentVersion) {
		    	currentNode = currentNode.versions[i];
		    	window.printCurrentNodeVer();
		    	break;
            }
        }
    }
}

/**
 * Prints the contents of the node
 */
window.printCurrentNodeVer = function() {
    //text
    document.getElementById("text").innerHTML = currentNode.text;
    
    //paths
    var paths = "";
    if (currentNode.paths != null) {
		for(var i=0,l=currentNode.paths.length;i<l;i++){
	    
	    	if (currentNode.paths[i].inventory != null) {
				paths += "<p><button id='path' onclick='addItem(game."+currentNode.name+")'>"+currentNode.paths[i].text+"</button></p>";
	    	} else {
				paths += "<p><button id='path' onclick='setNode(game."+currentNode.paths[i].direction+")'>"+currentNode.paths[i].text+"</button></p>";
	    	}
		}
    }
    document.getElementById("paths").innerHTML = paths;
    
    //display
    if (currentNode.c != c) {
		document.getElementById("container").className = currentNode.c;
		c = currentNode.c;
    }
    
    //music
    if (currentNode.music != null && currentNode.song != song) {
		document.getElementById("music").innerHTML = currentNode.music;
		song = currentNode.song;
    }
    
    //input
    if (currentNode.input != null) {
		key = currentNode.input[0];
		document.getElementById("textInput").innerHTML = "<input type='text' id = 'input'> <button onclick='limitAccess(game."+currentNode.input[1]+")' > --> </button>"
    } else {
		document.getElementById("textInput").innerHTML = "";
    }
    
    //changes
    if (currentNode.changes != null) {
    	for (var j = 0; j < currentNode.changes.length; j++)
			gameSettings[currentNode.changes[j].index] = currentNode.changes[j].change;
    }
}

/**
 * Password lock
 */
function limitAccess(room) {
    if (document.getElementById("input").value == key) {
		setNode(room);
    }
}

/**
 * NOT FUNCTIONAL
 * Add an item to player's inventory
 */
function addItem(node) {
    document.getElementById("inventory").innerHTML = document.getElementById("inventory").innerHTML + node.paths[0].inventory;
    setNode(node.paths[0].direction);
}

