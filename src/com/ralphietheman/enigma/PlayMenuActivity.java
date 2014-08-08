package com.ralphietheman.enigma;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class PlayMenuActivity extends Activity {
	public static final String puzzlepack1 = "puzzles1.xml";
	public static final String newpuzzlepack = "pizza.xml";
	public static ArrayList<Puzzle1> puzzlesOld;
	public static ArrayList<Puzzle2> puzzles;
	public static final ArrayList<String> cluetypes = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ScrollView scroll = new ScrollView(this);
		
		LinearLayout linear = new LinearLayout(this);
		linear.setOrientation(1);
		linear.setBackgroundColor(Color.rgb(112, 112, 118));
		
		//fillClueTypes();
		
		puzzles = null;
		try 
		{
			puzzles = parse(getAssets().open(newpuzzlepack));
			//puzzlesOld = parseOld(getAssets().open(puzzlepack1));
			Log.e("debug","Debug: Size of puzzles is: " + puzzles.size());			
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		catch (XmlPullParserException e) 
		{
			e.printStackTrace();
		}
        
		int puzzlenum = 1;
        int textsize = 40;
        int gray = Color.rgb( 255, 255, 255);
        
        for (int f=0; f<=7; f++) {
            for (int c=0; c<=5; c++) {
            	if(puzzlenum<=puzzles.size())
            	{
                    Button b = new Button (this);
                    b.setText("Puzzle "+puzzlenum);
                    b.setTextSize(textsize);
                    b.setTextColor(gray);
                    Drawable d = getResources().getDrawable(R.drawable.menubutton);
                    b.setBackground(d);
                    setButtonMargins(b);
                    
                    //Not sure what changed about this line
                    b.setOnClickListener(new OnClickListener(){
                    	public void onClick(View v){
                    		// TODO Auto-generated method stub
                            //To do: Extract information about which button is being clicked
                            //To do: Create an intent and pass on the extra information along the
                            //appropriate puzzle to the PuzzleActivity
                    		
                    		//Not yet implemented
                    		//Intent intent = new Intent(PlayMenuActivity.this, PuzzleActivityOld.class);
                    		Intent intent = new Intent(PlayMenuActivity.this, PuzzleActivity.class);
                    		if(puzzles != null)
                    		{
                    			int puzzlenum = v.getId();
                    			Puzzle2 move = puzzles.get(puzzlenum-1);
                    			
                    			Bundle b = new Bundle();
                    			b.putStringArrayList("puzzle", move.myClues);
                    			intent.putExtras(b);
                    			
                    			intent.putExtra("puzzle", move.myClues);
                    			//intent.putExtra("puzzle", move);
                    		}
                    		startActivity(intent);
                    	}
                    });  
                    b.setId(puzzlenum);
                    linear.addView(b, puzzlenum - 1);
                   	}
            	else
            	{
            		break;
            	}
                puzzlenum++;
            }
        }
		
		/*
		// Configurations of menu
        int puzzlenum = 1;
        int textsize = 70;
        int gray = Color.rgb( 255, 255, 255);
        
        for (int f=0; f<=7; f++) {
            for (int c=0; c<=5; c++) {
            	if(puzzlenum<=puzzles.size())
            	{
                    Button b = new Button (this);
                    b.setText("Puzzle "+puzzlenum);
                    b.setTextSize(textsize);
                    b.setTextColor(gray);
                    Drawable d = getResources().getDrawable(R.drawable.menubutton);
                    b.setBackground(d);
                    setButtonMargins(b);
                    
                    //Not sure what changed about this line
                    b.setOnClickListener(new OnClickListener(){
                    	public void onClick(View v){
                    		// TODO Auto-generated method stub
                            //To do: Extract information about which button is being clicked
                            //To do: Create an intent and pass on the extra information along the
                            //appropriate puzzle to the PuzzleActivity
                    		
                    		//Not yet implemented
                    		Intent intent = new Intent(PlayMenuActivity.this, PuzzleActivityOld.class);
                    		if(puzzles != null)
                    		{
                    			int puzzlenum = v.getId();
                    			Puzzle1 move = puzzles.get(puzzlenum-1);
                    			intent.putExtra("answer", move.myAnswer);
                    			intent.putExtra("clues", move.myClues);			
                    		}
                    		startActivity(intent);
                    	}
                    });  
                    b.setId(puzzlenum);
                    linear.addView(b, puzzlenum - 1);
                   	}
            	else
            	{
            		break;
            	}
                puzzlenum++;
            }
        }
        */
	        
	    scroll.addView(linear);
		setContentView(scroll);
	}
	
	private void setButtonMargins(Button b){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
        		LinearLayout.LayoutParams.MATCH_PARENT,      
        		LinearLayout.LayoutParams.MATCH_PARENT
        );
        params.setMargins(15, 5, 15, 5);
        b.setLayoutParams(params);
	}

	// For the tags title and summary, extracts their text values.
	private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
	    String result = "";
	    if (parser.next() == XmlPullParser.TEXT) {
	        result = parser.getText();
	        parser.nextTag();
	    }
	    return result;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play_menu, menu);
		return true;
	}
	
	public ArrayList<Puzzle2> parse(InputStream in) throws XmlPullParserException, IOException {
		ArrayList<Puzzle2> ret = null;
		try
		{
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in,null);
			ret =  readPuzzles(parser);
		}
		finally
		{
			in.close();
		}
		return ret;
	}
	
	public ArrayList<Puzzle1> parseOld(InputStream in) throws XmlPullParserException, IOException {
		ArrayList<Puzzle1> ret = null;
		try
		{
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in,null);
			ret =  readPuzzlesOld(parser);
		}
		finally
		{
			in.close();
		}
		return ret;
	}
	
	public ArrayList<Puzzle2> readPuzzles(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		//ArrayList<Puzzle1> puzzles = new ArrayList<Puzzle1>();
		ArrayList<Puzzle2> puzzles = new ArrayList<Puzzle2>();
		while(parser.next() != XmlPullParser.END_DOCUMENT)
		{
			int event = parser.getEventType();
			if(event == XmlPullParser.START_TAG)
			{
				String name = parser.getName();
				if(name.equals("puzzle"))
				{
					puzzles.add(readPuzzle(parser));
				}				
			}
		}
		return puzzles;
	}
	
	private Puzzle2 readPuzzle(XmlPullParser parser) throws XmlPullParserException, IOException {
	    parser.require(XmlPullParser.START_TAG, null, "puzzle");
	    String word = null;
	    ArrayList<String> clues = new ArrayList<String>();
	    while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        String name = parser.getName();
	        if (name.equals("word")) {
	            word = readText(parser);
	            clues.add(word);
	        }		        	
	        else 
	        {
	        	continue;
	        }
	    }
	    return new Puzzle2(clues);
	}
	
	public static class Puzzle2{
		public final ArrayList<String> myClues;
		private Puzzle2(ArrayList<String> clues)
		{
			this.myClues = clues;
		}
	}
	
	//Old version of game has been deprecated to below this comment
	
	// Processes number tags in the feed.
		private String readNumber(XmlPullParser parser) throws IOException, XmlPullParserException {
		    parser.require(XmlPullParser.START_TAG, null, "number");
		    String title = readText(parser);
		    parser.require(XmlPullParser.END_TAG, null, "number");
		    return title;
		}
		
		private String readClue(XmlPullParser parser) throws IOException, XmlPullParserException {
		    String clue = readText(parser);
		    return clue;
		}	
		
		private String readAnswer(XmlPullParser parser) throws IOException, XmlPullParserException {
		    parser.require(XmlPullParser.START_TAG, null, "answer");
		    String answer = readText(parser);
		    parser.require(XmlPullParser.END_TAG, null, "answer");
		    return answer;
		}
	
	public ArrayList<Puzzle1> readPuzzlesOld(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		ArrayList<Puzzle1> puzzles = new ArrayList<Puzzle1>();
		while(parser.next() != XmlPullParser.END_DOCUMENT)
		{
			int event = parser.getEventType();
			if(event == XmlPullParser.START_TAG)
			{
				String name = parser.getName();
				if(name.equals("puzzle"))
				{
					puzzles.add(readPuzzleOld(parser));
				}				
			}
		}
		return puzzles;
	}
	
	private Puzzle1 readPuzzleOld(XmlPullParser parser) throws XmlPullParserException, IOException {
	    parser.require(XmlPullParser.START_TAG, null, "puzzle");
	    String number = null;
	    String answer = null;
	    HashMap<String,String> clues = new HashMap<String,String>();
	    while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        String name = parser.getName();
	        if (name.equals("number")) {
	            number = readNumber(parser);
	        }
	        else if (name.equals("answer")) 
	        {
	            answer = readAnswer(parser);
	        }
	        else if (cluetypes.contains(name))
	        {
	        	clues.put(readClue(parser), name);
	        }		        	
	        else 
	        {
	        	continue;
//	            skip(parser);
	        }
	    }
	    return new Puzzle1(number, clues, answer);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void fillClueTypes()
	{
		cluetypes.add("part");
		cluetypes.add("phrase");
		cluetypes.add("idiom");
		cluetypes.add("noun");
		cluetypes.add("adjective");
	}
	
	public static class Puzzle1{
		public final String myNumber;
		public final String myAnswer;
		public final HashMap<String, String> myClues;
		private Puzzle1(String number, HashMap<String, String> clues, String answer)
		{
			this.myNumber = number;
			this.myClues = clues;
			this.myAnswer = answer;
		}	
	}
	

}
