package r.ivanov.csv.entities;

import lombok.Data;

@Data
public class CsvMetadata {
	private int empId = -1;
	private int projectId = -1;
	private int dateFrom = -1;
	private int dateTo = -1;
	private String[] headers = new String[] {};
}
