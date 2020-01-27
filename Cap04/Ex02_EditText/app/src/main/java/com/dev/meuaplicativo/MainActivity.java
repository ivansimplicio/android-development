package com.dev.meuaplicativo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    EditText editTextNome;
    EditText editTextEmail;
    EditText editTextSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextNome = findViewById(R.id.editTextNome);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSenha = findViewById(R.id.editTextSenha);
        editTextSenha.setOnEditorActionListener(this);

        final EditText editTextCep = findViewById(R.id.editTextCep);

        editTextCep.addTextChangedListener(new TextWatcher() {

            boolean isUpdating;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int after) {
                //Quando o texto é alterado o onTextChanged é chamado
                //Essa flag evita a chamada infinita desse método
                if(isUpdating){
                    isUpdating = false;
                    return;
                }
                //Ao apagar o texto, a máscara é removida,
                //então o posicionamento do cursor precisa
                //saber se o texto atual tinha ou não, uma máscara.
                boolean hasMask = s.toString().indexOf('.') > -1 || s.toString().indexOf('-') > -1;

                //Remove o '.' e '-' da String
                String str = s.toString().replaceAll("[.]", "").replaceAll("[-]", "");

                //Os parâmetros before e after dizem o tamanho
                //anterior e atual da String, se after > before é
                //porque está digitando, caso contrário, está apagando.
                if(after > before){
                    //Se tem mais de 5 caracteres
                    //coloca o '.' e o '-'
                    if(str.length() > 5){
                        str = str.substring(0,2) + '.' +
                                str.substring(2,5) + '-' +
                                str.substring(5);
                    //Se tem mais que 2, coloca só o ponto
                    }else if(str.length() > 2){
                        str = str.substring(0,2) + '.' +
                                str.substring(2);
                    }

                    //Seta a flag para evitar chamada infinita
                    isUpdating = true;
                    //Seta o novo texto
                    editTextCep.setText(str);
                    //Seta a posição do cursor
                    editTextCep.setSelection(editTextCep.getText().length());
                }else{
                    isUpdating = true;
                    editTextCep.setText(str);
                    //Se estiver apagando posiciona o cursor
                    //no local correto. Isso trata a deleção dos
                    //caracteres da máscara.
                    editTextCep.setSelection(Math.max(0, Math.min(hasMask ? start - before : start, str.length())));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if(v == editTextSenha && EditorInfo.IME_ACTION_DONE == actionId){
            String nome = editTextNome.getText().toString();
            String email = editTextEmail.getText().toString();
            String senha = editTextSenha.getText().toString();
            boolean ok = true;

            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                editTextEmail.setError(getString(R.string.msg_erro_email));
                ok = false;
            }
            if(!senha.equals("123")){
                editTextSenha.setError(getString(R.string.msg_erro_senha));
                ok = false;
            }
            if(ok){
                Toast.makeText(this, getString(R.string.msg_sucesso, nome, email), Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return false;
    }
}