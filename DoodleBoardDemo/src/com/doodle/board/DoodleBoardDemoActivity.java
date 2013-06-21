package com.doodle.board;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DoodleBoardDemoActivity extends Activity {
	private SurfaceView mSurfaceView = null;
	private SurfaceHolder mSurfaceHolder = null;
	private Button cleanButton = null;
	private Button colorButton = null;
	private float oldX = 0f;
	private float oldY = 0f;
	private boolean canDraw = false;
	private Paint mPaint = null;
	// 用来记录当前是哪一种颜色
	private int whichColor = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mSurfaceView = (SurfaceView) findViewById(R.id.surfaceview);
		mSurfaceHolder = mSurfaceView.getHolder();
		mPaint = new Paint();
		// 画笔的颜色
		mPaint.setColor(Color.RED);
		// 画笔的粗细
		mPaint.setStrokeWidth(2.0f);
		cleanButton = (Button) this.findViewById(R.id.flushbutton);
		// 按钮监听
		cleanButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 锁定整个SurfaceView
				Canvas mCanvas = mSurfaceHolder.lockCanvas();
				mCanvas.drawColor(Color.BLACK);
				// 绘制完成，提交修改
				mSurfaceHolder.unlockCanvasAndPost(mCanvas);
				// 重新锁一次
				mSurfaceHolder.lockCanvas(new Rect(0, 0, 0, 0));
				mSurfaceHolder.unlockCanvasAndPost(mCanvas);
			}
		});
		colorButton = (Button) this.findViewById(R.id.colorbutton);
		// 按钮监听
		colorButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Dialog dialog = new AlertDialog.Builder(DoodleBoardDemoActivity.this).setTitle("颜色设置")
						.setSingleChoiceItems(new String[] { "红色", "绿色", "蓝色" }, whichColor, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								switch (which) {
								case 0:
									// 画笔的颜色
									mPaint.setColor(Color.RED);
									whichColor = 0;
									break;
								case 1:
									// 画笔的颜色
									mPaint.setColor(Color.GREEN);
									whichColor = 1;
									break;
								case 2:
									// 画笔的颜色106
									mPaint.setColor(Color.BLUE);
									whichColor = 2;
									break;

								default:
									break;
								}
							}
						}).setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						}).create();
				dialog.show();
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 获取x坐标
		float x = event.getX();
		// 获取y坐标（不知道为什么要减去一个偏移值才对得准屏幕）
		float y = event.getY() - 50;
		// 第一次进来先不管
		if (canDraw) {
			// 获取触屏事件
			switch (event.getAction()) {
			// 如果是拖动事件
			case MotionEvent.ACTION_MOVE: {
				// 锁定整个SurfaceView
				Canvas mCanvas = mSurfaceHolder.lockCanvas();
				mCanvas.drawLine(x, y, oldX, oldY, mPaint);
				mSurfaceHolder.unlockCanvasAndPost(mCanvas);
				// 重新锁一次
				mSurfaceHolder.lockCanvas(new Rect(0, 0, 0, 0));
				mSurfaceHolder.unlockCanvasAndPost(mCanvas);
				break;
			}
			}
		}
		// 保存目前的x坐标值
		oldX = x;
		// 保存目前的y坐标值
		oldY = y;
		canDraw = true;
		return true;
	}
}