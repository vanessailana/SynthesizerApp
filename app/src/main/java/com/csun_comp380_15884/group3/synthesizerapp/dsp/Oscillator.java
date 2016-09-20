package com.csun_comp380_15884.group3.synthesizerapp.dsp;


/**
 * Created by marvin on 9/10/16.
 */

public class Oscillator {

    //Our lookup table variables sine Math.sin is processor heavy
    //We have to interpolate between predetermined values
    float [] mLUT;
    public float [] mInput;
    public float [] mInputAmp;


    int mLUTSize;
    int mLUTSizeM;
    float mLUTSizeF;

    public Oscillator()
    {

        mLUTSize = 4096;
        mLUTSizeM = mLUTSize -1;
        mLUTSizeF = (float) mLUTSize;
        mLUT = new float[mLUTSize];

        for(int i = 0; i < mLUTSize ; i++)
        {
            mLUT[i] = (float)Math.sin(i/mLUTSizeF*2.0f*Math.PI);
        }

        mInput = new float [4];
        mInputAmp = new float[4];

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

        if(oscillatorState.mPhase>1.0f)
        {
            oscillatorState.mPhase -= 1.0f;
        }

        float modulationTotal =
                mInput[0]*mInputAmp[0] + mInput[1]*mInputAmp[1] +
                        mInput[2]*mInputAmp[2]+ mInput[3]*mInputAmp[3];

        float output = lerp((oscillatorState.mPhase + modulationTotal) * mLUTSizeF, mLUT, mLUTSizeM);

        oscillatorState.mPhase += oscillatorState.mPhaseIncrement;

        return output;
    }

}
