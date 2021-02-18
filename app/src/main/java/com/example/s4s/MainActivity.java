package com.example.s4s;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.logging.Logger;



public class MainActivity extends AppCompatActivity {
//        implements SaveDialog.saveDialogListener {

    Button speak, save,help;
    private static final int REQUEST_CODE = 100;
    private TextView textOutput, tap, taptext, Dialogfilename;
    private String nameOffile, content;
    private int STORAGE_PERMISSION_CODE = 23;
    String notesSavePathInDevice= null;
    String rootpath;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        speak = findViewById(R.id.speak);
        textOutput = findViewById(R.id.txtSpeechInput);
        tap = findViewById(R.id.tap_to_speak);
        taptext = findViewById(R.id.tap);
        save = findViewById(R.id.save);
        help= findViewById(R.id.help);

//        isExternalStorageWritable();
//        isExternalStorageReadable();
//         rootpath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/S3S/";
//        File root = new File(rootpath);
//        if (!root.exists()) {
//            root.mkdirs();
//        }




        //to open dialog for user to input file name to save

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();


            }
        });


        //the end


        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ToSpeech.class);
                startActivity(intent);
            }
        });


        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHelpDialog();
            }
        });
    }


    // method to display help
    public  void showHelpDialog(){
        final Dialog dialog= new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.totext_helpdialog);

        Button button= dialog.findViewById(R.id.btndialog);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });


        dialog.show();


    }




    //This method is called when the button is pressed//

    public void onClick(View v)

//Create an Intent with “RecognizerIntent.ACTION_RECOGNIZE_SPEECH” action//

    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        try {

//Start the Activity and wait for the response//

            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException a) {

        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        builder.setTitle(R.string.app_name);
//        builder.setIcon(R.mipmap.ic_launcher);
//        builder.setMessage("If You Enjoy The App Please Rate us?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        Intent play =
//                                new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=kd.travellingtips"));
//                        startActivity(play);
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        finish();
//                    }
//                });
//        AlertDialog alert = builder.create();
//        alert.show();
        finish();
        startActivity(getIntent());

    }


    @Override

//Handle the results i.e show result on the screen with other button hidden//

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    textOutput.setText(result.get(0));
                    tap.setVisibility(View.INVISIBLE);
                    taptext.setVisibility(View.INVISIBLE);

                }
                break;
            }


        }
    }

    //opens dialog for user to save text
    public void openDialog() {



        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.savefile_dialog);

        final EditText editText = dialog.findViewById(R.id.filename);

        Button button = dialog.findViewById(R.id.btnsave);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                dialog.cancel();

//                String name = editText.getText().toString();
                if (!editText.getText().toString().isEmpty()) {

                    if (!textOutput.getText().toString().isEmpty()) {
                        String state = Environment.getExternalStorageState();
                        if (Environment.MEDIA_MOUNTED.equals(state)) {
                            if (Build.VERSION.SDK_INT >= 23) {
                                if (checkPermission()) {
                                    File sdcard = Environment.getExternalStorageDirectory();
                                    File dir = new File(sdcard.getAbsolutePath() + "/s4s/");
                                    dir.mkdir();
//                                    File file = new File(dir, "sample.txt");
                                    File file = new File(dir, editText.getText().toString()+".txt");
                                    FileOutputStream os = null;
                                    try {
                                        os = new FileOutputStream(file);
                                        os.write(textOutput.getText().toString().getBytes());
//                                        Toast.makeText(getApplicationContext(),"file saved to"+getFilesDir()+"/"+editText.getText().toString(), Toast.LENGTH_LONG).show();
                                        Toast.makeText(getApplicationContext(),"File Saved", Toast.LENGTH_LONG).show();
                                        dialog.cancel();
                                       editText.setText("");
                                        os.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    requestPermission(); // Code for permission
                                }
                            } else {
                                File sdcard = Environment.getExternalStorageDirectory();
                                File dir = new File(sdcard.getAbsolutePath() + "/s4s/");
                                dir.mkdir();
//                                File file = new File(dir, "sample.txt");
                                File file = new File(dir, editText.getText().toString()+".txt");

                                FileOutputStream os = null;
                                try {
                                    os = new FileOutputStream(file);
                                    os.write(textOutput.getText().toString().getBytes());
//                                    Toast.makeText(getApplicationContext(),"file saved to"+getFilesDir()+"/"+editText.getText().toString(), Toast.LENGTH_LONG).show();
                                    Toast.makeText(getApplicationContext(),"File Saved", Toast.LENGTH_LONG).show();
                                    dialog.cancel();
                                    editText.setText("");
                                    os.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Filename empty", Toast.LENGTH_LONG).show();
                }
            }

        });


        dialog.show();
    }






    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Write External Storage permission allows us to create files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("value", "Permission Granted, Now you can use local drive .");
            } else {
                Log.e("value", "Permission Denied, You cannot use local drive .");
            }
            break;




        }
    }
}













