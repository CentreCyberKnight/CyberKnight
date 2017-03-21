modules.tutorial = '2017-March-21';


var tutorial_Morph;
var arrowMorph;

tutorial_Morph.prototype = new ShadowMorph();
tutorial_Morph.prototype.constructor = tutorial_Morph;
tutorial_Morph.uber = ShadowMorph.prototype;

tutorial_Morph.prototype.init = function(ide, FSM)
{
    //FSM Stuff
    this.states = FSM.states;
    this.transitions = FSM.transitions;
    this.goals = FSM.goals;
    this.currentState = this.states[FSM.start];
    this.currentStateIndex = FSM.start;
    this.currentTransition = this.transitions[this.currentStateIndex];
    
    this.ide = ide;
    //this.setColor(new Color(238, 244, 66));
    this.alpha = 0;
    //this.noticesTransparentClick = true;
    //this.isVisible = false;
    
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
                console.log(transition.test)
                if (child.blockSpec == transition.test)
                {
                    console.log("YAY!!!");
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
    console.log(this.currentStateIndex);
    console.log(this.currentTransition);
    if (this.checkBlockState(this.ide, this.currentTransition))
        {
            this.currentStateIndex = this.currentTransition.nextState;
            this.currentState = this.states[this.currentStateIndex];
            this.currentTransition = this.transitions[this.currentStateIndex];
            for(var i=0; i<this.goals.length; i++)
                {
                    if(this.currentStateIndex == this.goals[i])
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

tutorial_Morph.prototpye.readFile = function(fileName)
{
    var reader = FileReader();
    var raw = reader.readAsText(fileName);
    
    return JSON.parse(raw);
}

function tutorial_Morph(ide, FSM)
{
    this.init(ide, FSM);
}

