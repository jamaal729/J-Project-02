package com.teamtreehouse.model;

import java.io.Serializable;

public class Player implements Comparable<Player>, Serializable {

	private static final long serialVersionUID = 1L;

	private String firstName;
	private String lastName;
	private int heightInInches;
	private boolean previousExperience;

	public Player(String firstName, String lastName, int heightInInches, boolean previousExperience) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.heightInInches = heightInInches;
		this.previousExperience = previousExperience;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public int getHeightInInches() {
		return heightInInches;
	}

	public boolean isPreviousExperience() {
		return previousExperience;
	}

	@Override
	public int compareTo(Player other) {
		// We always want to sort by last name then first name

		String thisFullName = this.getLastName() + this.getFirstName();
		String otherFullName = other.getLastName() + other.getFirstName();

		int compareFullNames = thisFullName.toString().compareToIgnoreCase(otherFullName.toString());
		return compareFullNames;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Player))
			return false;

		Player player = (Player) o;

		if (heightInInches != player.heightInInches)
			return false;
		if (previousExperience != player.previousExperience)
			return false;
		if (!firstName.equals(player.firstName))
			return false;
		return lastName.equals(player.lastName);
	}

	@Override
	public int hashCode() {
		int result = firstName.hashCode();
		result = 31 * result + lastName.hashCode();
		result = 31 * result + heightInInches;
		result = 31 * result + (previousExperience ? 1 : 0);
		return result;
	}

	public String getFullName() {
		return lastName + ", " + firstName;
	}

	public String experienced() {
		if (previousExperience)
			return "Yes";
		else
			return "No";
	}

	public String getExperienceLevel() {
		if (previousExperience)
			return "experienced";
		else
			return "inexperienced";
	}

	@Override
	public String toString() {
		return String.format("%-24s %-8s %-8s", getFullName(), getHeightInInches(), getExperienceLevel());
	}

}
