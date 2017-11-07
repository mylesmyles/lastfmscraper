package net.mylesputnam.lastfm.scraper.exceptions;

public class NoScraperTaskFoundException extends Exception {
	private static final long serialVersionUID = -8388074195582017535L;

	public NoScraperTaskFoundException(String message) {
		super(message);
	}
}
