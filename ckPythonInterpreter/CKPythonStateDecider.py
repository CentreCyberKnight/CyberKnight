#CKPythonStateDecider
#
#this class (and supporting classes) are used to detect what
#variables have been allocated, their memory properties,
#and their values.

#This class should be initialized at the beginning of the python
#script and updated after every line of code.

#This script will need to call the Algviz class in order to
#allocate, update, and describe variable status to the
#visualization


import types
import copy
import inspect
from ckPythonInterpreter import CKPythonState
from ckPythonInterpreter.CKUniqueAlgViz import *

def calcValueAddress(loc):
    return calcHex(loc-4)

def calcIdentifierAddress(loc):
    return calcHex(loc)

def calcHex(location):
    #print "calcHex",location
    if(isinstance(location,str)):
        return location
    
    else:
    #   print "returning",hex(location) 
        return hex(location)[2:]



def isVariable(obj):
    typeList=[int,basestring,float,AlgVizCompatable,list]
    for t in typeList:
        if( isinstance(obj,t)):
            return True
  
    return False




def isContainer(obj):
    typeList=[list]
    for t in typeList:
        if( isinstance(obj,t)):
            return True
  
    return False




#class is created to enable subclasses to be mapped to
#Algviz displays
class AlgVizCompatable(object):
    pass



#class to hold info on a variable and determine if a
#change has been made

class VarRegister(AlgVizCompatable):

    #varname
    #lastID
    #value - deep copy of the previous variable
    #visited


    def __init__(self,n,val,reg):
        #need to store...
        self.varname=n
        self.register=reg
        self.visited = False
        
        self._updateValue(val)

        #now ask reg to store the info...
        self.loc = self.register.createVariable(n,val)

    def getName(self):
        return self.varname


    def _updateValue(self,val):
        self.lastID=id(val)
        self.value=copy.deepcopy(val)
        if(isContainer(val)):
            self.length=len(val)
        else:
            self.length=1

    def resetVisit(self):
        self.visted=False
        
    def isVisited(self):
        return self.visisted



    def updateChange(self,val):
        self.visisted=True
        #print "visiting",self.value,val,self.length,len(val)

        if(id(val)!=self.lastID):#new data
            self.register.newValueMemory(self.loc,val,0)
        elif(val!=self.value):#replaced data
            #print "changing value"

            if(isContainer(val)):#like a list
                #need to be more interesting....
                self.deleteVariable(id(val))
                self.register.newValueMemory(self.loc,val,0,True)
            else:
                self.register.newValue(val)
        else:
            #no changes
            return

        #cleanup if change occured
        self._updateValue(val)
        


    def deleteVariable(self,location):
        self.register.deallocateVariable(location)
        








#keeps track of frames for use in deallocating memory
class FrameRegister(AlgVizCompatable):
    #memid - unique id of frame
    #vars - list of varregisters
    #memStart - memory of begining of frame
    #register - register state

    def __init__(self,memid,memStart,register):
        self.memid=memid
        self.vars=[]
        self.memStart=memStart
        self.register=register


    def resetVisits(self):
        for v in self.vars:
            v.resetVisit()

    def returnUnvisited(self):
        ret =[]
        for v in self.vars:
            if(not v.isVisited()):
                ret.append(v)
        return ret

    def visitVar(self,name,value):
        for v in self.vars:
            if (v.getName()==name):
                #check if the var has changed and apply update
                #set visit
                v.updateChange(value)
                return
                
        #Value not present, add to list
        v = VarRegister(name,value,self.register)
        self.vars.append(v)
        return

    def deleteFrame(self):
        for v in self.vars:
            v.deleteVariable()

        
            return self.memStart

#class to maintina state of the program
#new types will be detecte and sent to Algviz
#new variables will have memory allocated
#new values will be registered as needed


class CKPythonStateDecider(CKPythonState):

    #instances - dictionary of instances that have been sent to algviz
    #frames    - dictionary of frames in use
    #variables - dictionary of variables and last know value(s)
    #       -key is fcn-variable?
    #algviz -java algviz class
    #stackDepth-how many frames are there?  

    def __init__(self):
        
        self.algviz= getUniqueAlgViz(400)
        self.algviz.sendPythonVariable()
        
        self.frames=[]
        self.stackpointer=1600000

        self.files=[]
        self.values={}
        
    def addFileName(self,name):
        self.files.append(name)


    #declare variable info to algviz
    def createVariable(self,varname,value):
        location=self.stackpointer  #place to put new variable
        #call algviz to malloc stack memory
        #variable type, location at stack base        
        #print "mallocing variable memory at",location
        self.algviz.createVariableInstance(calcHex(location))
        #once variable is created
        #decrease stackptr by size of variable
        self.stackpointer-=8
        #  add and link value
        self.newValueMemory(location,value,0)
        #  add and link the name
        self.newValueMemory(location,varname,4)
        #return location of variable
        return location

   #mallocs the memory for new variable
   #loc is the location of the thing that points to the new mem
   #val is the value to fill the memory
    def newValueMemory(self,ptrLoc,value,offset,force=False):
       #malloc space for new variable...will be long code
       location=id(value)
       if(not self.values.has_key(location)or force):
       
           if(isinstance(value,str)):
               self.algviz.createStrInstance(value,calcHex(location))
           elif(isinstance(value,int)):
               self.algviz.createIntInstance(value,calcHex(location))
           elif(isinstance(value,float)):
               self.algviz.createFloatInstance(value,calcHex(location))
           elif(isinstance(value,list)):
               length=len(value)
               self.algviz.allocateList(calcHex(location),length)
               for i in range(length):
                   self.newValueMemory(location,value[i],i*4)
               
           else:
               print "Type not supported" 
               return
           #add to variables
           self.values[location]=value
       #cleanup
       #print "allocation space at",location," for value",value
       #now set pointer properly
       self.newPointer(ptrLoc,offset,location)
       



    def newValue(self,value):
       location=id(value)
       #long set of if statements to create the proper call, set proper values
       #MKB
       #print "putting value", value, "at location",location
       print "Unsuipported, is this needed?"


    def newPointer(self,fromLoc,offset,toLoc):
       #will need to convert to hex...
       #print "connecting pointer from ",fromLoc," to",toLoc
       self.algviz.setPointer(calcHex(fromLoc),calcHex(toLoc),offset);

    def deallocateVariable(self,loc):
       #print "deallocating memory",loc
       self.algviz.sendMemFreeMessage(calcHex(loc),8)

    def handleFrame(self,present_frame,file_name,fcn_name,fcn_line,depth):
        #print "-----------------"
        l = present_frame.f_locals
        if(not l):
            #MKB clean up here?
            return
        #if the depth is greater than the length of the frame, add it
        if(depth>self.getStackDepth()):
        #    print "appending, present depth is",depth
            self.frames.append(FrameRegister(id(present_frame),self.stackpointer,self))
            #now do work

        #if files has been specified, only report frames from those files 
        if(len(self.files)==0) or(file_name in self.files):
            f = self.frames[depth-1]#-1 to account for start at 0
            f.resetVisits()
    
            #print fcn_name,fcn_line
            restrictList=["daylight","timezone","accept2dyear","altzone"]
            for key,value in l.items():
                if not(key in restrictList) and (isVariable(value)):
                    #use length,variable tuple to id variables
             #       print "           ",key,value
                    f.visitVar(key,value)

        #clean up
        del present_frame  #MKB - do I need to del these?




    def getStackDepth(self):
        return len(self.frames)

    #find all of the variables to update
    def registerState(self):
        #print "=================="
        #will need to see if the state needs to be passed
        stack = inspect.stack()
 
        length = len(stack)
        i=length-1
        #need to remove existing stack values...
        if(length-1 < self.getStackDepth()): #-1 since we do not want to store this frame
            for i in range(length-1,self.getStackDepth()):
                self.frames[i].deleteFrame()
            for i in range(length-1,self.getStackDepth()):
                del self.frames[-1]
        

        #check existing variables
        while(i>0):
            fr = stack[i]
            #print fr[5]
            self.handleFrame(fr[0],fr[1],fr[3],fr[2],length-i)
            i-=1






    
        


        
        
