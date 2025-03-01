package org.wangpai.calculator.model.universal;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.wangpai.calculator.model.data.SpringConfig;

/**
 * @since 2021-9-27
 */
@Slf4j
public class CentralDatabase {
    /**
     * 这里凡是使用 volatile 的变量都使用了单例模式中的“双重检查锁定”
     */
    private static volatile AbstractApplicationContext springContext;
    private static final Object SPRINGCONTEXT_LOCK = new Object();

    private static volatile Map container;
    private static final Object CONTAINER_LOCK = new Object();

    /**
     * 程序运行开始时间（单位：ms）
     *
     * @since 2021-9-28
     */
    public static final long START_TIME = System.currentTimeMillis();

    static {
        log.info(System.lineSeparator()); // 先打印空行来分隔以前的日志，以提高视觉效果
        log.info("--------应用启动，日志信息开始输出：{}ms---------", System.currentTimeMillis() - CentralDatabase.START_TIME);
    }

    /**
     * 此方法是强同步方法，此方法不能返回 null，因此不能新建线程来创建 Spring 容器。
     * 提供此方法是为了进行“懒加载”
     *
     * 这里使用了单例模式中的“双重检查锁定”
     *
     * @since 2021-9-28
     */
    public static AbstractApplicationContext getSpringContext() {
        // 第一重判断
        if (CentralDatabase.springContext == null) {
            // 上锁
            synchronized (SPRINGCONTEXT_LOCK) {
                // 第二重判断
                if (CentralDatabase.springContext == null) {
                    log.info("开始初始化 Spring Bean。时间：{}ms", System.currentTimeMillis() - CentralDatabase.START_TIME);

                    CentralDatabase.springContext =
                            new AnnotationConfigApplicationContext(SpringConfig.class);

                    log.info("Spring Bean 初始化结束。时间：{}ms", System.currentTimeMillis() - CentralDatabase.START_TIME);
                }
            }
        }
        return CentralDatabase.springContext;
    }

    /**
     * 提供此方法是为了进行“懒加载”
     *
     * 这里使用了单例模式中的“双重检查锁定”
     *
     * @since 2021-9-27
     * @lastModified 2021-9-28
     */
    public static Map getContainer() {
        // 第一重判断
        if (CentralDatabase.container == null) {
            // 上锁
            synchronized (CONTAINER_LOCK) {
                // 第二重判断
                if (CentralDatabase.container == null) {
                    CentralDatabase.container = new HashMap();
                }
            }
        }
        return CentralDatabase.container;
    }
}

