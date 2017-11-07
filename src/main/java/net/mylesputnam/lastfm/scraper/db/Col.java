package net.mylesputnam.lastfm.scraper.db;

public enum Col {
	USER_NAME("name","varchar(200)"),
	USER_URL("url","varchar(300)"),
	USER_CREATED("created","timestamptz"),
	USER_FOLLOWERS_UPDATED("followers_updated","timestamptz"),
	USER_LISTENS_UPDATED("listens_updated", "timestamptz"),
	
	ARTIST_NAME("name","varchar(300)"),
	ARTIST_URL("url","varchar(300)"),
	ARTIST_LISTENS("listens","bigint"),
	ARTIST_LISTENERS("listeners","bigint"),
	ARTIST_POPULARITY("popularity","integer"),
	ARTIST_CREATED("created","timestamptz"),
	ARTIST_UPDATED("modified","timestamptz"),
	
	LISTENS_ARTIST_NAME("artist_name","varchar(300)"),
	LISTENS_USER_NAME("user_name","varchar(200)"),
	LISTENS_COUNT("count","bigint"),
	LISTENS_USER_RANK("user_rank","integer"),
	;
	
	public final String name;
	public final String type;
	
	private Col(String name, String type) {
		this.name = name;
		this.type = type;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
