package com.summer.main.drawingtool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Drawing canvas view: draw alternative shape
 * 
 * @author summer
 * @version 1.0
 */
public class DrawingCanvasView extends AbstractDrawView {
	
	/**Tag Obj for Log*/
	private final String TAG = DrawingCanvasView.class.getSimpleName();
	
	/** canvas left-top coordinator X */
	private int canvasX = startX;

	/** canvas left-top coordinator Y */
	private int canvasY = startY;
	
	/** canvas right-bottom coordinator X */
	private int canvasBX = 0;
	
	/** canvas right-bottom coordinator Y */
	private int canvasBY = 0;
	
	/** relative offset to screen */
	private int offset = 0;

	/**Bitmap object*/
	private Bitmap newbit = null;
	
	/** store all drawActions */
	private ArrayList<DrawAction> actionList = new ArrayList<DrawAction>();

	/** current Action Type */
	private DrawAction currAction = null;
	
	/**PaletteToolView obj */
	PaletteToolView palToolView = null;
	
	/**ShapeToolView obj*/
	ShapeToolView shpToolView = null;
	
	public DrawingCanvasView(Context context, AttributeSet arr) {
		super(context, arr);
	}
	
	@Override
	public void init()
	{
		canvasBX = this.width-5;
		canvasBY = this.height;
		offset = this.palToolView.getWidth() + canvasX;  	
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {	
		int action = event.getAction();
		if (action == MotionEvent.ACTION_CANCEL) {
			return false;
		}

		float touchX = event.getX();
		float touchY = event.getY();
		float size = event.getSize()*1000;
		
		if (action == MotionEvent.ACTION_DOWN) {
			if (checkTouchMainWindow(touchX- offset, touchY)) {
				setCurAction(touchX- offset, touchY,(int)size);
			}
		}

		if (action == MotionEvent.ACTION_MOVE) {
			if (currAction != null) {
				currAction.move(touchX- offset, touchY);
			}
		}

		if (action == MotionEvent.ACTION_UP) {
			if (currAction != null) {
				currAction.move(touchX- offset, touchY);
				actionList.add(currAction);
				currAction = null;
			}
		}

		return super.onTouchEvent(event);
	}

	/**
	 * configure current operation: square, circle or triangle
	 * @param x    pointX
	 * @param y    pointY
	 * @param size strokeSize
	 */
	public void setCurAction(float x, float y,int size) {
		int index = shpToolView.getCurrentShapeIndex();
		int currentColor = palToolView.getCurrentColor();
		
		switch (index) {
		case 0:
			currAction = new DrawSquare(x, y, currentColor,size);
			break;
		case 1:
			currAction = new DrawCircle(x, y, currentColor,size);
			break;
		case 2:
			currAction = new DrawTriangle(x, y, currentColor,size);
			break;
		case 3:
			currAction = new DrawRectangle(x, y, currentColor,size);
			break;
		}
	}

	/**
	 * checkTouchMainWindow
	 * @param x pointX
	 * @param y pointY
	 * @return true: point in canvas; false : point not in canvas
	 */
	public boolean checkTouchMainWindow(float x, float y) {
		if (x > canvasX && y > canvasY && x < canvasBX && y < canvasBY) {
			return true;
		}
		return false;
	}


	@Override
	public void onPaint(Canvas canvas) {
		drawMainWindow(canvas);
	}

	/**
	 * draw canvas
	 * 
	 * @param canvas canvas obj
	 */
	private void drawMainWindow(Canvas canvas) {
		newbit = Bitmap.createBitmap(canvasBX - canvasX, canvasBY - canvasY,
				Config.ARGB_8888);
		Canvas canvasTemp = new Canvas(newbit);
		canvasTemp.drawColor(Color.TRANSPARENT);

		for (int i = 0; i < actionList.size(); i++) {
			actionList.get(i).draw(canvasTemp);
		}

		if (currAction != null) {
			currAction.draw(canvasTemp);
		}
		
		canvas.drawBitmap(newbit, canvasX, canvasY, null);
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.BLACK);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(3);
		canvas.drawRect(canvasX, canvasY, canvasBX, canvasBY, mPaint);
	}
	
	/**
	 * clear all the records in canvas 
	 * @param resetBtn
	 */
	public void onResetCanvas(TextView resetBtn) {
		currAction = null;
		this.actionList.clear();
	}

	/**
	 * create file and parent directories
	 * @param filePath
	 * @param fileName
	 * @return File
	 */
	public File getFilePath(String filePath, String fileName) {
		File file = null;
		makeRootDirectory(filePath);
		try {
			file = new File(filePath + File.separator+fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}
	
	/**
	 * make parent directory if not
	 * @param filePath
	 */
	private  void makeRootDirectory(String filePath) {
		File file = null;
		try {
			file = new File(filePath);
			if (!file.exists()) {
				file.mkdirs();
			}
		} catch (Exception e) {
			Log.e(TAG, "fail to create directory");
		}
	}
	
	/**
	 * save image to specific path
	 * @param context
	 * @param file
	 */
	public void onSaveImage(Context context, File file) {
		try {
			if (newbit != null) {
				FileOutputStream outStream = new FileOutputStream(file);
				newbit.compress(CompressFormat.PNG, 100, outStream);
				Toast.makeText(context, context.getString(R.string.imgSavedPath) + file.getAbsolutePath(),
						Toast.LENGTH_LONG).show();
				outStream.flush();
				outStream.close();
			} else {
				Log.e(TAG, "bitmap object is null");
			}
		} catch (IOException e) {
			Log.e(TAG, "bitmap failed to be saved");
		}
	}

	public void setPaletteToolView(PaletteToolView palToolView) {
		this.palToolView = palToolView;
	}


	public void setShapeToolView(ShapeToolView shpToolView) {
		this.shpToolView = shpToolView;
	}
}