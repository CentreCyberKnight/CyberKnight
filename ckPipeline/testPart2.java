package ckPipeline;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import ckCommonUtils.CKProperties;
import ckGraphicsEngine.assets.CKSpritesheetAsset;

public class testPart2 {

	public testPart2()
	{
	}
	
	public static void test()
	{
		String bass=CKProperties.getValue("Pipeline_Path");
		//write the text file for Chad's codes
		
		//
		System.out.println("Start the spritesheet");
		//
		
		Write w=new Write(bass);
		ArrayList<TxtFile> files=w.getFiles();
		for(TxtFile fi:files){
			//System.out.println(f.getFolder());
			
			//Create an empty file called "done"
			File fil=new File(fi.getFolder().toFile(),"done.txt");
			
			//File filen=new File(fi.getFolder().toFile(),"mobster.txt");
			if(!fil.exists()){
				try{
					
					//
					System.out.println("parameter of the CKSpritesheetAsset in Pipeline class" + fi.getName());
					//
					
					CKSpritesheetAsset t=new CKSpritesheetAsset(fi.getName());
					t.pipeline();
				}
				catch(FileNotFoundException e){
					System.out.println("FILENOTFOUND");
				}
				try{
					fil.createNewFile();
				}
				catch(IOException e){
				}
			}
		}	
	}

	public static void main(String[] args)
	{
		test();
	}
}
