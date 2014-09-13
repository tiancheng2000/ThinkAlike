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

package com.hscardref.android.view;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.hscardref.android.adapter.NodeAdapter;
import com.hscardref.generic.viewmodel.NodeSelectorViewModel;
import com.hscardref.R;
import com.thinkalike.android.control.ComboBox;
import com.thinkalike.android.control.ComboBox.ComboBoxListener;
import com.hscardref.generic.common.Constant;
import com.thinkalike.generic.common.LogTag;
import com.thinkalike.generic.common.Util;
import com.thinkalike.generic.domain.NodeType;
import com.thinkalike.generic.event.PropertyChangeEvent;
import com.thinkalike.generic.event.PropertyChangeListener;
import com.thinkalike.generic.viewmodel.control.UINode;

/**
 * A list fragment representing a list of Node. This fragment also supports
 * tablet devices by allowing node item to be drag&dropped onto a
 * {@link WorkareaFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link FragmentCallbacks}
 * interface.
 */
public class NodeSelectorFragment extends Fragment implements OnItemClickListener{

	//-- Constants and Enums --------------------------
	private static final int[] ImageResIds = new int[]{R.drawable.btn_typea, R.drawable.btn_typeb, R.drawable.btn_typec, R.drawable.btn_typed, R.drawable.btn_typee, R.drawable.btn_typef, R.drawable.btn_typeg, R.drawable.btn_typeh, R.drawable.btn_typei, R.drawable.btn_typej, R.drawable.btn_typek}; 
	private static final NodeType[] ItemValues = new NodeType[]{NodeType.TypeA, NodeType.TypeB, NodeType.TypeC, NodeType.TypeD, NodeType.TypeE, NodeType.TypeF, NodeType.TypeG, NodeType.TypeH, NodeType.TypeI, NodeType.TypeJ, NodeType.TypeK};
	private static final NodeType DefaultValue = NodeType.TypeA;
	/**
	 * The serialization (saved instance state) Bundle key representing xxx
	 */
	//IMPROVE: confirm is it true that all required info is managed by ViewModel and needn't to be serialized  
	private static final String STATE_XXX = "xxx";

	//-- Inner Classes and Structures --------------------------
	//Timothy [D]: don't use such platform-dependent route for event dispatching, use EventBus instead. 
//	/**
//	 * A callback interface that all activities containing this fragment must implement. 
//	 * This mechanism allows activities to be notified of item selections.
//	 */
//	//IMPROVE: unify name convention, "XxxCallbacks" or "XxxListener" or something like
//	public interface FragmentCallbacks {
//		/**
//		 * Callback for when an item has been selected.
//		 */
//		public void onNodeSelected(String id);
//	}

	//-- Delegates and Events --------------------------	
	//-- Instance and Shared Fields --------------------------
	//NOTE: manage instance variables of Activity/Fragment by using onSavedInstanceState() -- Android LifeCycle Management
	//private FragmentCallbacks _listenerFromActivity = null; //relative activity listen to us
	private ComboBox<NodeType> _nodeTypeSelector;
	private NodeType _currentNodeType = DefaultValue; //managed as savedInstanceState
	private ListView _lv_nodeList;
	//MVVM
	private NodeSelectorViewModel _viewModel = null;
	private PropertyChangeListener _listenToVM = null;  //listen to relative ViewModel. SHOULD be a instance variable. 
														  //Registration side (ViewModel) will only keep their WeakReference.

	//-- Properties --------------------------	
	//-- Constructors --------------------------
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public NodeSelectorFragment() {
	}
	
	//-- Destructors --------------------------	
	//-- Base Class Overrides --------------------------
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Util.trace(LogTag.LifeCycleManagement, String.format("%s: onCreate", getClass().getSimpleName()));
		super.onCreate(savedInstanceState);
		
		//FD: 1.ICommand: calls onNodeTypeChanged() -- ref:onViewCreated()
		//    2.IPropery: observe PropertyName.NodeList changes
		if(_viewModel == null){
			_viewModel = NodeSelectorViewModel.getInstance();
			//IMPROVE: if it's possible that parent activity changes, then the inner NodeAdapter should be 
			//  recreated or get notified (e.g. implement a NodeSelectorVMListenerBase managing uiContext and nodeType). 
			final NodeSelectorFragment thisInstance = this;
			//listen to relative ViewModel.
			_listenToVM = new PropertyChangeListener(){
				@Override
				public void onPropertyChanged(PropertyChangeEvent event) {
					Util.trace(LogTag.ViewModel, String.format("[IProperty]::View PropertyChanged(name=%s, value=%s, listener=%s)", event.getPropertyName(), event.getNewValue(), thisInstance.getClass().getSimpleName()));
					assert(event.getPropertyName().equals(Constant.PropertyName.NodeList));
					@SuppressWarnings("unchecked")
					List<UINode> nodeList = (List<UINode>)event.getNewValue();
					thisInstance.updateNodeList(nodeList);
				}
			};
			_viewModel.addPropertyChangeListener(Constant.PropertyName.NodeList, _listenToVM);
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Util.trace(LogTag.LifeCycleManagement, String.format("%s: onCreateView", getClass().getSimpleName()));
		View rootView = inflater.inflate(R.layout.nodeselector,
				container, false); //kw: 3rd param must be false -- Android SDK
		
		RelativeLayout rl_nodeselector = (RelativeLayout) rootView.findViewById(R.id.rl_nodeselector);
		if(savedInstanceState!=null){
			_currentNodeType = (NodeType)savedInstanceState.getSerializable("currentNodeType"); //enum is serializable
		}
		_nodeTypeSelector = new ComboBox<NodeType>(this.getActivity(), ImageResIds, ItemValues, _currentNodeType);
		_nodeTypeSelector.registerListener(new ComboBoxListener<NodeType>(){
			@Override
			public void onSelectedItemChanged(NodeType nodeType) {
				Util.trace(LogTag.ViewModel, String.format("[ICommand]::View NodeType changed(=%s)", nodeType));
				if(_viewModel != null){
					_viewModel.onNodeTypeChanged(nodeType);
					_currentNodeType = nodeType;
				}
			}
		});
		rl_nodeselector.addView(_nodeTypeSelector);
		_lv_nodeList = (ListView) rootView.findViewById(R.id.lv_nodelist);
		_lv_nodeList.setOnItemClickListener(this);

		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		Util.trace(LogTag.LifeCycleManagement, String.format("%s: onViewCreated", getClass().getSimpleName()));
		super.onViewCreated(view, savedInstanceState);

		// Restore the previously serialized ... info.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_XXX)) {
			//...
		}
		
		//_viewModel.onRefreshNodeList();
	}

	@Override
	public void onResume() {
		_viewModel.onNodeTypeChanged(_currentNodeType);
		_viewModel.onRefreshNodeList();
		super.onResume();
	}

	@Override
	public void onAttach(Activity activity) {
		Util.trace(LogTag.LifeCycleManagement, String.format("%s: onAttach", getClass().getSimpleName()));
		super.onAttach(activity);

		//ViewModel may check type of parent Activity here. 
//		if (!(activity instanceof FragmentCallbacks)) {
//			throw new IllegalStateException(
//					"Activity must implement fragment's callbacks.");
//		}

		//_listenerFromActivity = (FragmentCallbacks) activity;
	}

	@Override
	public void onDetach() {
		Util.trace(LogTag.LifeCycleManagement, String.format("%s: onDetach", getClass().getSimpleName()));
		super.onDetach();

		//// Reset the active callbacks interface to null.
		//_listenerFromActivity = null;
	}
	
	@Override
	public void onDestroy() {
		Util.trace(LogTag.LifeCycleManagement, String.format("%s: onDestroy", getClass().getSimpleName()));
		super.onDestroy();
		_viewModel.removePropertyChangeListener(Constant.PropertyName.NodeList, _listenToVM);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		//NOTE: onSaveInstanceState() is not always called (such as when a user navigates back from activity B to activity A). 
		//       ref:http://forums.xamarin.com/discussion/6103/onsave-restoreinstancestate-by-back-button-pressing-not-called-but-destroyed  @TomOpgenorth
		Util.trace(LogTag.LifeCycleManagement, String.format("%s: onSaveInstanceState", getClass().getSimpleName()));
		outState.putSerializable("currentNodeType", _currentNodeType); //enum is serializable
		super.onSaveInstanceState(outState);
	}
	
	//-- Public and internal Methods --------------------------	
	//-- Private and Protected Methods --------------------------
	private void updateNodeList(List<UINode> uiNodeList){
		UINode[] uiNodes = new UINode[0];
		//kw: ArrayList.toArray() without specifying element type will convert blank ArrayList<T> to a blank Object[], which will causes type cast exception.
		_lv_nodeList.setAdapter(new NodeAdapter(this.getActivity(), uiNodeList.toArray(uiNodes)));
		//(UINode[])(_viewModel.getUINodeList().toArray())));
	}

	//-- Event Handlers --------------------------	
/*	kw: oops.. ListView.setOnItemSelectedListener() doesn't act as you think! use onItemClickListener instead. 
 *      ref:http://stackoverflow.com/questions/2454337/why-is-my-onitemselectedlistener-not-called-in-a-listview
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}
*/
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		assert(parent.getAdapter() instanceof NodeAdapter);
		NodeAdapter adapter = (NodeAdapter)parent.getAdapter();
		UINode uiNode = (UINode)adapter.getItem(position);
		Util.trace(LogTag.ViewModel, String.format("[ICommand]::View Node selected(=%s)", uiNode));
		if(_viewModel!=null)
			_viewModel.onNodeSelected(uiNode);
	}

}
