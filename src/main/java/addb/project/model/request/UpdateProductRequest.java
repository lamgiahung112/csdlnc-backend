package addb.project.model.request;

import lombok.Data;

@Data
public class UpdateProductRequest {
	private String id;
	private String gender;
	private String masterCategory;
	private String subCategory;
	private String articleType;
	private String baseColour;
	private String season;
	private int year;
	private String usage;
	private String productDisplayName;
}
