package com.cn.ailanguage.primarychinese.Activity_and_Fragment.OriDetail;

 import android.media.MediaPlayer;
 import android.net.Uri;
 import android.os.AsyncTask;
 import android.util.Log;

public class AudioPlayerTask extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... params) {
        String audioPath = params[0];
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioPath);
            Log.e("qxy", "audioPath: " + audioPath);
            Log.e("qxy", "MediaPlayer state after setting data source: " + mediaPlayer.getDuration());

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start(); // 在准备完成后开始播放
                    Log.e("qxy", "onPrepared: MediaPlayer prepared and ready to start");
                    Log.e("qxy", "start: " + mp.isPlaying() + " " + mp.getDuration());
                }
            });
            mediaPlayer.prepareAsync(); // 异步准备播放器
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                    Log.e("qxy", "onCompletion: MediaPlayer released");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
