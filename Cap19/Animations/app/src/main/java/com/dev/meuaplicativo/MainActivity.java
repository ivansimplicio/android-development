package com.dev.meuaplicativo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener,
        Animation.AnimationListener {

    private static final int DURACAO_ANIMACAO = 1000;

    private Spinner spinnerAnimations;
    private Spinner spinnerInterpolators;
    private Button buttonPlay;
    private Button buttonOpenAnim;
    private Button buttonOpenCoruja;
    private ImageView imageView;
    private Animation[] animations;
    private Interpolator[] interpolators;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerAnimations = findViewById(R.id.spinnerAnimations);
        spinnerInterpolators = findViewById(R.id.spinnerInterpolators);
        imageView = findViewById(R.id.imageView);
        buttonPlay = findViewById(R.id.buttonPlay);
        buttonOpenAnim = findViewById(R.id.buttonOpenAnim);
        buttonOpenCoruja = findViewById(R.id.buttonOpenCoruja);
        buttonPlay.setOnClickListener(this);
        buttonOpenAnim.setOnClickListener(this);
        buttonOpenCoruja.setOnClickListener(this);

        initInterpolators();
        initAnimations();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonPlay:
                executarAnimacao();
                break;
            case R.id.buttonOpenAnim:
                Intent intent = new Intent(this, Animacoes3Activity.class);
                Bundle animacao = ActivityOptions.makeCustomAnimation(this,
                        R.anim.slide_in_left, R.anim.slide_out_left).toBundle();
                ActivityCompat.startActivity(this, intent, animacao);
                break;
            case R.id.buttonOpenCoruja:
                Intent it = new Intent(this, SpriteActivity.class);
                View view = imageView;
                Bundle anim = ActivityOptions.makeScaleUpAnimation(
                        view, 0, 0, view.getWidth(), view.getHeight()).toBundle();
                ActivityCompat.startActivity(this, it, anim);
                break;
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        buttonPlay.setEnabled(true);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    private void initAnimations(){

        animations = new Animation[spinnerAnimations.getCount()];
        animations[0] = AnimationUtils.loadAnimation(this, R.anim.transparencia);
        animations[1] = AnimationUtils.loadAnimation(this, R.anim.rotacao);
        animations[2] = AnimationUtils.loadAnimation(this, R.anim.escala);
        animations[3] = AnimationUtils.loadAnimation(this, R.anim.translacao);
        animations[4] = AnimationUtils.loadAnimation(this, R.anim.tudo_junto);

        /*  CRIAÇÃO DAS ANIMAÇÕES PROGRAMATICAMENTE
        animations[0] = new AlphaAnimation(1, 0);
        animations[1] = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animations[2] = new ScaleAnimation(1, 3, 1, 3,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animations[3] = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.ABSOLUTE, 200,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.ABSOLUTE, 300);

        AnimationSet set = new AnimationSet(true);
        for(int i = 0; i < animations.length-2; i++){
            set.addAnimation(animations[i]);
        }
        animations[4] = set;*/
    }

    private void initInterpolators(){
        interpolators = new Interpolator[spinnerInterpolators.getCount()];
        interpolators[0] = new AccelerateDecelerateInterpolator();
        interpolators[1] = new AccelerateInterpolator();
        interpolators[2] = new AnticipateInterpolator();
        interpolators[3] = new AnticipateOvershootInterpolator();
        interpolators[4] = new BounceInterpolator();
        interpolators[5] = new CycleInterpolator(2);
        interpolators[6] = new DecelerateInterpolator();
        interpolators[7] = new LinearInterpolator();
        interpolators[8] = new OvershootInterpolator();
    }

    private void executarAnimacao(){
        Interpolator interpolator = interpolators[spinnerInterpolators.getSelectedItemPosition()];
        Animation animation = animations[spinnerAnimations.getSelectedItemPosition()];
        animation.setInterpolator(interpolator);
        animation.setAnimationListener(this);
        animation.setDuration(DURACAO_ANIMACAO);
        animation.setRepeatMode(Animation.REVERSE);
        //animation.setFillAfter(true);
        animation.setRepeatCount(1);
        imageView.requestLayout();
        imageView.setAnimation(animation);
        animation.start();
        buttonPlay.setEnabled(false);
    }
}