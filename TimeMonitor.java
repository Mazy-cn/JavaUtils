package com.hierway.vpline.utils;

import com.hierway.utility.api.result.ApiException;
import com.hierway.utility.api.result.ResultCode;
import com.hierway.vpline.dao.dataservice.mybatis.NestedCategoryMapper;
import com.hierway.vpline.log.MyLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;



/**
 * @ClassName: TimeMonitor
 * @Description:方法超时监控/仅限Spring容器注入过的bean
 * @Auther: Mazy
 * @create: 2019-05-21 14:53
 */
@Component
public class TimeMonitor {

    @Autowired
    private ThreadPoolManager threadPoolManager;
    private MyLogger logger = MyLogger.getLogger(this.getClass());
    @Resource
    private NestedCategoryMapper nestedCategoryMapper;
    /**
     * 通用service超时监控
     * @param totalTime
     * @param clazz
     * @param funcName
     * @param args
     * @return
     */
    @Transactional
    public Object monitor(int totalTime,Class clazz,String funcName,Object... args) {
        FutureTask<Object> task = new FutureTask<>(() -> {
            Object bean = SpringUtils.getBean(clazz);
            Class[] classOfParams = new Class[args.length];
            for(int j=0;j<args.length;j++){
                classOfParams[j]=args[j].getClass();
            }
            Method method = clazz.getDeclaredMethod(funcName,classOfParams);//只可调用当前Class的method
            return method.invoke(bean,args);
        });
        threadPoolManager.executor().submit(task);
        try {
            return task.get(totalTime, TimeUnit.SECONDS);//执行成功
        } catch (Exception e) {
            task.cancel(true);
            e.printStackTrace();
            logger.error(e.getMessage());
            /*第一层为ExecutionException，使用反射调用方法抛出异常会被反射异常包装,getCause获得反射异常，最后调用getTargetException取出实际异常进行判断
            是否是自定义异常，是则直接抛出，否则抛出执行超时，再自行检查实际报错原因*/
            if (e instanceof ExecutionException) {
                Throwable cause = e.getCause();
                if(cause instanceof InvocationTargetException){
                    InvocationTargetException targetException = (InvocationTargetException)cause;
                    Throwable realCause = targetException.getTargetException();
                    if(realCause instanceof ApiException){
                        throw (ApiException)realCause;
                    }
                }
            }
            throw new ApiException(ResultCode.VPLM_REQUEST_TIMEOUT_EXCEPTION_ERROR);//执行超时
        }
    }
    @Transactional
    public Object monitor(Class clazz,String funcName,Object... args) {
        int totalTime=10;
        FutureTask<Object> task = new FutureTask<>(() -> {
            Object bean = SpringUtils.getBean(clazz);
            Class[] classOfParams = new Class[args.length];
            for(int j=0;j<args.length;j++){
                classOfParams[j]=args[j].getClass();
            }
            Method method = clazz.getDeclaredMethod(funcName,classOfParams);
            return method.invoke(bean,args);
        });
        threadPoolManager.executor().submit(task);
        try {
            return task.get(totalTime, TimeUnit.SECONDS);//执行成功
        } catch (Exception e) {
            task.cancel(true);
            e.printStackTrace();
            logger.error(e.getMessage());
            /*第一层为ExecutionException，使用反射调用方法抛出异常会被反射异常包装,getCause获得反射异常，最后调用getTargetException取出实际异常进行判断
            是否是自定义异常，是则直接抛出，否则抛出执行超时，再自行检查实际报错原因*/
            if (e instanceof ExecutionException) {
                Throwable cause = e.getCause();
                if(cause instanceof InvocationTargetException){
                    InvocationTargetException targetException = (InvocationTargetException)cause;
                    Throwable realCause = targetException.getTargetException();
                    if(realCause instanceof ApiException){
                        throw (ApiException)realCause;
                    }
                }
            }
            throw new ApiException(ResultCode.VPLM_REQUEST_TIMEOUT_EXCEPTION_ERROR);//执行超时
        }
    }

    @Transactional
    public Object monitorParentMethod(Class clazz,String funcName,Object... args) {
        int totalTime=10;
        FutureTask<Object> task = new FutureTask<>(() -> {
            Object bean = SpringUtils.getBean(clazz);
            Class[] classOfParams = new Class[args.length];
            for(int j=0;j<args.length;j++){
                classOfParams[j]=args[j].getClass();
            }
            Method method = clazz.getMethod(funcName,classOfParams);//可调用Class的包括父类的method
            return method.invoke(bean,args);
        });
        threadPoolManager.executor().submit(task);
        try {
            return task.get(totalTime, TimeUnit.SECONDS);//执行成功
        } catch (Exception e) {
            task.cancel(true);
            e.printStackTrace();
            logger.error(e.getMessage());
            /*第一层为ExecutionException，使用反射调用方法抛出异常会被反射异常包装,getCause获得反射异常，最后调用getTargetException取出实际异常进行判断
            是否是自定义异常，是则直接抛出，否则抛出执行超时，再自行检查实际报错原因*/
            if (e instanceof ExecutionException) {
                Throwable cause = e.getCause();
                if(cause instanceof InvocationTargetException){
                    InvocationTargetException targetException = (InvocationTargetException)cause;
                    Throwable realCause = targetException.getTargetException();
                    if(realCause instanceof ApiException){
                        throw (ApiException)realCause;
                    }
                }
            }
            throw new ApiException(ResultCode.VPLM_REQUEST_TIMEOUT_EXCEPTION_ERROR);//执行超时
        }
    }
    @Transactional
    public Object monitorParentMethodUnlockTables(Class clazz,String funcName,Object... args) {
        int totalTime=10;
        FutureTask<Object> task = new FutureTask<>(() -> {
            Object bean = SpringUtils.getBean(clazz);
            Class[] classOfParams = new Class[args.length];
            for(int j=0;j<args.length;j++){
                classOfParams[j]=args[j].getClass();
            }
            Method method = clazz.getMethod(funcName,classOfParams);
            Object invoke;
            try {
                invoke = method.invoke(bean, args);
            }finally {
                nestedCategoryMapper.unlockTables();
            }
            return invoke;
        });
        threadPoolManager.executor().submit(task);
        try {
            return task.get(totalTime, TimeUnit.SECONDS);//执行成功
        } catch (Exception e) {

            task.cancel(true);
            e.printStackTrace();
            logger.error(e.getMessage());
            /*第一层为ExecutionException，使用反射调用方法抛出异常会被反射异常包装,getCause获得反射异常，最后调用getTargetException取出实际异常进行判断
            是否是自定义异常，是则直接抛出，否则抛出执行超时，再自行检查实际报错原因*/
            if (e instanceof ExecutionException) {
                Throwable cause = e.getCause();
                if(cause instanceof InvocationTargetException){
                    InvocationTargetException targetException = (InvocationTargetException)cause;
                    Throwable realCause = targetException.getTargetException();
                    if(realCause instanceof ApiException){
                        throw (ApiException)realCause;
                    }
                }
            }
            throw new ApiException(ResultCode.VPLM_REQUEST_TIMEOUT_EXCEPTION_ERROR);//执行超时
        }
    }
    @Transactional
    public Object monitorMethodUnlockTables(Class clazz,String funcName,Object... args) {
        int totalTime=10;
        FutureTask<Object> task = new FutureTask<>(() -> {
            Object bean = SpringUtils.getBean(clazz);
            Class[] classOfParams = new Class[args.length];
            for(int j=0;j<args.length;j++){
                classOfParams[j]=args[j].getClass();
            }
            Method method = clazz.getDeclaredMethod(funcName,classOfParams);
            Object invoke;
            try {
                invoke = method.invoke(bean, args);
            }finally {
                nestedCategoryMapper.unlockTables();
            }
            return invoke;
        });
        threadPoolManager.executor().submit(task);
        try {
            return task.get(totalTime, TimeUnit.SECONDS);//执行成功
        } catch (Exception e) {

            task.cancel(true);
            e.printStackTrace();
            logger.error(e.getMessage());
            /*第一层为ExecutionException，使用反射调用方法抛出异常会被反射异常包装,getCause获得反射异常，最后调用getTargetException取出实际异常进行判断
            是否是自定义异常，是则直接抛出，否则抛出执行超时，再自行检查实际报错原因*/
            if (e instanceof ExecutionException) {
                Throwable cause = e.getCause();
                if(cause instanceof InvocationTargetException){
                    InvocationTargetException targetException = (InvocationTargetException)cause;
                    Throwable realCause = targetException.getTargetException();
                    if(realCause instanceof ApiException){
                        throw (ApiException)realCause;
                    }
                }
            }
            throw new ApiException(ResultCode.VPLM_REQUEST_TIMEOUT_EXCEPTION_ERROR);//执行超时
        }
    }

    /**
     * @param totalTime 总超时时间 单位/秒
     * @param sleepTime 间隔获得锁时间 单位/毫秒
     * @param queryTime 获取锁限制次数
     * @param clazz 调用方法的对象
     * @param funcName 方法名
     * @param args 形参
     * @return
     */
    @Transactional
    public List<Object> lockNoWaitMonitor(int totalTime, int sleepTime, int queryTime, Class clazz,String funcName,Object... args) {
        FutureTask<List<Object>> task = new FutureTask<>(() -> {
            List<Object> list = new ArrayList<>();
            int i = 1;
            while (true) {
                try {
                    Object bean = SpringUtils.getBean(clazz);
                    Class[] classOfParams = new Class[args.length];
                    for(int j=0;j<args.length;j++){
                        classOfParams[j]=args[j].getClass();
                    }
                    Method method = clazz.getDeclaredMethod(funcName,classOfParams);
                    list = (List<Object>) method.invoke(bean,args);
                    if (list.isEmpty()) {
                        return null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (i == queryTime) {
                        throw new RuntimeException();
                    }
                    if(sleepTime>0){
                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException ie) {
                            throw new RuntimeException(ie);
                        }
                    }
                    i++;
                }
                if (!list.isEmpty()) {
                    break;
                }
            }
            return list;
        });
        threadPoolManager.executor().submit(task);
        try {
            return task.get(totalTime, TimeUnit.SECONDS);//执行成功
        } catch (Exception e) {
            task.cancel(true);
            e.printStackTrace();
            logger.error(e.getMessage());
            /*第一层为ExecutionException，使用反射调用方法抛出异常会被反射异常包装,getCause获得反射异常，最后调用getTargetException取出实际异常进行判断
            是否是自定义异常，是则直接抛出，否则抛出执行超时，再自行检查实际报错原因*/
            if (e instanceof ExecutionException) {
                Throwable cause = e.getCause();
                if(cause instanceof InvocationTargetException){
                    InvocationTargetException targetException = (InvocationTargetException)cause;
                    Throwable realCause = targetException.getTargetException();
                    if(realCause instanceof ApiException){
                        throw (ApiException)realCause;
                    }
                }
            }
            throw new ApiException(ResultCode.VPLM_REQUEST_TIMEOUT_EXCEPTION_ERROR);//执行超时
        }
    }
    @Transactional
    public List<Object> lockNoWaitMonitor(Class clazz,String funcName,Object... args) {
        int totalTime=10;
        int sleepTime=1000;
        int queryTime=3;
        FutureTask<List<Object>> task = new FutureTask<>(() -> {
            List<Object> list = new ArrayList<>();
            int i = 1;
            while (true) {
                try {
                    Object bean = SpringUtils.getBean(clazz);
                    Class[] classOfParams = new Class[args.length];
                    for(int j=0;j<args.length;j++){
                        classOfParams[j]=args[j].getClass();
                    }
                    Method method = clazz.getDeclaredMethod(funcName,classOfParams);
                    list = (List<Object>) method.invoke(bean,args);
                    if (list.isEmpty()) {
                        return null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (i == queryTime) {
                        throw new RuntimeException();
                    }
                    if(sleepTime>0){
                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException ie) {
                            throw new RuntimeException(ie);
                        }
                    }
                    i++;
                }
                if (!list.isEmpty()) {
                    break;
                }
            }
            return list;
        });
        threadPoolManager.executor().submit(task);
        try {
            return task.get(totalTime, TimeUnit.SECONDS);//执行成功
        } catch (Exception e) {
            task.cancel(true);
            e.printStackTrace();
            logger.error(e.getMessage());
            /*第一层为ExecutionException，使用反射调用方法抛出异常会被反射异常包装,getCause获得反射异常，最后调用getTargetException取出实际异常进行判断
            是否是自定义异常，是则直接抛出，否则抛出执行超时，再自行检查实际报错原因*/
            if (e instanceof ExecutionException) {
                Throwable cause = e.getCause();
                if(cause instanceof InvocationTargetException){
                    InvocationTargetException targetException = (InvocationTargetException)cause;
                    Throwable realCause = targetException.getTargetException();
                    if(realCause instanceof ApiException){
                        throw (ApiException)realCause;
                    }
                }
            }
            throw new ApiException(ResultCode.VPLM_REQUEST_TIMEOUT_EXCEPTION_ERROR);//执行超时
        }
    }
}
