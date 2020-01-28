package com.dev.meuaplicativo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView = findViewById(R.id.webView);
        /*webView.loadUrl("https://github.com/ivansimplicio");
        webView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });*/

        /*
        Para controlar a navegação na WebView pode-se usar os métodos:
        canGoBack() - checa se pode ir para trás
        canGoForward() - chega se pode ir para frente
        goBack() - vai para trás
        goForward() - vai para frente
        */
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(this, "dev");
        webView.loadUrl("file:///android_asset/index.html");
    }

    @JavascriptInterface
    public void showToast(String s, String t){
        Toast.makeText(this, "Nome: "+s+" Idade: "+t, Toast.LENGTH_SHORT).show();
    }
}