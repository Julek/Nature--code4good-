// Tutorial code using the ARchitect Javascript library

var files = ["tree.png", "river.png", "flower.png", "rock.png", "scenary.png", "caves.png"];

var Images = [];

// Create new images, which will be loaded right away
for (i = 0; i<6; i++) {
    Images[i] = new AR.ImageResource(files[i], { onError: errorLoadingImage });
}


// current selected object
var selectedObject = null;


//variable that keeps the jsonData received from the native app
var jsonObject;


//function that gets called when the displayed poi bubble is clicked
//sends the id of the selected poi to the native app
function generateOnPoiBubbleClickFunc(id) {
    return function() {
        document.location = "architectsdk://opendetailpage?id="+id;
    };
}


// creates a property animation
function createOnClickAnimation(imageDrawable) {
    var anim = new AR.PropertyAnimation( imageDrawable, 'scaling', 1.0, 1.2, 750, new AR.EasingCurve(AR.CONST.EASING_CURVE_TYPE.EASE_OUT_ELASTIC, {amplitude : 2.0}) );
    return anim;
}


// creates a function for assigning to label's and imageDrawable's onClickTrigger
function createClickTrigger(id) {
    return function() {
        // hide the bubble
        document.getElementById("footer").style.display = 'block';
        document.getElementById("poiName").innerHTML=jsonObject[id].name;
        document.getElementById("poiDesc").innerHTML=jsonObject[id].description.substring(0,25);
        document.getElementById("footer").onclick= generateOnPoiBubbleClickFunc(id);

        // reset the previous selected poi
        if(selectedObject !== null)
        {
            // reset the property animation
            selectedObject.animation.stop();

            selectedObject.arLabel.style.backgroundColor = '#FFFFFF80';
            selectedObject.img.scaling = 1.0;
            selectedObject.poiObj.renderingOrder = 0;
        }

        // set a new select status for the current selected poi
        selectedObject = jsonObject[id];
        selectedObject.arLabel.style.backgroundColor = '#FFFFFFFF';
        selectedObject.poiObj.renderingOrder = 1;

        // start the assigned animation
        selectedObject.animation.start();

        return true;
    };
}

//function called from the native app fia callJavascript method
//receives json-data as string and processes the contained information
function newData(jsonData) {
    jsonObject = jsonData;
    //document.getElementById("statusElement").innerHTML='Loading JSON objects';

    for (var i = 0; i < jsonObject.length; i++) {
        var poidrawables = [];
        var label = new AR.Label(jsonObject[i].name,1.0, {offsetY : -1.5,
                                 triggers: {
                                 onClick:
                                 createClickTrigger(jsonObject[i].id)},
                                 style : {textColor : '#FFC100',backgroundColor : '#FFFFFF80'}});

        jsonObject[i].arLabel = label;

        poiImage = Images[jsonObject[i].type];

        var img = new AR.ImageDrawable(poiImage, 2.0,
                                       {triggers: {
                                       onClick:
                                       createClickTrigger(jsonObject[i].id)}}
                                       );

        jsonObject[i].animation = createOnClickAnimation(img);
        jsonObject[i].img = img;

        poidrawables.push(label);
        poidrawables.push(img);
        geoLoc = new AR.GeoLocation(jsonObject[i].Point.latitude,jsonObject[i].Point.longitude,jsonObject[i].Point.altitude);
        jsonObject[i].poiObj = new AR.GeoObject(geoLoc, {drawables: {cam: poidrawables}});
    }

    //document.getElementById("statusElement").innerHTML='JSON objects loaded';
}

// Called if loading of the image fails.
function errorLoadingImage() {
    // set error message on HUD
    //document.getElementById("statusElement").innerHTML = "Unable to load image!";
}


// hide bubble and reset the selected poi if nothing was hit by a display click
AR.context.onScreenClick = function() {
    // hide the bubble
    document.getElementById("footer").style.display = 'none';

    // and reset the current selected poi
    if(selectedObject !== null) {
        // reset the property animation
        selectedObject.animation.stop();

        selectedObject.arLabel.style.backgroundColor = '#FFFFFF80';
        selectedObject.img.scaling = 1.0;
        selectedObject.poiObj.renderingOrder = 0;
        selectedObject = null;
    }
};

function getInfoBox() {
        var div = document.createElement('div');
        div.id = "infobox";
        div.innerHTML = "<table style='font-size:20px;font-weight:bold'><tr style='height:30px'><td>Type</td><td><select>  <option value='flower'>Flower</option><option value='tree'>Tree</option>" +
                        "<option value='river'>River</option>" +
                        "<option value='fish'>Fish</option>" +
                        "</select></td></tr>"+
                        "<tr style='height:30px'><td>Name</td><td><input type='text'></td></tr>"+
                        "<tr style='height:30px'><td>Description</td><td><input type='text'></td></tr></table>";
        // set style
        div.style.backgroundColor = "#FFFFFF";
        div.style.opacity = 0.6;
        // better to use CSS though - just set class
        //div.setAttribute('class', 'myclass'); // and make sure myclass has some styles in css
        var body = document.getElementById("body");
        if(body.firstChild) body.insertBefore(div,body.firstChild);
        else body.appendChild(div);
}

function submitTag() {
    var parent = document.getElementById("infobox").parentNode;
    parent.removeChild(document.getElementById("infobox"));
}
