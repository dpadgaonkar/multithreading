package multithreading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelThreadDemo {

	public static void main(String[] args) {
		
		int noOfThreads = Runtime.getRuntime().availableProcessors();
		System.out.println("Number of processors available: "+noOfThreads);
		int []num = new int []{1,2,3,4,5,6,7,8,9,10,11};
		ThreadManager tm = new ThreadManager();
		tm.initialize(num, noOfThreads);
	}

}

/*
 * Normal threads without executor service
 */
/*class ThreadManager{
	
	ThreadWorker []threads;
	void initialize(int []num, int noOfThreads) {
		threads = new ThreadWorker[noOfThreads];
		int chunkSize = (int)Math.ceil((num.length*1.0/noOfThreads));
		System.out.println("Chunk Size :"+chunkSize);
		for(int i = 0; i < noOfThreads; i++) {
			ThreadWorker worker = new ThreadWorker(num, i*chunkSize, (i+1)*chunkSize);
			Thread t = new Thread(worker);
			t.start();
			try {
				t.join();
			}catch(InterruptedException ie) {
				
			}
			threads[i] = worker;
		}
		
		int total = 0;
		for(int i = 0; i < noOfThreads; i++) {
			total = total + threads[i].getPatialSum(); 
		}
		System.out.println("Total: "+total);
	}	
}*/

/*
 * Normal threads with executor service
 */

class ThreadManager{
	
	ThreadWorker []threads;
	void initialize(int []num, int noOfThreads) {
		System.out.println("Using executor service");
		threads = new ThreadWorker[noOfThreads];
		ExecutorService execService = Executors.newFixedThreadPool(noOfThreads);
		int chunkSize = (int)Math.ceil((num.length*1.0/noOfThreads));
		System.out.println("Chunk Size :"+chunkSize);
		for(int i = 0; i < noOfThreads; i++) {
			ThreadWorker worker = new ThreadWorker(num, i*chunkSize, (i+1)*chunkSize);
			threads[i] = worker;
			execService.submit(worker);
		}
		execService.shutdown();
		try {
			execService.awaitTermination(10, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int total = 0;
		for(int i = 0; i < noOfThreads; i++) {
			total = total + threads[i].getPatialSum(); 
		}
		System.out.println("Total: "+total);

	}	
}


class ThreadWorker implements Runnable{
	
	private int[]arr;
	int low;
	int high;
	int partialSum;
	
	public ThreadWorker() {
	}
	
	public ThreadWorker(int []arr, int low, int high) {
		this.arr = arr;
		this.low = low;
		this.high = high;
	}
	

	@Override
	public void run() {
		partialSum = 0;
		for(int i = low; i < high && i < arr.length; i++) {
			partialSum = partialSum + arr[i];
		}
		System.out.println(Thread.currentThread().getName()+" sum is: "+partialSum);
	}
	
	public int getPatialSum() {
		return partialSum;
	}
	
}
