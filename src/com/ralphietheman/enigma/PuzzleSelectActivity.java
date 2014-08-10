package com.ralphietheman.enigma;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.annotation.SuppressLint;
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

public class PuzzleSelectActivity extends Activity {
	public static final String puzzlepack1 = "puzzles1.xml";
	public static final String newpuzzlepack = "pizza.xml";
	//public static ArrayList<Puzzle1> puzzlesOld;
	public static ArrayList<Puzzle> puzzles;
	public static final ArrayList<String> cluetypes = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// sets puzzlepack to puzzlepack clicked in PuzzlePackSelectActivity
		String puzzlepack = getPuzzlepackInfo();
		
		ScrollView scroll = new ScrollView(this);
		
		LinearLayout linear = new LinearLayout(this);
		linear.setOrientation(1);
		linear.setBackgroundColor(Color.rgb(112, 112, 118));
				
		puzzles = null;
		try 
		{
			puzzles = parse(getAssets().open(puzzlepack));
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
                    		Intent intent = new Intent(PuzzleSelectActivity.this, PuzzleActivity.class);
                    		if(puzzles != null)
                    		{
                    			int puzzlenum = v.getId();
                    			Puzzle move = puzzles.get(puzzlenum-1);
                    			
                    			Bundle b = new Bundle();
                    			b.putStringArrayList("puzzle", move.getClues());
                    			b.putString("number", move.getNumber());
                    			intent.putExtras(b);
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
	        
	    scroll.addView(linear);
		setContentView(scroll);
	}
	
	@SuppressLint("DefaultLocale")
	private String getPuzzlepackInfo(){
		Intent intent = getIntent();
		Bundle resource = intent.getExtras();
		return resource.getString("puzzlepack").toLowerCase() + ".xml";
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
		//ArrayList<Puzzle1> puzzles = new ArrayList<Puzzle1>();
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
	    String word = null;
	    ArrayList<String> clues = new ArrayList<String>();
	    String number = null;
	    while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        String name = parser.getName();
	        if (name.equals("word")) {
	            word = readText(parser);
	            clues.add(word);
	        }
	        else if(name.equals("number")){
	        	number = readText(parser);
	        }
	        else 
	        {
	        	continue;
	        }
	    }
	    return new Puzzle(clues, number);
	}
	
	public static class Puzzle{
		private ArrayList<String> myClues;
		private String myNumber;
		private Puzzle(ArrayList<String> clues, String number)
		{
			this.myClues = clues;
			this.myNumber = number;
		}
		
		@SuppressLint("DefaultLocale")
		public static List<String> toLowerCase(List<String> strings)
		{
		    ListIterator<String> iterator = strings.listIterator();
		    while (iterator.hasNext())
		    {
		        iterator.set(iterator.next().toLowerCase());
		    }
		    return strings;
		}
		
		public ArrayList<String> getClues(){
			return (ArrayList<String>) toLowerCase(myClues);
		}
		
		public String getNumber(){
			return myNumber;
		}
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

}
