package com.csun_comp380_15884.group3.synthesizerapp;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.util.concurrent.Semaphore;

/**
 * Created by marvin on 9/10/16.
 */
public class SynthesizerAudioOutputThread extends Thread
{
    private SynthesizerModel synthesizerModel;
    private boolean isRunning;

    private int timeStamp;

    private int [] mMidiData;

    Semaphore mMutex;



    SynthesizerAudioOutputThread ()
    {
        mMidiData = new int[3];
        isRunning = true;
        timeStamp = 0;

    }
    public void setSynthesizerModel(SynthesizerModel synthesizerModel)
    {
        this.synthesizerModel = synthesizerModel;
    }

    public void setMutex(Semaphore mutex)
    {
        mMutex = mutex;
    }
    public void run()
    {
        // set process priority
        setPriority(Thread.MAX_PRIORITY);
        // set the buffer size
        int buffsize = AudioTrack.getMinBufferSize(synthesizerModel.getSampleRate(),AudioFormat.CHANNEL_OUT_STEREO,AudioFormat.ENCODING_PCM_16BIT)/8;

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

            try{
                mMutex.acquire();
                try{

                    synthesizerModel.processReplacing(outputs,buffsize);

                }finally {
                    mMutex.release();
                }
            }catch (InterruptedException e)
            {

            }

            audioTrack.write(outputs, 0, buffsize);
        }

        audioTrack.stop();
        audioTrack.release();
    }

    public int getTimeStamp()
    {
        return timeStamp;
    }

    void stopSynthesizerAudioOutputThread()
    {
        isRunning = false;
    }

}
