package com.summer.main.drawingtool;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 
 * Shape Tool: provide alternative shapes for drawing
 * @author summer
 * @version 1.0
 *
 */
public class ShapeToolView  extends AbstractDrawView {
	
	/** selected shape */
	private int currentShapeIndex = 0;
	
	/** Shape Tool Coordinator X */
	private int shapeX = 0;

	/** Shape Tool Coordinator Y */
	private int shapeY = 20;

	/** Shape Tool row number */
	private int shapeHeightNum = 4;

	/** Shape Tool column number */
	private int shapeWidthNum = 1;

	/** Shape Tool cell width */
	private int shapeSide = 0;
	
	/** background color of selected shape */
	private int selectedCellColor = 0xffADD8E6;

	public ShapeToolView(Context context, AttributeSet arr) {
		super(context, arr);
	}

	@Override
	public void init()
	{
		shapeSide = this.width /2;
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
			checkTouchShapeTool(touchX, touchY);
		}

		return super.onTouchEvent(event);
	}
		
	/**
	 * check if touch point is inside ShapeTool Panel
	 * @param x pointX
	 * @param y pointY
	 * @return true: touch point is inside ShapeView Panel false :touch point
	 *         is outside ShapeView
	 */
	public boolean checkTouchShapeTool(float x, float y) {
		if (x > shapeX && y > shapeY
				&& x < shapeX + shapeSide * shapeWidthNum * 2
				&& y < shapeY + shapeSide * shapeHeightNum) {
			int tx = (int) ((x - shapeX) / (shapeSide * 2));
			int ty = (int) ((y - shapeY) / shapeSide);
			currentShapeIndex = ty * shapeWidthNum + tx;

			return true;
		}
		return false;
	}
	
	@Override
	public void onPaint(Canvas canvas)
	{
		drawShapeTool(canvas);
	}
	
	/**
	 * draw PaletteTool Panel
	 * @param canvas canvas obj
	 */
	private void drawShapeTool(Canvas canvas) {		
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(2);

		// paint alternative shape
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);
		for (int i = 0; i < shapeWidthNum; i++)
			for (int j = 0; j < shapeHeightNum; j++) {
				int cellX = shapeX + i * shapeSide;
				int cellY = shapeY + j * shapeSide;
				int cellBX = cellX + shapeSide * 2;
				int cellBY = cellY + shapeSide;
				int shapeIndex = i + j * shapeWidthNum;
				canvas.drawRect(cellX, cellY, cellBX, cellBY, paint);
				if (shapeIndex == currentShapeIndex) {
					paint.setColor(selectedCellColor);
					paint.setStyle(Paint.Style.FILL);
					canvas.drawRect(cellX + 2, cellY + 2, cellBX - 2,
							cellBY - 2, paint);
					paint.setColor(Color.BLACK);
					paint.setStyle(Paint.Style.STROKE);
				}

				drawShape(canvas, cellX, cellY, cellBX, cellBY, shapeIndex,
						paint);
			}
	}

	/**
	 * fill color in each cell
	 * @param canvas  canvas obj
	 * @param cellX   left-top pointX
	 * @param cellY   left-top pointY
	 * @param cellBX  right-bottom pointX
	 * @param cellBY  right-bottom pointY
	 * @param shapeIndex 0:square;1:circle;2:triangle
	 * @param paint   paint obj
	 */
	private void drawShape(Canvas canvas, int cellX, int cellY, int cellBX,
			int cellBY, int shapeIndex, Paint paint) {
		switch (shapeIndex) {
		case 0:
			drawSquare(canvas, cellX, cellY, cellBX, cellBY, paint);
			break;
		case 1:
			drawCircle(canvas, cellX, cellY, cellBX, cellBY, paint);
			break;
		case 2:
			drawTriangle(canvas, cellX, cellY, cellBX, cellBY, paint);
			break;
		case 3:
			drawRectangle(canvas, cellX, cellY, cellBX, cellBY, paint);
			break;
		default:
			drawCircle(canvas, cellX, cellY, cellBX, cellBY, paint);
			break;
		}
	}

	/**
	 * draw shape: square
	 * @param canvas  canvas obj
	 * @param cellX   left-top pointX
	 * @param cellY   left-top pointY
	 * @param cellBX  right-bottom pointX
	 * @param cellBY  right-bottom pointY
	 * @param paint   paint obj
	 */
	private void drawSquare(Canvas canvas, int cellX, int cellY, int cellBX,
			int cellBY, Paint paint) {
		int centerX = (cellX + cellBX) / 2;
		int centerY = (cellY + cellBY) / 2;
		int squareX = centerX - (shapeSide / 2 - 4);
		int squareY = centerY - (shapeSide / 2 - 4);
		int squareBX = centerX + (shapeSide / 2 - 4);
		int squareBY = centerY + (shapeSide / 2 - 4);
		canvas.drawRect(squareX, squareY, squareBX, squareBY, paint);
	}

	/**
	 * draw shape: circle
	 * @param canvas  canvas obj
	 * @param cellX   left-top pointX
	 * @param cellY   left-top pointY
	 * @param cellBX  right-bottom pointX
	 * @param cellBY  right-bottom pointY
	 * @param paint   paint obj
	 */
	private void drawCircle(Canvas canvas, int cellX, int cellY, int cellBX,
			int cellBY, Paint paint) {
		canvas.drawCircle((cellX + cellBX) / 2, (cellY + cellBY) / 2,
				shapeSide / 2 - 4, paint);
	}

	/**
	 * draw shape: triangle
	 * @param canvas  canvas obj
	 * @param cellX   left-top pointX
	 * @param cellY   left-top pointY
	 * @param cellBX  right-bottom pointX
	 * @param cellBY  right-bottom pointY
	 * @param paint   paint obj
	 */
	private void drawTriangle(Canvas canvas, int cellX, int cellY, int cellBX,
			int cellBY, Paint paint) {
		int centerX = (cellX + cellBX) / 2;
		int centerY = (cellY + cellBY) / 2;
		int point1X = centerX;
		int point1Y = centerY - (shapeSide / 2 - 4);
		int point2X = centerX - (shapeSide / 2 - 4);
		int point2Y = centerY + (shapeSide / 2 - 4);
		int point3X = centerX + (shapeSide / 2 - 4);
		int point3Y = centerY + (shapeSide / 2 - 4);

		Path path = new Path();
		path.moveTo(point1X, point1Y);
		path.lineTo(point2X, point2Y);
		path.moveTo(point2X, point2Y);
		path.lineTo(point3X, point3Y);
		path.moveTo(point3X, point3Y);
		path.lineTo(point1X, point1Y);
		path.close();
		canvas.drawPath(path, paint);
	}
	
	/**
	 * draw shape: rectangle
	 * @param canvas  canvas obj
	 * @param cellX   left-top pointX
	 * @param cellY   left-top pointY
	 * @param cellBX  right-bottom pointX
	 * @param cellBY  right-bottom pointY
	 * @param paint   paint obj
	 */
	private void drawRectangle(Canvas canvas, int cellX, int cellY, int cellBX,
			int cellBY, Paint paint) {
		int recX = cellX + shapeSide/4;
		int recY = cellY + shapeSide/4;
		int recBX = cellBX - shapeSide/4;
		int recBY = cellBY - shapeSide/4;
		canvas.drawRect(recX, recY, recBX, recBY, paint);
	}
	
	public int getCurrentShapeIndex() {
		return currentShapeIndex;
	}
}