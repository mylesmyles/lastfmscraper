package net.mylesputnam.lastfm.api.exceptions;

public class LastFmApiException extends RuntimeException {
	private static final long serialVersionUID = 4800661881914425680L;

	public LastFmApiException(String message) {
		super(message);
	}
	
	public LastFmApiException(String message, Exception parentException) {
		super(message, parentException);
	}
}
