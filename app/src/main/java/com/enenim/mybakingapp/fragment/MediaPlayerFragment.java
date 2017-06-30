package com.enenim.mybakingapp.fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enenim.mybakingapp.R;
import com.enenim.mybakingapp.config.Constants;
import com.enenim.mybakingapp.model.Step;
import com.enenim.mybakingapp.util.CommonUtil;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class MediaPlayerFragment extends Fragment implements ExoPlayer.EventListener, Constants{
    private static final String TAG = MediaPlayerFragment.class.getSimpleName();

    private OnMediaFragmentInteractionListener mListener;
    private Step step;

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView simpleExoPlayerView;
    private Long currentPosition = 0L;

    private int resumeWindow;
    private long resumePosition;
    private Uri uri;

    public MediaPlayerFragment() {
        // Required empty public constructor
    }
    public static MediaPlayerFragment newInstance(Step step) {
        MediaPlayerFragment fragment = new MediaPlayerFragment();
        fragment.setStep(step);
        Bundle args = new Bundle();
        args.putParcelable(KEY_STEP, step);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_IS_PLAYING)) {
            currentPosition = savedInstanceState.getLong("current_position");
        }

        //To preserve fragment during orientation changes
        setRetainInstance(true);

        clearResumePosition();

        if (getArguments() != null) {
            if(step == null){
                step = getArguments().getParcelable(KEY_STEP);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_media_player, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(!TextUtils.isEmpty(step.getVideoURL().trim())
                || CommonUtil.getMimeType(step.getThumbnailURL()).equalsIgnoreCase("video") ){
            if(CommonUtil.getMimeType(step.getThumbnailURL()).equalsIgnoreCase("video")){
                uri = Uri.parse(step.getThumbnailURL());
            }else {
                uri = Uri.parse(step.getVideoURL());
            }
            if(getActivity().findViewById(R.id.recipe_image_view_container) != null){
                getActivity().findViewById(R.id.recipe_image_view_container).setVisibility(View.GONE);
            }

            if(getActivity().findViewById(R.id.playerView) != null){
                getActivity().findViewById(R.id.playerView).setVisibility(View.VISIBLE);
            }

            initializePlayer();

        }else if(!TextUtils.isEmpty(step.getThumbnailURL().trim())){
            if(simpleExoPlayerView != null){
                simpleExoPlayerView.setVisibility(View.GONE);
            }
            if(getActivity().findViewById(R.id.recipe_image_view_container) != null){
                getActivity().findViewById(R.id.recipe_image_view_container).setVisibility(View.VISIBLE);
            }

        }else {
            if(getActivity().findViewById(R.id.recipe_image_view_container) != null){
                getActivity().findViewById(R.id.recipe_image_view_container).setVisibility(View.GONE);
            }
            if(getActivity().findViewById(R.id.playerView) != null){
                getActivity().findViewById(R.id.playerView).setVisibility(View.INVISIBLE);
            }
        }
    }

    public void onButtonPressed(Step step) {
        if (mListener != null) {
            mListener.onMediaFragmentInteraction(step);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMediaFragmentInteractionListener) {
            mListener = (OnMediaFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    public interface OnMediaFragmentInteractionListener {
        void onMediaFragmentInteraction(Step step);
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    private void initializePlayer() {
        if (mExoPlayer == null) {
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            LoadControl loadControl = new DefaultLoadControl();

            TrackSelector trackSelector = new DefaultTrackSelector();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mExoPlayer.setPlayWhenReady(true);
            simpleExoPlayerView = (SimpleExoPlayerView) getActivity().findViewById(R.id.playerView);

            simpleExoPlayerView.setUseController(true);
            simpleExoPlayerView.requestFocus();

            simpleExoPlayerView.setPlayer(mExoPlayer);

            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                    Util.getUserAgent(getContext(), "MyBakingApp"), bandwidthMeter);
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource videoSource = new ExtractorMediaSource(uri, dataSourceFactory, extractorsFactory, null, null);
            mExoPlayer.prepare(videoSource);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            //releasePlayer();
        }
        updateResumePosition();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("current_position", mExoPlayer.getCurrentPosition());
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            updateResumePosition();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private void updateResumePosition() {
        resumeWindow = mExoPlayer.getCurrentWindowIndex();
        resumePosition = mExoPlayer.isCurrentWindowSeekable() ? Math.max(0, mExoPlayer.getCurrentPosition())
                : C.TIME_UNSET;
    }

    private void clearResumePosition() {
        resumeWindow = C.INDEX_UNSET;
        resumePosition = C.TIME_UNSET;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
        if(mExoPlayer != null){
            simpleExoPlayerView = (SimpleExoPlayerView) getActivity().findViewById(R.id.playerView);
            if(simpleExoPlayerView != null){
                simpleExoPlayerView.setPlayer(mExoPlayer);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initializePlayer();
        /*if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            initializePlayer();
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initializePlayer();
        }
    }
}
