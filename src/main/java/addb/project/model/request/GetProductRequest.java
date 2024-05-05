package addb.project.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
public class GetProductRequest {
	private int page;
	private int pageSize;
	private String category;
	private String color;
	private String year;
	private String name;
}
