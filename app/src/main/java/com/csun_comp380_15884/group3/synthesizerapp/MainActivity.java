package com.csun_comp380_15884.group3.synthesizerapp;

/**
 * Created by marvin on 9/10/16.
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;


public class MainActivity extends AppCompatActivity
{
    SynthesizerAudioOutputThread thread;
    SynthesizerModel synthesizerModel;

    SeekBar fSlider;
    SeekBar masterVolumeSlider;
    float sliderval;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Our base Synthesizer model
        //TODO: DSP AND MIDI NOTE ON NOTE OFF
        synthesizerModel = new SynthesizerModel();


        thread = new SynthesizerAudioOutputThread();

        thread.setSynthesizerModel(synthesizerModel);


        // point the slider to the GUI widget
        fSlider = (SeekBar) findViewById(R.id.frequency);

        masterVolumeSlider = (SeekBar) findViewById(R.id.master_volume);

        // create a listener for the slider bar;
        OnSeekBarChangeListener listener = new OnSeekBarChangeListener()
        {

            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }

            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                int id = seekBar.getId();
                switch (id)
                {
                    case R.id.frequency:
                        sliderval = (float)progress/seekBar.getMax();
                        synthesizerModel.setParameter(id, sliderval);
                        break;
                    case R.id.master_volume:
                        sliderval = (float)progress/seekBar.getMax();
                        synthesizerModel.setParameter(id, sliderval);
                    default:
                        break;

                }

            }
        };

        // set the listener on the slider
        fSlider.setOnSeekBarChangeListener(listener);
        masterVolumeSlider.setOnSeekBarChangeListener(listener);

        thread.start();
    }


    public void onDestroy() {
        super.onDestroy();
        thread.stopSynthesizerAudioOutputThread();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread = null;
    }

}
