package info.devfiles.postman.engine;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class MultithreadQueueConsumer<T> implements QueueConsumer<T> {

	private BlockingQueue<T> queue;
	
	private ExecutorService executorService;
	
	public MultithreadQueueConsumer(BlockingQueue<T> queue, int nThreads) {
		this.queue = queue;
		Executors.newFixedThreadPool(nThreads);
	}
	
	@Override
	public void start() {
		int poolSize = ((ThreadPoolExecutor) executorService).getMaximumPoolSize();
		for (int i = 0; i < poolSize; i++) {
			executorService.submit(() -> {
				for (;;) {
					try {
						T obj = queue.take();
						handle(obj);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						break;
					}
				}
			});
		}
	}
	
	@Override
	public void stop(long timeout, TimeUnit unit) throws InterruptedException {
		executorService.shutdownNow();
		executorService.awaitTermination(timeout, unit);
	}
	
}
