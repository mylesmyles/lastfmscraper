package net.mylesputnam.lastfm.api.requests;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

public class RequestSchedulerTest {
	private static final long acceptableMsLatencyPerThread = 5;

	@Test
	public void testFewThreadsOnTimeWithShortDelay() {
		long delayMs = 25;
		int threadCount = 5;
		RequestScheduler scheduler = RequestScheduler.createWithRequestDelay(delayMs);
		long timeTaken = getTimeTakenForThreadsToFinish(threadCount, scheduler);
		
		long minMs = getMinimumAcceptableMs(delayMs, threadCount);
		long maxMs = getMaximumAcceptableMs(delayMs, threadCount);
		
		assertOnTime(timeTaken, minMs, maxMs);
	}
	
	@Test
	public void testFewThreadsOnTimeWithDefaultDelay() {
		int threadCount = 3;
		RequestScheduler scheduler = RequestScheduler.createWithDefaultRequestDelay();
		long timeTaken = getTimeTakenForThreadsToFinish(threadCount, scheduler);
		
		long minMs = getMinimumAcceptableMs(RequestScheduler.DEFAULT_REQUEST_DELAY_MS, threadCount);
		long maxMs = getMaximumAcceptableMs(RequestScheduler.DEFAULT_REQUEST_DELAY_MS, threadCount);
		
		assertOnTime(timeTaken, minMs, maxMs);
	}
	
	@Test
	public void testFewThreadsOnTimeWithLongDelay() {
		long delayMs = 666;
		int threadCount = 2;
		RequestScheduler scheduler = RequestScheduler.createWithRequestDelay(delayMs);
		long timeTaken = getTimeTakenForThreadsToFinish(threadCount, scheduler);
		
		long minMs = getMinimumAcceptableMs(delayMs, threadCount);
		long maxMs = getMaximumAcceptableMs(delayMs, threadCount);
		
		assertOnTime(timeTaken, minMs, maxMs);
	}
	
	@Test
	public void testManyThreadsOnTimeWithVeryShortDelay() {
		long delayMs = 3;
		int threadCount = 333;
		RequestScheduler scheduler = RequestScheduler.createWithRequestDelay(delayMs);
		long timeTaken = getTimeTakenForThreadsToFinish(threadCount, scheduler);
		
		long minMs = getMinimumAcceptableMs(delayMs, threadCount);
		long maxMs = getMaximumAcceptableMs(delayMs, threadCount);
		
		assertOnTime(timeTaken, minMs, maxMs);
	}
	
	@Test
	public void testFirstThreadIsNearlyInstant() {
		long delayMs = 1000;
		int threadCount = 1;
		RequestScheduler scheduler = RequestScheduler.createWithRequestDelay(delayMs);
		long timeTaken = getTimeTakenForThreadsToFinish(threadCount, scheduler);
		
		long minMs = getMinimumAcceptableMs(delayMs, threadCount);
		long maxMs = getMaximumAcceptableMs(delayMs, threadCount);
		
		assertOnTime(timeTaken, minMs, maxMs);
	}
	
	@Test
	public void testFifo() throws InterruptedException {
		long delayMs = 75;
		int threadCount = 10;
		
		RequestScheduler scheduler = RequestScheduler.createWithRequestDelay(delayMs);
		LinkedBlockingQueue<Integer> threadResultsInOrder = new LinkedBlockingQueue<>();
		CountDownLatch onThreadsComplete = new CountDownLatch(threadCount);
		
		for(int i = 0; i < threadCount; i++) {
			final int iterationNum = i;
			new Thread(() -> {
				try {
					scheduler.reserveRequest();
					threadResultsInOrder.add(iterationNum);
				}
				catch(InterruptedException e) {}
				onThreadsComplete.countDown();
			}).start();
			
			Thread.sleep(2);
		}
		
		try {
			onThreadsComplete.await(threadCount*delayMs + 1000, TimeUnit.MILLISECONDS);
		}
		catch (InterruptedException e) {
			throw new RuntimeException("Test was interrupted while waiting for threads to complete!");
		}
		
		Assert.assertEquals(threadCount, threadResultsInOrder.size());
		for(int i = 0; i < threadCount; i++) {
			Assert.assertEquals("A result came back out of order! Results: " + threadResultsInOrder.toString(),
					i, (int)threadResultsInOrder.poll());
		}
	}
	
	@Test
	public void testInterruptedThreadsAreRemovedFromRequestQueue() throws InterruptedException {
		long delayMs = 200;
		int threadCount = 10;
		
		RequestScheduler scheduler = RequestScheduler.createWithRequestDelay(delayMs);
		LinkedBlockingQueue<Integer> threadResultsInOrder = new LinkedBlockingQueue<>();
		
		List<Thread> waitingThreads = new LinkedList<>();
		for(int i = 0; i < threadCount; i++) {
			final int iterationNum = i;
			waitingThreads.add(new Thread(() -> {
				try {
					scheduler.reserveRequest();
					threadResultsInOrder.add(iterationNum);
				}
				catch(InterruptedException e) {}
			}));
		}
		
		for(Thread thread : waitingThreads) {
			thread.start();
		}
		
		Thread.sleep(delayMs + delayMs/2);
		
		int completedThreads = 0;
		for(Thread thread : waitingThreads) {
			if(!thread.isAlive()) {
				completedThreads++;
			}
			else {
				thread.interrupt();
			}
		}
		
		Thread.sleep(delayMs + delayMs/2);
		
		Assert.assertEquals(0, scheduler.threadsWaiting());
		Assert.assertEquals(completedThreads, threadResultsInOrder.size());
	}
	
	
	private void assertOnTime(long actualMs, long minMs, long maxMs) {
		String wasTooFast = "Task was too fast: Minimum time: " + minMs + "ms, Actual time: " + actualMs + "ms";
		String wasTooSlow = "Task was too slow: Maximum time: " + minMs + "ms, Actual time: " + actualMs + "ms";
		
		Assert.assertTrue(wasTooFast, actualMs >= minMs);
		Assert.assertTrue(wasTooSlow, actualMs <= maxMs);
	}

	private long getMaximumAcceptableMs(long delayMs, int threadCount) {
		int threadsThatWillBeWaiting = threadCount-1;
		return threadsThatWillBeWaiting*delayMs + threadCount*acceptableMsLatencyPerThread;
	}
	
	private long getMinimumAcceptableMs(long delayMs, int threadCount) {
		int threadsThatWillBeWaiting = threadCount-1;
		return threadsThatWillBeWaiting*delayMs;
	}
	
	private long getTimeTakenForThreadsToFinish(int threadCount, RequestScheduler scheduler) {
		List<Callable<Integer>> waitingThreads = generateWaitingThreads(threadCount, scheduler);
		ExecutorService threadRunner = Executors.newFixedThreadPool(7);
		
		long startTimeMs = System.currentTimeMillis();
		try {
			threadRunner.invokeAll(waitingThreads);
		}
		catch (InterruptedException e) {}
		long endTimeMs = System.currentTimeMillis();
		
		return endTimeMs - startTimeMs;
	}
	
	private List<Callable<Integer>> generateWaitingThreads(int threadCount, final RequestScheduler requestScheduler) {
		List<Callable<Integer>> waitingThreads = new LinkedList<>();
		
		for(int i = 0; i < threadCount; i++) {
			waitingThreads.add(getRequestCallable(requestScheduler, i));
		}
		
		return waitingThreads;
	}
	
	private Callable<Integer> getRequestCallable(final RequestScheduler requestScheduler, int numToReturn) {
		return () -> {
			requestScheduler.reserveRequest();
			return numToReturn;
		};
	}
}
