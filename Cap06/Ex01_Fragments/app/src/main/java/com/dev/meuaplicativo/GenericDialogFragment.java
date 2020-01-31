package com.dev.meuaplicativo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class GenericDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    private static final String EXTRA_ID = "id";
    private static final String EXTRA_MENSAGEM = "message";
    private static final String EXTRA_TITULO = "title";
    private static final String EXTRA_BOTOES = "buttons";
    private static final String DIALOG_TAG = "SimpleDialog";

    private int dialogId;

    public static GenericDialogFragment novoDialog(int id, int title, int message, int[] buttonsText){
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_ID, id);
        bundle.putInt(EXTRA_TITULO, title);
        bundle.putInt(EXTRA_MENSAGEM, message);
        bundle.putIntArray(EXTRA_BOTOES, buttonsText);

        GenericDialogFragment dialog = new GenericDialogFragment();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        dialogId = getArguments().getInt(EXTRA_ID);
        int titulo = getArguments().getInt(EXTRA_TITULO);
        int message = getArguments().getInt(EXTRA_MENSAGEM);
        int[] botoes = getArguments().getIntArray(EXTRA_BOTOES);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(titulo);
        alertDialogBuilder.setMessage(message);
        switch (botoes.length){
            case 3:
                alertDialogBuilder.setNeutralButton(botoes[2], this);
            case 2:
                alertDialogBuilder.setNegativeButton(botoes[1], this);
            case 1:
                alertDialogBuilder.setPositiveButton(botoes[0], this);
        }
        return alertDialogBuilder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        Activity activity = getActivity();
        if(activity instanceof AoClicarNoDialog){
            AoClicarNoDialog listener = (AoClicarNoDialog)activity;
            listener.aoClicar(dialogId, which);
        }
    }

    public void abrir(FragmentManager supportFragmentManager){
        Fragment dialogFragment = supportFragmentManager.findFragmentByTag(DIALOG_TAG);
        if(dialogFragment == null){
            show(supportFragmentManager, DIALOG_TAG);
        }
    }

    public interface AoClicarNoDialog{
        void aoClicar(int id, int botao);
    }
}