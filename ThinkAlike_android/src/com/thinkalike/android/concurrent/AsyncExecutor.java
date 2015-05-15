package com.thinkalike.android.concurrent;

import java.util.concurrent.Callable;

import android.os.AsyncTask;

import com.thinkalike.generic.common.LogTag;
import com.thinkalike.generic.common.Util;
import com.thinkalike.generic.concurrent.Executor;
import com.thinkalike.generic.concurrent.FutureTask;

public class AsyncExecutor<Result> implements Executor<Result>{

	//-- Constants and Enums -----------------------------------
	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	//-- Properties --------------------------------------------
	//-- Constructors ------------------------------------------
	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	@Override
	public void execute(Runnable task, Result result) {
		final Runnable thisTask = task;
		final Result thisResult = result;
		AsyncTask<Void, Integer, Result> async = new AsyncTask<Void, Integer, Result>(){
			protected Result doInBackground(Void... params) {
				thisTask.run();
				return thisResult;
			}

			@Override
			protected void onPostExecute(Result result) {
				if (thisTask instanceof FutureTask){
					((FutureTask<?,Result>)thisTask).onSuccess(result);
					//TODO: how to distinguish between onError(errCode). "Result" must be a composite class such as 
					//(Real Result + errCode) in order to solve this problem?...
				}
			}
			
			@Override
			protected void onCancelled() {
				if (thisTask instanceof FutureTask){  
					((FutureTask<?, ?>)thisTask).onCancelled();
				}
			}
		}; 
		async.execute();
	}

	@Override
	public void execute(Callable<Result> task) {
		final Callable<Result> thisTask = task;
		AsyncTask<Void, Integer, Result> async = new AsyncTask<Void, Integer, Result>(){
			protected Result doInBackground(Void... params) {
				Result result = null;
				try {
					result = thisTask.call();
				} catch (Exception e) {
					Util.error(LogTag.GenericThread, e.getMessage());
				}
				return result;
			}

			@Override
			protected void onPostExecute(Result result) {
				if (thisTask instanceof FutureTask){
					((FutureTask<?,Result>)thisTask).onSuccess(result);
					//TODO: how to distinguish between onError(errCode). "Result" must be a composite class such as 
					//(Real Result + errCode) in order to solve this problem?...
				}
			}
			
			@Override
			protected void onCancelled() {
				if (thisTask instanceof FutureTask){
					((FutureTask<?, ?>)thisTask).onCancelled();
				}
			}
		}; 
		async.execute();
	}
	
	//-- Public and internal Methods ---------------------------
	//-- Private and Protected Methods -------------------------
	//-- Event Handlers ----------------------------------------
}
