package com.dev.meuaplicativo;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class JogoDaVelhaView extends View {

    public static final int XIS = 1;
    public static final int BOLA = 2;
    public static final int VAZIO = 0;
    public static final int EMPATE = 3;

    int tamanho;
    int vez;
    int[][] tabuleiro;

    Paint paint;
    Bitmap imageX;
    Bitmap imageO;

    GestureDetector detector;
    JogoDaVelhaListener listener;

    int corDaBarra;
    float larguraDaBarra;

    public JogoDaVelhaView(Context context, AttributeSet attrs){
        super(context, attrs);
        vez = XIS;
        tabuleiro = new int[3][3];

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.JogoDaVelha);
        corDaBarra = a.getColor(R.styleable.JogoDaVelha_corDaBarra, Color.BLACK);
        larguraDaBarra = a.getDimension(R.styleable.JogoDaVelha_larguraDaBarra, 3);
    }

    public void reiniciarJogo(){
        tabuleiro = new int[3][3];
        invalidate();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable p = super.onSaveInstanceState();
        EstadoJogo estado = new EstadoJogo(p, vez, tabuleiro);
        return estado;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        EstadoJogo estado = (EstadoJogo) state;
        super.onRestoreInstanceState(estado.getSuperState());
        vez = estado.vez;
        tabuleiro = estado.tabuleiro;
        invalidate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);

        imageX = BitmapFactory.decodeResource(getResources(), R.drawable.x_mark);
        imageO = BitmapFactory.decodeResource(getResources(), R.drawable.o_mark);

        detector = new GestureDetector(this.getContext(), new VelhaTouchListener());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        tabuleiro = null;
        paint = null;
        imageX = null;
        imageO = null;
        detector = null;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if(getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT){
            Resources r = getResources();
            float quadrante = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 48,
                    r.getDisplayMetrics());

            tamanho = (int) quadrante * 3;
        }else if(getLayoutParams().width == ViewGroup.LayoutParams.MATCH_PARENT){
            tamanho = Math.min(
                    MeasureSpec.getSize(widthMeasureSpec),
                    MeasureSpec.getSize(heightMeasureSpec));
        }else{
            tamanho = getLayoutParams().width;
        }
        setMeasuredDimension(tamanho, tamanho);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int quadrante = tamanho / 3;

        //Desenhando as linhas
        paint.setColor(corDaBarra);
        paint.setStrokeWidth(larguraDaBarra);

        //Verticais
        canvas.drawLine(quadrante, 0,  quadrante, tamanho, paint);
        canvas.drawLine(quadrante*2, 0, quadrante*2, tamanho, paint);

        //Horizontais
        canvas.drawLine(0, quadrante, tamanho, quadrante, paint);
        canvas.drawLine(0, quadrante*2, tamanho, quadrante*2, paint);

        for(int linha = 0; linha < 3; linha++){
            for(int coluna = 0; coluna < 3; coluna++){
                int x = coluna * quadrante;
                int y = linha * quadrante;
                Rect rect = new Rect(x, y, x+quadrante, y+quadrante);

                if(tabuleiro[linha][coluna] == XIS){
                    canvas.drawBitmap(imageX, null, rect, null);
                }else if(tabuleiro[linha][coluna] == BOLA){
                    canvas.drawBitmap(imageO, null, rect, null);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    class VelhaTouchListener extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            int vencedor = gameOver();

            if(e.getAction() == MotionEvent.ACTION_UP && vencedor == VAZIO){
                int quadrante = tamanho / 3;

                int linha = (int) (e.getY() / quadrante);
                int coluna = (int) (e.getX() / quadrante);

                if(tabuleiro[linha][coluna] == VAZIO){
                    tabuleiro [linha][coluna] = vez;
                    vez = (vez == XIS) ? BOLA : XIS;
                    invalidate();

                    vencedor = gameOver();
                    if(vencedor != VAZIO){
                        if(listener != null){
                            listener.fimDeJogo(vencedor);
                        }
                    }
                    return true;
                }
            }
            return super.onSingleTapUp(e);
        }
    }

    private int gameOver(){
        //Horizontais
        if(ganhou(tabuleiro[0][0], tabuleiro[0][1], tabuleiro[0][2])){
            return tabuleiro[0][0];
        }
        if(ganhou(tabuleiro[1][0], tabuleiro[1][1], tabuleiro[1][2])){
            return tabuleiro[1][0];
        }
        if(ganhou(tabuleiro[2][0], tabuleiro[2][1], tabuleiro[2][2])){
            return tabuleiro[2][0];
        }
        //Verticais
        if(ganhou(tabuleiro[0][0], tabuleiro[1][0], tabuleiro[2][0])){
            return tabuleiro[0][0];
        }
        if(ganhou(tabuleiro[0][1], tabuleiro[1][1], tabuleiro[2][1])){
            return tabuleiro[0][1];
        }
        if(ganhou(tabuleiro[0][2], tabuleiro[1][2], tabuleiro[2][2])){
            return tabuleiro[0][2];
        }
        //Diagonais
        if(ganhou(tabuleiro[0][0], tabuleiro[1][1], tabuleiro[2][2])){
            return tabuleiro[0][0];
        }
        if(ganhou(tabuleiro[0][2], tabuleiro[1][1], tabuleiro[2][0])){
            return tabuleiro[0][2];
        }

        //Existem espaços vazios
        for(int linha = 0; linha < tabuleiro.length; linha++){
            for(int coluna = 0; coluna < tabuleiro[linha].length; coluna++){
                if(tabuleiro[linha][coluna] == VAZIO){
                    return VAZIO;
                }
            }
        }
        return EMPATE;
    }

    private boolean ganhou(int a, int b, int c){
        return (a == b && b == c && a != VAZIO);
    }

    public void setListener(JogoDaVelhaListener listener){
        this.listener = listener;
    }

    public interface JogoDaVelhaListener{
        void fimDeJogo(int vencedor);
    }

    static class EstadoJogo extends BaseSavedState{
        int vez;
        int[][] tabuleiro;

        private EstadoJogo(Parcelable p, int vez, int[][] tabuleiro){
            super(p);
            this.vez = vez;
            this.tabuleiro = tabuleiro;
        }

        private EstadoJogo(Parcel p){
            super(p);
            vez = p.readInt();
            tabuleiro = new int[3][3];
            for(int linha = 0; linha < tabuleiro.length; linha++){
                p.readIntArray(tabuleiro[linha]);
            }
        }

        public final Parcelable.Creator<EstadoJogo> CREATOR = new Parcelable.Creator<EstadoJogo>(){

            @Override
            public EstadoJogo createFromParcel(Parcel source) {
                return new EstadoJogo(source);
            }

            @Override
            public EstadoJogo[] newArray(int size) {
                return new EstadoJogo[size];
            }
        };

        @Override
        public void writeToParcel(Parcel parcel, int flags) {
            super.writeToParcel(parcel, flags);
            parcel.writeInt(vez);
            for(int linha = 0; linha < tabuleiro.length; linha++){
                parcel.writeIntArray(tabuleiro[linha]);
            }
        }
    }
}