package com.dev.meuaplicativo;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationUtils {

    public static final String ACAO_DELETE = "com.dev.meuaplicativo.notification.DELETE_NOTIFICATION";
    public static final String ACAO_NOTIFICATION = "com.dev.meuaplicativo.notification.NOTIFICATION";

    private static PendingIntent criarPendingIntent(Context context, String texto, int id){
        Intent resultIntent = new Intent(context, DetalheActivity.class);
        resultIntent.putExtra(DetalheActivity.EXTRA_TEXTO, texto);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(DetalheActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        return stackBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void criarNotificacaoSimples(Context context, String texto, int id){
        PendingIntent resultPendingIntent = criarPendingIntent(context, texto, id);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_notificacao)
                .setContentTitle("Simples "+id)
                .setContentText(texto)
                .setContentIntent(resultPendingIntent);

        NotificationManagerCompat nmc = NotificationManagerCompat.from(context);
        nmc.notify(id, builder.build());
    }

    public static void criarNotificacaoCompleta(Context context, String texto, int id){
        Uri uriSom = Uri.parse("android.resource://"+context.getPackageName()+"/raw/som_notificacao");
        PendingIntent pitAcao = PendingIntent.getBroadcast(context, 0, new Intent(ACAO_NOTIFICATION), 0);
        PendingIntent pitDelete = PendingIntent.getBroadcast(context, 0, new Intent(ACAO_DELETE), 0);

        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);

        PendingIntent pitNotificacao = criarPendingIntent(context, texto, id);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notificacao)
                .setContentTitle("Completa")
                .setContentText(texto)
                .setTicker("Chegou uma notificação")
                .setWhen(System.currentTimeMillis())
                .setLargeIcon(largeIcon)
                .setAutoCancel(true)
                .setContentIntent(pitNotificacao)
                .setDeleteIntent(pitDelete)
                .setLights(Color.BLUE, 1000, 5000)
                .setSound(uriSom)
                .setVibrate(new long[]{100, 500, 200, 800})
                .addAction(R.drawable.ic_acao_notificacao, "Ação customizada", pitAcao)
                .setNumber(id)
                .setSubText("Subtexto");

        NotificationManagerCompat nmc = NotificationManagerCompat.from(context);
        nmc.notify(id, builder.build());
    }

    public static void criarNotificationBig(Context context, int idNotificacao){
        int numero = 5;
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("Mensagens não lidas:");
        for(int i = 1; i <= numero; i++){
            inboxStyle.addLine("Mensagem: "+i);
        }
        inboxStyle.setSummaryText("Clique para abrir");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notificacao)
                .setContentTitle("Notificação")
                .setContentText("Vários itens pendentes")
                .setContentIntent(criarPendingIntent(context, "Mensagens não lidas", -1))
                .setNumber(numero)
                .setStyle(inboxStyle);

        NotificationManagerCompat nmc = NotificationManagerCompat.from(context);
        nmc.notify(idNotificacao, builder.build());
    }
}