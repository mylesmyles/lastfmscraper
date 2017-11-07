package net.mylesputnam.lastfm.scraper.webrequests;

import java.util.List;

import net.mylesputnam.lastfm.scraper.db.requests.update.LastFmRecordUpdates;

public interface LastFmWebRequest {
	public List<RequestParam> getNextParams();
	public void handleResponse(String requestResponse);
	public boolean isComplete();
	public LastFmRecordUpdates getRecordsToSave();
	public void onSaveComplete(Runnable saveCallback);
}
