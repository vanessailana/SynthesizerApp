package com.csun_comp380_15884.group3.synthesizerapp;

/**
 * Created by marvin on 9/10/16.
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.csun_comp380_15884.group3.synthesizerapp.envelopeGraph.EnvelopeGraph;
import com.csun_comp380_15884.group3.synthesizerapp.keyboard.KeyboardView;
import com.csun_comp380_15884.group3.synthesizerapp.keyboard.ScrollStripView;
import com.csun_comp380_15884.group3.synthesizerapp.knob.KnobListener;
import com.csun_comp380_15884.group3.synthesizerapp.knob.KnobView;
import com.csun_comp380_15884.group3.synthesizerapp.midi.MidiListener;

import java.util.concurrent.Semaphore;


public class MainActivity extends AppCompatActivity
{
    SynthesizerAudioOutputThread thread;
    SynthesizerModel synthesizerModel;

    SeekBar frequencySlider;
    SeekBar masterSlider;

    MidiListener midiListener;

    KnobView [] knobViews;

    KeyboardView keyboardView;

    ScrollStripView scrollStripView;


    boolean [] noteOnArray;

    int [] midiData;

    static Semaphore mutex = new Semaphore(1);


    EnvelopeGraph eg;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //Our base Synthesizer model
        //TODO: DSP AND MIDI NOTE ON NOTE OFF
        synthesizerModel = new SynthesizerModel();


        thread = new SynthesizerAudioOutputThread();

        thread.setMutex(mutex);

        thread.setSynthesizerModel(synthesizerModel);


        keyboardView = (KeyboardView) findViewById(R.id.kv);

        scrollStripView = (ScrollStripView) findViewById(R.id.ssv);

        eg = (EnvelopeGraph) findViewById(R.id.eg);

        //KNOBS//

        knobViews = new KnobView[20];

        knobViews[0] = (KnobView) findViewById(R.id.mod_0_0);
        knobViews[1] = (KnobView) findViewById(R.id.mod_0_1);
        knobViews[2] = (KnobView) findViewById(R.id.mod_0_2);
        knobViews[3] = (KnobView) findViewById(R.id.mod_0_3);

        knobViews[4] = (KnobView) findViewById(R.id.mod_1_0);
        knobViews[5] = (KnobView) findViewById(R.id.mod_1_1);
        knobViews[6] = (KnobView) findViewById(R.id.mod_1_2);
        knobViews[7] = (KnobView) findViewById(R.id.mod_1_3);

        knobViews[8] = (KnobView) findViewById(R.id.mod_2_0);
        knobViews[9] = (KnobView) findViewById(R.id.mod_2_1);
        knobViews[10] = (KnobView) findViewById(R.id.mod_2_2);
        knobViews[11] = (KnobView) findViewById(R.id.mod_2_3);

        knobViews[12] = (KnobView) findViewById(R.id.mod_3_0);
        knobViews[13] = (KnobView) findViewById(R.id.mod_3_1);
        knobViews[14] = (KnobView) findViewById(R.id.mod_3_2);
        knobViews[15] = (KnobView) findViewById(R.id.mod_3_3);

        knobViews[16] = (KnobView) findViewById(R.id.out_0);
        knobViews[17] = (KnobView) findViewById(R.id.out_1);
        knobViews[18] = (KnobView) findViewById(R.id.out_2);
        knobViews[19] = (KnobView) findViewById(R.id.out_3);


        KnobListener knobListener = new KnobListener() {

            @Override
            public void onKnobChanged(KnobView knobView, double newValue) {
                int id = knobView.getId();
                synthesizerModel.setParameter(id,(float) newValue);
            }

        };

        for(int i = 0 ; i < 20; i++){
            knobViews[i].setKnobListener(knobListener);
        }

        knobViews[0].setValue(synthesizerModel.getParameter(R.id.mod_0_0));
        knobViews[1].setValue(synthesizerModel.getParameter(R.id.mod_0_1));
        knobViews[2].setValue(synthesizerModel.getParameter(R.id.mod_0_2));
        knobViews[3].setValue(synthesizerModel.getParameter(R.id.mod_0_3));

        knobViews[4].setValue(synthesizerModel.getParameter(R.id.mod_1_0));
        knobViews[5].setValue(synthesizerModel.getParameter(R.id.mod_1_1));
        knobViews[6].setValue(synthesizerModel.getParameter(R.id.mod_1_2));
        knobViews[7].setValue(synthesizerModel.getParameter(R.id.mod_1_3));

        knobViews[8].setValue(synthesizerModel.getParameter(R.id.mod_2_0));
        knobViews[9].setValue(synthesizerModel.getParameter(R.id.mod_2_1));
        knobViews[10].setValue(synthesizerModel.getParameter(R.id.mod_2_2));
        knobViews[11].setValue(synthesizerModel.getParameter(R.id.mod_2_3));

        knobViews[12].setValue(synthesizerModel.getParameter(R.id.mod_3_0));
        knobViews[13].setValue(synthesizerModel.getParameter(R.id.mod_3_1));
        knobViews[14].setValue(synthesizerModel.getParameter(R.id.mod_3_2));
        knobViews[15].setValue(synthesizerModel.getParameter(R.id.mod_3_3));

        knobViews[16].setValue(synthesizerModel.getParameter(R.id.out_0));
        knobViews[17].setValue(synthesizerModel.getParameter(R.id.out_1));
        knobViews[18].setValue(synthesizerModel.getParameter(R.id.out_2));
        knobViews[19].setValue(synthesizerModel.getParameter(R.id.out_3));





        ///SLIDERS///
        // point the slider to the GUI widget
        masterSlider = (SeekBar) findViewById(R.id.master);
        //How you would make gui changes when loading presets for sliders
        masterSlider.setProgress((int)(100*synthesizerModel.getParameter(R.id.master)));

        // create a listener for the slider bar;
        OnSeekBarChangeListener listener = new OnSeekBarChangeListener() {

            public void onStopTrackingTouch(SeekBar seekBar) {}

            public void onStartTrackingTouch(SeekBar seekBar) {}

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int id = seekBar.getId();
                double newValue = (double) progress/seekBar.getMax();
                synthesizerModel.setParameter(id,(float) newValue);
            }
        };



        // set the listener on the slider
        masterSlider.setOnSeekBarChangeListener(listener);

        keyboardView = (KeyboardView) findViewById(R.id.kv);


        noteOnArray = new boolean[128];

        for (int i = 0; i < 128; i++) {
            noteOnArray[i] = false;
        }

        midiData = new int[3];

        keyboardView.setMidiListener(new MidiListener() {

            @Override
            public void onNoteOff(int channel, int note, int velocity) {
                    midiData [0] = 0x80;
                    midiData [1] = note;
                    midiData [2] = 0;

                try {
                    mutex.acquire();
                    try {

                        synthesizerModel.processEvents(midiData, 0);
                    } finally {
                        mutex.release();
                    }
                }catch (InterruptedException e){

                }
            }

            @Override
            public void onNoteOn(int channel, int note, int velocity) {
                midiData[0] = 0x90;
                midiData[1] = note;
                midiData[2] = velocity;

                try {
                    mutex.acquire();
                    try {

                        synthesizerModel.processEvents(midiData, 0);
                    } finally {
                        mutex.release();
                    }
                }catch (InterruptedException e){

                }

            }

        });

        scrollStripView = (ScrollStripView) findViewById(R.id.ssv);

        scrollStripView.bindKeyboard(keyboardView);

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
