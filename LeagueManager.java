import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;
import com.teamtreehouse.model.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map;
import java.util.Scanner;

public class LeagueManager {

	private static Player[] players = Players.load(); // Array players is unsorted
	private static Set<Player> freePlayers = new TreeSet<>(Arrays.asList(players));
	static List<Player> freePlayersList = new ArrayList<Player>(freePlayers);
	static List<Team> allTeams = new ArrayList<Team>();

	private static Map<String, ArrayList<Player>> heightGroup;
	private static Map<String, Integer> experienceLevels;

	private static Scanner scanner;
	private static String option;

	public static void main(String[] args) {

		scanner = new Scanner(System.in);
		System.out.printf("There are currently %d total players.%n%n", players.length);
		System.out.printf("[  ] %-24s %-8s %-8s %n", "Name", "Height", "Experienced?");
		System.out.printf("---- %-24s %-8s %-8s %n", "--------------------", "------", "------------");

		Iterator<Player> iter = freePlayers.iterator();
		int i = 0;
		int k = freePlayers.size();
		Player p = null;
		// System.out.printf("%nTesting iterator...%n");
		while (iter.hasNext() && i <= k) {
			i++;
			p = iter.next();
			System.out.printf("[%2s] %s %n", i, p);
		}

		do {
			option = displayMenu();

			switch (option.toLowerCase()) {
			case "1": // "create":
				createNewTeam();
				break;
			case "2": // "add":
				addPlayerToTeam();
				break;
			case "3": // "remove":
				removePlayerFromTeam();
				break;
			case "4": // "report":
				displayHeightReport();
				break;
			case "5": // "balance":
				displayLeagueBalanceReport();
				break;
			case "6": // "roster":
				displayRoster();
				break;
			case "7": // "free players":
				System.out.println("List of free players:");
				System.out.print("---------------------");
				listFreePlayers();
				break;
			case "0": // "quit":
				System.out.println("\nQuitting...\n");
				System.exit(0);
			default:
				System.out.printf("%nUnknown choice: '%s', retry:%n", option);
				scanner.nextLine();
			}

		} while (true);
	}

	public static String displayMenu() {
		System.out.println("\nMenu\n" + "---- " + "\n1: Create - Create a new team " + "\n2: Add - Add a player to a team "
				+ "\n3: Remove - Remove a player from a team " + "\n4: Report - View a report of a team by height "
				+ "\n5: Balance - View the League Balance Report " + "\n6: Roster - View roster (list of teams and players)"
				+ "\n7: Free players - View list of unassigned players" + "\n0: Quit - Exit the program ");
		System.out.printf("\nSelect an option: ");
		option = scanner.next();
		return option;
	}

	public static void createNewTeam() {

		System.out.print("\nChoose a name for the new team: ");
		String teamName = scanner.next();
		System.out.print("Enter the name of the coach: ");
		String teamCoach = scanner.next();

		Set<Player> teamPlayers = new TreeSet<Player>();
		Team team = new Team(teamName, teamCoach, teamPlayers);

		System.out.printf("Team '%s' has been created, coached by '%s'.%n", team.getTeamName(), team.getTeamCoach());

		allTeams.add(team);
		Collections.sort(allTeams);
		displayRoster();
	}

	public static Team selectTeam() {
		System.out.printf("The following teams are available:%n");

		int i = 1;
		for (Team t : allTeams) {
			System.out.printf("(%d.) %s %n", i++, t.getTeamName().toString());
		}
		System.out.printf("%nSelect a team: ");
		int selectedInt = scanner.nextInt();
		Team selectedTeam = allTeams.get(selectedInt - 1);

		System.out.printf("Selected team is: %s%n", selectedTeam.getTeamName());

		return selectedTeam;
	}

	private static Player freePlayerSelect() {

		System.out.printf("%nSelect a player: ");
		int selectedPlayerIndex = scanner.nextInt();
		Player selectedPlayer = freePlayersList.get(selectedPlayerIndex - 1);

		return selectedPlayer;
	}

	private static Player teamPlayerSelect(List<Player> teamPlayers) {

		System.out.printf("%nSelect a player: ");
		int selectedPlayerIndex = scanner.nextInt();
		Player selectedPlayer = teamPlayers.get(selectedPlayerIndex - 1);

		return selectedPlayer;
	}

	public static void updateFreePlayersList() {

		freePlayersList.clear();
		freePlayersList.addAll(freePlayers);
	}

	public static List<Player> teamPlayersAsList(Team currentTeam) {

		Set<Player> teamPlayers = new TreeSet<>(currentTeam.getTeamPlayers());
		List<Player> teamPlayersList = new ArrayList<>(teamPlayers);

		return teamPlayersList;
	}

	public static void addPlayerToTeam() {

		Team selectedTeam = selectTeam();

		System.out.printf("%nSelect a player to add to the team:");
		listFreePlayers();
		Player selectedPlayer = freePlayerSelect();

		selectedTeam.teamPlayers.add(selectedPlayer);
		System.out.printf("%s has been added to team %s%n", selectedPlayer.getFullName(), selectedTeam.getTeamName());

		freePlayers.remove(selectedPlayer);
		updateFreePlayersList();
	}

	public static void removePlayerFromTeam() {

		Team selectedTeam = selectTeam();
		List<Player> selectedTeamPlayers = teamPlayersAsList(selectedTeam);

		System.out.printf("%nSelect a player to remove from the team:");
		displayTeamPlayers(selectedTeam);
		Player selectedPlayer = teamPlayerSelect(selectedTeamPlayers);

		selectedTeam.teamPlayers.remove(selectedPlayer);
		System.out.printf("%s has been removed from team %s%n", selectedPlayer.getFullName(), selectedTeam.getTeamName());

		freePlayers.add(selectedPlayer);
		updateFreePlayersList();
	}

	public static void listFreePlayers() {

		System.out.printf("%n");
		for (int i = 0; i < freePlayersList.size(); i++) {
			Player chosenPlayer = freePlayersList.get(i);
			System.out.printf("(%2d.) %s %s, %s, %s %n", i + 1, chosenPlayer.getFirstName(), chosenPlayer.getLastName(),
					chosenPlayer.getHeightInInches(), chosenPlayer.isPreviousExperience());
		}
	}

	public static void displayHeightReport() {

		heightGroup = new LinkedHashMap<>();

		Team t = selectTeam();
		ArrayList<Player> listShort = new ArrayList<>();
		ArrayList<Player> listAverage = new ArrayList<>();
		ArrayList<Player> listTall = new ArrayList<>();

		for (Player p : t.getTeamPlayers()) {
			int height = p.getHeightInInches();
			// (35-40, 41-46, 47-50 inches)
			if (height >= 35 && height <= 40)
				listShort.add(p);
			else if (height >= 41 && height <= 46)
				listAverage.add(p);
			else if (height >= 47 && height <= 50)
				listTall.add(p);
		}

		heightGroup.put("Short", listShort);
		heightGroup.put("Average", listAverage);
		heightGroup.put("Tall", listTall);

		System.out.printf("%nHeight report for Team %s:", t.getTeamName());
		System.out.printf("%n------------------------------%n");

		for (String s : heightGroup.keySet()) {
			System.out.printf("%s:%n", s.toString());
			ArrayList<Player> pList = heightGroup.get(s);

			if (pList.size() != 0)
				for (Player p : pList)
					System.out.printf(" %s / %s inches%n", p.getFullName(), p.getHeightInInches());
			else
				System.out.println(" <none>");
		}
	}

	public static void displayLeagueBalanceReport() {

		System.out.printf("League balance report:%n");
		System.out.printf("----------------------%n");

		experienceLevels = new HashMap<>();

		for (Team t : allTeams) {
			int numOfExperienced = 0;
			int numOfInexperienced = 0;

			System.out.printf("Team %s:%n", t.getTeamName());

			for (Player p : t.getTeamPlayers()) {
				if (p.isPreviousExperience())
					numOfExperienced++;
				else
					numOfInexperienced++;
				experienceLevels.put("Experienced", numOfExperienced);
				experienceLevels.put("Inexperienced", numOfInexperienced);
			}

			System.out.printf(" Experienced: %s%n Inexperienced: %s%n", numOfExperienced, numOfInexperienced);

		}
	}

	public static void displayRoster() {

		System.out.printf("List of teams:");
		System.out.printf("%n--------------%n");
		for (Team team : allTeams) {
			System.out.printf("Team: %s, Team coach: %s %n", team.getTeamName(), team.getTeamCoach(),
					team.teamPlayers.toString());
			displayTeamPlayers(team);
		}
	}

	private static void displayTeamPlayers(Team team) {

		if (team.getTeamPlayers().size() == 0) {
			System.out.println("There are no players in the team");
		} else {
			// System.out.println("Players:");
			List<Player> currentPlayers = teamPlayersAsList(team);
			for (int i = 0; i < team.getTeamPlayers().size(); i++) {
				Player chosenPlayer = currentPlayers.get(i);
				System.out.printf("(%2d.) %s %s, %s, %s %n", i + 1, chosenPlayer.getFirstName(), chosenPlayer.getLastName(),
						chosenPlayer.getHeightInInches(), chosenPlayer.isPreviousExperience());
			}
		}
		System.out.println();
	}

}
