package com.summer.main.drawingtool;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Drawing Tool activity
 * 
 * @author summer
 * @version 1.0
 */
public class DrawingActivity extends Activity {
	
	/**Tag Obj for Log*/
	private final String TAG = DrawingActivity.class.getSimpleName();

	/** DrawingView obj */
	DrawingCanvasView drawingView = null;
	
	/**PaletteToolView obj */
	PaletteToolView paletteView = null;
	
	/**ShapeToolView obj*/
	ShapeToolView shapeView = null;
	
	/**a file to save snapshot*/
	File file = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//no title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_drawing);
		
		drawingView = (DrawingCanvasView) findViewById(R.id.drawing_view);
		paletteView = (PaletteToolView) findViewById(R.id.paletteTool_view);
		shapeView = (ShapeToolView) findViewById(R.id.shapeTool_view);
		drawingView.setPaletteToolView(paletteView);
		drawingView.setShapeToolView(shapeView);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return drawingView.onTouchEvent(event) ||paletteView.onTouchEvent(event) 
				|| shapeView.onTouchEvent(event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.drawing, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * save snapshot from screen
	 * @param v
	 */
	public void onSave(View v) {
		
		if(isExternalStorageWritable())
		{
			file = drawingView.getFilePath(Environment.getExternalStorageDirectory()
				.getAbsolutePath()+File.separator+"summer",getString(R.string.fileName));
			drawingView.onSaveImage(this, file);
			Log.d(TAG, "succeed to save snapShot");
		}
		else
		{
			Log.d(TAG, "no write permission for writing extenal storage");
		}
	}
	
	/**
	 * Checks if external storage is available for read and write
	 * @return boolean
	 */
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	/**
	 * send Email
	 * @param v
	 */
	public void onSendEmail(View v)
	{
		if(file == null || !file.exists())
		{
			this.onSave(v);
		}
		
		// Build the intent
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);    
		emailIntent.setType("application/octet-stream");  
		String[] emailReciver = new String[]{};    
		String  emailTitle = this.getString(R.string.emailTitle); 
		String emailContent = this.getString(R.string.emailContent);
		
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, emailReciver);  
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, emailTitle);  
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, emailContent);  
		emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
		
		// Verify it resolves
		PackageManager packageManager = getPackageManager();
		List<ResolveInfo> activities = packageManager.queryIntentActivities(emailIntent, 0);
		boolean isIntentSafe = activities.size() > 0;

		if(isIntentSafe)
		{
			startActivity(emailIntent);
			Log.d(TAG, "succeed to send Email");
		}
	}
	
	/**
	 * clear canvas
	 * @param v
	 */
	public void onReset(View v) {
		TextView resetBtn = (TextView) findViewById(R.id.resetButton);
		drawingView.onResetCanvas(resetBtn);
	}
	
	/**
	 * Exit App
	 * @param v
	 */
	public void onExit(View v) {
		String exitStr = this.getString(R.string.exitApp);
		String confirm = this.getString(R.string.confirm);
		String action_ok = this.getString(R.string.action_ok);
		String action_cancel = this.getString(R.string.action_cancel);
		AlertDialog alertDialog = new AlertDialog.Builder(this)
				.setTitle(exitStr)
				.setMessage(confirm)
				.setPositiveButton(action_ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
								System.exit(0);
							}

						}).setNegativeButton(action_cancel,

				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				}).create();
		alertDialog.show();
	}
}