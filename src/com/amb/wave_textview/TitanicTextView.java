package com.amb.wave_textview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Titanic
 * romainpiel
 * 13/03/2014
 */
public class TitanicTextView extends TextView {//自定义控件

    public interface AnimationSetupCallback {
        public void onSetupAnimation(TitanicTextView titanicTextView);
    }

    // callback fired at first onSizeChanged
    private AnimationSetupCallback animationSetupCallback;
    // wave shader coordinates
    private float maskX, maskY;
    // if true, the shader will display the wave
    private boolean sinking;
    // true after the first onSizeChanged
    private boolean setUp;

    // shader containing a repeated wave
    private BitmapShader shader;
    // shader matrix
    private Matrix shaderMatrix;
    // wave drawable
    private Drawable wave;
    // (getHeight() - waveHeight) / 2
    private float offsetY;

    public TitanicTextView(Context context) {
        super(context);
        init();
    }

    public TitanicTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TitanicTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {//矩阵初始化
        shaderMatrix = new Matrix();
    }

    public AnimationSetupCallback getAnimationSetupCallback() {
        return animationSetupCallback;
    }

    public void setAnimationSetupCallback(AnimationSetupCallback animationSetupCallback) {
        this.animationSetupCallback = animationSetupCallback;
    }

    public float getMaskX() {
        return maskX;
    }

    public void setMaskX(float maskX) {/*
	各个属性的接口
	
	*/
        this.maskX = maskX;
        invalidate();
    }

    public float getMaskY() {
        return maskY;
    }

    public void setMaskY(float maskY) {
        this.maskY = maskY;
        invalidate();
    }

    public boolean isSinking() {
        return sinking;
    }

    public void setSinking(boolean sinking) {
        this.sinking = sinking;
    }

    public boolean isSetUp() {
        return setUp;
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        createShader();
    }
	/*
	我们首先添加一个ColorStateList资源XML文件，XML文件保存在res/color/button_text.xml:
	Button btn=(Button)findViewById(R.id.btn);  
02. Resources resource=(Resources)getBaseContext().getResources();   
03. ColorStateList csl=(ColorStateList)resource.getColorStateList(R.color.button_text);  
04. if(csl!=null){  
05.      btn.setTextColor(color_state_list);//设置按钮文字颜色  
06. }  
     */

    @Override
    public void setTextColor(ColorStateList colors) {
        super.setTextColor(colors);
        createShader();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        createShader();

        if (!setUp) {
            setUp = true;
            if (animationSetupCallback != null) {
                animationSetupCallback.onSetupAnimation(TitanicTextView.this);
            }
        }
    }

    /**
     * Create the shader
     * draw the wave with current color for a background
     * repeat the bitmap horizontally, and clamp colors vertically
     */
    private void createShader() {

        if (wave == null) {
            wave = getResources().getDrawable(R.drawable.wave);//获取波浪图片
        }

        int waveW = wave.getIntrinsicWidth();//getIntrinsicWidth()和getIntrinsicHeight()返回的宽高应该是dp为单位的
        int waveH = wave.getIntrinsicHeight();

        Bitmap b = Bitmap.createBitmap(waveW, waveH, Bitmap.Config.ARGB_8888);
        /*
         *  ALPHA_8就是Alpha由8位组成

          	ARGB_4444就是由4个4位组成即16位

			ARGB_8888就是由4个8位组成即32位

			RGB_565就是R为5位，G为6位，B为5位共16位
         */
        
        Canvas c = new Canvas(b);

        c.drawColor(getCurrentTextColor());

        wave.setBounds(0, 0, waveW, waveH);//setBounds(x,y,width,height); x:组件在容器X轴上的起点 y:组件在容器Y轴上的起点 width:组件的长度 height:组件的
        wave.draw(c);

        shader = new BitmapShader(b, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        getPaint().setShader(shader);

        offsetY = (getHeight() - waveH) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // modify text paint shader according to sinking state
        if (sinking && shader != null) {

            // first call after sinking, assign it to our paint
            if (getPaint().getShader() == null) {
                getPaint().setShader(shader);
            }

            // translate shader accordingly to maskX maskY positions
            // maskY is affected by the offset to vertically center the wave
            shaderMatrix.setTranslate(maskX, maskY + offsetY);

            // assign matrix to invalidate the shader
            shader.setLocalMatrix(shaderMatrix);
        } else {
            getPaint().setShader(null);
        }

        super.onDraw(canvas);
    }
}
