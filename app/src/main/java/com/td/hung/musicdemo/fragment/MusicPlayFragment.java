package com.td.hung.musicdemo.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.google.gson.Gson;
import com.td.hung.musicdemo.R;
import com.td.hung.musicdemo.activity.MusicPlayActivity;
import com.td.hung.musicdemo.service.MusicService;
import com.td.hung.musicdemo.entity.Song;
import com.td.hung.musicdemo.util.MusicPreference;
import com.td.hung.musicdemo.util.MusicUtil;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Hung Tran on 01/11/2017.
 */

public class MusicPlayFragment extends Fragment implements MusicPlayActivity.OnButtonPlayClickListener, MusicPlayActivity.OnChangeTrackListener {

    private static MusicPlayFragment musicPlayFragment;
    private Context mContext;
    private Animation rotation;
    private RotateAnimation rotateAnimation;
    private CircleImageView circleImageView;

    public static MusicPlayFragment newInstance() {
        if (musicPlayFragment == null) {
            musicPlayFragment = new MusicPlayFragment();
        }
        return musicPlayFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_play_music, container, false);
        circleImageView = (CircleImageView) view.findViewById(R.id.cursh_image);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rotation = AnimationUtils.loadAnimation(mContext, R.anim.spin_around);
        rotateAnimation = new RotateAnimation(0, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(4000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        MusicPlayActivity.setOnButtonPlayClickListener(this);
        MusicPlayActivity.setOnChangeTrack(this);
        setInitImage();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void play() {
        circleImageView.setAnimation(rotateAnimation);
        circleImageView.startAnimation(rotateAnimation);
    }

    @Override
    public void pause() {
        circleImageView.setAnimation(null);
    }

    @Override
    public void ChangeImage(String path) {
        Bitmap bitmap = MusicUtil.newInstance().getImageSongFromPath(mContext, path);
        if (bitmap != null) {
            circleImageView.setImageBitmap(bitmap);
        } else {
            circleImageView.setImageResource(R.drawable.crush);
        }
        circleImageView.setAnimation(rotateAnimation);
        circleImageView.startAnimation(rotateAnimation);
    }

    private void setInitImage() {
        String stringStrong = MusicPreference.newInstance(mContext).getString(MusicService.CURRENT_SONG, "");
        if (stringStrong != "") {
            Gson gson = new Gson();
            Song currentlySong = gson.fromJson(stringStrong, Song.class);
            Bitmap bitmap = MusicUtil.getImageSongFromPath(mContext, currentlySong.getPath());
            if (bitmap != null) {
                circleImageView.setImageBitmap(bitmap);
            } else {
                circleImageView.setImageResource(R.drawable.crush);
            }
        }else {
            circleImageView.setImageResource(R.drawable.crush);
        }
    }
}
