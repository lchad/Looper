package com.liulishuo.looper;

import android.app.Application;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;

public class MediaPlayerUtils {
    private static MediaPlayerUtils sInstance;
    MediaPlayer mPlayer;
    AudioManager mAudioManager;
    Callback mCallback;

    private MediaPlayerUtils() {
        mAudioManager = (AudioManager) JupiterGlobal.context()
                .getSystemService(Application.AUDIO_SERVICE);
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if (mCallback != null) {
                    mCallback.onError();
                }
                return false;
            }
        });
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mCallback != null) {
                    mCallback.onComplete();
                }
            }
        });
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (mCallback != null) {
                    mCallback.onPrepare();
                }
            }
        });
    }

    public static MediaPlayerUtils getInstance() {
        synchronized (MediaPlayerUtils.class) {
            if (sInstance == null) {
                sInstance = new MediaPlayerUtils();
            }
            return sInstance;
        }
    }

    public void play(int id) {
        play(id, false, null);
    }

    public void play(int id, boolean loop) {
        play(id, loop, null);
    }

    public void play(int id, int voiceRate) {
        try {
            if (mPlayer == null) {
                mPlayer = new MediaPlayer();
            }
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            mPlayer.reset();
//            final int originalVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//            int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//
//            int realVoice = (int) ((voiceRate / 100.0) * maxVolume);
//            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, realVoice, 0);

            AssetFileDescriptor afd = JupiterGlobal.context().getResources().openRawResourceFd(id);
            mPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
            mPlayer.setLooping(false);
            mPlayer.prepare();
            mPlayer.start();
            afd.close();
        } catch (Exception e) {
            e.printStackTrace();
            mPlayer = null;
            if (mCallback != null) {
                mCallback.onError();
            }
        }
    }

    public void play(int id, boolean loop, Callback callback) {
        this.mCallback = callback;
        try {
            if (mPlayer == null) {
                mPlayer = new MediaPlayer();
            }
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            mPlayer.reset();
//            final int originalVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//            int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

//            int voiceRate = 20;
//
//            int realVoice = (int) ((voiceRate / 100.0) * maxVolume);
//            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, realVoice, 0);

            AssetFileDescriptor afd = JupiterGlobal.context().getResources().openRawResourceFd(id);
            mPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
            mPlayer.setLooping(loop);
            mPlayer.prepare();
            mPlayer.start();
            afd.close();
        } catch (Exception e) {
            e.printStackTrace();
            mPlayer = null;
            if (mCallback != null) {
                mCallback.onError();
            }
        }
    }

    public int getCurrentPlayPosition() {
        return mPlayer.getCurrentPosition();
    }

    public void stop() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
        }
    }

    public void destroy() {
        if (mPlayer != null) {
            try {
                stop();
                mPlayer.reset();
                mPlayer.release();
                mPlayer = null;
                mCallback = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sInstance = null;
    }

    public interface Callback {
        void onComplete();

        void onError();

        void onPrepare();
    }
}
