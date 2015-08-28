package com.summer.main.drawingtool;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * PaletteTool: provide alternative colors for drawing
 * @author summer
 * @version 1.0
 *
 */
public class PaletteToolView extends AbstractDrawView {

	/** Palette Tool Coordinator X */
	private int colorX = startX;

	/** Palette Tool Coordinator Y */
	private int colorY = startY;

	/** Palette Tool row number */
	private int colorHeightNum = 0;

	/** Palette Tool column number */
	private int colorWidthNum = 2;
	
	/** Palette Tool cell width */
	private  int colorSide = 0;
	
	/** selected color */
	private int currentColor = Color.BLACK;

	/** color array */
	private final int[] colorArray = new int[] { 0xff8B4513, Color.RED,
			Color.GREEN, Color.BLUE, Color.YELLOW, Color.BLACK, Color.MAGENTA,
			Color.CYAN, Color.GRAY, 0xffFF8C00, 0xff006400, 0xff800080,
			0xffFA8072, 0xff9370DB, 0xffADFF2F, 0xffDCDCDC,0xff47B8B1,0xffB8AD47,
			0xffB84747,0xff11DA14};
	
	public PaletteToolView(Context context, AttributeSet arr) {
		super(context, arr);
	}

	@Override
	public void init()
	{
		colorSide = this.width /colorWidthNum;
		colorHeightNum = colorArray.length / colorWidthNum;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		if (action == MotionEvent.ACTION_CANCEL) {
			return false;
		}

		float touchX = event.getX();
		float touchY = event.getY();
		
		if (action == MotionEvent.ACTION_DOWN) {
			checkTouchPaletteTool(touchX, touchY);
		}

		return super.onTouchEvent(event);
	}
	
	/**
	 * check if touch point is inside PaletteTool Panel
	 * @param x pointX
	 * @param y pointY
	 * @return true: touch point is inside PaletteToolView false :touch point
	 *         is outside PaletteToolView
	 */
	public boolean checkTouchPaletteTool(float x, float y) {
		int colorYRangeA = colorY + colorSide;
		int colorYRangeB = colorY + colorSide * (colorHeightNum + 1);
		if (x > colorX && y > colorYRangeA
				&& x < colorX + colorSide * colorWidthNum && y < colorYRangeB) {

			int tx = (int) ((x - colorX) / colorSide);
			int ty = (int) ((y - colorYRangeA) / colorSide);
			int index = ty * colorWidthNum + tx;
			if (index < colorArray.length) {
				currentColor = colorArray[index];
			} else {
				currentColor = Color.BLACK;
			}

			return true;
		}

		return false;
	}
	
	@Override
	public void onPaint(Canvas canvas) {
		drawPaletteTool(canvas);
	}

	/**
	 * draw Palette Tool
	 * @param canvas 
	 */
	private void drawPaletteTool(Canvas canvas) {
	  
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(2);

		// paint selected color border
		canvas.drawRect(colorX, colorY, colorX + colorSide * 2, colorY
				+ colorSide, paint);

		// fill selected color(default color is black)
		paint.setColor(currentColor);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawRect(colorX + 4, colorY + 4, colorX + colorSide * 2 - 4,
				colorY + colorSide - 4, paint);

		// paint alternative color
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);
		for (int i = 0; i < colorWidthNum; i++)
			for (int j = 0; j < colorHeightNum; j++) {
				int cellX = colorX + i * colorSide;
				int cellY = colorY + (j + 1) * colorSide;
				int cellBX = cellX + colorSide;
				int cellBY = cellY + colorSide;
				int colorIndex = i + j * colorWidthNum;

				canvas.drawRect(cellX, cellY, cellBX, cellBY, paint);

				fillColor(canvas, cellX, cellY, cellBX, cellBY, colorIndex);
			}
	}

	/**
	 * fill color in each cell
	 * @param canvas  canvas obj
	 * @param cellX   left-top pointX
	 * @param cellY   left-top pointY
	 * @param cellBX  right-bottom pointX
	 * @param cellBY  right-bottom pointY
	 * @param colorIndex color array index
	 */
	private void fillColor(Canvas canvas, int cellX, int cellY, int cellBX,
			int cellBY, int colorIndex) {
		if (colorIndex < colorArray.length && colorIndex >= 0) {
			drawCellColor(canvas, cellX, cellY, cellBX, cellBY,
					colorArray[colorIndex]);
		} else {
			drawCellColor(canvas, cellX, cellY, cellBX, cellBY, Color.BLACK);
		}
	}

	/**
	 * fill cell of alternative color
	 * @param canvas  canvas obj
	 * @param cellX   left-top pointX
	 * @param cellY   left-top pointY
	 * @param cellBX  right-bottom pointX
	 * @param cellBY  right-bottom pointY
	 * @param color   
	 */
	private void drawCellColor(Canvas canvas, int cellX, int cellY, int cellBX,
			int cellBY, int color) {
		Paint paint = new Paint();
		paint.setColor(color);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawRect(cellX + 4, cellY + 4, cellBX - 4, cellBY - 4, paint);
	}
	
	public int getCurrentColor() {
		return currentColor;
	}
}