//TODO
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
	var next;
	
	next = new TextMorph("NEXT"),
    this.setWidth(300);
    this.setHeight(200);
    this.text = new TextMorph(text,14,'sans-serif',false,false,'left',250);
    this.text.setColor(new Color(255,255,255));
    this.add(this.text);
    this.text.setPosition(new Point(10,10));
    this.nextButton = new BoxMorph();
    this.add(this.nextButton);
    this.nextButton.add(next),
    next.setPosition(new Point(10,10)),
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
    //console.log(currX, movemorph.dest.x, currY)
    if(Math.abs(currX-dest.x)<0.0001 && Math.abs(currY - dest.y) < 0.0001){
        //console.log("I am donezo");
        if(!movemorph.done){
            console.log(movemorph.morphAtPointer());
            movemorph.drop();
            tutorial.nextAnimation();
            movemorph.done = true;
        }
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
function paletteChange(target, tutorial)
{
    //console.log("target: " +  target);
    var categories = tutorial.ide.categories.children;
    console.log("Palette change being called");
    console.log(target);
    console.log(tutorial);
    for (var i = 0; i < categories.length; i++)
        {
            if (categories[i].children[0].text == target)
                {
                    categories[i].mouseClickLeft();
                }
            //console.log(categories[i].children[0].text);
        }
    tutorial.nextAnimation();

}


Animation_Morph.prototype = new Morph();
Animation_Morph.prototype.constructor = Animation_Morph;
Animation_Morph.uber = Morph.prototype;

function Animation_Morph(tutorial, type, duration, target, destination ){
    this.init(tutorial, type, duration, target, destination);
}

Animation_Morph.prototype.init = function(tutorial, type, duration, target, destination){
    console.log("LETS DO SOME ANIMATIONS");
    this.tutorial = tutorial;
    this.type = type;
    this.duration = duration;
    this.targetString = target;
    this.dest = destination;
    if(this.type == "move"){
        this.setUp = this.setUpMove;
        //this.action = function(){console.log("move")};
    }
    else if(this.type == "bounce"){
        this.setUp = this.setUpBounce;
    }
    else if (this.type == "palette")
        {
            this.setUp = this.setupPaletteChange;
        }
}
//how to reference the palette buttons: ide.categories.children[1].children[0].text
Animation_Morph.prototype.setupPaletteChange = function()
{
    var me = this;
   this.action = function()
    {
      
        paletteChange(me.targetString,me.tutorial);
        this.step = null;
       this.destroy();
    }
    this.tutorial.parent.add(this);
    
    this.target = this;
}
Animation_Morph.prototype.setUpMove = function(){
    console.log("Set Up Move");
    console.log(this.targetString);
    this.target = this.tutorial.returnMoveBlock(this.targetString);
    
    this.target = this.target.fullCopy();
    
    this.tutorial.ide.add(this.target);
    this.hand = new HandMorph(this.tutorial.parent);
    
    this.hand.processMouseMove, this.hand.processTouchStart, this.hand.processTouchMove, this.hand.prcessTouchEnd, this.hand.processMouseUp, this.hand.processDoubleClick, this.hand.processDrop, this.hand.processMouseDown = null;
    var hat = this.tutorial.returnHatBlock();
    var tempDest = hat.position();
    this.dest = new Point(tempDest.x,tempDest.y+hat.height()/2);
    this.hand.dest = hat;
        
    this.hand.drop = function(){
        console.log("Dropping")
        console.log("this in drop:",this)
        var target, morphToDrop;
        console.log(this.children);
        if (this.children.length !== 0) {
            console.log(this);
            morphToDrop = this.children[0];
            console.log("MorphToDrop:",morphToDrop);
            target = this.dest;
            console.log(this.dest)
            console.log("Target:",target);
            this.changed();
            target.add(morphToDrop);
            morphToDrop.changed();
            morphToDrop.removeShadow();
            this.children = [];
            this.setExtent(new Point());
            if (morphToDrop.justDropped) {
                morphToDrop.justDropped(this);
            }
            if (target.reactToDropOf) {
                target.reactToDropOf(morphToDrop, this);
            }
            this.dragOrigin = null;
        }
        console.log("Dropped")
    }
    
    this.tutorial.parent.add(this.hand);
    this.hand.setPosition(this.target.position());
    this.hand.grab(this.target);
    
    this.dest.y = this.dest.y + this.target.height()/2;
    var speedX = (this.dest.x - this.target.position().x)/this.duration;
    var speedY = (this.dest.y - this.target.position().y)/this.duration;
    this.hand.speed = {x:speedX,y:speedY};
    
    this.cleanUp = function(){
        this.target.destroy();
        this.hand.destroy();
    }
    var me = this;
     this.action = function(){
            moveAnimation(me.hand,me.dest,me.tutorial)
    };
}

Animation_Morph.prototype.setUpBounce = function(){
    this.target = this.tutorial.returnMoveBlock(this.targetString);
    this.target.count = 0;
    this.target.fps = 30;
    this.target.maxCount = this.duration*this.target.fps/100;
    this.target.oldStep = this.target.step;
    var me = this;
    this.action = function(){
        
        bounceAnimation(me.target,me.tutorial);
        
    }
    //console.log(this.target.maxCount);
}

Animation_Morph.prototype.animate = function(){
    console.log("Animate Called");
        this.setUp();
  
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
    /*
    while(!this.ide.sprites.contents[0])
    {
        setInterval(null, 1000);
    }*/
    console.log("Hat Called");
    var sprite = this.ide.sprites.contents[0];
    if(sprite){
        console.log("In Hat")
        var hatBlock = sprite.scripts.children[0];//<- this is where the hat is selected
        var done = false;
        
        while(!done){
            found = false;
            console.log("In Loop")
            children = hatBlock.children;
            for(var j = 0; j < children.length; j++){
                var child = children[j]
                if (child instanceof CommandBlockMorph){
                    console.log("Block Found")
                    found = true;
                }
            }
            if(found){
                    hatBlock = child;
                    console.log("New Hat: ", hatBlock.blockSpec);
            }
            else{
                console.log("Done")
                done = true;
            }
        }
    }
    return hatBlock;
};

tutorial_Morph.prototype.returnMoveBlock = function(direction)
{
    //will fullCopy the necessary block to move and return it.
    //Will be used with the moveMorph function
    //console.log(ide.palette.children);
    var blocks = ide.palette.children[0].children;
    //console.log("direction entered: " + direction);
    for (var i = 0; i < blocks.length; i++)
        {
            //console.log(blocks[i].blockSpec);
            if (blocks[i].blockSpec == direction)
                {
                    return blocks[i];
                }
        }

    console.log("Failed to find move block");
}
//This function checks the state of a specific hat block corresponding to a specific sprite
// TODO:
//     - Add functionality for selecting hat/sprite - index or name
tutorial_Morph.prototype.checkBlockState = function()
{
    var found = false;
    var done = false;
    var i = 0;
    var sprite = this.ide.sprites.contents[0];//<- this is where the sprite is selected
    var path = this.currentState.path.split(",");
    if(sprite){
        var hatBlock = sprite.scripts.children[0];//<- this is where the hat is selected
        console.log(path);
        
        while(!done){
            found = false;
            console.log(i);
            children = hatBlock.children;
            for(var j = 0; j < children.length; j++){
                var child = children[j]
                if (child instanceof CommandBlockMorph){
                    if (child.blockSpec == path[i]){
                        console.log("Block Found")
                        found = true;
                    }
                }
            }
            if(found){
                    hatBlock = child;
                    console.log("New Hat: ", hatBlock.blockSpec);
                    if(i == path.length-1)
                        {
                            return true;
                        }
                    i++;
            }
            else{
                console.log("Done")
                done = true;
            }
        }
    }  
    return false; 
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

tutorial_Morph.prototype.pointTo = function(aMorph){
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
        console.log("Click State");
        var me = this
        this.instructions.nextButton.mouseClickLeft = function(){
            me.transition();
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
            target = args[1];
            var hat = this.returnHatBlock();
            tempDest = hat.position();
            destination = new Point(tempDest.x,tempDest.y+hat.height()/2);
        }
        else if(type == "bounce"){
            target = args[1];        
        }
        else if (type == "palette")
            {
                //palette will only have 1 argument
                console.log("here");
                target = args[0];
                duration  = 0;
            }
        //console.log(destination);
        this.currentAnimations.push(new Animation_Morph(this, type, duration, target, destination));
        l++;
	}
    console.log(this.currentAnimations);
    this.currentAnimations[this.animationIndex].animate();
}

tutorial_Morph.prototype.cleanUpAnimations = function(){
    console.log("CLEAN UP CALLED")
    this.currentAnimations.forEach(
        function(anim){
            if(anim.cleanUp){
                anim.cleanUp();
            }
        }
    );
    this.currentAnimations = [];
}

tutorial_Morph.prototype.nextAnimation = function(){
    if(this.animationIndex == this.currentAnimations.length - 1){
            this.animationIndex = 0;
            this.cleanUpAnimations();
            this.transition();
        }
    else{
        this.animationIndex++;
       console.log(this.currentAnimations[this.animationIndex]); this.currentAnimations[this.animationIndex].animate();
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