package com.ralphietheman.enigma;

import java.util.ArrayList;
import java.util.TreeSet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint("DefaultLocale")
public class PuzzleActivity extends Activity {
	
	private SoundPool soundPool;
	private int winSoundID;
	private ArrayList<String> words;
	private ArrayList<String> ends;
	private TreeSet<String> answered;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		setContentView(R.layout.activity_puzzle_activity);
		
		setupResources();
		
		EditText editText = (EditText) findViewById(R.id.answer);
		editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		
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
		setEnds();		
		answered = new TreeSet<String>();
		
		TextView title = (TextView) findViewById(R.id.puzzletitle);
		title.setText("Puzzle " + b.getString("number"));
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
				buttonclue.setText(sanitizeString(clue));
			}
			else{
				SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
				String difficulty = preferences.getString("difficulty_preference", "n/a");
				if(difficulty.equals("Easy")){
					buttonclue.setText(clue.substring(0, 1).toUpperCase() + clue.substring(1).replaceAll(".", " _ "));
				}
				else if(difficulty.equals("Normal")){
					buttonclue.setText(clue.substring(0, 1).toUpperCase() + "?");
				}
				else if(difficulty.equals("Hard")){
					buttonclue.setText("?");
				}
			}
			index++;
		}
	}
	
	public String sanitizeString(String s)
	{
		return s.substring(0, 1).toUpperCase() + s.substring(1);
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
    			answeredButton.setText(sanitizeString(answer));
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
