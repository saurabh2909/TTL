package com.example.saubhagyam.thetalklist;

import android.content.Context;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class OutgoingCallActivity extends Fragment {
    int mutebit;

    Camera camera;
    FrameLayout surfaceView;
    SurfaceHolder surfaceHolder;
    /*int m_numOfCamera;
    int m_defaultCameraId;
    int m_currentCamera;*/
    private SurfaceHolder.Callback listener;
    ;
    private static int camId = Camera.CameraInfo.CAMERA_FACING_BACK;


    private Camera mCamera = null;
    private CameraView mCameraView = null;

    ImageView btn_mute, btn_cutcall, btn_speakers, changeCamera;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_outgoing_call, null);


        btn_cutcall = (ImageView) view.findViewById(R.id.outgoing_cutcall);
       /* btn_mute = (ImageView) view.findViewById(R.id.outgoing_mute);
        btn_speakers = (ImageView) view.findViewById(R.id.outgoing_speaker);*/


        surfaceView = (FrameLayout) view.findViewById(R.id.outgoingCallSurfaceView);


        try {
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);//you can use open(int) to use different cameras
        } catch (Exception e) {
            Log.d("ERROR", "Failed to get camera: " + e.getMessage());
        }

        if (mCamera != null) {
            mCameraView = new CameraView(getContext(), mCamera);//create a SurfaceView to show camera data
            surfaceView.addView(mCameraView);//add the SurfaceView to the layout
        }


        final MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.outgoing);
        AudioManager m_amAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        m_amAudioManager.setMode(AudioManager.MODE_IN_CALL);
        m_amAudioManager.setSpeakerphoneOn(false);
        mediaPlayer.setLooping(true);
        m_amAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, m_amAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
        mediaPlayer.start();


        btn_cutcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                getActivity().onBackPressed();

            }
        });

        btn_speakers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mutebit == 0) {

                } else {
                }

            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_outgoing_call);

       /* btn_cutcall = (ImageView) view.findViewById(R.id.outgoing_cutcall);
        btn_mute = (ImageView) view.findViewById(R.id.outgoing_mute);
        btn_speakers = (ImageView) view.findViewById(R.id.outgoing_speaker);


        surfaceView = (FrameLayout) view.findViewById(R.id.outgoingCallSurfaceView);*/


    }



}





/*
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }*/



