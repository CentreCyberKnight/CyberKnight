package ckPythonInterpreter;

import static ckCommonUtils.CKPropertyStrings.CH_EQUIP_SLOTS;

import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import ckCommonUtils.EquipmentComparator;
import ckGameEngine.CKBook;
import ckGameEngine.CKChapter;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKPage;
import ckGameEngine.CKStatsChangeListener;

public class CKEquippedView extends JPanel implements CKStatsChangeListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4653078960409713087L;
	CKGridActor character;
	Vector<String> slots; 
	
	public CKEquippedView(CKGridActor character)
	{
		this.character=character;
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		drawEquips(getOrderedList());
		character.addListener(this);
	}
	
	
	public Vector<String> getOrderedList()
	{
		CKChapter chap = this.character.getAbilities().getChapter(CH_EQUIP_SLOTS);
		Vector<String> list = new Vector<String>();
		Iterator<CKPage> iter = chap.getPages();
		while(iter.hasNext())
		{
			list.add( iter.next().getName());
		}
		Collections.sort(list,EquipmentComparator.getComparator());
		return list;
	}
	
	@SuppressWarnings("unused")
	private boolean shouldRedraw()
	{
		Vector<String> list = getOrderedList();
		if(list.size() != slots.size()) { return true; }		
		for(int i=0;i<list.size();i++)
		{
			if(list.get(i).compareTo(slots.get(i))!=0)
			{ return true; }
		}
		return false;
		
	}
	
	
	public void drawEquips(Vector<String> list)
	{
		slots= list;
		for(String s: list)
		{
			add(new CKEquipArtifactView(s,character));
		}
		add(Box.createVerticalGlue());
		revalidate();
	}


	@Override
	public void equippedChanged() {}	//do nothing
		

	@Override
	public void statsChanged(CKBook stats)
	{
		//if(shouldRedraw())
		//{
			removeAll();
			drawEquips(getOrderedList());
		//}
		
	}


	@Override
	public void cpChanged(int cp)
	{
		
		
	}
	
	
	
}
