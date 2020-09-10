package com.alibaba.learning;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@SpringBootTest
@Slf4j
class LearningApplicationTests {
	/* 读取文件数据的线程数量,并创建此容量的线程池 */
	private static final int THREAD_COUNT = 10;
	private static final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
	/* 创建Semaphore对象实例，构造函数的参数指定信号量的数目，为了方便说明问题，设为3 */
	private final Semaphore semaphore = new Semaphore(3);
	private final CountDownLatch latch=new CountDownLatch(THREAD_COUNT);

	@Test
	void contextLoads() {
	}
	@Test
	void testSemaphore() throws InterruptedException {
		/* 创建线程读取数据，并尝试获取数据库连接，将数据存储到数据库中 */
		for(int i = 0;i < THREAD_COUNT;i++){
			executorService.execute(() -> {
				try {
					/*从远程读数据*/
					log.info("{} will reading data from remote host",Thread.currentThread().getName());
					Thread.sleep(100);
					/* 通过acquire 函数获取数据库连接，如果成功将数据存储到数据库 */
					semaphore.acquire();
					log.info("{} got semaphore*******",Thread.currentThread().getName());
					Thread.sleep((long) (Math.random()*1000));
				}catch (InterruptedException e){
					e.printStackTrace();
				}finally {
					/* 最终使用release 函数释放信号量 */
					semaphore.release();
					log.info("{} released semaphore========",Thread.currentThread().getName());
					latch.countDown();
				}
			});
		}
		latch.await();
	}

}
