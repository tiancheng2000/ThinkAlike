package com.thinkalike.android.control;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thinkalike.R;

//IMPROVE: created during development of ThinkAlike prototype, a lot of details could be grain-fined. 
public class MessageBox {
	//-- Constants and Enums ----------------------------------------------
	public static final String TAG = MessageBox.class.getSimpleName();
	
	//-- Inner Classes and Structures -------------------------------------
	//-- Delegates and Events ---------------------------------------------
	//-- Instance and Shared Fields ---------------------------------------
	private Dialog _dialog;
	private Map<Object, Button> _btns = new HashMap<Object, Button>();
	
	//-- Properties -------------------------------------------------------
	public Dialog getDialog() {return _dialog;}
	public void setDialog(Dialog dialog) {this._dialog = dialog;}
	public Map<Object, Button> getBtns() {return _btns;}
	public void setBtns(Map<Object, Button> btns) {this._btns = btns;}
	
	//-- Constructors -----------------------------------------------------
	//-- Destructors ------------------------------------------------------
	//-- Base Class Overrides ---------------------------------------------
	//-- Public and internal Methods --------------------------------------
	public static MessageBox showDialog(Context context, Object titleText, Object contentText, Object leftText, Object rightText, boolean force) {
		final MessageBox messageBox = new MessageBox();
		
		View dialogView = LayoutInflater.from(context).inflate(R.layout.messagebox, null);
		
		TextView title = (TextView) dialogView.findViewById(R.id.title);
		title.setText(parseParam(context, titleText));
		
		TextView content = (TextView) dialogView.findViewById(R.id.content);
		if(contentText instanceof View) { 
			LinearLayout contLayout = (LinearLayout) content.getParent();
			contLayout.removeView(content); 
			contLayout.addView((View) contentText); 
		} else { 
			content.setText(parseParam(context, contentText));
		}
		
		Button left = (Button) dialogView.findViewById(R.id.left);
		left.setText(parseParam(context, leftText));
		if(context instanceof View.OnClickListener) {
			left.setOnClickListener((OnClickListener) context);
		}else{
			left.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss(messageBox); 
				}
			});
		}
		messageBox._btns.put(leftText, left); 
		
		Button right = (Button) dialogView.findViewById(R.id.right);
		if(rightText == null) { 
			LinearLayout rightParent = (LinearLayout) right.getParent();
			LinearLayout bottomLayout = (LinearLayout)rightParent.getParent(); 
			bottomLayout.removeView(rightParent); 
		} else {
			right.setText(parseParam(context, rightText));
			
			if(force) { 
				if(context instanceof View.OnClickListener) {
					right.setOnClickListener((OnClickListener) context);
				}
			} else {
				right.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						dismiss(messageBox); 
					}
				});
			}
			messageBox._btns.put(rightText, right); 
		}
		
		Dialog dialog = new AlertDialog.Builder(context).create();
		dialog.show(); 
		
		dialog.getWindow().setContentView(dialogView); 
		messageBox._dialog = dialog;
		return messageBox;
	}
	public static MessageBox showDialog(Context context, Object titleText, Object contentText, Object leftText, Object rightText) {
		return showDialog(context, titleText, contentText, leftText, rightText, false);
	}
	public static MessageBox showDialog(Context context, Object titleText, Object contentText, Object leftText) {
		return showDialog(context, titleText, contentText, leftText, null);
	}
	public static Button getBtn(MessageBox messageBox, Object key) {
		if(messageBox == null) {
			return null;
		} else {
			return messageBox._btns.get(key);
		}
	}
	public static void dismiss(MessageBox messageBox) {
		if(messageBox != null && messageBox._dialog!=null && messageBox._dialog.isShowing()) {
			messageBox._dialog.dismiss();
		}
	}
	
	//-- Private and Protected Methods ------------------------------------
	private static String parseParam(Context context, Object msg) {
		if(msg instanceof Integer) {
			int resId = Integer.parseInt(msg.toString());
			msg = context.getString(resId);
		}
		return msg.toString();
	}
	
	//-- Event Handlers ---------------------------------------------------
}
