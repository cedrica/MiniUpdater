package com.preag.miniupdater;

import javafx.event.Event;
import javafx.event.EventType;

public class MiniUpdaterEvent extends Event{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final EventType<MiniUpdaterEvent> START_OR_PAUSE_DOWNLOAD = new EventType<>("MiniUpdaterEventStartOrPauseDownload");

	public MiniUpdaterEvent(EventType<MiniUpdaterEvent> miniUpdaterEvent) {
		super(miniUpdaterEvent);
	}

}
