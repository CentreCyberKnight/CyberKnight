//TODO
//  - Handle if hat has children
//  - Handle if statements

modules.tutorial = '2017-March-21';

var Arrow;
var Instruct_Morph;
var tutorial_Morph;

//Arrow Morph
Arrow.prototype = new Morph();
Arrow.prototype.constructor = Arrow;
Arrow.uber = Morph.prototype;

function Arrow(height, width, color){
    this.init(height, width, color);
};

Arrow.prototype.init = function(height, width, color){
	Arrow.uber.init.call(this);
	this.height = height;
	this.width = width;
    	this.bounds = new Rectangle(0, 0, width, height);
    	this.setColor(color);
};

Arrow.prototype.drawNew = function(){
    var context, start, top, bottom;
    
    this.image = newCanvas(this.extent());
    context = this.image.getContext('2d');
    
    start = this.bounds.leftCenter();
    top = this.bounds.topRight();
    bottom = this.bounds.bottomRight();

    context.fillStyle = this.color.toString();

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

function Instruct_Morph(text){
    this.init(text);
}

Instruct_Morph.prototype.init = function(text){
    this.setWidth(300);
    this.setHeight(200);
    this.text = new TextMorph(text);
    this.add(this.text);
    this.nextButton = new BoxMorph();
    this.add(this.nextButton);
    this.nextButton.setPosition(new Point(this.width()-this.nextButton.width(), this.height()-this.nextButton.height() ));
}


//-----------------------------------------------------
    
function moveAnimation(movemorph, dest, tutorial){
    var currX = movemorph.position().x;
    var currY = movemorph.position().y;
    if(currX < dest.x){
        var dx = movemorph.speed.x;
    }

    if(currY < dest.y){
        var dy = movemorph.speed.y;
    }
    //console.log(currX, movemorph.dest.x, currY, mov)
    if(Math.abs(currX-dest.x)<0.0001 && Math.abs(currY - dest.y) < 0.0001){
        //console.log("I am donezo");
        tutorial.nextAnimation(function() {movemorph.destroy()});
        //movemorph.destroy();
    }
    movemorph.moveBy(new Point(dx, dy));
}

function bounceAnimation(movemorph, tutorial){
    if(movemorph.count <= movemorph.maxCount){
        // Based on d/dt ( Math.exp(-0.9*t) * Math.sin(0.5*movemorph.count) )
        // Needs some work  Desmos : -10e^{-.09x}\cdot \sin \left(.5x\right)
        var newY = -10*(-0.07 * Math.exp(-.07*movemorph.count) * Math.sin(.5*movemorph.count) + 0.5* Math.exp(-.07*movemorph.count) * Math.cos(.5*movemorph.count));
        movemorph.moveBy(new Point(0,newY));
        movemorph.count += 1;
    }
    else{
        tutorial.nextAnimation();
        movemorph.step = movemorph.oldStep;
        movemorph.fps = 0;
    }
}

Animation_Morph.prototype = new Morph();
Animation_Morph.prototype.constructor = Animation_Morph;
Animation_Morph.uber = Morph.prototype;

function Animation_Morph(tutorial, type, duration, target, destination ){
    this.init(tutorial, type, duration, target, destination);
}

Animation_Morph.prototype.init = function(tutorial, type, duration, target, destination){
    //console.log("LETS DO SOME ANIMATIONS");
    this.tutorial = tutorial;
    this.type = type;
    this.duration = duration;
    this.target = target;
    this.dest = destination;
    var me = this;
    if(this.type == "move"){
        this.setUpMove();
        this.action = function(){
            moveAnimation(me.target,me.dest,me.tutorial)
        };
        //this.action = function(){console.log("move")};
    }
    if(this.type == "bounce"){
        this.setUpBounce();
        this.action = function(){
            bounceAnimation(me.target,me.tutorial);
        }
    }
}

Animation_Morph.prototype.setUpMove = function(){
    this.dest.y = this.dest.y + this.target.height()/2;
    var speedX = (this.dest.x - this.target.position().x)/this.duration;
    var speedY = (this.dest.y - this.target.position().y)/this.duration;
    this.target.speed = {x:speedX,y:speedY};
}

Animation_Morph.prototype.setUpBounce = function(){
    this.target.count = 0;
    this.target.fps = 30;
    this.target.maxCount = this.duration*this.target.fps/100;
    this.target.oldStep = this.target.step;
    //console.log(this.target.maxCount);
}

Animation_Morph.prototype.animate = function(){
    this.target.step = this.action;
}

//-----------------------------------------------------

//Tutorial Morph

tutorial_Morph.prototype = new ShadowMorph();
tutorial_Morph.prototype.constructor = tutorial_Morph;
tutorial_Morph.uber = ShadowMorph.prototype;

function tutorial_Morph(ide, JSONstring, world){
    this.init(ide, JSONstring, world);                                         
}

tutorial_Morph.prototype.init = function(ide, JSONstring, world){
    var FSM = JSONstring;
    //console.log(FSM);
    this.FSM = FSM;
    this.states = FSM.states;
    this.transitions = FSM.transitions;
    this.goal = FSM.goal;
    this.currentAnimations = [];
    this.animationIndex = 0;
    
    this.ide = ide;
    
    this.alpha = 0;
    this.instructions = new Instruct_Morph('DEFAULT TEXT');
    this.ide.parent.add(this.instructions);
    
    this.fps = .5;
    //console.log(world);
    world.add(this);
    this.reactToWorldResize(world.bounds);
    //console.log("Set up tutorial")
    
    this.transition();
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
    while(!this.ide.sprites.contents[0])
    {
        setInterval(null, 1000);
    }
    
    if (this.ide.sprites.contents[0])
    {
        return this.ide.sprites.contents[0].scripts.children[0];
    }
     null;
};

tutorial_Morph.prototype.returnMoveBlock = function(direction)
{
    //will fullCopy the necessary block to move and return it.
    //Will be used with the moveMorph function
    var blocks = ide.palette.children[0].children;
    //console.log(blocks);
    for (var i = 0; i < blocks.length; i++)
        {
            //console.log(blocks[i].blockSpec);
            if (blocks[i].blockSpec == direction)
                {
                    return blocks[i];
                }
        }
    return null;
}
//This function checks the state of a specific hat block corresponding to a specific sprite
// TODO:
//     - Add functionality for selecting hat/sprite - index or name
tutorial_Morph.prototype.checkBlockState = function()
{
    var found = false;
    var sprite = this.ide.sprites.contents[0];//<- this is where the sprite is selected
    var path = this.currentState.path
    if(sprite)
    {
        var hatBlock = sprite.scripts.children[0];//<- this is where the hat is selected
        hatBlock.children.forEach(
            function(child){
            if (child instanceof CommandBlockMorph){
                if (child.blockSpec == path)
                {
                    found = true;
                }
            }
        });
    }  
    
    return found; 
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
    if(this.currentState.type == "block")
    {
        //console.log("here");
        if (this.ide.sprites.contents[0])
        {     
            //console.log("here1")
            if (this.checkBlockState())
            {
                //console.log("here2")
                this.transition();
            }
        }
    }
};

tutorial_Morph.prototype.transition = function(){
    if(!this.currentState){
        this.currentStateIndex = this.FSM.start;
    }
    else{
        this.currentStateIndex = this.currentTransition.nextState;
    }
    
    this.currentState = this.states[this.currentStateIndex];
    this.currentTransition = this.transitions[this.currentStateIndex];
    
    if(this.currentState.type == "goal"){
        this.endTutorial();
    }
    this.enterState();
}

tutorial_Morph.prototype.endTutorial = function(){
    console.log("RIP");
    this.instructions.destroy();
    this.destroy();
    tutorialDone.notifyDone();
}

tutorial_Morph.prototype.pointTo= function(aMorph){
	//amorph: the morph to be pointed at 
	var pointat,arrow,height,width,color,x;
	var pointat,arrow,height,width,color;
	
	pointat=aMorph.rightCenter();
	height=aMorph.height();
	width=height/2;
	color=new Color(0,200,200);
	arrow=new Arrow(height,width,color);
	arrow.setPosition(new Point(pointat.x,pointat.y-width));
	return arrow;
}

tutorial_Morph.prototype.enterState = function(){
    this.updateInstructions();
    if(this.currentState.type == "animate"){
        this.setUpGraphics();
    }
    else if(this.currentState.type == "click"){
        //console.log("Click State");
        var me = this
        this.instructions.nextButton.mouseClickLeft = function(){
            me.transition();
            //console.log(this);
            this.mouseClickLeft = null;
        };
    }/*
    else if(this.currentState.type == "block"){
        //console.log("Block State");
    }*/
}

tutorial_Morph.prototype.setUpGraphics = function(){
    //console.log("Display")
	var currStateGraphics, len, l, order, args, arrow, movemorph;
    currStateGraphics = this.currentState.graphic;
    len = currStateGraphics.length;
	l = 0;
	var type, duration, tempDest, target, destination,currGraphic;
    this.animationIndex = 0;
	//let graphics be an array, each command is an element
	while (l < len){
		currGraphic = currStateGraphics[l];
        type = currGraphic.command;
        args = currGraphic.arguments;
        duration = args[0];
        
        if(type == "move"){
            target = this.returnMoveBlock(args[1]).fullCopy();
            target.alpha = .5;
            this.ide.add(target);
            var hat = this.returnHatBlock();
            tempDest = hat.position();
            destination = new Point(tempDest.x,tempDest.y+hat.height()/2);
        }
        else if(type == "bounce"){
            target = this.returnMoveBlock(args[1]);        
        }
        
        //console.log(destination);
        this.currentAnimations.push(new Animation_Morph(this, type, duration, target, destination));
        l++;
	}
    this.currentAnimations[this.animationIndex].animate();
	//state need to be update after the first graphic display is done for the json file level 1
}

tutorial_Morph.prototype.nextAnimation = function(callBack){
    if(callBack){
        callBack();
    }
    if(this.animationIndex == this.currentAnimations.length - 1){
            this.animationIndex = 0;
            this.currentAnimations = [];
            this.transition();
        }
    else{
        this.animationIndex++;
        this.currentAnimations[this.animationIndex].animate();
    }
}

tutorial_Morph.prototype.updateInstructions = function(){
    //console.log("Instruction updater")
    var text;
	text=this.currentState.text;
	
	this.instructions.text.text = text;

	this.instructions.text.changed();
    
	this.instructions.text.drawNew();
}