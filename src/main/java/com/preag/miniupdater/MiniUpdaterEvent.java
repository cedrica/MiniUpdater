package com.preag.miniupdater;

import com.preag.core.ui.event.ApplicationEvent;

import javafx.event.EventType;

public class MiniUpdaterEvent extends ApplicationEvent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final EventType<MiniUpdaterEvent> START_OR_PAUSE_DOWNLOAD = new EventType<>("MiniUpdaterEventStartOrPauseDownload");

	public MiniUpdaterEvent(EventType<MiniUpdaterEvent> miniUpdaterEvent) {
		super(miniUpdaterEvent);
	}

}
