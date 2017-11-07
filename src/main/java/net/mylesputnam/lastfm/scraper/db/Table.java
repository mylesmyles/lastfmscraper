package net.mylesputnam.lastfm.scraper.db;

import java.util.Arrays;

import net.mylesputnam.lastfm.scraper.util.StaticList;

public enum Table {
	USER("users", Col.USER_NAME, Col.USER_URL, Col.USER_CREATED, Col.USER_FOLLOWERS_UPDATED, Col.USER_LISTENS_UPDATED),
	ARTIST("artists", Col.ARTIST_NAME, Col.ARTIST_URL, Col.ARTIST_LISTENS, Col.ARTIST_LISTENERS, Col.ARTIST_POPULARITY, Col.ARTIST_CREATED, Col.ARTIST_UPDATED),
	LISTENS("listens", Col.LISTENS_ARTIST_NAME, Col.LISTENS_USER_NAME, Col.LISTENS_COUNT, Col.LISTENS_USER_RANK),
	;
	
	public final String name;
	public final StaticList<Col> columns;
	private Table(String name, Col...columns) {
		this.name = name;
		this.columns = StaticList.create(Arrays.asList(columns));
	}
	
	@Override
	public String toString() {
		return name;
	}
}
