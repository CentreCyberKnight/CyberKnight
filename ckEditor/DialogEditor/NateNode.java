package ckEditor.DialogEditor;

import javax.swing.JLabel;

import ckEditor.treegui.CKSingleParent;
import ckGameEngine.CKSpellCast;
import ckGameEngine.actions.CKGameAction;
import ckGameEngine.actions.CKGameActionListenerInterface;
import ckGameEngine.actions.CKNullAction;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

public class NateNode extends JLabel {

		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		//private int id; // good coding practice would have this as private
		private String reply;
		private String speaker;
		private boolean startNode;
		private boolean endNode;
		private boolean randomNode;
		private int id;
		private CKDialogGraph g;
		private CKSingleParent singleParent;
		
		public boolean isStartNode() {
			return startNode;
		}
		public void setStartNode(boolean startNode) {
			this.startNode = startNode;
		}
		public boolean isRandomNode() {
			return randomNode;
		}
		public void setRandomNode(boolean randomNode) {
			this.randomNode = randomNode;
		}
		public boolean isEndNode() {
			return endNode;
		}
		public void setEndNode(boolean endNode) {
			this.endNode = endNode;
		}
		public NateNode(String r) {
			super(r);
			this.startNode=false;
			this.endNode=false;
			this.randomNode=false;
			this.reply=r;
			this.speaker="The Mysterious Man";
			this.singleParent = new CKSingleParent();
			this.singleParent.add(new CKNullAction());
		}
		

		public NateNode(String r, CKDialogGraph g) {
			this(r);
			this.g=g;
			int max= 0;

			for(NateNode nn: this.getG().getVertices()){
				if(nn.getId()>=max) {max=nn.getId()+1;}
			}
			this.setId(max);

		}
		
		public DirectedSparseMultigraph<NateNode,NateLink> getG() {
			return g;
		}
		public void setG(CKDialogGraph g2) 
		{
			this.g = g2;
			singleParent.removeFromParent();
			if( ((CKDialogGraph) g2).getHiddenNode()!=null)
			{
				 ((CKDialogGraph) g2).getHiddenNode().addIT(singleParent);
			}
		}
		public String toString() { // Always a good idea for debuging
			String reply = "";
			if(this.isRandomNode())
			{
				reply = "<i>";
			}
			reply = reply + getSpeaker() + ":<p> " + getReply();
			if(reply.length()>40)
			{
				reply = getSpeaker() + ":<p> " + getReply().substring(0, 35-(getSpeaker().length())) + "...";
			}
			if(this.isRandomNode())
			{
				reply = reply + "<i>";
			}
				
			
			return reply;
		}
		public String getReply() {
			return reply;
		}
		public void setReply(String reply) {
			this.reply = reply;
		}
		public String getSpeaker() {
			return speaker;
		}
		public void setSpeaker(String speaker) {
			this.speaker = speaker;
		}
		public void setId(int nodeCount) {

			this.id=nodeCount;
		}
		public int getId() {
			return id;
		}
		public void doAction(CKGameActionListenerInterface listener, CKSpellCast cast) {
			((CKGameAction) singleParent.getChildAt(0)).doAction(listener, cast);
		}
		public CKSingleParent getSingleParent() {
			return singleParent;
		}
		public void setSingleParent(CKSingleParent singleParent) {
			this.singleParent = singleParent;
		}
		
		
		
		
	
}
