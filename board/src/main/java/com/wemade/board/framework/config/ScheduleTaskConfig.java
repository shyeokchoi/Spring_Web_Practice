/***************************************************
 * Copyright(c) 2023-2024 WEMADE right reserved.
 *
 * Revision History
 * Author : dev_tony85
 * Date : Mon Jun 19 2023
 * Description : scheduler config
 *
 ****************************************************/
package com.wemade.board.framework.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.util.ErrorHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * Thread 설정(async, schedule)
 *
 * @author dev_tony85
 *
 */
@Slf4j
@Configuration
@EnableScheduling
public class ScheduleTaskConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5);
        scheduler.setThreadNamePrefix("Scheduler-");
//        scheduler.setErrorHandler(e -> {
//            log.error("*** custom schedule error handle", e);
//        });

        scheduler.setErrorHandler(new CustomErrorHandler());
        scheduler.initialize();

        taskRegistrar.setTaskScheduler(scheduler);
    }

    class CustomErrorHandler implements ErrorHandler {

        @Override
        public void handleError(Throwable t) {
            log.error("==> schedule error handle", t);
        }
    }

}