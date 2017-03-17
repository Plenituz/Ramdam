package com.plenituz.ramdam.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

/**
 * Created by Plenituz on 14/01/2015.
 */
public class SoundPlayer {


    volatile private static ArrayList<MediaPlayer> allMps = new ArrayList<MediaPlayer>();
    public static boolean muted = false;
    public static SoundHandler handler;

    public static void init(){
        handler = new SoundHandler();
    }

    public static class SoundHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            ((Runnable)msg.obj).run();
        }
    }

    public static void playSound(Context context, final String url, final onSoundPlayedListener onSoundPlayedListener, final Runnable onError, final onSoundCreatedListener onSoundCreatedListener) throws IOException {
        //MediaPlayer mp = MediaPlayer.create(context, Uri.parse(url));
        final MediaPlayer mp = new MediaPlayer();
        mp.setDataSource(context, Uri.parse(url));
        mp.prepare();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                allMps.remove(mp);
                if (onSoundPlayedListener != null) {
                    Message mPlayed = handler.obtainMessage();
                    mPlayed.obj = new Runnable() {
                        @Override
                        public void run() {
                            onSoundPlayedListener.onSoundPlayed(url);
                        }
                    };
                    handler.sendMessage(mPlayed);
                }
            }
        });
        mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.v("error", "" + what);
                if (onError != null) {
                    Message mError = handler.obtainMessage();
                    mError.obj = onError;
                    handler.sendMessage(mError);
                }
                return false;
            }
        });
        mp.start();
        if(onSoundCreatedListener != null) {
            Message mCrea = handler.obtainMessage();
            mCrea.obj = new Runnable() {
                @Override
                public void run() {
                    onSoundCreatedListener.onSoundCreated(mp);
                }
            };
            handler.sendMessage(mCrea);
        }
        allMps.add(mp);
    }

    public static void playSoundThreaded(Context context, String url, onSoundPlayedListener onSoundPlayedListener, Runnable onError, onSoundCreatedListener onSoundCreatedListener){
        if (muted)
            return;
        new ThreadedSoundPlayer(context, url, onSoundPlayedListener, onError, onSoundCreatedListener).start();
    }

    public interface onSoundPlayedListener{
        void onSoundPlayed(String url);
    }
    public interface onSoundCreatedListener{
        void onSoundCreated(MediaPlayer mp);
    }


    public static void stopSounds(){
        ArrayList<MediaPlayer> copy = (ArrayList<MediaPlayer>) allMps.clone();
        try{
            for(MediaPlayer mp: copy){
                try{
                    mp.stop();
                    mp.release();
                    allMps.remove(mp);
                }catch (IllegalStateException e){
                    //this happen when you stop a music and it's already stopped
                }
            }
        }catch (ConcurrentModificationException e){
            e.printStackTrace();
            //this happen when stopping level while changing color
        }
    }
    public static void setMuted(boolean muted){
        if(muted){
            stopSounds();
        }
        SoundPlayer.muted = muted;
    }

    private static class ThreadedSoundPlayer extends Thread{
        private final Context context;
        private final String url;
        private final onSoundPlayedListener onSoundPlayedListener;
        private final onSoundCreatedListener onSoundCreatedListener;
        private Thread t;
        private Runnable onErr;

        public ThreadedSoundPlayer(Context context, String url, onSoundPlayedListener onSoundPlayedListener, Runnable onError, onSoundCreatedListener onSoundCreatedListener) {
            this.context = context;
            this.url = url;
            this.onSoundPlayedListener = onSoundPlayedListener;
            onErr = onError;
            this.onSoundCreatedListener = onSoundCreatedListener;
        }


        @Override
        public void run() {
            try{
                playSound(context, url, onSoundPlayedListener, onErr, onSoundCreatedListener);
            }catch (IOException e){
                e.printStackTrace();
                Message msg = handler.obtainMessage();
                msg.obj = onErr;
                handler.sendMessage(msg);
                e.printStackTrace();
            }

        }

        @Override
        public synchronized void start() {
            if (t == null)
            {
                t = new Thread (this, "soundPlayer");
                t.start ();
            }
        }
    }
}


