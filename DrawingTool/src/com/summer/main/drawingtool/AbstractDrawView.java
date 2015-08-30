package com.summer.main.drawingtool;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * AbstractDrawView 
 * @author summer
 * @version 1.0
 *
 */
public abstract class AbstractDrawView  extends SurfaceView implements Runnable,
SurfaceHolder.Callback {
	
	/** loop painting flag */
	protected boolean mLoop = true;

	/** Activity obj*/
	protected Context context = null;
	
	/** Paint Obj */
	protected Paint mPaint = null;

	/** SurfaceHolder Obj */
	protected SurfaceHolder mSurfaceHolder = null;

	/** view coordinator X */
	protected int startX = 5;

	/** view coordinator Y */
	protected int startY = 5;
	
	/**view obj width*/
	protected int width = 0;
	
	/**view obj height*/
	protected int height = 0;
	
	public AbstractDrawView(Context context, AttributeSet arr) {
		super(context, arr);
		init(context, arr);
	}
	
	/**
	 * initialize data
	 * @param context
	 * @param arr
	 */
	protected void init(Context context, AttributeSet arr)
	{
		this.context = context;
		mPaint = new Paint();
		mSurfaceHolder = this.getHolder();
		mSurfaceHolder.addCallback(this);
		this.setFocusable(true);
		//new Thread(this).start();
	}
	
	/**
	 * draw view using canvas obj
	 */
	public void onDrawView()
	{
		Canvas canvas = mSurfaceHolder.lockCanvas();
		if (mSurfaceHolder == null || canvas == null) {
			return;
		}
		
		canvas.drawColor(Color.WHITE);
		onPaint(canvas);
		mSurfaceHolder.unlockCanvasAndPost(canvas);
	}
	
	/**
	 * do painting in canvas
	 * @param canvas
	 */
	public abstract void onPaint(Canvas canvas);
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		this.width = getWidth();
		this.height = getHeight();
		new Thread(this).start();
		init();
	}
	
	/**
	 * initialize data
	 */
	protected void init()
	{
		
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mLoop = false;
	}
	
	@Override
	public void run() {
		while (mLoop) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synchronized (mSurfaceHolder) {
				onDrawView();
			}
		}
	}
}