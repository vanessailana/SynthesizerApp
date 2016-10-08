package com.csun_comp380_15884.group3.synthesizerapp.dsp;

/**
 * Created by marvin on 9/21/16.
 */

public class ADSR {



    private float mSustainLevel;
    private float mAttackLevel;
    private float mAttack;
    private float mDecay;
    private float mRelease;

    private float mAttackShape;
    private float mDecayShape;
    private float mReleaseShape;

    private float mSampleRate;

    private boolean mLoop;

    public ADSR()
    {
        setAttackRate(0.01f);
        setDecayRate(0.01f);
        setSustainLevel(1.0f);
        setReleaseRate(0.01f);

        setAttackShape(1.0f);
        setDecayShape(1.0f);
        setReleaseShape(1.0f);

        setAttackLevel(1.0f);
        setSampleRate(44100.0f);

        setLoop(false);
    }


    public float process(ADSRState adsrState)
    {
        switch (adsrState.mStage)
        {
            case kIdle:
                break;
            case kStageAttack:
                adsrState.mEnvValue = (float)(Math.pow(adsrState.mCounter*mAttack, mAttackShape)*mAttackLevel);
                if(adsrState.mEnvValue >= mAttackLevel)
                {
                    adsrState.mCounter = 0;
                    adsrState.mEnvValue = mAttackLevel;
                    adsrState.mStage = ADSRState.ADSRStage.kStageDecay;
                }
                break;
            case kStageDecay:
                if(mAttackLevel >= mSustainLevel)
                {
                    adsrState.mEnvValue = mAttackLevel
                            - (float) Math.pow(adsrState.mCounter * mDecay, mDecayShape)
                            * (mAttackLevel-mSustainLevel);
                    if(adsrState.mEnvValue <= mSustainLevel)
                    {
                        adsrState.mCounter = 0;
                        adsrState.mEnvValue = mSustainLevel;
                        adsrState.mStage = ADSRState.ADSRStage.kStageSustain;
                    }
                }
                else if (mAttackLevel <= mSustainLevel)
                {
                    adsrState.mEnvValue = mAttackLevel
                            + (float) Math.pow(adsrState.mCounter * mDecay, mDecayShape)
                            * (mSustainLevel-mAttackLevel);
                    if(adsrState.mEnvValue >= mSustainLevel)
                    {
                        adsrState.mCounter = 0;
                        adsrState.mEnvValue = mSustainLevel;
                        adsrState.mStage = ADSRState.ADSRStage.kStageSustain;
                    }
                }

            case kStageSustain:
                adsrState.mCounter = 0;
                adsrState.mEnvValue = mSustainLevel;
                if(mLoop)
                {
                    adsrState.mStage = ADSRState.ADSRStage.kStageAttack;
                }
                break;
            case kStageRelease:
                adsrState.mEnvValue  = mSustainLevel-(float)Math.pow(adsrState.mCounter*mRelease, mReleaseShape)*mSustainLevel;
                if (adsrState.mEnvValue  < 0.00001f) {
                    adsrState.mCounter = 0;
                    adsrState.mEnvValue  = 0.0f;
                    adsrState.mStage = ADSRState.ADSRStage.kIdle;
                }
                break;
            default:
                break;


        }
        adsrState.mCounter = adsrState.mCounter+1;
        return adsrState.mEnvValue * adsrState.mLevel;
    }

    public void setAttackRate(float attackRate)
    {
        if(attackRate <= 0.01f)
        {
            attackRate = 0.01f;
        }
        mAttack = (1.0f/ (mSampleRate*attackRate));
    }


    public void setDecayRate(float decayRate)
    {
        if(decayRate <= 0.0f)
        {
            decayRate = 0.01f;
        }

        mDecay = (1.0f / (mSampleRate * decayRate));

    }

    public void setReleaseRate(float releaseRate)
    {

        if(releaseRate <= 0.0f)
        {
            releaseRate = 0.01f;
        }

        mRelease = (1.0f / (mSampleRate * releaseRate));

    }


    public void setSustainLevel(float sustainLevel)
    {
        if(sustainLevel > 1.0f)
        {
           sustainLevel = 1.0f;
        }
        else if(sustainLevel <= .000001f)
        {
            sustainLevel = .000001f;
        }

        mSustainLevel = sustainLevel;

    }


    public void setAttackLevel(float attackLevel)
    {
        if(attackLevel > 1.0f)
        {
            attackLevel = 1.0f;
        }
        else if(attackLevel <= .000001f)
        {
            attackLevel = .000001f;
        }

        mAttackLevel = attackLevel;

    }

    public void setLoop(boolean loop)
    {
        mLoop = loop;
    }

    public void setAttackShape(float attackShape)
    {
        if(attackShape <= .01f)
        {
            attackShape = .01f;
        }
        else if(attackShape >= 100.f)
        {
            attackShape = 100.f;
        }
        mAttackShape = attackShape;
    }

    public void setDecayShape(float decayShape)
    {
        if(decayShape <= .01f)
        {
            decayShape = .01f;
        }
        else if(decayShape >= 100.f)
        {
            decayShape = 100.f;
        }
        mDecayShape = decayShape;
    }

    public void setReleaseShape(float releaseShape)
    {
        if(releaseShape <= .01f)
        {
            releaseShape = .01f;
        }
        else if(releaseShape >= 100.f)
        {
            releaseShape = 100.f;
        }
        mReleaseShape = releaseShape;
    }



    public void setSampleRate(float sampleRate)
    {
        mSampleRate = sampleRate;
    }
}
