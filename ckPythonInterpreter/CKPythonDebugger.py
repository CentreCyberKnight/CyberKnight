#CKPythonDebugger
#

import bdb
#import StringIO
import tempfile
import os
from ckPythonInterpreter import CKPythonDebuggerInterface
#from ckPythonInterpreter import CKPythonState
from ckPythonInterpreter.CKUniqueEditor import *
from ckPythonInterpreter.CKUniqueAlgViz import *

#class CKPythonDebugger(CKPythonDebuggerInterface):
class CKPythonDebugger(CKPythonDebuggerInterface,bdb.Bdb):

    def __init__(self):
        bdb.Bdb.__init__(self)
        self.codeFile=None
        self.editor = getUniqueEditor()
        self.PYState = getUniquePythonState()
                
    def storeProgram(self,code):
        #get file name
        junk,self.codeFile=tempfile.mkstemp()
        self.PYState.addFileName(self.codeFile)
        #fill file up
        print "return value is",self.codeFile
        f = open(self.codeFile,"w")
        f.write(code)
        f.close()
        
    def __execMe(self):
        execfile(self.codeFile)
        
    def runProgram(self):
        if(self.codeFile==None):
            return
        self.set_break(self.codeFile,1)
        self.runcall(self.__execMe)
        #now clean up
        self.editor.RemoveHL()
        try:
            os.remove(self.codeFile);
        except OSError:
            pass
         
         

    def user_line(self, frame):
        filename = frame.f_code.co_filename
        linenumber=frame.f_lineno
        if(filename==self.codeFile):
            self.editor.RemoveHL()
            self.editor.HiLight(linenumber,linenumber+1)
            self.PYState.registerState()
            raw_input("<press enter to continue>")
        self.set_step()
        
    