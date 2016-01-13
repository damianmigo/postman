package info.devfiles.postman.engine;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class MultithreadQueueConsumer<T> extends QueueConsumer<T> {
	
	static final Logger logger = LogManager.getLogger(MultithreadQueueConsumer.class);

	private BlockingQueue<T> queue;
	
	private ExecutorService executorService;
	
	public MultithreadQueueConsumer(BlockingQueue<T> queue, int nThreads) {
		this.queue = queue;
		executorService = Executors.newFixedThreadPool(nThreads);
		logger.info("Initialized MultithreadQueueConsumer with " + nThreads + " threads");
	}
	
	@Override
	public void doStart() {
		
		logger.info("Starting threads");
		
		int poolSize = ((ThreadPoolExecutor) executorService).getMaximumPoolSize();
		for (int i = 0; i < poolSize; i++) {
			executorService.submit(() -> {
				logger.info("Consumer started");
				for (;;) {
					try {
						T obj = queue.take();
						handle(obj);
					} catch (InterruptedException e) {
						logger.info("Thread has been interrupted");
						Thread.currentThread().interrupt();
						break;
					} catch (Exception e) {
						logger.error("An unexpected error ocurred", e);
					}
				}
			});
		}
	}
	
	@Override
	public void doStop(long timeout, TimeUnit unit) throws InterruptedException {
		logger.info("Shutting down MultithreadQueueConsumer ..."); 
		executorService.shutdownNow();
		executorService.awaitTermination(timeout, unit);
		logger.info("MultithreadQueueConsumer has been stopped"); 
	}
	
}
