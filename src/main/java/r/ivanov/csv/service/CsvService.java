package r.ivanov.csv.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import r.ivanov.csv.entities.CsvMetadata;
import r.ivanov.csv.entities.Employer;
import r.ivanov.csv.entities.Team;
import r.ivanov.csv.service.helper.CsvParser;
import r.ivanov.csv.service.helper.CsvReader;

@Service
@Slf4j
public class CsvService {

	@Autowired
	CsvParser csvParser;

	@Autowired
	CsvReader csvReader;

	public List<Team> getTeam(MultipartFile file) {
		List<String[]> lines = null;
		try {
			lines = csvReader.readFromCSV(file);
		} catch (Exception e) {
			log.error("Error while reading csv file " + e.getMessage());
		}
		CsvMetadata csvObject = csvReader.extractCsvMetadata(lines.get(0));
		return findTeam(createEmployers(lines, csvObject));

	}

	private List<Employer> createEmployers(List<String[]> lines, CsvMetadata csvObject) {
		List<Employer> employers = new ArrayList<>();

		for (int i = 1; i < lines.size(); i++) {
			Employer employer = csvParser.convertToEmployer(lines.get(i), csvObject, i);
			employers.add(employer);
		}
		return employers;
	}

	private List<Team> findTeam(List<Employer> employers) {
		List<Team> teams = new ArrayList<>();
		for (int i = 0; i < employers.size() - 1; i++) {
			for (int j = i + 1; j < employers.size(); j++) {
				Employer firstEmpl = employers.get(i);
				Employer secondEmpl = employers.get(j);

				if (firstEmpl.getProjectId() == secondEmpl.getProjectId() && hasOverlap(firstEmpl, secondEmpl)) {
					long overlapDays = getOverlap(firstEmpl, secondEmpl);
					if (overlapDays > 0) {
						updateTeam(teams, firstEmpl, secondEmpl, overlapDays);
					}
				}
			}
		}
		return teams;
	}

	private long getOverlap(Employer firstEmpl, Employer secondEmpl) {
		LocalDate periodStartDate = firstEmpl.getDateFrom().isBefore(secondEmpl.getDateFrom())
				? secondEmpl.getDateFrom()
				: firstEmpl.getDateFrom();

		LocalDate periodEndDate = firstEmpl.getDateTo().isBefore(secondEmpl.getDateTo()) ? firstEmpl.getDateTo()
				: secondEmpl.getDateTo();

		return Math.abs(ChronoUnit.DAYS.between(periodStartDate, periodEndDate));
	}

	private boolean hasOverlap(Employer firstEmpl, Employer secondEmpl) {
		return (firstEmpl.getDateFrom().isBefore(secondEmpl.getDateTo())
				|| firstEmpl.getDateFrom().isEqual(secondEmpl.getDateTo()))
				&& (firstEmpl.getDateTo().isAfter(secondEmpl.getDateFrom())
						|| firstEmpl.getDateTo().isEqual(secondEmpl.getDateFrom()));
	}

	private boolean isTeamPresent(Team team, long firstEmplId, long secondEmplId) {
		return (team.getEmployer1() == firstEmplId && team.getEmployer2() == secondEmplId)
				|| (team.getEmployer1() == secondEmplId && team.getEmployer2() == firstEmplId);
	}

	private void updateTeam(List<Team> teams, Employer firstEmpl, Employer secondEmpl, long overlapDays) {
		AtomicBoolean isPresent = new AtomicBoolean(false);
		teams.forEach(team -> {
			if (isTeamPresent(team, firstEmpl.getEmpId(), secondEmpl.getEmpId())) {
				team.addOverlapDuration(overlapDays);
				isPresent.set(true);
			}
		});
		if (!isPresent.get()) {
			Team newTeam = new Team();
			newTeam.setEmployer1(firstEmpl.getEmpId());
			newTeam.setEmployer2(secondEmpl.getEmpId());
			newTeam.setDuration(overlapDays);
			teams.add(newTeam);
		}
	}

}
