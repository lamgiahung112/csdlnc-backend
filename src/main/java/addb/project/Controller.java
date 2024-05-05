package addb.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

import addb.project.model.Product;
import addb.project.model.request.GetProductRequest;
import addb.project.model.request.UpdateProductRequest;

@RestController
@CrossOrigin
@RequestMapping("/data")
public class Controller {
	@Autowired
	private DataService dataService;
	
	@GetMapping
	public List<Product> getSnapshots(@ModelAttribute @RequestAttribute GetProductRequest request) {
		return dataService.getSnapshots(request);
	}
	
	@PostMapping(value = "/update")
	public void updateProduct(@Nullable @RequestPart(name = "file", required = false) MultipartFile file, @RequestParam("metadata") String request) {
		UpdateProductRequest parsedRequest = new Gson().fromJson(request, UpdateProductRequest.class);
		dataService.handleUpdateProduct(file, parsedRequest);
	}
}
