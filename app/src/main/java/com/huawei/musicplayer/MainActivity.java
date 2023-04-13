package com.huawei.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ImageView nextIv,playIv,lastIv;
    TextView nameIv,singerIv;
    ListView musicLV;
    SeekBar seekBar;

    private int[] musicnum = {1,2,3,4,5,6,7,8};
    private  String[] music_name = {"Black Bird","Booty music","花海","LOSER","前前前世","晴天","Lemon","心做し"};
    private  String[] singer = {"ぼくのりりっくのぼうよみ","Git Fresh","周杰伦","BIGBANG","RADWIMPS","周杰伦","米津玄师","双笙"};
    private  int[]  songsid;
    private MediaPlayer mediaPlayer;
    private int status =0 ;
    private  double time = 0;
    private  Thread thread;
    private Boolean s = false;
    int now= 0;
    int now2 =0;
    List<Map<String,Object>>  listitems = new ArrayList<>();
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            seekBar.setProgress(status);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaPlayer = new MediaPlayer();
        initView();
        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.ls);
        songsid = new int[]{R.raw.bb,R.raw.bm,R.raw.hh,R.raw.ls,R.raw.qqqs,R.raw.qt,R.raw.xz,R.raw.zrqk};
        try {
            setListViewAdapter();
        } catch (Exception e) {
            Log.i("TAG", "读取歌曲失败");
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                s=true;
                mediaPlayer.seekTo(seekBar.getProgress());
                status = seekBar.getProgress();
                s=false;
            }
        });

        playIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()){
                    if (mediaPlayer !=null && mediaPlayer.isPlaying()){
                        now = mediaPlayer.getCurrentPosition();
                        mediaPlayer.pause();
                        playIv.setImageResource(R.mipmap.ic_public_play);
                    }
                }else {

                        mediaPlayer.seekTo(now);
                        mediaPlayer.start();
                        playIv.setImageResource(R.mipmap.ic_public_pause);
                }
            }
        });

        lastIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (now2 == 0){
                    Toast.makeText(MainActivity.this,"已经是第一首了",Toast.LENGTH_SHORT);
                }else {
                    now2 = now2 -1 ;
                    playMusic(now2);
                }


            }
        });
        nextIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (now2 == music_name.length-1){
                    Toast.makeText(MainActivity.this,"已经是最后一首了",Toast.LENGTH_SHORT);
                }else {
                    now2 = now2 +1 ;
                    playMusic(now2);
                }
            }
        });


    }


    private void setListViewAdapter() {

        for (int i = 0;i<music_name.length;i++){
            Map<String,Object>  listitem = new HashMap<>();
            listitem.put("musicnum",musicnum[i]);
            listitem.put("song",music_name[i]);
            listitem.put("singer",singer[i]);
            listitem.put("songid",songsid[i]);
            listitems.add(listitem);

        }
        SimpleAdapter simpleAdapter =new SimpleAdapter(this,listitems,
                R.layout.list_item,new String[]{"musicnum","song","singer"},
                new int[]{R.id.item_local_music_num,R.id.item_local_music_name,R.id.item_local_music_singer});
        musicLV.setAdapter(simpleAdapter);

        musicLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                playMusic(position);
                 now2 = position;
            }
        });

    }
    public void playMusic(int now1){
        s=true;
        status = 0;
        if (mediaPlayer!=null){
            mediaPlayer.reset();
        }
        mediaPlayer = MediaPlayer.create(getApplicationContext(), songsid[now1]);
        mediaPlayer.start();
        nameIv.setText(music_name[now1]);
        singerIv.setText(singer[now1]);

        //设置seekbar
        time = mediaPlayer.getDuration();
        int i = new Double(time).intValue();
        seekBar.setMax(i);
        seekBar.setProgress(0);
        thread();
        s=false;
        playIv.setImageResource(R.mipmap.ic_public_pause);

    }


    public void thread(){
        thread = new Thread(){
            public void run(){

                while(status<time){
                    while (s);
                    try{
                        int currentPosition=getCurrentPosition();
                        status = currentPosition;}
                    catch (Exception e){

                    }
                    handler.sendEmptyMessage(0x111);

                }
            }
        };
        thread.start();
    }


    private int getCurrentPosition(){
        if(mediaPlayer!=null){
            return mediaPlayer.getCurrentPosition();
        }
        return 0;    }



    private  void  initView(){
        nextIv = findViewById(R.id.local_music_bottom_next);
        playIv = findViewById(R.id.local_music_bottom_play);
        lastIv = findViewById(R.id.local_music_bottom_last);
        nameIv = findViewById(R.id.local_music_bottom_name);
        singerIv = findViewById(R.id.local_music_singer);
        musicLV = findViewById(R.id.local_music_lv);
        seekBar = findViewById(R.id.sb);

    }



}
