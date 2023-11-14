package com.example.musicslayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import java.util.Random;
import java.util.ArrayList;
import android.provider.MediaStore;

/**
 * The main activity that displays the list of songs and provides controls for playback.
 */
public class MainActivity extends AppCompatActivity {

    // Views and variables
    ImageView shuffle_button;
    ListView listView;
    ArrayList<Song> audioList = new ArrayList<>();
    Random rand = new Random();
    MediaPlayerManager mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize MediaPlayerManager
        mediaPlayer = MediaPlayerManager.getInstance(getApplicationContext());

        // Initialize Views
        listView = findViewById(R.id.listView);
        shuffle_button = findViewById(R.id.shuffle_button);

        // Load the list of songs
        loadList();

        // Set up the adapter for the song list
        SongListAdapter adapter = new SongListAdapter(audioList);
        listView.setAdapter(adapter);

        // Shuffle button click listener
        shuffle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the current song index to a random position in the list
                mediaPlayer.setCurrentSongIndex(rand.nextInt(audioList.size()));
                openMusicPlayerActivity();
            }
        });

        // Handles clicking on a song from the list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int clickedSongIndex = position;
                int currentSongIndex = mediaPlayer.getCurrentSongIndex();

                if (currentSongIndex == position) {
                    // If clicking on currently running song, open MusicPlayerActivity without resetting the song
                    openMusicPlayerActivity();
                } else {
                    // The clicked song is not the currently playing song, restart the song
                    mediaPlayer.stop();
                    mediaPlayer.setCurrentSongIndex(clickedSongIndex);
                    openMusicPlayerActivity();
                }
            }
        });
    }

    /**
     * Method to open MusicPlayerActivity.
     */
    private void openMusicPlayerActivity() {
        Intent intent = new Intent(MainActivity.this, MusicPlayerActivity.class);
        intent.putExtra("songsList", audioList);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * Loads songs from storage into ArrayList.
     */
    void loadList() {
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String[] projection = {
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION
        };

        // Query the media store for audio files
        Cursor audioCursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection, selection, null, MediaStore.Audio.Media.TITLE);

        // Populate the audioList with songs
        if (audioCursor != null) {
            if (audioCursor.moveToFirst()) {
                do {
                    audioList.add(new Song(
                            audioCursor.getString(0),
                            audioCursor.getString(1),
                            audioCursor.getString(2),
                            audioCursor.getString(3)));
                } while (audioCursor.moveToNext());
            }
        }
        // Close the cursor to avoid memory leaks
        if (audioCursor != null) {
            audioCursor.close();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (listView != null) {
            // Update the list view with the currently playing song highlighted
            listView.setAdapter(new SongListAdapter(audioList));
        }
        if (mediaPlayer.getCurrentSongIndex() != -1) {
            // Automatically scroll to the currently playing song
            listView.setSelection(mediaPlayer.getCurrentSongIndex());
        }
    }
}
