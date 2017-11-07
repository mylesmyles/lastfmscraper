package net.mylesputnam.lastfm.scraper.webrequests;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import net.mylesputnam.lastfm.scraper.data.LastFmUser;
import net.mylesputnam.lastfm.scraper.db.requests.update.DbUpdateRequest;
import net.mylesputnam.lastfm.scraper.db.requests.update.InsertUserRequest;
import net.mylesputnam.lastfm.scraper.db.requests.update.LastFmRecordUpdates;

public class SeedUserRequest implements LastFmWebRequest {
	private final String seedUsername;
	private final List<LastFmUser> usersFound;
	private Runnable onSaveComplete;
	
	public SeedUserRequest(String seedUsername) {
		this.seedUsername = seedUsername;
		this.usersFound = new LinkedList<>();
		this.onSaveComplete = null;
	}

	@Override
	public List<RequestParam> getNextParams() {
		return Arrays.asList(new RequestParam("method", "user.getinfo"), new RequestParam("user", seedUsername));
	}

	@Override
	public void handleResponse(String requestResponse) {
		System.out.println("Got response:");
		System.out.println(requestResponse);
		System.out.println("Adding it to users found");
		usersFound.add(new LastFmUser(seedUsername, "mylesputnam.net"));
	}

	@Override
	public boolean isComplete() {
		return !usersFound.isEmpty();
	}

	@Override
	public LastFmRecordUpdates getRecordsToSave() {
		List<DbUpdateRequest> updateRequests = usersFound.stream().map(user -> new InsertUserRequest(user))
				.collect(Collectors.toList());
		return new LastFmRecordUpdates(updateRequests, onSaveComplete);
	}

	@Override
	public void onSaveComplete(Runnable saveCallback) {
		this.onSaveComplete = saveCallback;
	}
}
