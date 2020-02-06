package com.dev.meuaplicativo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import androidx.fragment.app.Fragment;

public abstract class InternetFragment extends Fragment {

    private ConexaoReceiver receiver;

    @Override
    public void onResume() {
        super.onResume();
        receiver = new ConexaoReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    public abstract void iniciarDownload();

    class ConexaoReceiver extends BroadcastReceiver{
        boolean primeiraVez = true;

        @Override
        public void onReceive(Context context, Intent intent) {
            if(primeiraVez){
                primeiraVez = false;
                return;
            }
            if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
                if(LivroHttp.temConexao(context)){
                    iniciarDownload();
                }
            }
        }
    }
}