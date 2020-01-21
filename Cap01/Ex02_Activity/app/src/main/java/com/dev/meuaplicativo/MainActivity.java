package com.dev.meuaplicativo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Log.i("Log::Teste", "Tela1::onCreate");
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(this);

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(this);

        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(this);

        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button:
                String texto = editText.getText().toString();
                Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
                break;
            case R.id.button2:
                Intent it = new Intent(this, Tela2Activity.class);
                it.putExtra("nome", "Ivan Simplício");
                it.putExtra("idade", 22);
                startActivity(it);
                break;
            case R.id.button3:
                Cliente cliente = new Cliente(1, "Ivan Simplício");
                Intent intent = new Intent(this, Tela2Activity.class);
                intent.putExtra("cliente", cliente);
                startActivity(intent);
                break;
            case R.id.button4:
                Pessoa pessoa = new Pessoa(22, "Ivan Simplício");
                Intent its = new Intent(this, Tela2Activity.class);
                its.putExtra("pessoa", pessoa);
                startActivity(its);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Log::Teste", "Tela1::onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Log::Teste", "Tela1::onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("Log::Teste", "Tela1::onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Log::Teste", "Tela1::onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Log::Teste", "Tela1::onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Log::Teste", "Tela1::onDestroy");
    }
}