package addb.project.model;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class StockSnapshot {
	private String date;
	private double open;
	private double high;
	private double low;
	private double close;
	private double adjClose;
	private double volume;
	
	public static StockSnapshot fromResult(Result result) {
		byte[] rowKey = result.getRow();
	    String date = Bytes.toString(rowKey);
		byte[] openBytes = result.getValue(Bytes.toBytes("STOCK_PRICES"), Bytes.toBytes("open"));
        byte[] highBytes = result.getValue(Bytes.toBytes("STOCK_PRICES"), Bytes.toBytes("high"));
        byte[] lowBytes = result.getValue(Bytes.toBytes("STOCK_PRICES"), Bytes.toBytes("low"));
        byte[] closeBytes = result.getValue(Bytes.toBytes("STOCK_PRICES"), Bytes.toBytes("close"));
        byte[] adjCloseBytes = result.getValue(Bytes.toBytes("STOCK_PRICES"), Bytes.toBytes("adjClose"));
        byte[] volumeBytes = result.getValue(Bytes.toBytes("STOCK_PRICES"), Bytes.toBytes("volume"));
        
        
        double open = Bytes.toDouble(openBytes);
        double high = Bytes.toDouble(highBytes);
        double low = Bytes.toDouble(lowBytes);
        double close = Bytes.toDouble(closeBytes);
        double adjClose = Bytes.toDouble(adjCloseBytes);
        double volume = Bytes.toDouble(volumeBytes);
        
        return StockSnapshot.builder()
				.date(date)
				.open(open)
				.high(high)
				.low(low)
				.close(close)
				.adjClose(adjClose)
				.volume(volume)
			.build();
	}
}
