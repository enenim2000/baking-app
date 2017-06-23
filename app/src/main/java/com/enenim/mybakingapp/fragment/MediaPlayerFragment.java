package com.enenim.mybakingapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enenim.mybakingapp.R;
import com.enenim.mybakingapp.config.Constants;
import com.enenim.mybakingapp.model.Step;
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

    private OnDetailFragmentInteractionListener mListener;
    private Step step;

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView simpleExoPlayerView;

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

        //CommonUtil.getMimeType(step.getVideoURL());

        if(!KEY_EMPTY.equalsIgnoreCase(step.getVideoURL().trim())){
            //Do nothing
            getActivity().findViewById(R.id.recipe_image_view_container).setVisibility(View.GONE);
            getActivity().findViewById(R.id.playerView).setVisibility(View.VISIBLE);
            initializePlayer(Uri.parse(step.getVideoURL()));
        }else if(!KEY_EMPTY.equalsIgnoreCase(step.getThumbnailURL().trim())){
            simpleExoPlayerView.setVisibility(View.GONE);
            getActivity().findViewById(R.id.recipe_image_view_container).setVisibility(View.VISIBLE);
        }else {
            getActivity().findViewById(R.id.recipe_image_view_container).setVisibility(View.GONE);
            getActivity().findViewById(R.id.playerView).setVisibility(View.INVISIBLE);
        }
    }

    public void onButtonPressed(Step step) {
        if (mListener != null) {
            mListener.onDetailFragmentInteraction(step);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDetailFragmentInteractionListener) {
            mListener = (OnDetailFragmentInteractionListener) context;
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

    public interface OnDetailFragmentInteractionListener {
        void onDetailFragmentInteraction(Step step);
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    private void initializePlayer(Uri uri) {
        if (mExoPlayer == null) {
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

            LoadControl loadControl = new DefaultLoadControl();

            TrackSelector trackSelector = new DefaultTrackSelector();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mExoPlayer.setPlayWhenReady(true);

            simpleExoPlayerView = (SimpleExoPlayerView) getActivity().findViewById(R.id.playerView);
            //simpleExoPlayerView.setVisibility(View.VISIBLE);

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
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
}
