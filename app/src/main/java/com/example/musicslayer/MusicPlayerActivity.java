package com.example.musicslayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Activity for playing and controlling music playback.
 */
public class MusicPlayerActivity extends AppCompatActivity {

    // Views
    TextView titleView, currentTimeView, totalTimeView, artistView;
    SeekBar seekBar;
    ImageView musicIcon, pauseButton, prevButton, nextButton;
    ArrayList<Song> audioList;
    Song currentSong;
    MediaPlayerManager mediaPlayer;
    int x=0;
    boolean userIsSeeking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        // Initialize MediaPlayerManager
        mediaPlayer = MediaPlayerManager.getInstance(getApplicationContext());

        // Initialize Views
        titleView = findViewById(R.id.song_title);
        artistView = findViewById(R.id.song_artist);
        currentTimeView = findViewById(R.id.current_time);
        totalTimeView = findViewById(R.id.total_time);
        seekBar = findViewById(R.id.seek_bar);
        pauseButton = findViewById(R.id.pause_play_button);
        prevButton = findViewById(R.id.previous_button);
        nextButton = findViewById(R.id.next_button);
        musicIcon = findViewById(R.id.music_icon_big);
        titleView.setSelected(true);

        // Get the list of songs from the intent
        audioList = (ArrayList<Song>) getIntent().getSerializableExtra("songsList");

        // Set up the song and views
        setSong();

        // Set the OnCompletionListener to play the next song when the current one completes
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Only play the next song if not manually stopped
                if (!mediaPlayer.isManuallyStopped()) {
                    playNextSong();
                }
                mediaPlayer.resetManuallyStoppedFlag();
            }
        });

        // Update seekbar and UI elements on a separate thread
        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    // If-Statement ensures the seekbar doesn't update while being dragged
                    if (!userIsSeeking && mediaPlayer != null) {
                        int currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                    }

                    // Update current time display
                    currentTimeView.setText(convertToMili(mediaPlayer.getCurrentPosition()+""));

                    // Update play/pause button icon
                    if(mediaPlayer.isPlaying()){
                        pauseButton.setImageResource(R.drawable.baseline_pause_circle);
                    } else {
                        pauseButton.setImageResource(R.drawable.baseline_play_circle);
                    }
                }
                // Schedule the thread to run after a delay
                new Handler().postDelayed(this, 100);
            }
        });

        // Handle seekbar interactions
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Do nothing here, progress will be updated only when the user releases the seek bar
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // The user has started interacting with the seek bar
                userIsSeeking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // The user has stopped interacting with the seek bar
                userIsSeeking = false;

                // Update the progress only when the user releases the seek bar
                if (mediaPlayer != null) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                }
            }
        });
    }

    /**
     * Set up the selected song and views.
     */
    void setSong() {
        currentSong = audioList.get(mediaPlayer.getCurrentSongIndex());
        titleView.setText(currentSong.getTitle());
        artistView.setText(currentSong.getArtist());
        totalTimeView.setText(convertToMili(currentSong.getDuration()));
        pauseButton.setOnClickListener(v-> pausePlay());
        nextButton.setOnClickListener(v-> playNextSong());
        prevButton.setOnClickListener(v-> playPreviousSong());

        // Start playing the selected song
        playMusic();
    }

    /**
     * Start MediaPlayer and set seekbar to the beginning.
     */
    private void playMusic(){
        mediaPlayer.play(currentSong.getPath());
        seekBar.setProgress(0);
        seekBar.setMax(mediaPlayer.getDuration());
    }

    /**
     * Play the next song in the list.
     */
    private void playNextSong(){
        if(mediaPlayer.getCurrentSongIndex() == audioList.size()-1){
            return;
        }
        mediaPlayer.setCurrentSongIndex(mediaPlayer.getCurrentSongIndex() + 1);
        mediaPlayer.stop();
        setSong();
    }

    /**
     * Play the previous song in the list.
     */
    private void playPreviousSong(){
        if(mediaPlayer.getCurrentSongIndex() == 0) {
            return;
        }
        mediaPlayer.setCurrentSongIndex(mediaPlayer.getCurrentSongIndex() - 1);
        mediaPlayer.stop();
        setSong();
    }

    /**
     * Pause or play the current song.
     */
    private void pausePlay(){
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.resume();
    }

    /**
     * Convert duration from milliseconds to a formatted string (mm:ss).
     * @param duration The duration in milliseconds.
     * @return The formatted duration string.
     */
    @NonNull
    public static String convertToMili(String duration){
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }
}
