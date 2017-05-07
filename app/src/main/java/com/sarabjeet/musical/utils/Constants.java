package com.sarabjeet.musical.utils;

/**
 * Created by sarabjeet on 8/5/17.
 */

public class Constants {
    public interface ACTION {
        String MAIN_ACTION = "com.sarabjeet.musical.action.main";
        String ACTION_PLAY = "com.sarabjeet.musical.action.play";
        String ACTION_PAUSE = "com.sarabjeet.musical.action.pause";
        String ACTION_PREVIOUS = "com.sarabjeet.musical.action.previous";
        String ACTION_NEXT = "com.sarabjeet.musical.action.next";
        String STARTFOREGROUND_ACTION = "com.sarabjeet.musical.action.startforeground";
        String STOPFOREGROUND_ACTION = "com.sarabjeet.musical.action.stopforeground";
    }

    public interface NOTIFICATION_ID {
        int FOREGROUND_SERVICE = 101;
    }
}
