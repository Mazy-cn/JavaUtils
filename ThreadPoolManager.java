package com.hierway.vpline.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: ThreadPoolManager
 * @Description:线程池管理
 * @Auther: Mazy
 * @create: 2019-05-23 17:32
 */

@Configuration
public class ThreadPoolManager {
    /**
     *
     * @return 核心线程10，无界线程池，60s回收，直接提交
     */
  @Bean
  public ThreadPoolExecutor executor(){
      return new ThreadPoolExecutor(10,Integer.MAX_VALUE,60, TimeUnit.SECONDS, new SynchronousQueue<>());
  }

}
