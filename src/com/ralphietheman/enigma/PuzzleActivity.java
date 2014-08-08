package com.ralphietheman.enigma;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeSet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PuzzleActivity extends Activity {
	
	private SoundPool soundPool;
	private int winSoundID;
	private ArrayList<String> words;
	private ArrayList<String> ends;
	private TreeSet<String> answered;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		EditText editText = (EditText) findViewById(R.id.answer);
		
		/* Trying ways to clear the textbox when an answer is entered
		editText.setOnKeyListener(new OnKeyListener(){
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER){
					EditText editText = (EditText) findViewById(R.id.answer);
					editText.setText("");
				}
				return true;
			}
		});
		*/
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		setupResources();
		setContentView(R.layout.activity_puzzle_activity);
		
		//Sound resources
	    // Set the hardware buttons to control the music
	    this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
	    // Load the sound
	    soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
	    winSoundID = soundPool.load(this, R.raw.win, 1);
		updateButtonText();	
	}
	
	public void setupResources(){
		Intent puzzleinfo = getIntent();
		Bundle b = puzzleinfo.getExtras();
		words = b.getStringArrayList("puzzle");
		ends = new ArrayList<String>();
		answered = new TreeSet<String>();
		setEnds();
	}
	
	public void setEnds(){
		ends.add(words.get(0));
		ends.add(words.get(4));
	}

	public void playSoundID(int soundID)
	{
		float volume = getSoundSettings();
		soundPool.play(soundID, volume, volume, 1, 0, 1f);
	}
	
	public float getSoundSettings()
	{
	      AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
	      float actualVolume = (float) audioManager
	          .getStreamVolume(AudioManager.STREAM_MUSIC);
	      float maxVolume = (float) audioManager
	          .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	      float volume = actualVolume / maxVolume;
	      return volume;
	}
	
	public ArrayList<Button> getButtons(){
		ArrayList<Button> buttonlist = new ArrayList<Button>();
		Button clue1 = (Button) findViewById(R.id.clue1);
		Button clue2 = (Button) findViewById(R.id.clue2);
		Button clue3 = (Button) findViewById(R.id.clue3);
		Button clue4 = (Button) findViewById(R.id.clue4);
		Button clue5 = (Button) findViewById(R.id.clue5);
		
		buttonlist.add(clue1);
		buttonlist.add(clue2);
		buttonlist.add(clue3);
		buttonlist.add(clue4);
		buttonlist.add(clue5);
		
		Drawable d = getResources().getDrawable(R.drawable.menubutton);
		for(Button clue: buttonlist){
			clue.setBackground(d);
		}
		return buttonlist;
	}
	
	public void updateButtonText(){
		ArrayList<Button> buttonlist = getButtons();
		
		int index = 0;
		for(String clue: words)
		{
			Button buttonclue = buttonlist.get(index);
			if(ends.contains(clue)){
				buttonclue.setText(clue);
			}
			else{
				buttonclue.setText("?");
			}
			index++;
		}
	}
	
	@SuppressLint("DefaultLocale")
	public void checkAnswer(View view)
	{
    	EditText editText = (EditText) findViewById(R.id.answer);
    	String answer = editText.getText().toString();//.toLowerCase();
    	//If one of the 5 words contains the answer and it's not one of the end words and it hasn't already been answered
    	if(words.contains(answer) || words.contains(answer + "(s)") && !ends.contains(answer) && !answered.contains(answer))
    	{
    		answered.add(answer);
    		Button answeredButton = getButtonByWord(answer);
    		if(answeredButton != null){
    			answeredButton.setText(answer);
    		}
    		playSoundID(winSoundID);
    		if(answered.size() == 3){
        		for(Button b: getButtons())
        		{
        			Drawable d = getResources().getDrawable(R.drawable.greenmenubutton);
        			b.setBackground(d);
        		}
    		}
    		editText.setText("");
    	}
	}
	
	public void clearText(View view){
		EditText editText = (EditText) findViewById(R.id.answer);
		editText.setText("");
	}

	public Button getButtonByWord(String word){
		int match = words.indexOf(word);
		if(match != -1){
			return getButtons().get(match);
		}
		else{
			return null;	
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.puzzle_activity_old, menu);
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
