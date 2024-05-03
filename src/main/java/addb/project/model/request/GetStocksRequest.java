package addb.project.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
public class GetStocksRequest {
	private String fromDate;
	private String toDate;
	private int pageSize;
}
