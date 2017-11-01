package net.mylesputnam.lastfm.scraper.exceptions;

@SuppressWarnings("serial")
public class DbConnectionError extends RuntimeException {
	public DbConnectionError(String message) {
		super(message);
	}
}
