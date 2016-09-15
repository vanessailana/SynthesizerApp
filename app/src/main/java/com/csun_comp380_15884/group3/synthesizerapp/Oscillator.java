package com.csun_comp380_15884.group3.synthesizerapp;


/**
 * Created by marvin on 9/10/16.
 */

public class Oscillator {

    //Our lookup table variables sine Math.sin is processor heavy
    //We have to interpolate between predetermined values
    float mLUT [];
    int mLUTSize;
    int mLUTSizeM;
    float mLUTSizeF;

    Oscillator()
    {

        mLUTSize = 4096;
        mLUTSizeM = mLUTSize -1;
        mLUTSizeF = (float) mLUTSize;
        mLUT = new float[mLUTSize];

        for(int i = 0; i < mLUTSize ; i++)
        {
            mLUT[i] = (float)Math.sin(i/mLUTSizeF*2.0f*Math.PI);
        }

    }

    private float lerp(float phase, float buffer [], int mask)
    {
        int intPart = (int) phase;
        float fracPart = phase-intPart;
        float a = buffer[intPart & mask];
        float b = buffer[(intPart+1) & mask];
        return a + (b - a) * fracPart;
    }

    public float process(OscillatorState oscillatorState)
    {


        float output = lerp(oscillatorState.mPhase * mLUTSizeF, mLUT, mLUTSizeM);

        oscillatorState.mPhase += oscillatorState.mPhaseIncrement;

        return output;
    }

}
