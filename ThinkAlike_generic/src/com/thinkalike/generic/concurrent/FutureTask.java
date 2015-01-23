package com.thinkalike.generic.concurrent;

import java.util.concurrent.Callable;

public abstract class FutureTask<Params, Result> implements Callable<Result>{

	//-- Constants and Enums -----------------------------------
	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	//private WeakReference<Params[]> _paramsRef; 
	private Params[] _params;
	
	//-- Properties --------------------------------------------
	//-- Constructors ------------------------------------------
	public FutureTask(Params... params){
		_params = params;
	}
	
	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	/**
	 * Runs on new thread after the task being executed by an {@link #AsyncExecutor}. This
	 * method will then invoke {@link #call(Params... params)}.
	 *  
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public final Result call() {
		return call(_params);
	}
	
	//-- Public and internal Methods ---------------------------
	/**
	 * Override this method to define the task which will run on a new thread. The 
	 * specified parameters are the parameters passed to the constructor.  
	 * 
	 * @param params The parameters of the task.
	 * @return A result, defined by the subclass of this task.
	 */
	@SuppressWarnings("unchecked")
	protected abstract Result call(Params... params);

	//-- Private and Protected Methods -------------------------
	//-- Event Handlers ----------------------------------------
	public void onSuccess(Result result){
	}
	
	public void onError(int errCode){
	}
	
	public void onCancelled(){
	}

}
