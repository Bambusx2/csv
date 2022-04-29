package r.ivanov.csv.service.helper;


import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;

import lombok.extern.slf4j.Slf4j;
import r.ivanov.csv.entities.CsvMetadata;

@Component
@Slf4j
public class CsvReader {
	
	public CsvMetadata extractCsvMetadata(String[] headers) {
		CsvMetadata csvObject = new CsvMetadata();
		csvObject.setHeaders(headers);

		for (int i = 0; i < headers.length; i++) {
			String header = headers[i];
			header = header.trim();
			if (header.equals("EmpID")) {
				csvObject.setEmpId(i);
			} else if (header.equals("ProjectID")) {
				csvObject.setProjectId(i);
			} else if (header.equals("DateFrom")) {
				csvObject.setDateFrom(i);
			} else if (header.equals("DateTo")) {
				csvObject.setDateTo(i);
			}
		}
		return csvObject;
	}

	public List<String[]> readFromCSV(MultipartFile file) {
		List<String[]> lines = new ArrayList<>();
		try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
			lines = csvReader.readAll();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return lines;
	}


}
