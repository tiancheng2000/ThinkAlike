/**
* Copyright 2013-2014 Tiancheng Hu
* 
* Licensed under the GNU Lesser General Public License, version 3.0 (LGPL-3.0, the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
*     http://opensource.org/licenses/lgpl-3.0.html
*     
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.thinkalike.android.control;

import java.lang.reflect.Array;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.thinkalike.R;

//IMPROVE: 1.implement a generic version "Abstract View/Control"(UIComboBox) 2.use generics (e.g."<NodeType>") 
public class ComboBox<T> extends LinearLayout{
	//-- Constants and Enums -----------------------------------
	//-- Inner Classes and Structures --------------------------
	
	//-- Delegates and Events ----------------------------------
	public interface ComboBoxListener<T> {
		public void onSelectedItemChanged(T itemValue);
	}
	
	//-- Instance and Shared Fields ----------------------------
	private LinearLayout _ll_root; //layout root
	private ImageView _iv_imageButton_base; //template control created by UI designer
	private ImageView[] _iv_imageButtons;
	private int[] _imageResIds;
	private T[] _itemValues;
	private int _idx_default = 0;
	private ComboBoxListener<T> _comboBoxListener;
	private Animation _showAnimation;
	private boolean _isExpanded;	//the status in which all selectable items are shown
	private View.OnClickListener _listenToButtonClick;

	//-- Properties --------------------------------------------
	
	//-- Constructors ------------------------------------------
	public ComboBox(Context context, int[] imageResIds, T[] itemValues, T defaultValue) {
		super(context);
		assert(imageResIds!=null && itemValues!=null && imageResIds.length==itemValues.length && defaultValue!=null);
		
		LayoutInflater.from(context).inflate(R.layout.talib_combobox, this, true);
		_ll_root = (LinearLayout) findViewById(R.id.ll_root);
		_iv_imageButton_base = (ImageView) findViewById(R.id.iv_imageButton_base);
		
		//1.listener to click event of image buttons
		_listenToButtonClick = new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				int id_selected = v.getId();
				int idx_selected = ((Integer)v.getTag()).intValue();

				//FD: 1.1st item: a.(when collapsed)act as a currently selected item  b.(when expended)act as a controller 
				//    2.other items: if clicked, collapse the combobox and activate onSelectItemChange()
				if(!_isExpanded){ // && id_selected==0){
					assert(id_selected==0);
					_isExpanded = true;
					setImageAndValue(_iv_imageButtons[0], 0);
					expand();
					return;
				}
				
				_isExpanded = false;
				_iv_imageButtons[0].setImageResource(_imageResIds[idx_selected]);
				_idx_default = idx_selected;
				collapse();

				if (_comboBoxListener != null)
					_comboBoxListener.onSelectedItemChanged(_itemValues[_idx_default]);
			}
		};
		
		//2.initialize image buttons (including register listener)
		_imageResIds = new int[imageResIds.length];
		System.arraycopy(imageResIds, 0, _imageResIds, 0, imageResIds.length);
		//_itemValues = new T[itemValues.length]; //ref:What's the reason I can't create generic array types in Java  - Stack Overflow
		_itemValues = (T[]) Array.newInstance(itemValues[0].getClass(), itemValues.length);
		System.arraycopy(itemValues, 0, _itemValues, 0, itemValues.length);
		for (int i=0; i<_itemValues.length; i++){
			if(_itemValues[i]==defaultValue){
				_idx_default = i;
				break;
			}
		}
		initView();
	}

	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	
	//-- Public and internal Methods ---------------------------
	public void registerListener(ComboBoxListener<T> listener) {
		_comboBoxListener = listener;
	}

	//IMPROVE: setRelativePosition()
	//public setRelativePosition(x, y)
	
	//-- Private and Protected Methods -------------------------
	@SuppressWarnings("deprecation")
	private void initView() {
		//FD: 1.1st item: a.(when collapsed)act as a currently selected item  b.(when expended)act as a controller 
		//    2.other items: if clicked, collapse the combobox and activate onSelectItemChange()
		_iv_imageButtons = new ImageView[_imageResIds.length];
		for(int i=0; i<_iv_imageButtons.length; i++){
			_iv_imageButtons[i] = new ImageView(this.getContext());
			//copy styles from template control (but ImageView is not Cloneable)
			_iv_imageButtons[i].setLayoutParams(_iv_imageButton_base.getLayoutParams());
			_iv_imageButtons[i].setPadding(_iv_imageButton_base.getPaddingLeft(), _iv_imageButton_base.getPaddingTop(),
					_iv_imageButton_base.getPaddingRight(), _iv_imageButton_base.getPaddingBottom());
			_iv_imageButtons[i].setBackgroundDrawable(_iv_imageButton_base.getBackground()); //Android 3.1 compatible
			//set image content from resource files, and set index
			if(i==0){
				_iv_imageButtons[i].setId(0);
				setImageAndValue(_iv_imageButtons[i], _idx_default);
				_iv_imageButtons[i].setVisibility(View.VISIBLE);
			}
			else{
				_iv_imageButtons[i].setId(i);
				setImageAndValue(_iv_imageButtons[i], i);
				_iv_imageButtons[i].setVisibility(View.INVISIBLE);
			}
			//set listeners
			_iv_imageButtons[i].setOnClickListener(_listenToButtonClick);
			//load to container
			_ll_root.addView(_iv_imageButtons[i], i);
		}
		
		_isExpanded = false;
		//setInvisible();
	}

	private void setImageAndValue(ImageView iv, int idx) {
		iv.setImageResource(_imageResIds[idx]);
		iv.setTag(idx);
	}

	private void collapse() {
		for(int i=1; i<_iv_imageButtons.length; i++){
			_iv_imageButtons[i].setVisibility(View.INVISIBLE);
		}
	}

	private void expand() {
		for(int i=1; i<_iv_imageButtons.length; i++){
			_iv_imageButtons[i].setVisibility(View.VISIBLE);
		}
		animateExpanding();
	}

	private void animateExpanding() {
		_showAnimation = AnimationUtils.loadAnimation(this.getContext(), R.anim.talib_fade_in);
		for(int i=1; i<_iv_imageButtons.length; i++){
			_iv_imageButtons[i].startAnimation(_showAnimation);
		}
	}
	
	//-- Event Handlers ----------------------------------------
}
