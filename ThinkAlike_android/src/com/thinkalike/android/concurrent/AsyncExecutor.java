/**
* Copyright 2013-2015 Tiancheng Hu
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
		AsyncTask<Void, Long, Result> async = new AsyncTask<Void, Long, Result>(){
			protected Result doInBackground(Void... params) {
				//NOTE: task.run() should be responsible for setting the result into the input variable "result"
				//      ref:java.util.concurrent.Executors.RunnableAdapter<T>
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

			@Override
			protected void onProgressUpdate(Long... values) {
				if (thisTask instanceof FutureTask){  
					((FutureTask<?, ?>)thisTask).onProgressPublished(values);
				}
			}

		}; 
		async.execute();
	}

	@Override
	public void execute(Callable<Result> task) {
		final Callable<Result> thisTask = task;
		AsyncTask<Void, Long, Result> async = new AsyncTask<Void, Long, Result>(){
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
			
			@Override
			protected void onProgressUpdate(Long... values) {
				if (thisTask instanceof FutureTask){  
					((FutureTask<?, ?>)thisTask).onProgressPublished(values);
				}
			}
		}; 
		async.execute();
	}
	
	//-- Public and internal Methods ---------------------------
	//-- Private and Protected Methods -------------------------
	//-- Event Handlers ----------------------------------------
}
