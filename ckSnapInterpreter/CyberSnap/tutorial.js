/*

Independent Research - CyberKnight - Spring 2017

Maggie Feng, Nick Miller, and Ben Wells

*/


modules.tutorial = '2017-March-21';

var Arrow;
var Instruct_Morph;
var tutorial_Morph;

/*
    Arrow Morph - Currently not being used by the tutorial
*/
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

/*
    Instruction Morph - Is used for a text box with a "next" button. Instantiated by the tutorial.
*/
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
/*
    moveAnimation - moves a morph from current location to destinaion
    
    Parameters:
        movemorph - the morph you want to move, movemorph needs speed defined (see setUpMove)
        dest - a Point object where you want to move to
        tutorial - the tutorial object
*/
function moveAnimation(movemorph, dest, tutorial){
    var currX = movemorph.position().x;
    var currY = movemorph.position().y;
    if(currX < dest.x){
        var dx = movemorph.speed.x;
    }

    if(currY < dest.y){
        var dy = movemorph.speed.y;
    }
    if(Math.abs(currX-dest.x)<0.0001 && Math.abs(currY - dest.y) < 0.0001){
        if(!movemorph.done){
            console.log(movemorph.morphAtPointer());
            if(movemorph.drop()){
                tutorial.nextAnimation();
            }
            movemorph.done = true;
        }
    }
    movemorph.moveBy(new Point(dx, dy));
}

/*
    //BOUNCE NEEDS WORK - Math is a little off
    bounceAnimation - bounces a morph up and down in current location
    
    Parameters:
        movemorph - the morph you want to bounce (Call setUpBounce to make sure the attributes are set)
        tutorial - the tutorial object
*/
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

/*
    paletteChange - changes the current selected palette in the SNAP IDE to target
    Parameters:
        target - string name of the palette
        tutorial - the tutorial object
*/
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

/*
    waitAnimations - autimatically added to the end of a series of animations to make sure they complete properly. Just waits a set amount of frames.
    Parameters:
        waitMorph - The animationMorph itself
        tutorial - the tutorial object
*/
function waitAnimation(waitMorph, tutorial){
    if(waitMorph.count <= waitMorph.maxCount){
        console.log("Waiting!")
        waitMorph.count += 1;
    }
    else{
        tutorial.nextAnimation();
        waitMorph.destroy();
    }
}

/*
    Animation_Morpn - used to hold all the data for the animation and executes the animation when animate()
    is called
    Each Animation has a setUp function to prepare the movemorph for the animation. After setUp is finished
    the movemorph's step function is set to the animation step function.
    Parameters:
        tutorial - the tutorial object
        type - string of the type of animation
        duration - duration of the animation
        target - the morph to be animated
        destination - the destination of a animation (only currently used by move)
*/
Animation_Morph.prototype = new Morph();
Animation_Morph.prototype.constructor = Animation_Morph;
Animation_Morph.uber = Morph.prototype;

function Animation_Morph(tutorial, type, duration, target, destination ){
    this.init(tutorial, type, duration, target, destination);
}

Animation_Morph.prototype.init = function(tutorial, type, duration, target, destination){
    this.tutorial = tutorial;
    this.type = type;
    this.duration = duration;
    this.targetString = target;
    this.dest = destination;
    if(this.type == "move"){
        this.setUp = this.setUpMove;
    }
    else if(this.type == "bounce"){
        this.setUp = this.setUpBounce;
    }
    else if (this.type == "palette"){
        this.setUp = this.setupPaletteChange;
    }
    else if(this.type == "wait"){
        console.log("In here");
        console.log(this.setUpWait);
        this.setUp = this.setUpWait;
    }
}

/*
    Each of the setUp functions is setting the specific attributes that the animation step function will
    need.
    
    setUpMove - uses a handMorph to move the block to the scripting area. The handMorph is altered, so the
    user cannot interact with it.
*/
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
    
    //redefing a hand morph's drop function to automatically add it to the hat.
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
            console.log("Dropped")
            return true;
        }
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
}

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

Animation_Morph.prototype.setUpWait = function(){
    console.log("Set Up Wait")
    this.target = this;
    this.tutorial.parent.add(this);
    this.target.fps = 30;
    this.target.count = 0;
    this.target.maxCount = this.duration;
    var me = this;
    this.action = function(){
        console.log("in wait action");
        waitAnimation(me.target, me.tutorial);
    }
    console.log("Wait Set Up")
}

/*
    Calls the setUp function. Sets the target's step to the new animation step function.
*/
Animation_Morph.prototype.animate = function(){
    console.log("Animate Called");
    this.setUp();
  
    this.target.step = this.action;
}

//-----------------------------------------------------

/*

Tutorial Morph

This is the primary morph that runs the tutorial.

For CyberKnight, the tutorial is created in CKTutorialAction.java.

Parameters:
    ide - the SNAP! IDE_Morph, created in snap.html
    tutorial - a js object, currently the Java is taking a JSON string defined for the level and passing it as an object to the tutorial_morph 
    world - the morphic WorldMorph being used by SNAP!
*/
tutorial_Morph.prototype = new ShadowMorph();
tutorial_Morph.prototype.constructor = tutorial_Morph;
tutorial_Morph.uber = ShadowMorph.prototype;

function tutorial_Morph(ide, JSONstring, world){
    this.init(ide, JSONstring, world);                                         
}

tutorial_Morph.prototype.init = function(ide, tutorial, world){
    var FSM = tutorial;
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
    world.add(this);
    this.reactToWorldResize(world.bounds);
    
    this.transition();
}

tutorial_Morph.prototype.openIn = function(world)
{
    world.add(this);
    this.reactToWorldResize(world.bounds);
}

/*
    makes tutorial take up whole window.
*/
tutorial_Morph.prototype.reactToWorldResize = function (rect)
{
    this.setPosition(rect.origin);
    this.setExtent(rect.extent());
    
    this.instructions.setPosition(new Point(this.width()/2,this.height()/2));
}

/*
    returnHatBlock() returns the current hat block. If the hat has children it will return the 
    lowest child. Used for animations.
*/
tutorial_Morph.prototype.returnHatBlock = function()
{
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

/*
    returnMoveBlock() - returns specified command block to use in animation
*/
tutorial_Morph.prototype.returnMoveBlock = function(direction)
{
    var blocks = ide.palette.children[0].children;
    for (var i = 0; i < blocks.length; i++)
        {
            if (blocks[i].blockSpec == direction)
                {
                    return blocks[i];
                }
        }
    console.log("Failed to find move block");
}

/*
    checkBlockState - check the blocks attached to the current hat block and compares them to 
    the curresntState.path (a string of blockspecs). If the blocks attached to the hat match the
    list, the function returns true. Otherwise, it returns false.
*/
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

/*
    morphic step function only used by block state. Calls checkBlockState().
*/
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

/*
    Transitions to the next state in the list of states. If next state is a goal state, the tutorial
    ends.
*/
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

/*
    Ends the tutorial. 
    tutorialDone.notifyDone() - Notifies the Java that the tutorial has ended
*/
tutorial_Morph.prototype.endTutorial = function(){
    this.instructions.destroy();
    this.destroy();
    tutorialDone.notifyDone();
}

//CURRENTLY NOT BEING USED
tutorial_Morph.prototype.pointTo = function(aMorph){
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

/*
    Handles setting up a state and calling appropriate functions
*/
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
    }
}

/*
    Used in Animation States. Creates the animation morph objects that handle the animations.
    Stores Animations in array. Starts first Animation.
*/
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
        else if (type == "palette"){
            //palette will only have 1 argument
            console.log("here");
            target = args[0];
            duration  = 0;
        }
        this.currentAnimations.push(new Animation_Morph(this, type, duration, target, destination));
        l++;
	}
    
    //ADD WAIT ANIMATION TO END OF ANIMATION ARRAY
    var wait = new Animation_Morph(this, "wait", 50, null, null);
    
    this.currentAnimations.push(wait);
    
    this.currentAnimations[this.animationIndex].animate();
}

/*
    Handles the clean up of movemorphs after an animation
*/
tutorial_Morph.prototype.cleanUpAnimations = function(){
    this.currentAnimations.forEach(
        function(anim){
            if(anim.cleanUp){
                anim.cleanUp();
            }
        }
    );
    this.currentAnimations = [];
}

/*
    When an animation has completetd, it calls nextAnimation to move to the next animation.
*/
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

/*
    Updates the instruction morph text when state changes.
*/
tutorial_Morph.prototype.updateInstructions = function(){
    var text;
	text=this.currentState.text;
	this.instructions.text.text = text;
	this.instructions.text.changed();
    this.instructions.text.drawNew();
}