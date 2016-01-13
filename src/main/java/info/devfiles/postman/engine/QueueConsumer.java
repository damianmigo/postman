package info.devfiles.postman.engine;

import java.util.concurrent.TimeUnit;

public abstract class QueueConsumer<T> {

	private volatile boolean running = false;
	
	public synchronized void start() {
		if (running) {
			throw new IllegalStateException("queue consumer has been already started");
		}
		doStart();
		running = true;
	}
	
	protected abstract void doStart();
	
	public synchronized void stop(long timeout, TimeUnit unit) throws InterruptedException {
		if (!running) {
			throw new IllegalStateException("queue consume has not been started");
		}
		doStop(timeout, unit);
		running = false;
	}
	
	protected abstract void doStop(long timeout, TimeUnit unit) throws InterruptedException;
	
	public abstract void handle(T obj);
	
}
