package com.botree.audiodemo;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.File;

public class StartActivity extends AppCompatActivity {

    Context mContext;

    File file1, file2, file3, drop;
    FFmpeg ffmpeg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        final File myFolder=new File(Environment.getExternalStorageDirectory()+"/myFolder");
        Log.v("###","myFolder "+myFolder.getPath());

        try{
            file1 = new File(Environment.getExternalStorageDirectory().getPath()+"/post.mp3");
            file2 = new File(Environment.getExternalStorageDirectory().getPath()+"/pre.mp3");
            file3 = new File(myFolder,"/mmm.mp3");

//            Log.d("A Exists", ""+file1.exists());
//            Log.d("B Exists", ""+file2.exists());
        }catch (Exception e){
            e.printStackTrace();
        }

        mContext = this;

        initialization();

//[0:0][1:0][2:0][3:0]

//        String[] cmds = {"-version", "--enable-libmp3lame"};


          String[] cmds = {"-i",file2.getPath(),"-i",file1.getPath(),"-filter_complex","amix=inputs=2:duration=first",file3.getPath()}; // Merge //input =  No of files // First file should be recored file
/*
        String[] cmds = {"-i",file1.getPath(),"-i",file2.getPath(),"-filter_complex","[0:0][1:0]concat=n=2:v=0:a=1[out]","-map","[out]",file3.getPath()}; // Concat //n= No of file
*/

        try {
            ffmpeg.execute(cmds, new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {
                    Log.d("onStart()","onStart()");
                }

                @Override
                public void onProgress(String message) {
                    Log.d("onProgress()","onProgress() "+message);
                }

                @Override
                public void onFailure(String message) {
                    Log.d("onFailure()","onFailure() "+message);
                }

                @Override
                public void onSuccess(String message) {
                    Log.d("onSuccess()","onSuccess() "+ message);
                }

                @Override
                public void onFinish() {
                    Log.d("onFinish()","onFinish()");
                    File file11=new File(myFolder,"/mmm.mp3");
                    File file22=new File(new File(Environment.getExternalStorageDirectory(),"/myFolder"),"/theme.mp3");
                    file11.renameTo(file22);
                    Toast.makeText(StartActivity.this, "Completed", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (FFmpegCommandAlreadyRunningException e){
            e.printStackTrace();
        }

    }

    void initialization(){
        ffmpeg = FFmpeg.getInstance(mContext);

        try {

            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {

                @Override
                public void onStart() {
                    Log.d("onStart()","loadBinaryonStart()");
                }

                @Override
                public void onFailure() {
                    Log.d("onFailure()","loadBinaryonFailure()");
                }

                @Override
                public void onSuccess() {
                    Log.d("onSuccess()","loadBinaryonSuccess()");
                }

                @Override
                public void onFinish() {
                    Log.d("onFinish()","loadBinaryonFinish()");
                }
            });

        } catch (FFmpegNotSupportedException e) {
            // Handle if FFmpeg is not supported by device
            e.printStackTrace();
        }
    }

}
