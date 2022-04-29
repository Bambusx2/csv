package r.ivanov.csv.service.helper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import r.ivanov.csv.entities.CsvMetadata;
import r.ivanov.csv.entities.Employer;

@Component
@Slf4j
public class CsvParser {

	DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendOptional(DateTimeFormatter.ofPattern("M/d/uuuu"))
			.appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
			.appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")).parseCaseInsensitive().optionalStart()
			.optionalEnd().optionalStart().appendZoneOrOffsetId().toFormatter();

	public Employer convertToEmployer(String[] line, CsvMetadata csvObject, int lineNumber) {
		Employer employer = new Employer();
		parseEmpId(line, csvObject, employer);
		parseProjectId(line, csvObject, employer);
		parseDateFrom(line, csvObject, employer);
		parseDateTo(line, csvObject, employer);
		return employer;
	}

	private void parseEmpId(String[] line, CsvMetadata csvObject, Employer employer) {
		if (csvObject.getEmpId() != -1) {
			try {
				String empId = line[csvObject.getEmpId()];
				employer.setEmpId(Long.valueOf(empId));
			} catch (Exception e) {
				log.error("Emp id not found " + e.getMessage());
			}
		} else {
			log.error("Index out of bounds.");
			log.error("Emp Id header not found ");
		}
	}

	private void parseProjectId(String[] line, CsvMetadata csvObject, Employer employer) {
		if (csvObject.getEmpId() != -1) {
			try {
				String projectId = line[csvObject.getProjectId()];
				employer.setProjectId(Long.valueOf(projectId));
			} catch (Exception e) {
				log.error("Project id not found " + e.getMessage());
			}
		} else {
			log.error("Index out of bounds.");
			log.error("Project id header not found ");
		}
	}

	private void parseDateFrom(String[] line, CsvMetadata csvObject, Employer employer) {
		if (csvObject.getEmpId() != -1) {
			try {
				String dateFrom = line[csvObject.getDateFrom()];
				if (dateFrom.equalsIgnoreCase("NULL")) {
					dateFrom = LocalDate.now().toString();
				}
				LocalDate localDate = LocalDate.parse(dateFrom, formatter);
				employer.setDateFrom(localDate);
			} catch (Exception e) {
				log.error("Date from not found " + e.getMessage());
			}
		} else {
			log.error("Index out of bounds.");
			log.error("Date from header not found ");
		}
	}

	private void parseDateTo(String[] line, CsvMetadata csvObject, Employer employer) {
		if (csvObject.getEmpId() != -1) {
			try {
				String dateTo = line[csvObject.getDateTo()];
				if (dateTo.equalsIgnoreCase("NULL")) {
					dateTo = LocalDate.now().toString();
				}
				LocalDate localDate = LocalDate.parse(dateTo, formatter);
				employer.setDateTo(localDate);
			} catch (Exception e) {
				log.error("Date to not found " + e.getMessage());
			}
		} else {
			log.error("Index out of bounds.");
			log.error("Date to header not found ");
		}
	}

}
