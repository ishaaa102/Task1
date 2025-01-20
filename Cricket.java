import java.util.*;

class Player{
    private final String name;
    private boolean isOut;

    public Player(String name){
        this.name=name;
        this.isOut=false;
    }

    public String getName(){
        return name;
    }

    public boolean isOut(){
        return isOut;
    }

    public void setOut() {
        this.isOut = true;
    }
}

class Team{
    private final String name;
    private final List<Player> players;
    private int totalScore;

    public Team(String name, List<String> playerNames){
        this.name=name;
        this.players= new ArrayList<>();
        for (String playerName : playerNames){
            this.players.add(new Player(playerName));
        }
        this.totalScore =0;
    }
    public String getName() {
        return name;
    }

    public List<Player> getPlayers(){
        return players;
    }

    public int getTotalScore(){
        return totalScore;
    }

    public void addRuns(int runs){
        this.totalScore += runs;
    }
}

class RandomEventGenerator {
    private static final String[] events = {"0", "1", "2", "3", "4", "5", "6", "W"};
    private final Random random = new Random();

    public String generateEvent() {
        return events[random.nextInt(events.length)];
    }
}

class WatchController {
    private final Team team1;
    private final Team team2;
    private final int overs;

    public WatchController(Team team1, Team team2, int overs){
        this.team1 = team1;
        this.team2 = team2;
        this.overs = overs;
    }

    public void startMatch() {
        System.out.println("Starting the match between " + team1.getName() + " and " + team2.getName());
        int team1Score = playInnings(team1);
        int team2Score = playInnings(team2);

        System.out.println("\n Match results");
        System.out.println(team1.getName() + " scored " + team1Score);
        System.out.println(team2.getName() + " scored " + team2Score);

        if(team1Score > team2Score){
            System.out.println(team1.getName() + " wins");
        } else if (team2Score >team1Score) {
            System.out.println(team2.getName() + " wins");
        }
        else {
            System.out.println("Match draws");
        }
    }

    private int playInnings(Team team){
        System.out.println("\n" + team.getName() + " is batting");
        RandomEventGenerator generator = new RandomEventGenerator();
        int balls = overs * 6;
        int currentPlayerIndex =0;

        for (int i=0; i<balls; i++){
            if (currentPlayerIndex >= team.getPlayers().size()){
                System.out.println("all players are out");
                break;
            }

            String event = generator.generateEvent();
            System.out.println("ball " + (i+1) + ": " + event);

            if(event.equals("W")) {
                System.out.println(team.getPlayers().get(currentPlayerIndex).getName() + " is out");
                team.getPlayers().get(currentPlayerIndex).setOut();
                currentPlayerIndex++;
            }
            else{
                int runs = Integer.parseInt(event);
                team.addRuns(runs);
                System.out.println("runs scored : " + runs + ", total : " + team.getTotalScore());
            }
        }
        return team.getTotalScore();
    }
}

public class Cricket {
    public static void main(String[] args) {
        List<String> team1Players = Arrays.asList("player1_t1", "player2_t1", "player3_t1", "player4_t1", "player5_t1");
        List<String> team2Players = Arrays.asList("player1_t2", "player2_t2", "player3_t2", "player4_t2", "player5_t2");

        Team team1 = new Team("team A", team1Players);
        Team team2 = new Team("team B", team2Players);

        WatchController matchController = new WatchController(team1, team2, 2);
        matchController.startMatch();
    }
}
