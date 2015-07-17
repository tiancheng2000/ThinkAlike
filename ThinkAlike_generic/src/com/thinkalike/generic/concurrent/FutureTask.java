package com.thinkalike.generic.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class FutureTask<Params, Result> implements Callable<Result>, RunnableFuture<Result>{

	//-- Constants and Enums -----------------------------------
	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	//private WeakReference<Params[]> _paramsRef; 
	private java.util.concurrent.FutureTask<Result> _delegateFT;
	private Params[] _params;
	
	//-- Properties --------------------------------------------
	private ProgressPublishListener _listenerProgressPublish;
	public final void setProgressListener(ProgressPublishListener l){
		_listenerProgressPublish = l;
	}

	//-- Constructors ------------------------------------------
	@SuppressWarnings("unchecked")
	public FutureTask(Params... params){
		final FutureTask<Params, Result> thisInstance = this;
		Callable<Result> callable = new Callable<Result>(){
			@Override
			public Result call() throws Exception {
				return thisInstance.call(_params);
			}
		};
		_delegateFT = new java.util.concurrent.FutureTask<Result>(callable);
		_params = params;
	}
	
	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	/**
	 * Runs on new thread after the task being executed by an {@link #AsyncExecutor}. This
	 * method will then invoke {@link #call(Params... params)}.
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 *  
	 * @see java.lang.Runnable#run()
	 */
	public final Result call() throws InterruptedException, ExecutionException {
		_delegateFT.run();
		return _delegateFT.get();
	}
	
    public final void run() {
    	_delegateFT.run();
    }
    public Result get() throws InterruptedException, ExecutionException {
        return _delegateFT.get();
    }
    public Result get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
    	return _delegateFT.get(timeout, unit);
    }

    public boolean isCancelled() {
        return _delegateFT.isCancelled();
    }

    public boolean isDone() {
        return _delegateFT.isDone();
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        return _delegateFT.cancel(mayInterruptIfRunning);
    }

    
    //-- Public and internal Methods ---------------------------
	//-- Private and Protected Methods -------------------------
	/**
	 * Override this method to define the task which will run on a new thread. The 
	 * specified parameters are the parameters passed to the constructor.  
	 * 
	 * @param params The parameters of the task.
	 * @return A result, defined by the subclass of this task.
	 */
	@SuppressWarnings("unchecked")
	protected abstract Result call(Params... params);

	/**
	 * Call this method in the overidden call() method to update progress.   
	 */
	protected final void publishProgress(Integer... values){
		if(_listenerProgressPublish!=null){
			_listenerProgressPublish.onProgressPublished(values);
		}
	}

	//-- Event Handlers ----------------------------------------
	public void onSuccess(Result result){
	}
	
	public void onError(int errCode){
	}
	
	public void onCancelled(){
	}

	public void onProgressPublished(Long... progress){
	}

}
