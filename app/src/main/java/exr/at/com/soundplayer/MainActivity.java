package exr.at.com.soundplayer;

import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.view.MotionEvent;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity {

    private MediaPlayer mMedia;
    private Handler handler = new Handler();
    private SeekBar seekBar1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // call method ValumnControls()
        ValumnControls();


        if(mMedia != null){
            mMedia.release();
        }

        final TextView txtView = (TextView)findViewById(R.id.textView1);
        txtView.setText("Source : music.mp3");

        /* Resource in R.
         * mMedia = MediaPlayer.create(this, R.raw.music);
         * mMedia.start();
         */

        /*
         * from DataSource
         * mMedia = new MediaPlayer();
         * mMedia.setDataSource("http://www.thaicreate.com/music/mymusic.mp3");
         * mMedia.start();
         *
         */
        mMedia = MediaPlayer.create(this, R.raw.music);

        // seekBar1 Control media
        seekBar1 = (SeekBar)findViewById(R.id.seekBar1);
        seekBar1.setMax(mMedia.getDuration());
        seekBar1.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                UpdateseekChange(v);
                return false;
            }
        });




        final Button btn1 = (Button) findViewById(R.id.button1); // Start
        final Button btn2 = (Button) findViewById(R.id.button2); // Pause
        final Button btn3 = (Button) findViewById(R.id.button3); // Stop


        // Start
        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtView.setText("Playing : music.mp3....");
                mMedia.start();
                startPlayProgressUpdater();
                btn1.setEnabled(false);
                btn2.setEnabled(true);
                btn3.setEnabled(true);
            }
        });

        // Pause
        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtView.setText("Pause : music.mp3");
                mMedia.pause();
                btn1.setEnabled(true);
                btn2.setEnabled(false);
                btn3.setEnabled(false);
            }
        });

        // Stop
        btn3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtView.setText("Stop Play : music.mp3");
                mMedia.stop();
                btn1.setEnabled(true);
                btn2.setEnabled(false);
                btn3.setEnabled(false);
                try {
                    mMedia.prepare();
                    mMedia.seekTo(0);
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });

    }

    private void UpdateseekChange(View v){
        if(mMedia.isPlaying()){
            SeekBar sb = (SeekBar)v;
            mMedia.seekTo(sb.getProgress());
        }
    }

    public void startPlayProgressUpdater() {
        seekBar1.setProgress(mMedia.getCurrentPosition());

        if (mMedia.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    startPlayProgressUpdater();
                }
            };
            handler.postDelayed(notification,1000);
        }
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if(mMedia != null){
            mMedia.release();
        }
    }


    private void ValumnControls()
    {
        try
        {
            SeekBar seekBar2 = (SeekBar)findViewById(R.id.seekBar2);
            final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            seekBar2.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            seekBar2.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));


            seekBar2.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
            {
                public void onStopTrackingTouch(SeekBar arg0)
                {
                }

                public void onStartTrackingTouch(SeekBar arg0)
                {
                }

                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2)
                {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}