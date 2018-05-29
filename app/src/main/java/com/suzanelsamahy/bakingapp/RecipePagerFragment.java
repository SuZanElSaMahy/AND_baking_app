package com.suzanelsamahy.bakingapp;


import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.suzanelsamahy.bakingapp.models.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipePagerFragment extends Fragment {


    public RecipePagerFragment() {
        // Required empty public constructor
    }


//    @BindView(R.id.recipe_step_tablayout)
//    TabLayout recipeStepTabLayout;
    private Unbinder unbinder;

    @BindView(R.id.playerView)
    SimpleExoPlayerView mPlayerView;


    @BindView(R.id.step_tv)
    TextView stepTv;


    @BindView(R.id.step_placeholder_iv)
    ImageView mStepIv;


    private List<Step> stepList = new ArrayList<>();
    private int stepNumber;
    SimpleExoPlayer mExoPlayer;
    private long playbackPosition;
    public static final String PLAYBACK_POSITION = "playbackPos";
    public static final String PARCE_STEP_ARRAY = "stepArray";
    public static final String PARCE_STEP_NUMBER = "position";
    private boolean playIfReady;
    public static final String PLAY_IF_READY = "playIfReady";
    public static final String URL = "url";
    private String url;



    public static RecipePagerFragment getInstance(int stepNumber, List<Step>stepList) {
        RecipePagerFragment fragment = new RecipePagerFragment();
        fragment.stepList = stepList;
        fragment.stepNumber = stepNumber;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_pager, container, false);
        unbinder = ButterKnife.bind(this, view);

        if(savedInstanceState!=null){
            stepList = savedInstanceState.getParcelableArrayList(PARCE_STEP_ARRAY);
            stepNumber = savedInstanceState.getInt(PARCE_STEP_NUMBER);
            playbackPosition = savedInstanceState.getLong(PLAYBACK_POSITION);
            playIfReady = savedInstanceState.getBoolean(PLAY_IF_READY);
            url= savedInstanceState.getString(URL);
        }



        Step currentStep = stepList.get(stepNumber);
        String videoUrl = currentStep.getVideoURL();
        url = currentStep.getVideoURL();
        if(!TextUtils.isEmpty(videoUrl) ){
            mPlayerView.setVisibility(View.VISIBLE);
            mStepIv.setVisibility(View.GONE);
            initializePlayer(Uri.parse(videoUrl));
            onFullScreenMode();
        } else if (!TextUtils.isEmpty(currentStep.getThumbnailURL())){
            mPlayerView.setVisibility(View.GONE);
            mStepIv.setVisibility(View.VISIBLE);
            Picasso.with(getActivity()).load(currentStep.getThumbnailURL()).into(mStepIv);
        }
        stepTv.setText(currentStep.getDescription());

        return view;
    }


    private void initializePlayer(Uri mediaUri){
        if(mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer =  ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            //Data Source
            String userAgent = Util.getUserAgent(getActivity(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getActivity(),
                    userAgent), new DefaultExtractorsFactory(),null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(playIfReady);
            mExoPlayer.seekTo(playbackPosition);


        }
    }

    public void onFullScreenMode(){
        int orientation = getResources().getConfiguration().orientation;
        boolean twoPane = getResources().getBoolean(R.bool.isTwoPane);

        if(orientation == Configuration.ORIENTATION_LANDSCAPE && !twoPane){
            mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            mPlayerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            mPlayerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
           // hide UI
            View decorView = getActivity().getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }


    private void releasePlayer(){
        if(mExoPlayer!=null){
            mExoPlayer.stop();
            mExoPlayer.release();
        }
        mExoPlayer = null;
    }

    @Override
    public void onStart() {
        super.onStart();
//        if (Util.SDK_INT > 23) {
//            initializePlayer(Uri.parse(url));
//        }
        initializePlayer(Uri.parse(url));
    }


    @Override
    public void onResume(){
        super.onResume();
        initializePlayer(Uri.parse(url));
        mExoPlayer.seekTo(playbackPosition);
        mExoPlayer.setPlayWhenReady(playIfReady);
//        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
//            initializePlayer(Uri.parse(url));
//        } else {
//
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        if(unbinder!= null)
            unbinder.unbind();
    }

    @Override
    public void onPause() {
        super.onPause();
        playbackPosition = mExoPlayer.getCurrentPosition();
        playIfReady = mExoPlayer.getPlayWhenReady();
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList(PARCE_STEP_ARRAY, (ArrayList<? extends Parcelable>) stepList);
        savedInstanceState.putInt(PARCE_STEP_NUMBER, stepNumber);
        savedInstanceState.putLong(PLAYBACK_POSITION, playbackPosition);
        savedInstanceState.putBoolean(PLAY_IF_READY, playIfReady);
        savedInstanceState.putString(URL,stepList.get(stepNumber).getVideoURL());
    }

}
