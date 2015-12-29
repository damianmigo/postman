package info.devfiles.postman.engine;

import java.util.concurrent.TimeUnit;

public interface QueueConsumer<T> {

	void start();
	
	void stop(long timeout, TimeUnit unit) throws InterruptedException;
	
	void handle(T obj);
	
}
