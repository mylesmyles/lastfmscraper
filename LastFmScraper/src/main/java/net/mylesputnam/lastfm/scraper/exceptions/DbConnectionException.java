package net.mylesputnam.lastfm.scraper.exceptions;

@SuppressWarnings("serial")
public class DbConnectionException extends RuntimeException {
	public DbConnectionException(String message) {
		super(message);
	}
}
