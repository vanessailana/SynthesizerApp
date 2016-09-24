package com.csun_comp380_15884.group3.synthesizerapp.dsp;

/**
 * Created by marvin on 9/21/16.
 */

public class ADSR {



    private float mSustainLevel;
    private float mAttack;
    private float mDecay;
    private float mRelease;
    private float mSampleRate;


    public ADSR()
    {
        setAttackRate(0.01f);
        setDecayRate(0.01f);
        setSustainLevel(1.0f);
        setReleaseRate(0.01f);
    }


    public float process(ADSRState adsrState)
    {
        switch (adsrState.mStage)
        {
            case kIdle:
                break;
            case kStageAttack:
                adsrState.mEnvValue += mAttack;
                if(adsrState.mEnvValue >= 1.0f)
                {
                    adsrState.mEnvValue = 1.0f;

                    adsrState.mStage = ADSRState.ADSRStage.kStageDecay;
                }
                break;
            case kStageDecay:
                adsrState.mEnvValue -= mDecay;
                if(adsrState.mEnvValue <= mSustainLevel)
                {
                    adsrState.mEnvValue = mSustainLevel;
                    adsrState.mStage = ADSRState.ADSRStage.kStageSustain;
                }
            case kStageSustain:
                adsrState.mEnvValue = mSustainLevel;
                break;
            case kStageRelease:
                adsrState.mEnvValue  -= mRelease;
                if (adsrState.mEnvValue  < 0.00001f) {
                    adsrState.mEnvValue  = 0.0f;
                    adsrState.mStage = ADSRState.ADSRStage.kIdle;
                }
                break;
            default:
                break;
        }

        return adsrState.mEnvValue * adsrState.mLevel;
    }

    public void setAttackRate(float attackRate) {
        if(attackRate <= 0.0f)
        {
            attackRate = 0.01f;
        }
        mAttack = (1.0f/(mSampleRate*attackRate));
    }


    public void setDecayRate(float decayRate) {
        if(decayRate <= 0.0f)
        {
            decayRate = 0.01f;
        }
        mDecay = (1.0f/(mSampleRate*decayRate));
    }

    public void setSustainLevel(float sustainLevel) {
        mSustainLevel = sustainLevel;
    }


    public void setReleaseRate(float releaseRate) {

        if(releaseRate <= 0.0f)
        {
            releaseRate = 0.01f;
        }

        mRelease = (1.0f/(mSampleRate*releaseRate));
    }

    public void setSampleRate(float sampleRate) {
        mSampleRate = sampleRate;
    }
}
