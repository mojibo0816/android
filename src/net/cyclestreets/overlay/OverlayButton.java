package net.cyclestreets.overlay;

import net.cyclestreets.util.Brush;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

class OverlayButton
{
	private final Drawable img_;
	private final Rect pos_;
	private final float radius_;
	private boolean enabled_;
	private boolean pressed_;
	
	private boolean rightAlign_;
	
	public OverlayButton(final Drawable image, final int left, final int top, final float curveRadius)
	{
		img_ = image;
        pos_ = new Rect(left, 
        				top, 
        				left + image.getIntrinsicWidth(), 
        				top + image.getIntrinsicHeight());
        radius_ = curveRadius;
        enabled_ = true;
        pressed_ = false;
        rightAlign_ = false;
	} // OverlayButton
	
	public void enable(final boolean on) { enabled_ = on; }
	public void pressed(final boolean on) { pressed_ = on; }
	
	public void rightAlign() { rightAlign_ = true; }
	
	public int right() { return pos_.right;	}
	public int height() { return pos_.height(); }
	
	public void draw(final Canvas canvas)
	{
        final Rect screen = canvas.getClipBounds();
        
        if(rightAlign_)
        	reflectPosition(screen);
        
        screen.offset(pos_.left, pos_.top);
        screen.right = screen.left + pos_.width();
        screen.bottom = screen.top + pos_.height();
	    
        final RectF rounded = new RectF(screen);
		canvas.drawRoundRect(rounded, radius_, radius_, enabled_ ? Brush.White : Brush.LightGrey);
		
		if(enabled_ && pressed_)
		{
			shrinkAndDrawInner(canvas, rounded, Brush.LightGrey);
			shrinkAndDrawInner(canvas, rounded, Brush.White);
		} // if ...

        img_.setBounds(screen);
        img_.draw(canvas);
	} // drawButton
	
	private void reflectPosition(final Rect screen) 
	{
		int width = pos_.width();
		pos_.left = (screen.width() - pos_.width()) - pos_.left;
		pos_.right = pos_.left + width;
		rightAlign_ = false;
	} // leftOffset
	
	private void shrinkAndDrawInner(final Canvas canvas, final RectF rect, final Paint brush)
	{
		rect.left += 4;
		rect.top += 4;
		rect.right -= 4;
		rect.bottom -= 4;
        canvas.drawRoundRect(rect, radius_, radius_, brush);	 
	} // shrinkAndDrawInner
	
	public boolean hit(final MotionEvent event)
	{
		if(!enabled_)
			return false;
		
		int x = (int)event.getX();
		int y = (int)event.getY();
		
		return pos_.contains(x, y);
	} // contains
} // class OverlayButton