package com.csun_comp380_15884.group3.synthesizerapp.dsp;

/**
 * Created by marvin on 9/21/16.
 */

public class VoiceState {

    public OscillatorState [] mOS;
    public ADSRState [] mAS;

    public int mKey;
    public VoiceState()
    {
        mOS = new OscillatorState[4];
        mAS = new ADSRState[4];
        for(int i = 0 ; i < 4 ; i++)
        {
            mOS[i] = new OscillatorState();
            mAS[i] = new ADSRState();
        }
        mKey = -1;
    }
}
