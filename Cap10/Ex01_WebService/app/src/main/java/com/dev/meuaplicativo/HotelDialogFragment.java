package com.dev.meuaplicativo;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

public class HotelDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {

    private static final String DIALOG_TAG = "editDialog";
    private static final String EXTRA_HOTEL = "hotel";

    private EditText editTextNome;
    private EditText editTextEndereco;
    private RatingBar ratingBarEstrelas;

    private Hotel hotel;

    public static HotelDialogFragment newInstance(Hotel hotel){
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_HOTEL, hotel);

        HotelDialogFragment dialog = new HotelDialogFragment();
        dialog.setArguments(bundle);

        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hotel = (Hotel) getArguments().getSerializable(EXTRA_HOTEL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_dialog_hotel, container, false);

        editTextNome = layout.findViewById(R.id.editTextNome);
        editTextNome.requestFocus();
        editTextEndereco = layout.findViewById(R.id.editTextEndereco);
        editTextEndereco.setOnEditorActionListener(this);
        ratingBarEstrelas = layout.findViewById(R.id.ratingBarInputEstrelas);

        if(hotel != null){
            editTextNome.setText(hotel.nome);
            editTextEndereco.setText(hotel.endereco);
            ratingBarEstrelas.setRating(hotel.estrelas);
        }

        //Exibe o teclado virtual ao exibir o Dialog
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        getDialog().setTitle(R.string.acao_novo);

        return layout;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if(EditorInfo.IME_ACTION_DONE == actionId){
            Activity activity = getActivity();
            if(activity instanceof AoSalvarHotel) {
                if (hotel == null) {
                    hotel = new Hotel(editTextNome.getText().toString(),
                            editTextEndereco.getText().toString(),
                            ratingBarEstrelas.getRating());
                } else {
                    hotel.nome = editTextNome.getText().toString();
                    hotel.endereco = editTextEndereco.getText().toString();
                    hotel.estrelas = ratingBarEstrelas.getRating();
                }

                AoSalvarHotel listener = (AoSalvarHotel) activity;
                listener.salvouHotel(hotel);
                //Fechar o Dialog
                dismiss();
                return true;
            }
        }
        return false;
    }

    public void abrir(FragmentManager fm){
        if(fm.findFragmentByTag(DIALOG_TAG) == null){
            show(fm, DIALOG_TAG);
        }
    }

    public interface AoSalvarHotel{
        void salvouHotel(Hotel hotel);
    }
}