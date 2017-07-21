package com.teamtreehouse.model;

import java.util.Set;
import java.util.TreeSet;

public class Team implements Comparable<Team> {

	private String teamName;
	private String teamCoach;
	public Set<Player> teamPlayers = new TreeSet<>();

	public final String getTeamName() {
		return teamName;
	}

	public final String getTeamCoach() {
		return teamCoach;
	}

	public Team(String teamName, String teamCoach, Set<Player> teamPlayers) {
		this.teamName = teamName;
		this.teamCoach = teamCoach;
		this.teamPlayers = teamPlayers;
	}

	public TreeSet<Player> getTeamPlayers() {
		return new TreeSet<>(teamPlayers);
	}

	@Override
	public int compareTo(Team o) {
		return this.teamName.compareToIgnoreCase(o.teamName);
	}

	@Override
	public String toString() {
		return String.format("Team name: %s, Team coach: %s %n", getTeamName(), getTeamCoach());
	}

}
