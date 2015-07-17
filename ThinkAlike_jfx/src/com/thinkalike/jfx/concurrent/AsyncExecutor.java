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

package com.thinkalike.jfx.concurrent;

import java.util.ArrayDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.concurrent.Task;

import com.thinkalike.generic.common.LogTag;
import com.thinkalike.generic.common.Util;
import com.thinkalike.generic.concurrent.Executor;
import com.thinkalike.generic.concurrent.FutureTask;


public class AsyncExecutor<Result> implements Executor<Result>{

	//-- Constants and Enums -----------------------------------
	private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
	private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
	private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
	private static final int KEEP_ALIVE = 1;
	
	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	/*
	 * Usage of THREAD_POOL_EXECUTOR and SERIAL_EXECUTOR are referenced from android.os.AsyncTask
	 */
    private static final ThreadFactory _threadFactory = new ThreadFactory() {
        private final AtomicInteger _count = new AtomicInteger(1);
        public Thread newThread(Runnable r) {
            return new Thread(r, "AsyncTask #" + _count.getAndIncrement()); //yet another AsyncTask
        }
    };

    private static final BlockingQueue<Runnable> _poolWorkQueue =
            new LinkedBlockingQueue<Runnable>(128);
	
	private static final java.util.concurrent.Executor THREAD_POOL_EXECUTOR
		= new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
					TimeUnit.SECONDS, _poolWorkQueue, _threadFactory);
	
    public static final java.util.concurrent.Executor SERIAL_EXECUTOR = new SerialExecutor();
    private static class SerialExecutor implements java.util.concurrent.Executor {
        final ArrayDeque<Runnable> _tasks = new ArrayDeque<Runnable>();
        Runnable _active;

        public synchronized void execute(final Runnable r) {
            _tasks.offer(new Runnable() {
                public void run() {
                    try {
                        r.run();
                    } finally {
                        scheduleNext();
                    }
                }
            });
            if (_active == null) {
                scheduleNext();
            }
        }

        protected synchronized void scheduleNext() {
            if ((_active = _tasks.poll()) != null) {
                THREAD_POOL_EXECUTOR.execute(_active);
            }
        }
    }
   
    private static volatile java.util.concurrent.Executor _defaultExecutor = SERIAL_EXECUTOR;
	
	//-- Properties --------------------------------------------
	//-- Constructors ------------------------------------------
	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	@Override
	public void execute(Runnable task, Result result) {
		final Runnable thisTask = task;
		final Result thisResult = result;
		Task<Result> asyncTask = new Task<Result>(){

			@Override
			protected Result call() throws Exception {
				//NOTE: task.run() should be responsible for setting the result into the input variable "result"
				//      ref:java.util.concurrent.Executors.RunnableAdapter<T>
				thisTask.run();
				return thisResult;
			} 
			
			@Override
			protected void succeeded() {
				super.succeeded();
				if (thisTask instanceof FutureTask){
					((FutureTask<?,Result>)thisTask).onSuccess(thisResult);
					//TODO: how to distinguish between onError(errCode). "Result" must be a composite class such as 
					//(Real Result + errCode) in order to solve this problem?...
				}
			}

			@Override
			protected void cancelled() {
				super.cancelled();
				if (thisTask instanceof FutureTask){
					((FutureTask<?, ?>)thisTask).onCancelled();
				}
			}

			@Override
			protected void failed() {
				super.failed();
			}

			@Override
			protected void updateProgress(long workDone, long max) {
				super.updateProgress(workDone, max);
				if (thisTask instanceof FutureTask){  
					((FutureTask<?, ?>)thisTask).onProgressPublished(Long.valueOf(workDone), Long.valueOf(max)); 
				}
			}  
			
		};
		_defaultExecutor.execute(asyncTask);

	}

	@Override
	public void execute(Callable<Result> task) {
		final Callable<Result> thisTask = task;
		Task<Result> asyncTask = new Task<Result>(){

			Result _result = null;

			@Override
			protected Result call() throws Exception {
				try {
					_result = thisTask.call();
				} catch (Exception e) {
					Util.error(LogTag.GenericThread, e.getMessage());
				}
				return _result;
			} 
			
			@Override
			protected void succeeded() {
				super.succeeded();
				if (thisTask instanceof FutureTask){
					((FutureTask<?,Result>)thisTask).onSuccess(_result);
					//TODO: how to distinguish between onError(errCode). "Result" must be a composite class such as 
					//(Real Result + errCode) in order to solve this problem?...
				}
			}

			@Override
			protected void cancelled() {
				super.cancelled();
				if (thisTask instanceof FutureTask){
					((FutureTask<?, ?>)thisTask).onCancelled();
				}
			}

			@Override
			protected void failed() {
				super.failed();
			}

			@Override
			protected void updateProgress(long workDone, long max) {
				super.updateProgress(workDone, max);
				if (thisTask instanceof FutureTask){  
					((FutureTask<?, ?>)thisTask).onProgressPublished(Long.valueOf(workDone), Long.valueOf(max)); 
				}
			}  
			
		};
		_defaultExecutor.execute(asyncTask);
		
	}
	
  	//-- Public and internal Methods ---------------------------
	//-- Private and Protected Methods -------------------------
	//-- Event Handlers ----------------------------------------
}
