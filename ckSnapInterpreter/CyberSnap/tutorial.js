modules.tutorial = '2017-Feb-22';


var tutorial_Morph;
var arrowMorph;

tutorial_Morph.prototype = new ShadowMorph();
tutorial_Morph.prototype.constructor = tutorial_Morph;
tutorial_Morph.uber = ShadowMorph.prototype;

tutorial_Morph.prototype.init = function(ide)
{
    this.ide = ide;
    this.setColor(new Color(238, 244, 66));
    this.alpha = 0;
    this.noticesTransparentClick = true;
    //this.isVisible = false;
    this.boxy = new BoxMorph();
    this.boxy.setLeft(10);
    this.boxy.setRight(100);
    this.boxy.setColor(new Color(100,100,100));
    this.fps = 5;
    console.log();
}
tutorial_Morph.prototype.openIn = function(world)
{
    world.add(this);
    this.reactToWorldResize(world.bounds);
    this.add(this.boxy);
    this.boxy.show();
}

tutorial_Morph.prototype.reactToWorldResize = function (rect)
{
    this.setPosition(rect.origin);
    this.setExtent(rect.extent());
}

tutorial_Morph.prototype.checkBlockState = function(ide,s,hat,test)
{
    var sprite = ide.sprites.contents[0]//.scripts.children[0];
    var found = false;
    if(sprite)
    {
	var hatBlock = sprite.scripts.children[0];
	console.log(hatBlock);
	hatBlock.children.forEach(
	    function(child){
		if (child instanceof CommandBlockMorph){
		    console.log(typeof child.blockSpec);
		    if (child.blockSpec == test)
		    {
			console.log("YAY!!!");
			found = true;
		    }
		}
	    }
	);
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
    if (this.checkBlockState(tutorial.ide, 0, 0, "forward"))
        {
            this.currentStateIndex = this.currentState.transition.nextState;
            return this.finalState(this.currentStateIndex);
        }
    return false;
}
tutorial_Morph.prototype.finalState = function(index)
{
    if (this.currentStateIndex == this.finalStateIndex)
        {
            return true;
        }
    return false;
}
function tutorial_Morph(ide)
{
    this.init(ide);
}

