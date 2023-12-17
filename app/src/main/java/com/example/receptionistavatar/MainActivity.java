package com.example.receptionistavatar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;


import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;

import android.view.View;

import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

        VideoView video;

        String textOutput;


        // activity result launcher to start intent
        private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        ArrayList<String> d = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if (d != null && !d.isEmpty()) {
                            textOutput = d.get(0);
                            setPath(textOutput);
                        }
                    }
                });

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            video = findViewById(R.id.video);

            String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.help;
            playVideo(videoPath);

            video.setOnCompletionListener(mp -> startVoiceInput());
        }
        public void startVoiceInput()
        {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            );
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Thanks to Stop!!");
            // starting intent for result
            activityResultLauncher.launch(intent);
        }
        public void playVideo(String videoPath)
        {
            Uri videoUri = Uri.parse(videoPath);
            video.setVideoURI(videoUri);
            video.start();
        }
        public void setPath(String result)
        {
            String videoPath;
            String res = result.toLowerCase();
            if(res.equals("who are you"))
            {
                videoPath = "android.resource://" + getPackageName() + "/" + R.raw.receptionist;
                playVideo(videoPath);
            }
            else if(res.equals("can i meet with harry"))
            {
                videoPath = "android.resource://" + getPackageName() + "/" + R.raw.harry;
                playVideo(videoPath);
            }
            else if(res.equals("thanks"))
            {
                finish();
            }
            else
            {
                videoPath = "android.resource://" + getPackageName() + "/" + R.raw.understood;
                playVideo(videoPath);
            }
        }
}
