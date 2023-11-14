package com.example.musicslayer;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

//Custom adapter for populating a ListView with songs.
public class SongListAdapter extends BaseAdapter {

    private ArrayList<Song> audioList;

    /**
     * Constructor to initialize the adapter with a list of songs.
     *
     * @param audioList List of songs to be displayed in the ListView.
     */
    public SongListAdapter(ArrayList<Song> audioList) {
        this.audioList = audioList;
    }

    /**
     * Get the number of items in the adapter.
     *
     * @return The number of items in the adapter.
     */
    @Override
    public int getCount() {
        return audioList.size();
    }

    /**
     * Get the item at the specified position in the adapter.
     *
     * @param position The position of the item to retrieve.
     * @return The item at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return audioList.get(position);
    }

    /**
     * Get the row ID associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set.
     * @return The ID of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * ViewHolder pattern for optimizing ListView performance by reusing views.
     */
    static class ViewHolder {
        TextView titleTextView;
        TextView artistTextView;
        ImageView songImageView;
    }

    /**
     * Get a View that displays the data at the specified position in the data set.
     *
     * @param position    The position of the item within the adapter's data set.
     * @param convertView The old view to reuse, if possible.
     * @param parent      The parent that this view will eventually be attached to.
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String title = audioList.get(position).getTitle();
        String artist = audioList.get(position).getArtist();
        Context context = parent.getContext();
        View view;
        ViewHolder viewHolder;

        // Check if the view is being reused, otherwise inflate a new view
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.song_row_item, parent, false);

            // Create a ViewHolder and store references to the views to be reused
            viewHolder = new ViewHolder();
            viewHolder.titleTextView = view.findViewById(R.id.title);
            viewHolder.artistTextView = view.findViewById(R.id.artist);
            viewHolder.songImageView = view.findViewById(R.id.songImage);

            // Store the ViewHolder as a tag in the view
            view.setTag(viewHolder);
        } else {
            // Reuse the view and retrieve the stored ViewHolder
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        // Set background color based on whether the song is currently playing
        if (MediaPlayerManager.getCurrentSongIndex() == position) {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_red));
        } else {
            view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.black));
        }

        // Set the title and artist of the song
        viewHolder.titleTextView.setText(title);
        viewHolder.artistTextView.setText(artist);

        // Set the song image using a drawable resource
        Resources res = context.getResources();
        String drawableName = "song_icon";
        int resId = res.getIdentifier(drawableName, "drawable", context.getPackageName());
        Drawable drawable = ContextCompat.getDrawable(context, resId);
        viewHolder.songImageView.setImageDrawable(drawable);

        return view;
    }
}
