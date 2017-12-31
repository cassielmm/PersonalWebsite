(function () {
    'use strict';
    function getAjaxData(event) {
        // create an XMLHttpRequest object - the variable request now points to that object
        var request = new XMLHttpRequest();
        // the requested filename is given by the input element id
        // the json files are kept in a separate data folder
        var filename = '/data/' + event.target.id + '.json';  
        // We invoke the methods (open and send) on the XMLHttpRequest object
        // denoted by the variable request.
        // Specify a GET request for the JSON file 
        request.open('GET', filename);
        request.send();
        // define the function to be called when the response is received.
        // we specify the onreadystatechange property of the XMLHttpRequest object 
        // denoted by the variable request.
        request.onreadystatechange = function () {
            // check that the response is complete and the request was successful
            if (request.readyState === 4 && request.status === 200) {
                // Display the response - responseText is the JSON encoded string
                displayInfo(request.responseText);
            }
        };
    }
    function displayInfo(jsonString) {
        // convert the JSON encoded string to object
        var jsonObj = JSON.parse(jsonString);
        // build a string from the properties and their values.
        var info = '';
        for (var prop in jsonObj) {
            info += '<p>' + prop + ': ' + jsonObj[prop] + '<p>';
        }
        // display the additional info in the description html element
 
        document.getElementById('response').innerHTML = info;
    }
    // Register event handlers to get the additional info if requested
    document.getElementById('objects').addEventListener('click', getAjaxData, false);
}());