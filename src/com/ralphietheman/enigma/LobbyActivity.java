package com.ralphietheman.enigma;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class LobbyActivity extends Activity {
	
	//Some to try playing sounds when buttons are pressed
	private SoundPool soundPool;
	private int buttonSoundID;
	private int welcomeSoundID;
	boolean loaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        
	    // Set the hardware buttons to control the music
	    this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
	    // Load the sound
	    soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
	    buttonSoundID = soundPool.load(this, R.raw.button, 1);		
	    welcomeSoundID = soundPool.load(this, R.raw.welcome, 1);
	    
		setContentView(R.layout.activity_lobby);
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
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
	    playSoundID(welcomeSoundID);
        getMenuInflater().inflate(R.menu.lobby, menu);
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
    
	/** Called when the user clicks the about button */
	public void showAboutPage(View view) {
		playSoundID(buttonSoundID);
	    // Do something in response to button
		//Intent intent = new Intent(this, AboutActivity.class);
		//startActivity(intent);
	}
	
	/** Called when the user clicks the options button */
	public void showOptionsPage(View view) {
		playSoundID(buttonSoundID);
	    // Do something in response to button
		//Intent intent = new Intent(this, OptionsActivity.class);
		//startActivity(intent);
	}
	
	/** Called when the user clicks the instructions button */
	public void showInstructionsPage(View view) {
		playSoundID(buttonSoundID);
	    // Do something in response to button
		//Intent intent = new Intent(this, InstructionActivity.class);
		//startActivity(intent);
	}
	
	/** Called when the user clicks the play button */
	public void showPlayPage(View view) {
		playSoundID(buttonSoundID);
	    // Do something in response to button
		Intent intent = new Intent(this, PlayMenuActivity.class);
		startActivity(intent);
	}	
}
