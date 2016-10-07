package com.csun_comp380_15884.group3.synthesizerapp.dsp;

/**
 * Created by marvin on 9/21/16.
 */



public class ADSRState {

    public enum ADSRStage
    {
        kIdle,
        kStageAttack,
        kStageDecay,
        kStageSustain,
        kStageRelease,
    };

    public float mEnvValue;
    public float mLevel;
    public float mCounter;
    public ADSRStage mStage;


    public ADSRState()
    {
        mEnvValue = 0.f;
        mLevel = 0.f;
        mCounter = 0.f;
        mStage = ADSRStage.kIdle;
    }


}
