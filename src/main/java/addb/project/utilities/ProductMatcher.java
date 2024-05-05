package addb.project.utilities;

import java.util.Date;

import addb.project.model.Product;
import addb.project.model.request.GetProductRequest;

public class ProductMatcher {
	public static boolean matches(Product prod, GetProductRequest request) {
		boolean isCategoryMatch = true;
		boolean isColorMatch = true;
		boolean isNameMatch = true;
		boolean isYearMatch = true;
		
		if (request.getCategory() != null) {
			isCategoryMatch = 
					prod.getMasterCategory().contains(request.getCategory()) 
					|| prod.getSubCategory().contains(request.getCategory());
		}
		
		if (request.getColor() != null) {
			isColorMatch =
					prod.getBaseColour().equals(request.getColor());
		}
		
		if (request.getName() != null) {
			isNameMatch = prod.getProductDisplayName().contains(request.getName());
		}
		
		if (request.getYear() != null) {
			isYearMatch = String.valueOf(prod.getYear()).equals(request.getYear());
		}
		return isCategoryMatch && isColorMatch && isNameMatch && isYearMatch;
	}
}
