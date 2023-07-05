package com.example.playerdemo;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.widget.Toast;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainPresenter implements MainContract.Presenter{

    private MainContract.View view;

    private Uri selectedNotificationSound;

    private MediaPlayer mediaPlayer;

    private Timer stopPlaybackTimer;

    private static final int NOTIFICATION_SOUND_REQUEST_CODE = 100;

    public MainPresenter(MainContract.View view) {
        this.view = view;
    }

    @Override
    public void onSelectNotificationSoundClick() {
        Activity activity = (Activity) view;
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Notification Sound");
        activity.startActivityForResult(intent, NOTIFICATION_SOUND_REQUEST_CODE);



    }

    private String getSoundNameFromUri(Uri selectedNotificationSound) {
        Activity activity = (Activity) view;
        String soundName = null;
        Ringtone ringtone = RingtoneManager.getRingtone(activity, selectedNotificationSound);
        if (ringtone != null){
            soundName = ringtone.getTitle(activity);
        }
        return soundName;
    }

    @Override
    public void onPlayNotificationClick(String durationString) {
        if (selectedNotificationSound != null){
            if(durationString.isEmpty()){
                view.showDurationNotEntered();
                return;
            }

            int duration = Integer.parseInt(durationString);
            //Play Notification sound for the specified duration
            playNotificationSound(duration);
        }else {
            view.showNotificationSoundNotSelected();
        }

    }

    private void playNotificationSound(final int duration) {
        Activity activity = (Activity) view;
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(activity, selectedNotificationSound);
            mediaPlayer.setLooping(true);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();

                    //Stop playback after the specified duration
                    new CountDownTimer(duration * 1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            stopPlayBack();
                            onDurationOver();
                        }
                    }.start();
                }
            });
            mediaPlayer.prepareAsync();

        } catch (IOException e) {
            e.printStackTrace();
            view.showFailedToPlayNotification();
        }
    }

    private void onDurationOver() {
        view.onDurationOver();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == NOTIFICATION_SOUND_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            selectedNotificationSound = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
        }

        String soundName = getSoundNameFromUri(selectedNotificationSound);

        view.showNotificationSoundSelected(soundName);
    }

    private void stopPlayBack() {
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (stopPlaybackTimer != null){
            stopPlaybackTimer.cancel();
            stopPlaybackTimer = null;
        }
    }

    @Override
    public void onDestroy() {
        stopPlayBack();
    }
}
