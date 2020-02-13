package com.dev.meuaplicativo;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Animacoes3Activity extends AppCompatActivity {

    FrameLayout frame;
    ImageView imageView;
    private boolean reverterEscala;
    private boolean reverterAlpha;
    private Animacoes animacoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animacoes3);

        frame = findViewById(R.id.frame);
        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                executarAnimacao();
            }
        });
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.HONEYCOMB_MR1){
            animacoes = new Animacao3();
        }else if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR1){
            animacoes = new AnimacaoPlus();
        }else{
            Toast.makeText(this, "Animações não vão funcionar", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    private void executarAnimacao(){
        int numeroAnimacoes = 6;
        int animacao = (int)(Math.random() * numeroAnimacoes);
        switch (animacao){
            case 0:
                animacoes.girar();
                break;
            case 1:
                animacoes.girarEmX();
                break;
            case 2:
                animacoes.girarEmY();
                break;
            case 3:
                animacoes.escala();
                break;
            case 4:
                animacoes.opacidade();
                break;
            case 5:
                animacoes.movimentar();
                break;
        }
    }

    private AnimatorListenerAdapter listener = new AnimatorListenerAdapter() {

        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            imageView.setEnabled(false);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            imageView.setEnabled(true);
        }
    };

    interface Animacoes{
        void girar();
        void girarEmX();
        void girarEmY();
        void escala();
        void opacidade();
        void movimentar();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    class Animacao3 implements Animacoes{

        @Override
        public void girar() {
            ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "rotation", 0, 360);
            animator.addListener(listener);
            animator.start();
        }

        @Override
        public void girarEmX() {
            ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "rotationX", 0, 360);
            animator.addListener(listener);
            animator.start();
        }

        @Override
        public void girarEmY() {
            ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "rotationY", 0 , 360);
            animator.addListener(listener);
            animator.start();
        }

        @Override
        public void escala() {
            float novoX = reverterEscala ? 1.0f : 1.5f;
            float novoY = reverterEscala ? 1.0f : 1.5f;

            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(imageView, "scaleX", novoX),
                    ObjectAnimator.ofFloat(imageView, "scaleY", novoY));
            set.addListener(listener);
            reverterEscala = !reverterEscala;
        }

        @Override
        public void opacidade() {
            ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "alpha", reverterAlpha ? 1.0f : 0.5f);
            animator.addListener(listener);
            animator.start();
            reverterAlpha = !reverterAlpha;
        }

        @Override
        public void movimentar() {
            float novoX = (float)(Math.random() * (frame.getWidth() - imageView.getWidth()));
            float novoY = (float)(Math.random() * (frame.getHeight() - imageView.getHeight()));

            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(imageView, "x", novoY),
                    ObjectAnimator.ofFloat(imageView, "y", novoY));
            set.addListener(listener);
            set.start();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    class AnimacaoPlus implements Animacoes{

        private ViewPropertyAnimator getAnimator(){
            return imageView.animate().setListener(listener);
        }

        @Override
        public void girar() {
            getAnimator().rotationXBy(360);
        }

        @Override
        public void girarEmX() {
            getAnimator().rotationXBy(360);
        }

        @Override
        public void girarEmY() {
            getAnimator().rotationYBy(360);
        }

        @Override
        public void escala() {
            float novoX = reverterEscala ? 1.0f : 1.5f;
            float novoY = reverterEscala ? 1.0f : 1.5f;

            getAnimator().scaleX(novoX).scaleY(novoY);
            reverterEscala = !reverterEscala;
        }

        @Override
        public void opacidade() {
            getAnimator().alpha(reverterAlpha ? 1.0f : 0.5f);
            reverterAlpha = !reverterAlpha;
        }

        @Override
        public void movimentar() {
            float novoX = (float)(Math.random() * (frame.getWidth() - imageView.getWidth()));
            float novoY = (float)(Math.random() * (frame.getHeight() - imageView.getHeight()));
            getAnimator().x(novoX).y(novoY);
        }
    }
}