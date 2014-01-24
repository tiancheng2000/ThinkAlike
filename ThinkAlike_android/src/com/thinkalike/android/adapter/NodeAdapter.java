package com.thinkalike.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.thinkalike.android.common.Constant;
import com.thinkalike.generic.common.Util;
import com.thinkalike.generic.viewmodel.control.INodeView;
import com.thinkalike.generic.viewmodel.control.UINode;

public class NodeAdapter extends BaseAdapter {
	//-- Constants and Enums ----------------------------------------------
	public static final String TAG = NodeAdapter.class.getSimpleName();
	
	//-- Inner Classes and Structures -------------------------------------
	//-- Delegates and Events ---------------------------------------------
	//-- Instance and Shared Fields ---------------------------------------
	protected Context _context;
	protected UINode[] _uiNodes;
	
	//-- Properties -------------------------------------------------------
	//-- Constructors -----------------------------------------------------
	public NodeAdapter(Context context, UINode[] uiNodes) {
		assert (uiNodes != null);
		_context = context;
		_uiNodes = uiNodes;
	}
	
	//-- Destructors ------------------------------------------------------
	//-- Base Class Overrides ---------------------------------------------
	@Override
	public int getCount() {
		return _uiNodes.length;
	}
	@Override
	public Object getItem(int position) {
		return _uiNodes[position];
	}
	@Override
	public long getItemId(int position) {
		return position; 
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Util.trace(TAG, "getView:+ " + position + " " + convertView);
		UINode uiNode = _uiNodes[position];
		if (convertView == null) {
			convertView = (View) uiNode.getView();
			if (convertView == null) {
				//1.DO -> View, general to all kinds of Node
				convertView = (View) uiNode.createView();
				convertView.setTag(uiNode);
				//2.ListView related layout setting, some differ on NodeType (e.g.Text cells, Image cells)
				if(convertView instanceof ImageView){
					convertView.setLayoutParams(new AbsListView.LayoutParams(parent.getWidth(), 
							parent.getHeight()/Constant.NodeSelector.NUM_CELLS_IN_NODELIST_VERT));
					((ImageView)convertView).setScaleType(ScaleType.FIT_CENTER);
				}
			}
		} else {
			UINode tag = (UINode) convertView.getTag();
			if (tag != uiNode) {
				uiNode.attachView(((INodeView) convertView));
				convertView.setTag(uiNode);
			}
		}
		//uncommon style must be reset here (e.g.black-white background switch for Text cells)
		return convertView;
	}
	
	//-- Public and internal Methods --------------------------------------
	//-- Private and Protected Methods ------------------------------------
	//-- Event Handlers ---------------------------------------------------
}
