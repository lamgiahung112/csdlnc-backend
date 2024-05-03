package addb.project.utilities;

import java.util.Date;

import addb.project.model.StockSnapshot;
import addb.project.model.request.GetStocksRequest;

public class StockSnapshotMatcher {
	public static boolean matches(StockSnapshot snapshot, GetStocksRequest request) {
		if (request.getFromDate() == null && request.getToDate() == null) {
			return true;
		}
		
		boolean isFromDateMatch = true;
		boolean isToDateMatch = true;
		
		String[] snapshotDateVals = snapshot.getDate().split("-");
		Date snapshotDate = new Date(
				Integer.parseInt(snapshotDateVals[0]), 
				Integer.parseInt(snapshotDateVals[1]) -1, 
				Integer.parseInt(snapshotDateVals[2])
			);
		
		if (request.getFromDate() != null) {
			String[] fromDateVals = request.getFromDate().split("-");
			Date fromDate = new Date(
					Integer.parseInt(fromDateVals[0]), 
					Integer.parseInt(fromDateVals[1]) -1, 
					Integer.parseInt(fromDateVals[2])
				);
			
			isFromDateMatch = fromDate.getTime() <= snapshotDate.getTime();
		}
		
		if (request.getToDate() != null) {
			String[] toDateVals = request.getToDate().split("-");
			Date toDate = new Date(
					Integer.parseInt(toDateVals[0]), 
					Integer.parseInt(toDateVals[1]) -1, 
					Integer.parseInt(toDateVals[2])
				);
			isToDateMatch = toDate.getTime() >= snapshotDate.getTime();
		}
		
		return isFromDateMatch && isToDateMatch;
	}
}
