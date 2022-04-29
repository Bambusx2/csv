package r.ivanov.csv.entities;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Employer {
	private Long empId;
	private Long projectId;
	private LocalDate dateFrom;
	private LocalDate dateTo;

}
