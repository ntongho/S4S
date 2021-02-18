package com.example.s4s;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;

public class ToSpeech extends AppCompatActivity {

    TextToSpeech textToSpeech;
    EditText write;
    Button help,speak,listen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_speech);
        write=findViewById(R.id.write);
        speak= findViewById(R.id.speak);
        listen=findViewById(R.id.listen);
        help=findViewById(R.id.help);

        textToSpeech= new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    int lang = textToSpeech.setLanguage(Locale.ENGLISH);
                    textToSpeech.setPitch(0.8f);
                }
            }

        });


        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak.setEnabled(true);
                String s= write.getText().toString();
                int speech=textToSpeech.speak(s,TextToSpeech.QUEUE_FLUSH,null);
//                Toast.makeText(ToSpeech.this , s, Toast.LENGTH_LONG).show();


            }
        });


        // to go back to speech to text activity
        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        // to show a help dialog box for text to speech
help.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        showDialog();
    }
});




    }
    @Override
    public void onDestroy(){
        // Don't forget to shutdown tts!
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }


    //function to show dialogbox
    public  void showDialog(){
        final Dialog dialog= new Dialog(ToSpeech.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.tospeech_helpdialog);

        Button button= dialog.findViewById(R.id.btndialog);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });


        dialog.show();


    }
}
