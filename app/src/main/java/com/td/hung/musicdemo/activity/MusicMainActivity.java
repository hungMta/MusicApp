package com.td.hung.musicdemo.activity;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.td.hung.musicdemo.R;
import com.td.hung.musicdemo.entity.Song;
import com.td.hung.musicdemo.service.MusicService;
import com.td.hung.musicdemo.util.MusicPreference;
import com.td.hung.musicdemo.util.MusicUtil;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by PC on 15/10/2017.
 */

public class MusicMainActivity extends AppCompatActivity implements View.OnClickListener, MusicLibaryActivity.SongClickListener, SeekBar.OnSeekBarChangeListener {


    public static final String TAG = "mussic";
    public static final String KEY_LIST_SONG = "KEY_LIST_SONG";

    private ArrayList<Song> songList = new ArrayList<>();
    private ImageView btnPrevious;
    private ImageView btnNext;
    private ImageView btnPlay;
    private ImageView btnMenu;

    private TextView txtSongName;
    private TextView txtSongArtist;
    private TextView txtCurrentDuration;
    private TextView txtTotalDuration;


    private MusicService musicService;
    private boolean isFirstOpen;
    private CircleImageView circleImageView;
    private SeekBar seekBar;
    private Thread threadProgress;

    // receive broadcast when init service
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                final Song song = (Song) bundle.getSerializable(MusicService.CURRENT_SONG);
                if (song != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG,"receive broadcast when init service");
                            txtSongName.setText(song.getTitle());
                            txtSongArtist.setText(song.getArtist());
                            btnPlay.setImageResource(R.drawable.ic_play_);
                            isFirstOpen = true;
                        }
                    });
                }
            }
        }
    };

    private BroadcastReceiver receiverChangeTrack = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                final Song song = (Song) bundle.getSerializable(MusicService.CURRENT_SONG);
                if (song != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtSongName.setText(song.getTitle());
                            txtSongArtist.setText(song.getArtist());
                            btnPlay.setImageResource(R.drawable.ic_pause);
                            circleImageView.setAnimation(rotateAnimation);
                            circleImageView.startAnimation(rotateAnimation);
                           // updateProgressBar();
                        }
                    });
                }
            }
        }
    };
    private RotateAnimation rotateAnimation;
    private Animation rotation;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        btnNext = (ImageView) findViewById(R.id.btn_next);
        btnPrevious = (ImageView) findViewById(R.id.btn_previous);
        btnPlay = (ImageView) findViewById(R.id.btn_play);
        btnMenu = (ImageView) findViewById(R.id.btn_menu);

        txtSongArtist = (TextView) findViewById(R.id.txt_song_artist);
        txtSongName = (TextView) findViewById(R.id.txt_song_name);
        txtCurrentDuration = (TextView) findViewById(R.id.txt_current);
        txtTotalDuration = (TextView) findViewById(R.id.txt_total);

        circleImageView = (CircleImageView) findViewById(R.id.cursh_image);

        seekBar = (SeekBar) findViewById(R.id.seekBar);

        rotation = AnimationUtils.loadAnimation(this, R.anim.spin_around);
        rotateAnimation = new RotateAnimation(0, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(4000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);


        btnNext.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);
        btnMenu.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);

        getSongList();

        MusicLibaryActivity.setSongClikcListener(this);
        Log.d("mussic","milliSecondsToTimer : " +MusicUtil.instance().milliSecondsToTimer(10));
        Log.d("mussic","milliSecondsToTimer : " +MusicUtil.instance().milliSecondsToTimer(100));
        initService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(
                MusicService.OPENAPP));
        registerReceiver(receiverChangeTrack, new IntentFilter(
                MusicService.CHANGETRACK));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        unregisterReceiver(receiverChangeTrack);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_menu:
                openSongList();
                isFirstOpen = false;
                break;
            case R.id.btn_next:
                musicService.next();
                isFirstOpen = false;
                break;
            case R.id.btn_play:
                if (isFirstOpen) {
                    Log.d(TAG,"is first open");
                    musicService.startPlay(musicService.getCurrentlySong().getID());
                    btnPlay.setImageResource(R.drawable.ic_pause);
                    circleImageView.setAnimation(rotateAnimation);
                    circleImageView.startAnimation(rotateAnimation);
                } else {
                    Log.d(TAG," not first open");
                    if (localBinder.getService().isPlaying()) {
                        localBinder.getService().pause();
                        circleImageView.setAnimation(null);
                        btnPlay.setImageResource(R.drawable.ic_play_);
                    } else {
                        localBinder.getService().play();
                        circleImageView.setAnimation(rotateAnimation);
                        circleImageView.startAnimation(rotateAnimation);
                        btnPlay.setImageResource(R.drawable.ic_pause);
                    }
                }
                isFirstOpen = false;
                break;
            case R.id.btn_previous:
                musicService.previous();
                isFirstOpen = false;
                break;

        }
    }

    private void openSongList() {
        Intent intent = new Intent(this, MusicLibaryActivity.class);
        intent.putExtra(KEY_LIST_SONG, songList);
        startActivity(intent);
    }

    private MusicService.LocalBinder localBinder;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            localBinder = (MusicService.LocalBinder) service;
            musicService = localBinder.getService();
            if (localBinder.getService() != null) {
                Log.d("service", "is not null");
            } else {
                Log.d("service", "is  null");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("service", "onServiceDisconnected");
        }
    };

    public void getSongList() {
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            musicCursor.getNotificationUri();
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int data = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            //add songs to list
            int index = 0;
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String path = musicCursor.getString(data);
                songList.add(new Song(path, thisId, thisTitle, thisArtist, index));
                index++;
            }
            while (musicCursor.moveToNext());
        }
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void songClick(final Song song) {

        if (!isMyServiceRunning(MusicService.class)) {
            Intent intent = new Intent(this, MusicService.class);
            intent.putExtra("uri", song.getUri());
            bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        } else {
            musicService.startPlay(song.getID());
        }

    }

    private void initService() {
        Intent intent = new Intent(this, MusicService.class);
        intent.putExtra(KEY_LIST_SONG, songList);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }


    public void updateProgressBar() {
        final long totalDuration = MusicPreference.newInstance(getApplicationContext()).getLong(MusicService.GET_DURATION, 0);
        long currentDuration = MusicPreference.newInstance(getApplicationContext()).getLong(MusicService.GET_CURRENTPOSITITION, 0);
        Log.d("mussic","totalDuration : " +totalDuration);
        Log.d("mussic","current : " +currentDuration);
        threadProgress = new Thread(new Runnable() {
            @Override
            public void run() {
                for (long i = 0; i < totalDuration; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final long finalI = i;

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("mussic","current : " +finalI);
                            Log.d("mussic","milliSecondsToTimer : " +MusicUtil.instance().milliSecondsToTimer(finalI));
                            txtCurrentDuration.setText("" + MusicUtil.instance().milliSecondsToTimer(finalI));
                            int progress = (int) (MusicUtil.instance().getProgressPercentage(finalI, totalDuration));
                            seekBar.setProgress(progress);
                        }
                    });
                }
            }
        });
        threadProgress.start();
    }

    /**
     * Background Runnable thread
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = MusicPreference.newInstance(getApplicationContext()).getLong(MusicService.GET_DURATION, 0);
            long currentDuration = MusicPreference.newInstance(getApplicationContext()).getLong(MusicService.GET_CURRENTPOSITITION, 0);

            // Displaying Total Duration time
            txtTotalDuration.setText("" + MusicUtil.instance().milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            txtCurrentDuration.setText("" + MusicUtil.instance().milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = (int) (MusicUtil.instance().getProgressPercentage(currentDuration, totalDuration));
            seekBar.setProgress(progress);
            Toast.makeText(getApplicationContext(), "update time", Toast.LENGTH_SHORT);
        }
    };


    // seek bar
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
