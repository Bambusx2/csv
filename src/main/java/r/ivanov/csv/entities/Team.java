package r.ivanov.csv.entities;

import lombok.Data;

@Data
public class Team {

	private long employer1;
	private long employer2;
	private long duration;

	public void addOverlapDuration(long overlap) {
		this.duration += overlap;
	}
}
