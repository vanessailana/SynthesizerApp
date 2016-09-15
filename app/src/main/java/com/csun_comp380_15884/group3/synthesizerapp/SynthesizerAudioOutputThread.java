package com.csun_comp380_15884.group3.synthesizerapp;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * Created by marvin on 9/10/16.
 */
public class SynthesizerAudioOutputThread extends Thread
{
    private SynthesizerModel synthesizerModel;
    private boolean isRunning;

    SynthesizerAudioOutputThread ()
    {
        isRunning = true;

    }
    public void setSynthesizerModel(SynthesizerModel synthesizerModel)
    {
        this.synthesizerModel = synthesizerModel;
    }
    public void run()
    {
        // set process priority
        setPriority(Thread.MAX_PRIORITY);
        // set the buffer size
        int buffsize = 2048;

        // create an audiotrack object


        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                synthesizerModel.getSampleRate(), AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT, buffsize,
                AudioTrack.MODE_STREAM);

        //Interleaved audio buffer mixes outL and outR buffers
        short outputs [] = new short[buffsize];

        // start audio
        audioTrack.play();

        // synthesis loop
        while (isRunning)
        {
            synthesizerModel.processReplacing(outputs,buffsize);
            audioTrack.write(outputs, 0, buffsize);
        }

        audioTrack.stop();
        audioTrack.release();
    }

    void stopSynthesizerAudioOutputThread()
    {
        isRunning = false;
    }

}
