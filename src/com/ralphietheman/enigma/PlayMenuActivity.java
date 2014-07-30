package com.ralphietheman.enigma;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

public class PlayMenuActivity extends Activity {
	public static final String puzzlepack1 = "puzzles1.xml";
	public static ArrayList<Puzzle> puzzles;
	public static final ArrayList<String> cluetypes = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Code for creating layout is outdated
        TableLayout layout = new TableLayout (this);
        layout.setLayoutParams( new TableLayout.LayoutParams(4,5) );

        layout.setBackgroundColor(Color.rgb(112, 112, 118));
        layout.setGravity(Gravity.CENTER);
		
		fillClueTypes();
		
		 puzzles = null;
			try 
			{
				puzzles = parse(getAssets().open(puzzlepack1));
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
	        int textsize = 20;
	        for (int f=0; f<=7; f++) {
	            TableRow tr = new TableRow(this);
	            for (int c=0; c<=5; c++) {
	            	if(puzzlenum<=puzzles.size())
	            	{
	                    Button b = new Button (this);
	                    b.setText(""+puzzlenum);
	                    b.setTextSize(textsize);
	                    b.setTextColor(Color.rgb( 255, 255, 255));
	                    //Not sure what changed about this line
	                    //b.setOnClickListener(this);  
	                    b.setId(puzzlenum);
	                    tr.addView(b, 80,80);            		
	            	}
	            	else
	            	{
	            		break;
	            	}
	                puzzlenum++;
	            }
	            layout.addView(tr);
	        }
		
		setContentView(R.layout.activity_play_menu);
	}

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
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
        //To do: Extract information about which button is being clicked
        //To do: Create an intent and pass on the extra information along the
        //appropriate puzzle to the PuzzleActivity
		
		//Not yet implemented
		//Intent intent = new Intent(this, PuzzleActivity.class);
		if(puzzles != null)
		{
			int puzzlenum = v.getId();
			Puzzle move = puzzles.get(puzzlenum-1);
			//intent.putExtra("answer", move.myAnswer);
			//intent.putExtra("clues", move.myClues);			
		}
		//startActivity(intent);
	}
	
	public ArrayList<Puzzle> parse(InputStream in) throws XmlPullParserException, IOException {
		ArrayList<Puzzle> ret = null;
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
	
	public ArrayList<Puzzle> readPuzzles(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		ArrayList<Puzzle> puzzles = new ArrayList<Puzzle>();
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
	
	private Puzzle readPuzzle(XmlPullParser parser) throws XmlPullParserException, IOException {
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
	    return new Puzzle(number, clues, answer);
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
	
	public static class Puzzle{
		public final String myNumber;
		public final String myAnswer;
		public final HashMap<String, String> myClues;
		private Puzzle(String number, HashMap<String, String> clues, String answer)
		{
			this.myNumber = number;
			this.myClues = clues;
			this.myAnswer = answer;
		}	
	}
}
