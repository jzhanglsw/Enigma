package com.ralphietheman.enigma;

import java.util.ArrayList;
import java.util.HashMap;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class PuzzleActivityOld extends Activity {
	
	private SoundPool soundPool;
	private int winSoundID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_puzzle_activity_old);
		
		//Sound resources
	    // Set the hardware buttons to control the music
	    this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
	    // Load the sound
	    soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
	    winSoundID = soundPool.load(this, R.raw.win, 1);
		updateButtonText();	
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
	
	public void updateButtonText()
	{
		// This should work. Passed the map through. Check if the for loop is working.
		ArrayList<Button> buttonlist = new ArrayList<Button>();
		Button clue1 = (Button) findViewById(R.id.clue1);
		Button clue2 = (Button) findViewById(R.id.clue2);
		Button clue3 = (Button) findViewById(R.id.clue3);
		buttonlist.add(clue1);
		buttonlist.add(clue2);
		buttonlist.add(clue3);
        Drawable d = getResources().getDrawable(R.drawable.menubutton);
        clue1.setBackground(d);
        clue2.setBackground(d);
        clue3.setBackground(d);
		Intent puzzleinfo = getIntent();
		@SuppressWarnings("unchecked")
		HashMap<String, String> clues = (HashMap<String, String>) puzzleinfo.getSerializableExtra("clues");
		int index = 0;
		
		//Gets the settings value for whether to display hints to the user
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		Boolean displayHints = preferences.getBoolean("display_hints_preference", true);

		for(String clue: clues.keySet())
		{
			Button buttonclue = buttonlist.get(index);
			if(displayHints){
				buttonclue.setOnClickListener(new OnClickListener(){
					public void onClick(View v){
						Intent puzzleinfo = getIntent();		
						@SuppressWarnings("unchecked")
						HashMap<String, String> clues = (HashMap<String, String>) puzzleinfo.getSerializableExtra("clues");
						String buttontext = ((Button) v).getText().toString();
						Button change = (Button) v;
						// If the button currently has the clue itself
						if(clues.keySet().contains(buttontext))
						{
							change.setText(clues.get(buttontext));
						}
						// If the button currently has what type of clue it is
						else if(clues.values().contains(buttontext))
						{
							for(String key: clues.keySet())
							{
								if(clues.get(key).equals(buttontext))
								{
									change.setText(key);
								}
							}
						}
					}
				});
			}
			buttonclue.setText(clue);
			index++;
		}
	}
	
	@SuppressLint("DefaultLocale")
	public void checkAnswer(View view)
	{
		Intent intent = getIntent();
    	EditText editText = (EditText) findViewById(R.id.answer);
    	String answer = editText.getText().toString().toLowerCase();
    	if(answer.equals(intent.getStringExtra("answer")))
    	{
    		ArrayList<Button> buttonlist = new ArrayList<Button>();
    		Button clue1 = (Button) findViewById(R.id.clue1);
    		Button clue2 = (Button) findViewById(R.id.clue2);
    		Button clue3 = (Button) findViewById(R.id.clue3);
    		buttonlist.add(clue1);
    		buttonlist.add(clue2);
    		buttonlist.add(clue3);
    		for(Button b: buttonlist)
    		{
    			b.setBackgroundColor(Color.rgb(48, 214, 48));
    		}
    		playSoundID(winSoundID);
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
