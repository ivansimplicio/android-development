package com.dev.meuaplicativo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SpriteActivity extends AppCompatActivity {

    private AnimationDrawable spriteAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sprite);

        ImageView imageView = findViewById(R.id.imageView);
        imageView.setBackgroundResource(R.drawable.sprite_coruja);
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(spriteAnimation.isRunning()){
                    spriteAnimation.stop();
                }else{
                    spriteAnimation.start();
                }
            }
        });
        spriteAnimation = (AnimationDrawable)imageView.getBackground();
    }
}