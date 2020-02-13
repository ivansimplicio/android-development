package com.dev.meuaplicativo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BluetoothChatActivity extends AppCompatActivity
        implements View.OnClickListener,
        DialogInterface.OnClickListener,
        DialogInterface.OnCancelListener {

    private static final String SERVICO = "bluetoothChat";
    private static final UUID MEU_UUID = UUID.fromString("5981fe68-c234-4b95-a907-fe1cba49a81b");
    private static final int BT_TEMPO_DESCOBERTA = 30;
    private static final int BT_ATIVAR = 0;
    private static final int BT_VISIVEL = 1;
    private static final int MSG_TEXTO = 0;
    private static final int MSG_DESCONECTOU = 2;

    private ThreadServidor threadServidor;
    private ThreadCliente threadCliente;
    private ThreadComunicacao threadComunicacao;

    private BluetoothAdapter adaptadorBluetooth;
    private List<BluetoothDevice> dispositivosEncontrados;
    private EventosBluetoothReceiver eventosBluetoothReceiver;
    private DataInputStream is;
    private DataOutputStream os;

    private ArrayAdapter<String> mensagensAdapter;
    private TelaHandler telaHandler;
    private ProgressDialog aguardeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_chat);

        telaHandler = new TelaHandler();
        mensagensAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        ((ListView)findViewById(R.id.listHistorico)).setAdapter(mensagensAdapter);

        eventosBluetoothReceiver = new EventosBluetoothReceiver();
        dispositivosEncontrados = new ArrayList<>();
        adaptadorBluetooth = BluetoothAdapter.getDefaultAdapter();

        if(adaptadorBluetooth != null){
            if(!adaptadorBluetooth.isEnabled()){
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, BT_ATIVAR);
            }
        }else{
            Toast.makeText(this, R.string.msg_erro_bt_indisponivel, Toast.LENGTH_SHORT).show();
            finish();
        }

        IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        IntentFilter filter2 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(eventosBluetoothReceiver, filter1);
        registerReceiver(eventosBluetoothReceiver, filter2);

        findViewById(R.id.buttonEnviar).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(eventosBluetoothReceiver);
        paraTudo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bluetooth_chat, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_cliente:
                dispositivosEncontrados.clear();
                adaptadorBluetooth.startDiscovery();
                exibirProgressDialog(R.string.msg_procurando_dispositivos, 0);
                break;
            case R.id.action_servidor:
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, BT_TEMPO_DESCOBERTA);
                startActivityForResult(discoverableIntent, BT_VISIVEL);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == BT_ATIVAR){
            if(resultCode != RESULT_OK){
                Toast.makeText(this, R.string.msg_ativar_bluetooth, Toast.LENGTH_SHORT).show();
                finish();
            }
        }else if(requestCode == BT_VISIVEL){
            if(resultCode == BT_TEMPO_DESCOBERTA){
                iniciaThreadServidor();
            }else{
                Toast.makeText(this, R.string.msg_aparelho_invisivel, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void exibirDispositivosEncontrados(){
        aguardeDialog.dismiss();
        String[] aparelhos = new String[dispositivosEncontrados.size()];
        for(int i = 0; i < dispositivosEncontrados.size(); i++){
            aparelhos[i] = dispositivosEncontrados.get(i).getName();
        }
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.aparelhos_encontrados)
                .setSingleChoiceItems(aparelhos, -1, this)
                .create();
        dialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        iniciaThreadCliente(which);
        dialog.dismiss();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        adaptadorBluetooth.cancelDiscovery();
        paraTudo();
    }

    @Override
    public void onClick(View v) {
        EditText editText = findViewById(R.id.editText);
        String mensagem = editText.getText().toString();
        editText.setText("");
        try{
            if(os != null){
                os.writeUTF(mensagem);
                mensagensAdapter.add("Eu: "+mensagem);
                mensagensAdapter.notifyDataSetChanged();
            }
        }catch (IOException e){
            e.printStackTrace();
            telaHandler.obtainMessage(MSG_DESCONECTOU, e.getMessage()+"[0]").sendToTarget();
        }
    }

    private void exibirProgressDialog(int mensagem, long tempo){
        aguardeDialog = ProgressDialog.show(this, getString(R.string.aguarde),
                getString(mensagem), true, true, this);
        aguardeDialog.show();
        if(tempo > 0){
            telaHandler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    if(threadComunicacao == null){
                        aguardeDialog.cancel();
                    }
                }
            }, tempo * 1000);
        }
    }

    private void paraTudo(){
        if(threadComunicacao != null){
            threadComunicacao.parar();
            threadComunicacao = null;
        }
        if(threadServidor != null){
            threadServidor.parar();
            threadServidor = null;
        }
        if(threadCliente != null){
            threadCliente.parar();
            threadCliente = null;
        }
    }

    private void iniciaThreadServidor(){
        exibirProgressDialog(R.string.mensagem_servidor, BT_TEMPO_DESCOBERTA);
        paraTudo();
        threadServidor = new ThreadServidor();
        threadServidor.iniciar();
    }

    private void iniciaThreadCliente(final int which){
        paraTudo();
        threadCliente = new ThreadCliente();
        threadCliente.iniciar(dispositivosEncontrados.get(which));
    }

    private void trataSocket(final BluetoothSocket socket){
        aguardeDialog.dismiss();
        threadComunicacao = new ThreadComunicacao();
        threadComunicacao.iniciar(socket);
    }

    private class ThreadServidor extends Thread{
        BluetoothServerSocket serverSocket;
        BluetoothSocket clientSocket;

        public void run(){
            try{
                serverSocket = adaptadorBluetooth.listenUsingRfcommWithServiceRecord(SERVICO, MEU_UUID);
                clientSocket = serverSocket.accept();
                trataSocket(clientSocket);
            }catch(IOException e){
                telaHandler.obtainMessage(MSG_DESCONECTOU, e.getMessage()+"[1]").sendToTarget();
                e.printStackTrace();
            }
        }

        public void iniciar(){
            start();
        }

        public void parar(){
            try{
                serverSocket.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    private class ThreadCliente extends Thread{
        BluetoothDevice device;
        BluetoothSocket socket;

        public void run(){
            try{
                socket = device.createRfcommSocketToServiceRecord(MEU_UUID);
                socket.connect();
                trataSocket(socket);
            }catch(IOException e){
                e.printStackTrace();
                telaHandler.obtainMessage(MSG_DESCONECTOU, e.getMessage()+"[2]").sendToTarget();
            }
        }

        public void iniciar(BluetoothDevice device){
            this.device = device;
            start();
        }

        public void parar(){
            try{
                socket.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private class ThreadComunicacao extends Thread{

        String nome;
        BluetoothSocket socket;

        public void run(){
            try{
                nome = socket.getRemoteDevice().getName();
                is = new DataInputStream(socket.getInputStream());
                os = new DataOutputStream(socket.getOutputStream());
                String string;
                while(true){
                    string = is.readUTF();
                    telaHandler.obtainMessage(MSG_TEXTO, nome+": "+string).sendToTarget();
                }
            }catch (IOException e){
                e.printStackTrace();
                telaHandler.obtainMessage(MSG_DESCONECTOU, e.getMessage()+"[3]").sendToTarget();
            }
        }

        public void iniciar(BluetoothSocket socket){
            this.socket = socket;
            start();
        }

        public void parar(){
            try{
                is.close();
            }catch(IOException e){
                e.printStackTrace();
            }
            try{
                os.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    private class EventosBluetoothReceiver extends BroadcastReceiver{

        public void onReceive(Context context, Intent intent){
            if(BluetoothDevice.ACTION_FOUND.equals(intent.getAction())){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                dispositivosEncontrados.add(device);
            }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(intent.getAction())){
                exibirDispositivosEncontrados();
            }
        }
    }

    private class TelaHandler extends Handler{

        public void handleMessage(Message msg){
            super.handleMessage(msg);

            if(msg.what == MSG_TEXTO){
                mensagensAdapter.add(msg.obj.toString());
                mensagensAdapter.notifyDataSetChanged();
            }else if(msg.what == MSG_DESCONECTOU){
                Toast.makeText(BluetoothChatActivity.this, getString(R.string.msg_desconectou)+
                        msg.obj.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}