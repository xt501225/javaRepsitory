package com.summer.main.drawingtool;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Draw shape: draw circle, square and triangle
 * @author summer
 * @version 1.0
 */
public class DrawAction {
	public int color;
	protected int size = 2;
	DrawAction() {
		color = Color.BLACK;
	}

	DrawAction(int color) {
		this.color = color;
	}

	public void draw(Canvas canvas) {
	}

	public void move(float mx, float my) {

	}
}

/**
 * draw square function
 */
class DrawSquare extends DrawAction {
	float startX;
	float startY;
	float stopX;
	float stopY;

	DrawSquare() {
		startX = 0;
		startY = 0;
		stopX = 0;
		stopY = 0;
	}

	DrawSquare(float x, float y, int color,int size) {
		super(color);
		startX = x;
		startY = y;
		stopX = x;
		stopY = y;
		this.size = size;
	}

	public void draw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(color);
		paint.setStrokeWidth(size);
		float shapeSide = Math.max(stopX - startX, stopY - startY);
		float centerX = (startX + stopX) / 2;
		float centerY = (startY + stopY) / 2;
		float squareX = centerX - (shapeSide / 2 - 4);
		float squareY = centerY - (shapeSide / 2 - 4);
		float squareBX = centerX + (shapeSide / 2 - 4);
		float squareBY = centerY + (shapeSide / 2 - 4);
		canvas.drawRect(squareX, squareY, squareBX, squareBY, paint);
	}

	public void move(float mx, float my) {
		stopX = mx;
		stopY = my;
	}
}

/**
 * draw circle function
 * 
 */
class DrawCircle extends DrawAction {
	float startX;
	float startY;
	float stopX;
	float stopY;
	float radius;

	DrawCircle() {
		startX = 0;
		startY = 0;
		stopX = 0;
		stopY = 0;
		radius = 0;
	}

	DrawCircle(float x, float y, int color,int size) {
		super(color);
		startX = x;
		startY = y;
		stopX = x;
		stopY = y;
		radius = 0;
		this.size = size;
	}

	public void draw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(color);
		paint.setStrokeWidth(size);
		canvas.drawCircle((startX + stopX) / 2, (startY + stopY) / 2, radius,
				paint);
	}

	public void move(float mx, float my) {
		stopX = mx;
		stopY = my;
		radius = (float) ((Math.sqrt((mx - startX) * (mx - startX)
				+ (my - startY) * (my - startY))) / 2);
	}
}

/**
 * draw circle function
 * 
 */
class DrawTriangle extends DrawAction {
	float startX;
	float startY;
	float stopX;
	float stopY;
	float radius;

	DrawTriangle() {
		startX = 0;
		startY = 0;
		stopX = 0;
		stopY = 0;
	}

	DrawTriangle(float x, float y, int color,int size) {
		super(color);
		startX = x;
		startY = y;
		stopX = x;
		stopY = y;
		this.size = size;
	}

	public void draw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(color);
		paint.setStrokeWidth(size);
		
		float shapeSide = Math.max(stopX - startX, stopY - startY);
		float centerX = (startX + stopX) / 2;
		float centerY = (startY + stopY) / 2;
		float point1X = centerX;
		float point1Y = centerY - (shapeSide / 2 - 4);
		float point2X = centerX - (shapeSide / 2 - 4);
		float point2Y = centerY + (shapeSide / 2 - 4);
		float point3X = centerX + (shapeSide / 2 - 4);
		float point3Y = centerY + (shapeSide / 2 - 4);

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

	public void move(float mx, float my) {
		stopX = mx;
		stopY = my;
	}
}

/**
 * Draw rectangle
 *
 */
class DrawRectangle extends DrawAction{
	float startX;
	float startY;
	float stopX;
	float stopY;
	int size;
	
	DrawRectangle(){
		startX=0;
		startY=0;
		stopX=0;
		stopY=0;
	}
	
	DrawRectangle(float x,float y,int color,int size){
		super(color);
		startX=x;
		startY=y;
		stopX=x;
		stopY=y;
		this.size=size;
	}
	
	public void draw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(color);
		paint.setStrokeWidth(size);
		canvas.drawRect(startX, startY, stopX, stopY, paint);
	}
	
	public void move(float mx,float my){
		stopX=mx;
		stopY=my;
	}
}