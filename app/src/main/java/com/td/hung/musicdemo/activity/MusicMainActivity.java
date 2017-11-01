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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
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

import com.td.hung.musicdemo.R;
import com.td.hung.musicdemo.entity.Song;
import com.td.hung.musicdemo.recyclerview.SongListRecyclerViewAdapter;
import com.td.hung.musicdemo.service.MusicService;
import com.td.hung.musicdemo.util.MusicPreference;
import com.td.hung.musicdemo.util.MusicUtil;
import com.td.hung.musicdemo.viewpager.MusicViewPagerAdapter;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by PC on 15/10/2017.
 */

public class MusicMainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, SongListRecyclerViewAdapter.OnItemSongClickListener {


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
    private Context mContext;

    private MusicService musicService;
    private boolean isFirstOpen;
    private CircleImageView circleImageView;
    private SeekBar seekBar;
    private Thread threadProgress;
    private Handler mHander = new Handler();

    private long totalDuration;
    private long currentDuration;

    private MusicViewPagerAdapter musicViewPagerAdapter;
    private ViewPager viewPager;
    private static OnButtonPlayClickListener onButtonPlayClickListener;
    private ImageView imgDotOne;
    private ImageView imgDotTwo;
    private ImageView imgDotThree;

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
                            Log.d(TAG, "receive broadcast when init service");
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
                            totalDuration = MusicPreference.newInstance(getApplicationContext()).getLong(MusicService.GET_DURATION, 0);
                            currentDuration = MusicPreference.newInstance(getApplicationContext()).getLong(MusicService.GET_CURRENTPOSITITION, 0);
                            updateProgressBar();
                        }
                    });
                }
            }
        }
    };

    private BroadcastReceiver receiverPauseTrack = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            pauseProgressBar();
        }
    };

    private BroadcastReceiver receiverPlayTrack = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateProgressBar();
        }
    };

    private RotateAnimation rotateAnimation;
    private Animation rotation;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        mContext = this;
        btnNext = (ImageView) findViewById(R.id.btn_next);
        btnPrevious = (ImageView) findViewById(R.id.btn_previous);
        btnPlay = (ImageView) findViewById(R.id.btn_play);
        btnMenu = (ImageView) findViewById(R.id.btn_menu);
        imgDotOne = (ImageView) findViewById(R.id.img_dot_one);
        imgDotThree = (ImageView) findViewById(R.id.img_dot_three);
        imgDotTwo = (ImageView) findViewById(R.id.img_dot_two);

        txtSongArtist = (TextView) findViewById(R.id.txt_song_artist);
        txtSongName = (TextView) findViewById(R.id.txt_song_name);
        txtCurrentDuration = (TextView) findViewById(R.id.txt_current);
        txtTotalDuration = (TextView) findViewById(R.id.txt_total);

        circleImageView = (CircleImageView) findViewById(R.id.cursh_image);

        seekBar = (SeekBar) findViewById(R.id.seekBar);

        viewPager = (ViewPager) findViewById(R.id.music_view_pager);

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

        SongListRecyclerViewAdapter.setOnItemSongClickListener(this);
        getSongList();
        initService();
        totalDuration = MusicPreference.newInstance(this).getLong(MusicService.GET_DURATION, 0);
        currentDuration = MusicPreference.newInstance(this).getLong(MusicService.GET_CURRENTPOSITITION, 0);
        initViewPager();
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(MusicService.OPENAPP));
        registerReceiver(receiverChangeTrack, new IntentFilter(MusicService.CHANGETRACK));
        registerReceiver(receiverPauseTrack, new IntentFilter(MusicService.PAUSETRACK));
        registerReceiver(receiverPlayTrack, new IntentFilter(MusicService.PLAYTRACK));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        unregisterReceiver(receiverChangeTrack);
        unregisterReceiver(receiverPlayTrack);
        unregisterReceiver(receiverPauseTrack);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_menu:
                isFirstOpen = false;
                break;
            case R.id.btn_next:
                musicService.next();
                isFirstOpen = false;
                threadProgress = null;
                break;
            case R.id.btn_play:
                if (isFirstOpen) {
                    Log.d(TAG, "is first open");
                    musicService.startPlay(musicService.getCurrentlySong().getID());
                    btnPlay.setImageResource(R.drawable.ic_pause);
                    circleImageView.setAnimation(rotateAnimation);
                    circleImageView.startAnimation(rotateAnimation);
                    onButtonPlayClickListener.pause();
                } else {
                    Log.d(TAG, " not first open");
                    if (localBinder.getService().isPlaying()) {
                        localBinder.getService().pause();
                        circleImageView.setAnimation(null);
                        btnPlay.setImageResource(R.drawable.ic_play_);
                        onButtonPlayClickListener.pause();
                    } else {
                        localBinder.getService().play();
                        circleImageView.setAnimation(rotateAnimation);
                        circleImageView.startAnimation(rotateAnimation);
                        btnPlay.setImageResource(R.drawable.ic_pause);
                        onButtonPlayClickListener.play();
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


    private void initViewPager() {
        musicViewPagerAdapter = new MusicViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(musicViewPagerAdapter);
        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        imgDotOne.setImageResource(R.drawable.ic_dot_active);
                        imgDotTwo.setImageResource(R.drawable.ic_dot_none_active);
                        imgDotThree.setImageResource(R.drawable.ic_dot_none_active);
                        break;
                    case 1:
                        imgDotOne.setImageResource(R.drawable.ic_dot_none_active);
                        imgDotTwo.setImageResource(R.drawable.ic_dot_active);
                        imgDotThree.setImageResource(R.drawable.ic_dot_none_active);
                        break;
                    case 2:
                        imgDotOne.setImageResource(R.drawable.ic_dot_none_active);
                        imgDotTwo.setImageResource(R.drawable.ic_dot_none_active);
                        imgDotThree.setImageResource(R.drawable.ic_dot_active);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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


    private void initService() {
        Intent intent = new Intent(this, MusicService.class);
        intent.putExtra(KEY_LIST_SONG, songList);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    /**
     * background thread update Time
     */

    private boolean stopTimeTask;
    private Runnable mUpdateTimeTask = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "mUpdateTimeTask : " + currentDuration);
            txtCurrentDuration.setText("" + MusicUtil.instance().milliSecondsToTimer(currentDuration / 1000));
            seekBar.setProgress(MusicUtil.instance().getProgressPercentage(currentDuration, totalDuration));
            currentDuration += 1000;
            if (!stopTimeTask)
                mHandler.postDelayed(this, 1000);
        }
    };

    public void updateProgressBar() {
        txtTotalDuration.setText("" + MusicUtil.instance().milliSecondsToTimer(totalDuration / 1000));
        stopTimeTask = false;
        mHander.postDelayed(mUpdateTimeTask, 100);
    }

    private void pauseProgressBar() {
        stopTimeTask = true;
        mHander.removeCallbacks(mUpdateTimeTask);
    }


    /**
     * seekbar
     *
     * @param seekBar
     * @param progress
     * @param fromUser
     */

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.d(TAG, " seekbar onProgressChanged");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.d(TAG, " seekbar onStartTrackingTouch");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.d(TAG, " seekbar onStopTrackingTouch");
        currentDuration = MusicUtil.instance().progressToTimer(seekBar.getProgress(), totalDuration);
        musicService.seekToTime((int) currentDuration);
    }

    @Override
    public void itemSongClicked(Song song) {
        if (!isMyServiceRunning(MusicService.class)) {
            Intent intent = new Intent(this, MusicService.class);
            intent.putExtra("uri", song.getUri());
            bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        } else {
            musicService.startPlay(song.getID());
        }
    }

    public interface OnButtonPlayClickListener {
        void play();
        void pause();
    }

    public static void setOnButtonPlayClickListener(OnButtonPlayClickListener listener){
        onButtonPlayClickListener = listener;
    }
}
