package com.example.player;

import java.io.File;

public class ListData {
    String title, artist;
    File song;

    public ListData(String title, String artist, File song)
    {
        this.title=title;
        this.artist=artist;
        this.song=song;
    }
}
