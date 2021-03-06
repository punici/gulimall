package com.punici.gulimall.search.thread;

import java.util.concurrent.*;

public class ThreadTest
{
    public static ExecutorService service = Executors.newFixedThreadPool(10);
    
    public static void main(String[] args) throws ExecutionException, InterruptedException
    {
        // CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
        // System.out.println("当前线程：" + Thread.currentThread().getId());
        // int i = 10 / 2;
        // System.out.println("结果运行：" + i);
        // }, service);
        
        // CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
        // System.out.println("当前线程：" + Thread.currentThread().getId());
        // int i = 10 / 0;
        // System.out.println("结果运行：" + i);
        // return i;
        // }, service).whenComplete((res, exception) -> {
        // //虽然能得到异常信息，但但是没法修改返回数据
        // System.out.println("异步任务调用完成,结果是:"+res+"异常是："+exception);
        // }).exceptionally(throwable -> {
        // // 可以感知异常，同时返回默认值
        // return 1000;
        // });
        /**
         * 方法执行完成后处理 无论失败还是成功
         */
        // CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
        // System.out.println("当前线程：" + Thread.currentThread().getId());
        // int i = 10 / 0;
        // System.out.println("结果运行：" + i);
        // return i;
        // }, service).handle((res, exception) -> {
        // if(Objects.nonNull(res))
        // {
        // return res * 20;
        // }
        // if(Objects.nonNull(exception))
        // {
        // return 0;
        // }
        // return 0;
        // });
        
        /**
         * 线程串行化 1) thenRun不能获取上一步的执行结果 无返回值 2) thenAccept能接受上一步结果，无返回值 3)thenApply能接受上一步结果，有返回值
         */
        /**
         * CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
         * System.out.println("当前线程：" + Thread.currentThread().getId()); int i = 10 / 2;
         * System.out.println("结果运行：" + i); return i; }, service).thenApply((res) -> {
         * System.out.println("任务2启动了"); System.out.println("结果是" + res); return "Hello" + res; });
         * System.out.println(future.get()); // Integer integer = future.get(); //
         * System.out.println(integer);
         */
        /**
         * 两个都完成
         */
        // CompletableFuture<Object> future1 = CompletableFuture.supplyAsync(() -> {
        // System.out.println("任务1,当前线程：" + Thread.currentThread().getId());
        // int i = 10 / 2;
        // System.out.println("任务1,结果运行：" + i);
        // return i;
        // }, service);
        //
        // CompletableFuture<Object> future2 = CompletableFuture.supplyAsync(() -> {
        // System.out.println("任务2开始,当前线程：" + Thread.currentThread().getId());
        //
        // try
        // {
        // Thread.sleep(3000);
        // }
        // catch (InterruptedException e)
        // {
        // e.printStackTrace();
        // }
        // System.out.println("任务2结束");
        // return "hello";
        // }, service);
        
        // future1.runAfterBothAsync(future2, () -> {
        // //不能感知前两步的结果
        // System.out.println("任务3开始");
        // }, service);
        
        // future1.thenAcceptBothAsync(future2, (f1, f2) -> {
        // // 能感知前两步的结果
        // System.out.println("-----------");
        // System.out.println("任务3开始");
        // System.out.println(f1);
        // System.out.println(f2);
        // }, service);
        
        // CompletableFuture<String> combineAsync = future1.thenCombineAsync(future2, (f1, f2) -> {
        // // 能感知前两步的结果
        // System.out.println("-----------");
        // System.out.println("任务3开始");
        // System.out.println(f1);
        // System.out.println(f2);
        // return f1 + ":" + f2 + "->hello";
        // }, service);
        /**
         * 只要一个执行完就执行3
         */
        // future1.runAfterEitherAsync(future2, () -> {
        // 不感知上两部结果
        // System.out.println("任务3执行");
        // }, service);
        
        // future1.acceptEitherAsync(future2, (res) -> {
        // //能感知结果，没有返回值
        // System.out.println("任务3执行" + res);
        // }, service);
        //
        // CompletableFuture<String> result = future1.applyToEitherAsync(future2, (res) -> {
        // //能感知结果，没有返回值
        // System.out.println("任务3执行,结果" + res);
        // return res.toString();
        // }, service);
        //
        // System.out.println(result.get());
        
        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
            System.out.println("查询商品的图片信息");
            return "hello.jpg";
        }, service);
        
        CompletableFuture<String> future4 = CompletableFuture.supplyAsync(() -> {
            System.out.println("查询商品的属性");
            return "hello.jpg";
        }, service);
        
        CompletableFuture<String> future5 = CompletableFuture.supplyAsync(() -> {
            try
            {
                Thread.sleep(5000);
                System.out.println("查询商品的介绍");
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            
            return "hello.jpg";
        }, service);
        
        /**
         * 所有都做完
         */
        CompletableFuture<Void> future = CompletableFuture.allOf(future3, future4, future5);
        
        //future.get();// 等待所有任务做完
        
        System.out.println("main end");
    }
    
    public static void thread(String[] args) throws ExecutionException, InterruptedException
    {
        System.out.println("开始");
        
        // 方法一
        // Thread01 thread01 = new Thread01();
        // thread01.start();
        
        // 方法二
        // Runable01 runable01 = new Runable01();
        // new Thread(runable01).run();
        
        // 方法三
        // FutureTask<Integer> futureTask = new FutureTask<>(new Callable01());
        // new Thread(futureTask).start();
        // //阻塞 前边执行完毕，才可以执行获取返回值这个方法。
        // //等待整个线程执行完成，获取返回结果。
        // Integer integer = futureTask.get();
        // System.out.println(integer);
        
        // 我们以后的业务代码里面，以上三种启动线程的方式禁止使用。
        // 【将所有多线异步任务都交给线程池执行】
        // new Thread(()->System.out.println("hello")).start();
        
        // 区别：
        // 1 2 不能得到返回值
        // 3 可以得到返回值
        // 1 2 3 都不能控制资源
        // 4 可以控制资源，性能稳定
        // service.execute(new Runable01());
        /**
         * corePoolSize 保留在池中的线程数 即使处于空闲状态 除非设置了allowCoreThreadTimeOut
         *
         * maximumPoolSize *池中允许的最大线程数
         *
         * keepalivueTime 存活时间 如果当前线程大于core的数量 释放空闲的线程 maximumPoolsize-corePoolSize
         * 只要线程空闲大于指定的keepAlivuetime unit:时间单位 BlockingQUeue<Runnable> workQueue 阻塞队列 如果任务有很多
         * 就会将目前多的任务放在队列里面 只要有线程空闲，就会去队列里面取出新的任务继续执行 threadFactory 线程创建工厂 RejectedExecutionHandler 如果队列满了
         * 按照我们指定得拒绝策略拒绝指定任务
         *
         * 工作顺序 1)、线程池创建好 准备好core数量的核心线程，准备接受任务 1.1、core满了 就将在进来的任务放入阻塞队列中 空闲的core就会自己去阻塞队列获取任务执行 1.2、阻塞队列满了
         * 就直接开新线程执行 最大只能开到max指定数量 1.3、max满了就用RejectedExecutionHandler 拒绝任务 1.4、max都执行完成，有很多空闲
         * 指定时间以后keepAlivueTime以后 释放max-core(195)这些线程
         *
         * new LinkedBlockingQueue<>() 默认是Integer最大值 内存不够
         *
         * 一个线程池 core:7 max:20 queue:50 100并发进来怎么分配 7个会立即得到执行 50个进入队列 再开13个进行执行，剩下的30个就使用拒绝策略 如果不想抛弃还要执行
         * CallerRunsPolicy 同步方式
         */
        // 最原始的方式：
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 200, 10, TimeUnit.SECONDS, new LinkedBlockingDeque<>(100000),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        
        // 快速创建线程池
        Executors.newCachedThreadPool(); // core是0 所有都可回收
        Executors.newFixedThreadPool(10);// 固定大小 core=max 都不可以回收
        Executors.newScheduledThreadPool(10); // 定时任务的线程池
        Executors.newSingleThreadExecutor(); // 单线程的线程池,后台从队列里面获取任务 挨个执行
        
        System.out.println("结束");
    }
    
    // 方法二
    public static class Runable01 implements Runnable
    {
        
        @Override
        public void run()
        {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("结果运行：" + i);
        }
    }
    
    // 方法三
    public static class Callable01 implements Callable<Integer>
    {
        
        @Override
        public Integer call() throws Exception
        {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            int i = 10 / 2;
            
            System.out.println("结果运行：" + i);
            return i;
        }
    }
}
