package com.example.musicslayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * Singleton class to manage the MediaPlayer instance for playing audio files.
 */
public class MediaPlayerManager {
    private static MediaPlayerManager instance;
    private MediaPlayer mediaPlayer;
    private Context context;
    private boolean isPlaying;
    private static int currentSongIndex;
    private boolean isManuallyStopped = false;

    /**
     * Private constructor to enforce singleton pattern and initialize MediaPlayer.
     *
     * @param context The application context.
     */
    private MediaPlayerManager(Context context) {
        this.context = context;
        initializeMediaPlayer();
    }

    /**
     * Retrieves the singleton instance of MediaPlayerManager.
     *
     * @param context The application context.
     * @return The MediaPlayerManager instance.
     */
    public static MediaPlayerManager getInstance(Context context) {
        if (instance == null) {
            instance = new MediaPlayerManager(context);
        }
        return instance;
    }

    /**
     * Initializes the MediaPlayer instance.
     */
    private void initializeMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
    }

    /**
     * Plays the specified audio file.
     *
     * @param audioFile The path to the audio file.
     */
    public void play(String audioFile) {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.setDataSource(audioFile);
                mediaPlayer.prepare();
                mediaPlayer.start();
                isPlaying = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Pauses the currently playing audio.
     */
    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPlaying = false;
        }
    }

    /**
     * Resumes playback of the paused audio.
     */
    public void resume() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            isPlaying = true;
        }
    }

    /**
     * Stops the audio playback and resets the MediaPlayer.
     */
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            isPlaying = false;
            isManuallyStopped = true;
        }
    }

    /**
     * Resets the MediaPlayer without stopping it.
     */
    public void reset() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            isPlaying = false;
        }
    }

    /**
     * Releases the MediaPlayer resources.
     */
    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            isPlaying = false;
        }
    }

    /**
     * Checks if the MediaPlayer is currently playing.
     *
     * @return True if MediaPlayer is playing, false otherwise.
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * Checks if the MediaPlayer was manually stopped.
     *
     * @return True if manually stopped, false otherwise.
     */
    public boolean isManuallyStopped() {
        return isManuallyStopped;
    }

    /**
     * Resets the flag indicating manual stop.
     */
    public void resetManuallyStoppedFlag() {
        isManuallyStopped = false;
    }

    /**
     * Retrieves the index of the currently playing song.
     *
     * @return The index of the currently playing song.
     */
    public static int getCurrentSongIndex() {
        return currentSongIndex;
    }

    /**
     * Sets the index of the currently playing song.
     *
     * @param x The index of the currently playing song.
     */
    public static void setCurrentSongIndex(int x) {
        currentSongIndex = x;
    }

    /**
     * Sets a listener for the completion of audio playback.
     *
     * @param listener The listener to be notified on playback completion.
     */
    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
        mediaPlayer.setOnCompletionListener(listener);
    }

    /**
     * Retrieves the current playback position in the audio.
     *
     * @return The current playback position in milliseconds.
     */
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    /**
     * Sets the playback position to the specified progress in the audio.
     *
     * @param progress The progress to set the playback position to, in milliseconds.
     */
    public void seekTo(int progress) {
        mediaPlayer.seekTo(progress);
    }

    /**
     * Retrieves the total duration of the audio.
     *
     * @return The total duration of the audio in milliseconds.
     */
    public int getDuration() {
        return mediaPlayer.getDuration();
    }
}
