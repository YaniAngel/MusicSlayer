package com.example.musicslayer;

import java.io.Serializable;

/**
 * Represents a music song with details such as path, title, artist, and duration.
 */
public class Song implements Serializable {
    private final String path;
    private final String title;
    private final String artist;
    private final String duration;

    /**
     * Constructor for creating a Song object.
     * @param path The file path of the song.
     * @param title The title of the song.
     * @param artist The artist of the song.
     * @param duration The duration of the song in a formatted string (mm:ss).
     */
    public Song(String path, String title, String artist, String duration) {
        // Add validation if necessary
        this.path = path;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
    }

    /**
     * Gets the file path of the song.
     * @return The file path.
     */
    public String getPath() {
        return path;
    }

    /**
     * Gets the title of the song.
     * @return The title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the artist of the song.
     * @return The artist.
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Gets the duration of the song in a formatted string (mm:ss).
     * @return The formatted duration string.
     */
    public String getDuration() {
        return duration;
    }
}
