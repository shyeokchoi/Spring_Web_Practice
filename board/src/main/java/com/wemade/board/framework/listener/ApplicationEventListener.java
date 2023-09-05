package com.wemade.board.framework.listener;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * springboot application event listener
 *
 * @author youngrok.kim
 *
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ApplicationEventListener {

	/**
	 * This event is executed as late as conceivably possible to indicate that the
	 * application is ready to service requests.
	 */
	@EventListener
	public void startedEventHandler(ApplicationStartedEvent event) {
		// 어플리케이션이 시작 될때
		log.warn("!!! BootApplicationEventListener.ApplicationStartedEvent");
	}

	@EventListener
	public void readyEventHandler(ApplicationReadyEvent event) {
		// 어플리케이션 컴포넌트가 모두 준비가 완료 될때
		log.warn("!!! BootApplicationEventListener.ApplicationReadyEvent");
	}

	@EventListener
	public void refreshedEventHandler(ContextRefreshedEvent event) {
		log.warn("!!! BootApplicationEventListener.ContextRefreshedEvent");
	}

	@EventListener
	public void closedEventHandler(ContextClosedEvent event) {
		log.warn("!!! BootApplicationEventListener.ContextClosedEvent");
	}

}