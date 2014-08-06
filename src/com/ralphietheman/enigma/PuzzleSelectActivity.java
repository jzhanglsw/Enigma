package com.ralphietheman.enigma;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class PuzzleSelectActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ScrollView scroll = new ScrollView(this);
		LinearLayout linear = new LinearLayout(this);
		linear.setOrientation(1);
		linear.setBackgroundColor(Color.rgb(112, 112, 118));
		
        //To do: replace this block of code with an xml parser that 
        //parses an xml of the puzzle pack name and associates it with
        //its file name (which is passed into the intent)
        ArrayList<String> puzzlepacks = new ArrayList<String>();
        puzzlepacks.add("Pizza");
        puzzlepacks.add("Lasagna");
        puzzlepacks.add("Donut");
        puzzlepacks.add("Ramen");
        puzzlepacks.add("Torta");
        puzzlepacks.add("Taco");        
		
        int puzzlepacknum = 1;
        int textsize = 70;
        int gray = Color.rgb( 255, 255, 255);
        
        for(String puzzleTitle: puzzlepacks){
    		Button b = new Button(this);
    		b.setText(puzzleTitle);
    		b.setTextSize(textsize);
    		b.setTextColor(gray);
            Drawable d = getResources().getDrawable(R.drawable.menubutton);
            b.setBackground(d);
            setButtonMargins(b);
            
            b.setOnClickListener(new OnClickListener(){
            	public void onClick(View v){
            		// TODO Auto-generated method stub
                    //To do: Extract information about which button is being clicked
                    //To do: Create an intent and pass on the extra information along the
                    //appropriate puzzle to the PuzzleActivity
            		
            		//Not yet implemented
            		Intent intent = new Intent(PuzzleSelectActivity.this, PlayMenuActivity.class);
            		startActivity(intent);
            	}
            });
            b.setId(puzzlepacknum);
            linear.addView(b,puzzlepacknum-1);
        }
        
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.puzzle_select, menu);
		return true;
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
