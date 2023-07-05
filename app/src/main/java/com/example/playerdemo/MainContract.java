package com.example.playerdemo;

public interface MainContract {

    interface View{
        void showNotificationSoundSelected(String soundName);
        void showNotificationSoundNotSelected();
        void showDurationNotEntered();
        void showFailedToPlayNotification();

        void onDurationOver();
    }

    interface Presenter{
        void onSelectNotificationSoundClick();
        void onPlayNotificationClick(String durationString);
        void onDestroy();
    }
}
