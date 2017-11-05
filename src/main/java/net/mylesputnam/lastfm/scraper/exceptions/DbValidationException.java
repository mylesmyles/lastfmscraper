package net.mylesputnam.lastfm.scraper.exceptions;

public class DbValidationException extends RuntimeException {
	private static final long serialVersionUID = -7414486275929406060L;
	public DbValidationException(String message, Exception parentException) {
		super(message, parentException);
	}
}
