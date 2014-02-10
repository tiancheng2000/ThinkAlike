package com.thinkalike.android.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.thinkalike.R;
import com.thinkalike.android.control.ImageNodeView;
import com.thinkalike.generic.common.Constant;
import com.thinkalike.generic.common.LogTag;
import com.thinkalike.generic.common.Util;
import com.thinkalike.generic.event.PropertyChangeEvent;
import com.thinkalike.generic.event.PropertyChangeListener;
import com.thinkalike.generic.viewmodel.WorkareaViewModel;
import com.thinkalike.generic.viewmodel.control.INodeView;
import com.thinkalike.generic.viewmodel.control.UINode;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link ItemListActivity} in two-pane mode (on tablets) or a
 * {@link CanvasActivity} on handsets.
 */
public class WorkareaFragment extends Fragment {

	//-- Constants and Enums --------------------------
	public static final String TAG = WorkareaFragment.class.getSimpleName();
	//public static final String ARG_ITEM_ID = "item_id";
	
	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events --------------------------	
	//-- Instance and Shared Fields --------------------------	
	// GUI controls
	//private TextView _tv_content;
	//private ImageView _iv_content;
	private ProgressDialog _pd;
	private ImageNodeView _inv_nodeselected;
	private Button _btn_OK;
	
	// FragmentCallbacks

	// MVVM
	private WorkareaViewModel _viewModel = null;
	/**
	 * listen to relative ViewModel. SHOULD be an instance variable.<br>
	 * Registration side (ViewModel) will only keep listener's WeakReference, so that View can be safely released.
	 */
	private PropertyChangeListener _listenToVM = null; 
														
	//-- Properties --------------------------
	//-- Constructors --------------------------
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public WorkareaFragment() {
	}

	//-- Destructors --------------------------
	//-- Base Class Overrides --------------------------
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Util.trace(LogTag.LifeCycleManagement, String.format("%s: onCreate", getClass().getSimpleName()));
		super.onCreate(savedInstanceState);

		final WorkareaFragment thisInstance = this;
		if(_viewModel == null){
			_viewModel = WorkareaViewModel.getInstance();
			_listenToVM = new PropertyChangeListener(){
				@Override
				public void onPropertyChanged(PropertyChangeEvent event) {
					Util.trace(LogTag.ViewModel, String.format("[IProperty] PropertyChanged(name=%s, value=%s, listener=%s)", event.getPropertyName(), event.getNewValue(), thisInstance.getClass().getSimpleName()));
					if (event.getPropertyName().equals(Constant.PropertyName.Node)){
						updateWorkarea((UINode)event.getNewValue());
					}
					else if (event.getPropertyName().equals(Constant.PropertyName.IsBusy)){
						//turn on/off circular progress bar 
						showProgressBar((Boolean)event.getNewValue());
					}
				}
			};
			_viewModel.addPropertyChangeListener(Constant.PropertyName.Node, _listenToVM);
		}

//		if (getArguments().containsKey(ARG_ITEM_ID)) {
//			// Load the dummy content specified by the fragment
//			// arguments. In a real-world scenario, use a Loader
//			// to load content from a content provider.
//			String item_id = getArguments().getString(ARG_ITEM_ID);
//			_item = _viewModel.getItem(item_id);
//		}
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Util.trace(LogTag.LifeCycleManagement, String.format("%s: onCreateView", getClass().getSimpleName()));
		View rootView = inflater.inflate(R.layout.workarea,
				container, false); //kw: 3rd param must be false -- Android SDK

		//initialize Editor's canvas based on DOs (Page[])
		_btn_OK = (Button) rootView.findViewById(R.id.btn_ok); 
		_btn_OK.setVisibility(View.INVISIBLE);
		_inv_nodeselected = (ImageNodeView) rootView.findViewById(R.id.iv_nodeselected); 
		//NOTE: if ImageView/ImageNodeView is instantiated dynamically without its dimensions specified, use the following callback instead. 

//		if (_item != null) {
//			final String imagePath = (String)_item.getProperty(Constant.PropertyName.Content);
//			_iv_content.setImageDrawable(getResources().getDrawable(R.drawable.placeholder_image)); //default image (inner resource)
//			//IMPROVE: (in onCreateView()) rootView.getWidth()/getHeight() or container.getWidth()/getHeight() can only get 0 value.
//			//IMPROVE: (in onResume()) _iv_content's w/h cannot be retrieved even here.., even recursively to its parents.
//			//int width_image = Util.getActualLayoutWidth(_iv_content);
//			//int height_image = Util.getActualLayoutHeight(_iv_content);
//			_iv_content.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener(){
//				@Override
//				public boolean onPreDraw() {
//					_iv_content.getViewTreeObserver().removeOnPreDrawListener(this);
//					int width_image = Util.getActualLayoutWidth(_iv_content);
//					int height_image = Util.getActualLayoutHeight(_iv_content);
//					Util.trace(null, LogTag.ResourceThread, "(width_image,height_image)=("+width_image+","+height_image+")");
//					//Bitmap contentImage = (Bitmap)MediaManager.getInstance().get(contentText, width_image, height_image, true); //only use this for commonly used media
//					//iv_content.setImageBitmap(contentImage);
//					MediaAsyncLoader.asyncLoadImageFile(
//							Util.appendPath(Config.getPhotoResourcePath(), imagePath),
//							width_image, height_image, _onResourceLoadListener);
//					_viewModel.setBusy(true); //if we can also put ImageView into ViewModel, isBusy flag can be encapsulated within ImageView.loadBitmap()
//
//					return true;
//				}
//			});
//		}

		return rootView;
	}

	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		Util.trace(LogTag.LifeCycleManagement, String.format("%s: onViewCreated", getClass().getSimpleName()));
		super.onViewCreated(view, savedInstanceState);

		//_viewModel.onXxx();
	}
	
	//-- Public and internal Methods --------------------------	
	//-- Private and Protected Methods --------------------------	
	private void showProgressBar(boolean bFlag) {
		if (bFlag){
			_pd = ProgressDialog.show(this.getActivity(), this.getActivity().getString(R.string.loading), "");
		}
		else{
			if(_pd!=null){
				Util.trace(LogTag.ViewModel, "ProgressBar turned off");
				_pd.dismiss();
				_pd = null;
			}
		}
	}

	private void updateWorkarea(UINode uiNode){
		if (uiNode == null){
			_inv_nodeselected.setImageBitmap(null);
			_btn_OK.setVisibility(View.INVISIBLE);
			return;
		}

		INodeView nodeView = uiNode.getView();
		if(nodeView==null){
			nodeView = uiNode.attachView(_inv_nodeselected); //attach and update uiData to an existed raw ImageView 
			_btn_OK.setVisibility(View.VISIBLE);
		}
		else if (!(nodeView instanceof View)){
			//...
		}
		else {
			Util.error(TAG, "updateWorkarea(): uiNode's view type is incompatible: " + ((nodeView==null)?null:nodeView.getClass().getSimpleName()));
			return;
		}
//		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//		rlp.addRule(RelativeLayout.CENTER_IN_PARENT);
//		_rl_workarea.addView((View)nodeView, rlp);
//		//nodeView.onParentViewChanged(_rl_workarea);
//		//Otherwise, use INodeView.attachedToView(_rl_workarea) instead.
	}
	
	//-- Event Handlers --------------------------

}

