package com.example.playerdemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.playerdemo.databinding.ActivityMainBinding;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private ActivityMainBinding binding;

    private MainPresenter mainPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.txtSoundName.setVisibility(View.GONE);

        mainPresenter = new MainPresenter(this);

        binding.btnNotificationSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectNotificationSoundClick();
            }
        });

        binding.btnPlaySound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlayNotificationClick();
            }
        });


    }

    private void onPlayNotificationClick() {

        String durationString = binding.editTxtDuration.getText().toString();
        mainPresenter.onPlayNotificationClick(durationString);
    }

    private void onSelectNotificationSoundClick() {
        mainPresenter.onSelectNotificationSoundClick();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mainPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.onDestroy();

    }

    @Override
    public void showNotificationSoundSelected(String soundName) {
        binding.txtSoundName.setVisibility(View.VISIBLE);
        binding.txtSoundName.setText(soundName);
        Toast.makeText(this, "Notification Sound Selected", Toast.LENGTH_SHORT).show();
    }

    public void showNotificationSoundNotSelected() {
        Toast.makeText(this, "Please select A Notification Sound", Toast.LENGTH_SHORT).show();
    }

    public void showDurationNotEntered() {
        Toast.makeText(this, "Please Enter A duration", Toast.LENGTH_SHORT).show();
    }


    public void showFailedToPlayNotification() {
        Toast.makeText(this, "Failed To Play Notification", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDurationOver() {
        Toast.makeText(this, "Thank You!", Toast.LENGTH_SHORT).show();
    }
}