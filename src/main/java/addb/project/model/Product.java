package addb.project.model;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Product {
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
	
	public static Product fromResult(Result result) {
		byte[] rowKey = result.getRow();
	    String id = Bytes.toString(rowKey);
		byte[] genderBytes = result.getValue(Bytes.toBytes("ID"), Bytes.toBytes("gender"));
        byte[] masterCategoryBytes = result.getValue(Bytes.toBytes("ID"), Bytes.toBytes("masterCategory"));
        byte[] subCategoryBytes = result.getValue(Bytes.toBytes("ID"), Bytes.toBytes("subCategory"));
        byte[] articleTypeBytes = result.getValue(Bytes.toBytes("ID"), Bytes.toBytes("articleType"));
        byte[] baseColourBytes = result.getValue(Bytes.toBytes("ID"), Bytes.toBytes("baseColour"));
        byte[] seasonBytes = result.getValue(Bytes.toBytes("ID"), Bytes.toBytes("season"));
        byte[] yearBytes = result.getValue(Bytes.toBytes("ID"), Bytes.toBytes("year"));
        byte[] usageBytes = result.getValue(Bytes.toBytes("ID"), Bytes.toBytes("usage"));
        byte[] productDisplayNameBytes = result.getValue(Bytes.toBytes("ID"), Bytes.toBytes("productDisplayName"));
        
        
        String gender = Bytes.toString(genderBytes);
        String masterCategory = Bytes.toString(masterCategoryBytes);
        String subCategory = Bytes.toString(subCategoryBytes);
        String articleType = Bytes.toString(articleTypeBytes);
        String baseColour = Bytes.toString(baseColourBytes);
        String usage = Bytes.toString(usageBytes);
        String season = Bytes.toString(seasonBytes);
        int year = Bytes.toInt(yearBytes);
        String productDisplayName = Bytes.toString(productDisplayNameBytes);
        
        return Product.builder()
				.id(id)
				.gender(gender)
				.masterCategory(masterCategory)
				.subCategory(subCategory)
				.articleType(articleType)
				.baseColour(baseColour)
				.usage(usage)
				.season(season)
				.year(year)
				.productDisplayName(productDisplayName)
			.build();
	}
}
