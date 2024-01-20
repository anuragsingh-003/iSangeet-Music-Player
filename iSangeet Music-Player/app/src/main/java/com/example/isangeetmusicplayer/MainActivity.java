package com.example.isangeetmusicplayer;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ListView listView;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        // External storage permission granted using dexter library
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Toast.makeText(MainActivity.this, "Runtime Permission Granted", Toast.LENGTH_SHORT).show();
                        ArrayList<File> mySongs = fetchSongs(Environment.getExternalStorageDirectory());
                        String[] items = new String[mySongs.size()];
                        for (int i = 0; i < mySongs.size(); i++) {
                            items[i] = mySongs.get(i).getName().replace(".mp3", "");
                        }
                        //Display the songs using Array Adapter
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, items);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                              Intent intent = new Intent(MainActivity.this,play_song.class);
                              String currentSong =  listView.getItemAtPosition(position).toString();
                              intent.putExtra("SongList",mySongs);
                              intent.putExtra("CurrentSongs",currentSong);
                              intent.putExtra("position",position);
                              startActivity(intent);

                            }
                        });

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }
//        Media player using local source
//        MediaPlayer mediaPlayer1 = MediaPlayer.create(this,R.raw.song);

//        MediaPlayer using Remote source
//        mediaPlayer = new MediaPlayer();
//        try {
//            mediaPlayer.setDataSource("http://103.212.226.231/music/01%20Sweet%20Caroline.mp3");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
////                buffering of data source
//                Toast.makeText(MainActivity.this, "Ready to Play", Toast.LENGTH_SHORT).show();
//                mp.start();
//            }
//        });
//        mediaPlayer.prepareAsync();
//        mediaPlayer.start();


    // Method will return mp3 file into array List
    public ArrayList <File>fetchSongs(File file){
        ArrayList arrayList=new ArrayList();
        File[] songs =file.listFiles();
        // Search for the music file in the directory if found it will added into the array
        // It will recursively add the files in to the array
        if(songs!=null){
            for(File myFile :songs){
                if(!myFile.isHidden()&& myFile.isDirectory()){
                    arrayList.addAll(fetchSongs(myFile));
                }
                else{
                    if(myFile.getName().endsWith(".mp3")&& !myFile.getName().startsWith(".")){
                        arrayList.add(myFile);
                    }
                }
            }
        }
        return arrayList;
    }

}