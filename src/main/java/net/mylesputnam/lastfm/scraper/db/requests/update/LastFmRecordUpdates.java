package net.mylesputnam.lastfm.scraper.db.requests.update;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class LastFmRecordUpdates {
	private final List<DbUpdateRequest> updateRequests;
	private final Runnable onUpdateComplete;
	
	public LastFmRecordUpdates(List<DbUpdateRequest> updateRequests) {
		this.updateRequests = updateRequests;
		this.onUpdateComplete = null;
	}
	
	public LastFmRecordUpdates(List<DbUpdateRequest> updateRequests, Runnable onUpdateComplete) {
		this.updateRequests = updateRequests;
		this.onUpdateComplete = onUpdateComplete;
	}
	
	public void updateRecords(Connection connection) {
		for(DbUpdateRequest updateRequest : updateRequests) {
			try {
				updateRequest.makeUpdateRequest(connection);
			} catch (SQLException e) {
				System.out.println("Problem updating record for " + updateRequest);
				e.printStackTrace();
			}
		}
		
		if(onUpdateComplete != null) {
			onUpdateComplete.run();
		}
	}
}
