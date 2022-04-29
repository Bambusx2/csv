package r.ivanov.csv.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import r.ivanov.csv.entities.Team;
import r.ivanov.csv.service.CsvService;

@RequestMapping("/csv")
@RestController
public class CsvController {

	@Autowired
	CsvService csvService;

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public List<Team> uploadCsv(@RequestPart("file") MultipartFile file) {
		return csvService.getTeam(file);
	}

}
