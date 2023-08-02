package com.example.player;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.player.databinding.ActivityMainBinding;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    File[] files;
    ListData listData;
    ArrayList<ListData> arrayList = new ArrayList<>();
    ListAdapter listAdapter;
    ActivityMainBinding binding;
    public static final int MY_PERMISSIONS_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE))
                Toast.makeText(this, "EXPLANATION", Toast.LENGTH_LONG).show();

            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST);
                Toast.makeText(this, "EXPLANATION NO NEEDED", Toast.LENGTH_LONG).show();
            }
        }

        getMusic();
    }

    public void bind() {

        listAdapter = new ListAdapter(MainActivity.this, arrayList);
        binding.listView.setAdapter(listAdapter);
        binding.listView.setClickable(true);
        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String path = Environment.getExternalStorageDirectory().getPath() + "/Download/" + files[i].getName();
                MediaPlayer mediaPlayer = MediaPlayer.create(peekAvailableContext(), Uri.parse(path));
                mediaPlayer.start();

            }
        });
    }


    public void getMusic() {
        String path = Environment.getExternalStorageDirectory().getPath() + "/Download";
        files = new File(path).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                boolean result;
                if (name.endsWith(".mp3")) {
                    result = true;
                } else {
                    result = false;
                }
                return result;
            }
        });
        MediaMetadataRetriever mediaMetadataRetriever = (MediaMetadataRetriever) new MediaMetadataRetriever();
        try {
            for (int i = 0; i < files.length; i++) {
                Uri uri = (Uri) Uri.fromFile(files[i]);
                mediaMetadataRetriever.setDataSource(MainActivity.this, uri);
                String artist = (String) mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                String title = (String) mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                Log.d("music", (i + 1) + "/" + files.length + " " + artist + " " + title);

                listData = new ListData(title, artist, files[i]);
                arrayList.add(listData);
            }
            bind();

        } catch (NullPointerException e) {
            Log.d("music", e.toString());
        }

    }
}