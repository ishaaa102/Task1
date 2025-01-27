import java.util.*;

enum PlayerType {
    BATSMAN,
    BOWLER
}

class Player {
    private final String name;
    private final PlayerType type;

    public Player(String name, PlayerType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public PlayerType getType() {
        return type;
    }
}

class Team {
    private final String name;
    private final List<Player> players;
    private int totalScore;

    public Team(String name, List<String> playerNames) {
        this.name = name;
        this.players = new ArrayList<>();
        for (int i = 0; i < playerNames.size(); i++) {
            PlayerType type;
            if (i < 4) {
                type = PlayerType.BATSMAN;
            } else {
                type = PlayerType.BOWLER;
            }
            this.players.add(new Player(playerNames.get(i), type));
        }
        this.totalScore = 0;
    }

    public String getName() {
        return name;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void addRuns(int runs) {
        this.totalScore += runs;
    }

    public boolean hasOnePlayerLeft() {
        return players.size() == 1;
    }

    public void removePlayer() {
        if (players.size() > 0) {
            players.remove(0);
        }
    }
}

class RandomEventGenerator {
    private static final String[] events = {"0", "1", "2", "3", "4", "5", "6", "W"};
    private final Random random = new Random();

    public String generateEvent() {
        int rand = random.nextInt(10);
        if (rand < 8) {
            return events[random.nextInt(7)];
        } else {
            return "W";
        }
    }
}

class MatchController {
    private final Team team1;
    private final Team team2;
    private final int overs;

    public MatchController(Team team1, Team team2, int overs) {
        this.team1 = team1;
        this.team2 = team2;
        this.overs = overs;
    }

    public void startMatch() {
        System.out.println("Starting the match between " + team1.getName() + " and " + team2.getName());
        int team1Score = playInnings(team1, null);
        System.out.println("\n" + team1.getName() + " scored " + team1Score);

        System.out.println("Now, " + team2.getName() + " is batting...");
        playInnings(team2, team1Score);
        System.out.println("\nMatch Ended!");
    }

    private int playInnings(Team team, Integer team1Score) {
        RandomEventGenerator generator = new RandomEventGenerator();
        int balls = overs * 6;
        int currentPlayerIndex = 0;
        Player currentBatsman = team.getPlayers().get(currentPlayerIndex);

        for (int i = 0; i < balls; i++) {
            if (team.hasOnePlayerLeft()) {
                System.out.println(team.getName() + " has only 1 player left. Match is over.");
                break;
            }

            String event = generator.generateEvent();
            System.out.println("Ball " + (i + 1) + ": " + event);

            if (event.equals("W")) {
                System.out.println(currentBatsman.getName() + " is out.");
                team.removePlayer();
                currentPlayerIndex++;
                if (currentPlayerIndex < team.getPlayers().size()) {
                    currentBatsman = team.getPlayers().get(currentPlayerIndex);
                }
            } else {
                int runs = Integer.parseInt(event);
                team.addRuns(runs);
                printScoreboard(team, currentBatsman, runs, i + 1);

                if (team1Score != null && team.getTotalScore() > team1Score) {
                    System.out.println(team.getName() + " has exceeded " + team1.getName() + "'s score by 1, match ends!");
                    break;
                }
            }
        }
        return team.getTotalScore();
    }

    private void printScoreboard(Team team, Player currentBatsman, int runs, int ballNumber) {
        int oversCompleted = ballNumber / 6;
        int ballsLeft = (overs - oversCompleted) * 6 - (ballNumber % 6);
        System.out.println("\nScoreboard after ball " + ballNumber + ":");
        System.out.println("Overs: " + oversCompleted + "." + (ballNumber % 6));
        System.out.println("Batting Team: " + team.getName());
        System.out.println("Current Batsman: " + currentBatsman.getName());
        System.out.println("Runs on current ball: " + runs);
        System.out.println("Total Runs: " + team.getTotalScore());
        System.out.println("Balls Left: " + ballsLeft);
        System.out.println("------------------");
    }
}

public class Cricket {
    public static void main(String[] args) {
        List<String> team1Players = Arrays.asList("player1_t1", "player2_t1", "player3_t1", "player4_t1", "player5_t1");
        List<String> team2Players = Arrays.asList("player1_t2", "player2_t2", "player3_t2", "player4_t2", "player5_t2");

        Team team1 = new Team("team A", team1Players);
        Team team2 = new Team("team B", team2Players);

        MatchController matchController = new MatchController(team1, team2, 2);
        matchController.startMatch();
    }
}
