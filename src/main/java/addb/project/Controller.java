package addb.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import addb.project.model.StockSnapshot;
import addb.project.model.request.GetStocksRequest;

@RestController
@CrossOrigin
@RequestMapping("/data")
public class Controller {
	@Autowired
	private DataService dataService;
	
	@GetMapping
	public List<StockSnapshot> getSnapshots(@ModelAttribute @RequestAttribute GetStocksRequest request) {
		return dataService.getSnapshots(request);
	}
	
	@PostMapping("/csv")
	public void uploadCSV(@RequestPart("file") MultipartFile file) {
		dataService.readAndAddSnapshotsFromFile(file);
	}
}
