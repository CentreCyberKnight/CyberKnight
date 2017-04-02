modules.tutorial = '2017-March-21';

var Arrow;
var Instruct_Morph;
var tutorial_Morph;

//https://codepen.io/KryptoniteDove/post/load-json-file-locally-using-pure-javascript
function loadJSON(filename,callback) {
    var xobj = new XMLHttpRequest();
    xobj.overrideMimeType("application/json");
    xobj.open('GET', filename, false);
    xobj.onreadystatechange = function () {
        if (xobj.readyState == 4 && xobj.status == "0") {
            callback(xobj.responseText);
        }
    };
    xobj.send(null);  
}

//Arrow Morph
Arrow.prototype=new Morph();
Arrow.prototype.constructor = Arrow;
Arrow.uber = Morph.prototype;

function Arrow(height,width,color){
    this.init(height,width,color);
};

Arrow.prototype.init=function(height,width,color){
	Arrow.uber.init.call(this);
	this.height=height;
	this.width=width;
    this.bounds=new Rectangle(0,0,width,height);
    this.color=color;
};

Arrow.prototype.drawNew=function(){
    var context,start,top,bottom;
    
    this.image = newCanvas(this.extent());
    context = this.image.getContext('2d');
    
    start=this.bounds.leftCenter();
    top=this.bounds.topRight();
    bottom=this.bounds.bottomRight();

    context.fillStyle=this.color.toString();

    context.beginPath();
    context.moveTo(start.x,start.y);
    context.lineTo(top.x,top.y);
    context.lineTo(bottom.x,bottom.y);
    context.closePath();
    
    context.strokeStyle = 'black';
    context.lineWidth = 1;
    context.stroke();
    context.fill();
};

//-----------------------------------------------------

//Instruction Morph
Instruct_Morph.prototype = new BoxMorph();
Instruct_Morph.prototype.constructor = Instruct_Morph;
Instruct_Morph.uber = BoxMorph.prototype;

function Instruct_Morph(test){
    this.init(test);
}

Instruct_Morph.prototype.init = function(test){
    this.setWidth(300);
    this.setHeight(200);
    this.text = new TextMorph("hi");
    this.add(this.text);
    this.nextButton = new BoxMorph();
    this.add(this.nextButton);
    this.nextButton.setPosition(new Point(this.width()-this.nextButton.width(), this.height()-this.nextButton.height() ));
    //this.nextButton.setPosition(new Point(this.width()/2, this.height()/2));
    //Instruction_Morph.uber.init.call(this);
    //this.drawNew();
}



//-----------------------------------------------------

//Tutorial Morph


tutorial_Morph.prototype = new ShadowMorph();
tutorial_Morph.prototype.constructor = tutorial_Morph;
tutorial_Morph.uber = ShadowMorph.prototype;

function tutorial_Morph(ide, fileName)
{
    loadJSON(fileName, function(response){
        var actual_JSON = JSON.parse(response);
        //console.log(actual_JSON);
        console.log(this);
        tutorial_Morph.prototype.init(ide, actual_JSON);                                      
    });
}
/*
function tutorial_Morph(ide,fileName){
    this.init(ide,fileName);
}
*/
tutorial_Morph.prototype.readFile = function(fileName)
{
    loadJSON(fileName,function(response) {
        // Parse JSON string into object
        //console.log(response);
        var actual_JSON = JSON.parse(response);
        //console.log('FILE CONTENTS:');
        //console.log(actual_JSON);
        return actual_JSON;
    });
}

tutorial_Morph.prototype.init = function(ide, FSM)
{
    //FSM Stuff
    this.states = FSM.states;
    this.transitions = FSM.transitions;
    this.goal = FSM.goal;
    this.currentState = this.states[FSM.start];
    this.currentStateIndex = FSM.start;
    this.currentTransition = this.transitions[this.currentStateIndex];
    
    this.ide = ide;
    
    this.alpha = 0;
    this.instructions = new Instruct_Morph('test');
    this.add(this.instructions);
    
    this.fps = 1;
}

tutorial_Morph.prototype.openIn = function(world)
{
    world.add(this);
    this.reactToWorldResize(world.bounds);
}

tutorial_Morph.prototype.reactToWorldResize = function (rect)
{
    this.setPosition(rect.origin);
    this.setExtent(rect.extent());
    
    this.instructions.setPosition(new Point(this.width()/2,this.height()/2));
}
tutorial_Morph.prototype.returnHatBlock = function()
{
    return ide.sprites.contents[0].scripts.children[0];
};
tutorial_Morph.prototype.returnMoveBlock = function(direction)
{
    //will fullCopy the necessary block to move and return it.
    //Will be used with the moveMorph function
    var blocks = ide.palette.children[0].children;
    for (var i = 0; i < children.length; i++)
        {
            if (blocks[i].blockSpec.localeCompare(direction) == 0)
                {
                    var returner = blocks[i].fullCopy();
                    ide.add(returner);
                    return returner;
                }
        }
    return null;
}
//This function checks the state of a specific hat block corresponding to a specific sprite
// TODO:
//     - Add functionality for selecting hat/sprite - index or name
tutorial_Morph.prototype.checkBlockState = function(ide,transition)
{
    var sprite = ide.sprites.contents[0]//<- this is where the sprite is selected
    var found = false;
    if(sprite)
    {
        var hatBlock = sprite.scripts.children[0];//<- this is where the hat is selected
        //console.log(hatBlock);
        hatBlock.children.forEach(
            function(child){
            if (child instanceof CommandBlockMorph){
                console.log(typeof child.blockSpec);
                console.log(transition.path)
                if (child.blockSpec == transition.path)
                {
                    found = true;
                }
            }
        });
    }
    return found;
}

tutorial_Morph.prototype.moveMorph = function(clickmorph, movemorph)
{
    //clickmorph: the morph on screen to click. Once clicked, the movemorph will move positions
    //movemorph: the morph on screen to move. It will have an array of points as an attribute to move around the screen.
      clickmorph.mouseClickLeft =  function()
                    {
                        var pos = 0;
                        var point = movemorph.potentialPoints[movemorph.index];
                        var deltax = point.x - movemorph.position().x;
                        var deltay = point.y - movemorph.position().y;
                        console.log(point);
                        console.log("deltax: " + deltax + " deltay: " + deltay);
                        id = setInterval(frame, 5);   
                        function frame() {
                                if (pos == 25) {
                                    clearInterval(id);
                                    movemorph.setPosition(point);
                                    if ((movemorph.index+1) >= (movemorph.potentialPoints.length))
                                        {
                                            
                                            movemorph.index = 0;
                                        }
                                    else{
                                    
                                    movemorph.index = movemorph.index + 1;
                                    }
                                } else {
                                    pos = pos + 1;
                                    movemorph.moveBy(new Point(deltax/25,deltay/25));
                                }
                    }      
      }
      
}
tutorial_Morph.prototype.setPoints = function(movemorph, points)
{
    //movemorph: morph to move across the screen.
    //points: an array of points that the movemorph will move through
    movemorph.potentialPoints = points;
    movemorph.index = 0; //index for the next point to move to in array of potentialPoints
}
tutorial_Morph.prototype.step = function()
{
    //console.log(this.currentStateIndex);
    //console.log(this.currentTransition);
    if (this.checkBlockState(this.ide, this.currentTransition))
        {
            this.currentStateIndex = this.currentTransition.nextState;
            this.currentState = this.states[this.currentStateIndex];
            this.currentTransition = this.transitions[this.currentStateIndex];
            //we will need to update the graphics here as well
            //and reset the moveMorph attribute for the instruction morph
            for(var i=0; i<this.goal.length; i++)
                {
                    if(this.currentStateIndex == this.goal[i])
                        {
                            this.destroy();
                            return true;
                        }
                }
        }
    return false;
}
/*
tutorial_Morph.prototype.finalState = function(index)
{
    if (this.currentStateIndex == this.finalStateIndex)
        {
            return true;
        }
    return false;
}*/

tutorial_Morph.portotype.pointTo= function(aMorph){
	//amorph: the morph to be pointed at 
	var pointat,arrow,height,width,color,x;
	
	pointat=aMorph.rightCenter();
	height=aMorph.height/2;
	width=height;
	color=new Color(0,0,255);
	x=height/2
	arrow=new PenMorph();
	arrow.setPosition(new Point(pointat.x,pointat.y-x));
	tutorial_Morph.add(arrow);
	return arrow;
}

tutorial_Morph.prototype.display=function(){
	var garphics,len,l,order,arg;
	
	graphics=this.state[this.currentstate];
	len=graphics.length;
	l=0;
	//let graphics be an array, each command is an element
	while (l < len){
		order=graphics[l];
		//need the user to give the block to move in Json file
		if (order.command == "pointTo"){
			arg=order.arg;
			this.pointTo(arg);
			
		}
		//need the user to give the movemorph and set potentialpoints in JSon file
		if (order.command == "move"){
			arg=order.arg;
			var movemorph=arg.movemorph;
			var pointlist=arg.potentialpoints;
			this.setPoints(movemorph,pointlist);
			var clickmorph=new StringMorph(arg.text);
			this.moveMorph(clickmorph,movemorph);
			
		}
		l++;
		this.step();
	}
	//how to destroy the display morph
	
}
