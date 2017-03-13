modules.tutorial = '2017-Feb-22';


var tutorial_Morph;
var arrowMorph;

tutorial_Morph.prototype = new ShadowMorph();
tutorial_Morph.prototype.constructor = tutorial_Morph;
tutorial_Morph.uber = ShadowMorph.prototype;

tutorial_Morph.prototype.init = function()
{
    this.setColor(new Color(238, 244, 66));
    this.alpha = 0;
    this.noticesTransparentClick = true;
    //this.isVisible = false;
    this.boxy = new BoxMorph();
    this.boxy.setLeft(10);
    this.boxy.setRight(100);
    this.boxy.setColor(new Color(100,100,100));
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

function tutorial_Morph()
{
    this.init();
}

