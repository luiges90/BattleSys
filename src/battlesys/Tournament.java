package battlesys;

import battlesys.io.NullPrintStream;
import battlesys.exception.BattleSysException;
import java.io.*;
import java.util.*;

/**
 * Methods for running a round-robin tournament
 * @author Peter
 */
public final class Tournament {

    private Tournament() {
    }

    /**
     * According to the team name field of every player, group the player together into list of list of players
     * with every list of players being in the same team (as another list of players).
     * @param tp Player to be processed
     * @return Teamed up players
     */
    private static final List<PlayerList> teamUp(PlayerList tp) {
        List<PlayerList> ttp = new ArrayList<PlayerList>(tp.size());
        for (Player thisPlayer : tp) {
            if (thisPlayer.placed) {
                continue;
            }
            PlayerList sameTeam = thisPlayer.getPlayersOfSameTeam(tp);
            ttp.add(sameTeam);
            for (Player p : sameTeam) {
                p.placed = true;
            }
        }

        return ttp;
    }

    /**
     * Get team names of respective players
     * @param p Player list
     * @return Team name list
     */
    public static final String[] getTeamNames(List<PlayerList> p) {
        String[] teamNames = new String[p.size()];
        for (int i = 0; i < p.size(); ++i) {
            teamNames[i] = p.get(i).get(0).getTeamName();
        }
        return teamNames;
    }

    /**
     * Get the schedule string
     * @param numGroups Number of groups
     * @param numActualTeam Actual number of teams in every group
     * @param teamIndexOffset
     * @param teamNames Names of all the teams
     * @return
     */
    private static final String getSchedule(int numGroups, int[] numActualTeam, int[] teamIndexOffset, String[] teamNames) {
        int h, i, j, k;
        StringBuilder s = new StringBuilder();
        for (h = 0; h < numGroups; ++h) {

            s.append("[b][u]第").append(h + 1).append("組[/u][/b]\n\n");

            //For each round
            for (i = 0; i < numActualTeam[h] - 1; ++i) {

                s.append("[b]第").append(i + 1).append("輪[/b]\n");

                for (j = teamIndexOffset[h]; j < numActualTeam[h] + teamIndexOffset[h]; ++j) {

                    k = (i + j - teamIndexOffset[h] + 1) % (numActualTeam[h]) + teamIndexOffset[h];

                    s.append(teamNames[j]).append(" 對 ").append(teamNames[k]).append('\n');

                }

                s.append('\n');

            }

            s.append('\n');

        }

        return s.toString();
    }

    /**
     * Prepare the list of players, before calling the actual tournament method.
     * This methods shuffles the list and group the players together
     * @param tp List of players that is going to have the tournament
     * @return A list of playerList, each playerList consists of players teamed up according to their team name
     */
    public static final List<PlayerList> prepare(PlayerList tp) {
        //Shuffle the array
        Utility.shuffle(tp);

        //Find teams and placed them into same array slot
        return Tournament.teamUp(tp);
    }
  
    /**
     * Really run a tournament. The detailed result will be written to current folder, named round-x-y.txt
     * @param p List of grouped player as returned by the Prepare method.
     * @param maxPlayerInGroup Maximum number of player in the same group
     * @param replayTime How many times the round-robin is to be replayed
     * @param makeImage Whether images should be created and uploaded to ftp server
     * @param rankingShowData Whether to show player data in the rankings
     * @param writeAllResults Whether to write all results, or only the scores
     * @param progressBar The JProgressBar object to show the progress. null to disable this function.
     * @param scheduleWriter Output stream to write the schedule, null to discard all these outputs
     * @param totalStatWriter Output stream to write the statistics of every player and overall statistics, null to discard all these outputs
     * @param moveScoreWriter Output stream to write the scores of each moves, null to discard all these outputs
     * @return The result of the tournament, that is, the list of players sorted with respect to the score they get, in ascending order
     * @throws BattleSysException
     */
    public static final PlayerBattleRecord[] tournament(List<PlayerList> p, int maxPlayerInGroup, int replayTime,
            boolean makeImage, boolean rankingShowData, boolean writeAllResults, javax.swing.JProgressBar progressBar,
            Writer scheduleWriter, Writer totalStatWriter, Writer moveScoreWriter, File baseFilePath) throws BattleSysException, IOException {

        int h, i, j, k;
        if (scheduleWriter == null) {
            scheduleWriter = new PrintWriter(new NullPrintStream());
        }
        if (totalStatWriter == null) {
            totalStatWriter = new PrintWriter(new NullPrintStream());
        }
        if (moveScoreWriter == null) {
            moveScoreWriter = new PrintWriter(new NullPrintStream());
        }

        if (p.size() == 1) {
            System.err.println("There is only one team!");
            return new PlayerBattleRecord[0];
        }

        PlayerBattleRecord[] playerBr = null;

        //When max round occur
        String maxRoundStr = "";

        //Get team names
        String[] teamNames = Tournament.getTeamNames(p);

        //Group players
        //Number of groups
        int numGroups = (p.size() - 1) / maxPlayerInGroup + 1;
        //How many teams in a group
        int teamInGroup = p.size() / numGroups;
        //How many groups have one more team than other
        int moreTeamGroup = p.size() % numGroups;
        //Actual number of teams in every group
        int[] numActualTeam = new int[numGroups];
        //Offset of team index in each group
        int[] teamIndexOffset = new int[numGroups];

        //Compute the above things
        for (h = 0; h < numGroups; ++h) {
            numActualTeam[h] = h < moreTeamGroup ? teamInGroup + 1 : teamInGroup;
            if (h > 0) {
                teamIndexOffset[h] = teamIndexOffset[h - 1] + numActualTeam[h - 1];
            } else {
                teamIndexOffset[h] = 0;
            }
        }

        //Generate Schedule
        scheduleWriter.write(getSchedule(numGroups, numActualTeam, teamIndexOffset, teamNames));
        scheduleWriter.flush();

        //CompeteTable ct = new CompeteTable(p);

        //compute number of total battles
        int numberOfCompletedBattle = 0;
        int totalBattles = 0;

        for (h = 0; h < numGroups; ++h) {
            for (i = 0; i < numActualTeam[h] - 1; ++i) {
                for (j = teamIndexOffset[h]; j < numActualTeam[h] + teamIndexOffset[h]; ++j) {
                    totalBattles++;
                }
            }
        }

        totalBattles *= replayTime * numGroups;
        if (progressBar != null) {
            progressBar.setValue(0);
            progressBar.setMinimum(0);
            progressBar.setMaximum(totalBattles);
        }

        int totalRound = 0;
        int maxRound = 0;
        int noBattles = 0;

        //Total player scores weighted each ability score value
        long hpScore, atkScore, defScore, spdScore, morScore;
        //Player battle time count
        long playerBattleTimeCnt = 0;

        //List of all moves
        Collection<Move> allMoves = Move.getAllMoves();

        //Total Scores of the players having this attack
        Map<Move, Long> moveScore = new HashMap<Move, Long>();

        //How many players (in terms of battle time) have this attack
        Map<Move, Long> moveCnt = new HashMap<Move, Long>();

        hpScore = atkScore = defScore = spdScore = morScore = 0;
        for (Move m : allMoves) {
            moveScore.put(m, 0L);
            moveCnt.put(m, 0L);
        }

        long lastUpdate = System.currentTimeMillis();

        for (int replay = 0; replay < replayTime; replay++) {

            System.err.println("Replay " + replay);

            //Battle, for every group
            for (h = 0; h < numGroups; ++h) {

                //In each group
                for (i = 0; i < numActualTeam[h] - 1; ++i) {

                    PrintWriter output = null;

                    if (writeAllResults) {
                        try {
                            output = new PrintWriter(new BufferedWriter(new FileWriter(new File(baseFilePath, "round" + (h + 1) + '-' + (i + 1) + ".txt"))));
                        } catch (Exception ex) {
                            System.err.println("Error occured during creating file: " + "round" + (h + 1) + '-' + (i + 1) + ".txt:" + ex.getMessage());
                            System.exit(1);
                        }
                    } else {
                        output = new PrintWriter(new NullPrintStream());
                    }

                    output.println("[b][u]第" + (h + 1) + "組第" + (i + 1) + "輪[/u][/b]");

                    //For each pair
                    for (j = teamIndexOffset[h]; j < numActualTeam[h] + teamIndexOffset[h]; ++j) {

                        numberOfCompletedBattle++;
                        if (progressBar != null) {
                            progressBar.setValue(numberOfCompletedBattle);
                            progressBar.setString("場次： " + numberOfCompletedBattle + "/" + totalBattles
                                    + "(進度 " + Math.round((double) numberOfCompletedBattle / totalBattles * 100 * 100) / 100 + "%)");
                        } else {
                            if (System.currentTimeMillis() - lastUpdate > 1000) {
                                System.err.println("場次： " + numberOfCompletedBattle + "/" + totalBattles
                                        + "(進度 " + Math.round((double) numberOfCompletedBattle / totalBattles * 100 * 100) / 100 + "%)");
                                lastUpdate = System.currentTimeMillis();
                            }
                        }


                        k = (i + j - teamIndexOffset[h] + 1) % (numActualTeam[h]) + teamIndexOffset[h];

                        //Get team member arrays
                        PlayerList t1 = p.get(j);
                        PlayerList t2 = p.get(k);

                        SingleBattleResult btr = null;
                        try {
                            btr = Player.battle(t1, t2, teamNames[j], teamNames[k], i, h + "." + j + "-" + k);
                        } catch (Exception ex) {
                            System.err.println("for players " + teamNames[j] + " and " + teamNames[k] + "!");
                            ex.printStackTrace();
                            System.exit(1);
                        }

                        output.println(btr.getResultString());

                        totalRound += btr.getRound();
                        if (btr.getRound() > maxRound) {
                            maxRound = btr.getRound();
                            maxRoundStr = "第" + (h + 1) + "組第" + (i + 1) + "回：" + teamNames[j] + "對" + teamNames[k];
                        }
                        ++noBattles;

                        if (replayTime == 1) {
                            if (makeImage) {
                                String path = btr.getBattleRecord().generateGraph(baseFilePath);
                                output.println("[IMG]" + path + "[/IMG]");
                            } else {
                                output.println("[IMG]" + btr.getBattleRecord().getFilePath() + "[/IMG]");
                            }

                        }

                    }//j for loop - for each pair

                    //A copy of players in this group for sorting
                    //We don't want the original order to be changed!

                    playerBr = new PlayerBattleRecord[numActualTeam[h]];

                    for (int m = teamIndexOffset[h]; m < numActualTeam[h] + teamIndexOffset[h]; ++m) {
                        playerBr[m - teamIndexOffset[h]] = new PlayerBattleRecord(p.get(m));
                    }

                    Arrays.sort(playerBr);

                    output.println();
                    output.println("積分表：");

                    output.println("名次\t名稱\t勝\t敗\t和\t積分");
                    int[] rank = new int[numActualTeam[h]];
                    int currentRank = 1;
                    rank[numActualTeam[h] - 1] = 1;
                    for (j = numActualTeam[h] - 2; j >= 0; --j) {
                        if (playerBr[j].compareTo(playerBr[j + 1]) != 0) {
                            currentRank = numActualTeam[h] - j;
                        }
                        rank[j] = currentRank;
                    }
                    for (j = numActualTeam[h] - 1; j >= 0; --j) {
                        output.print(rank[j] + "\t" + playerBr[j].getName() + "\t" + playerBr[j].getWin() + "\t"
                                + playerBr[j].getLose() + "\t" + playerBr[j].getDraw() + "\t" + playerBr[j].getScore());
                        if (rankingShowData) {
                            for (Player pl : playerBr[j].getPlayers()) {
                                output.print(pl.getAbyDescription());
                            }
                        }
                        output.println();
                    }

                    output.close();

                    //write aby and move statistic - get sum
                    //ignore the first 10% of the scores as they skew the result and practically no one will use configs like them.
                    for (int g = 0/*(int) (playerBr.length * 0.1)*/; g < playerBr.length; g++) {
                        for (Player pl : playerBr[g].getPlayers()) {
                            hpScore += (pl.getInitHp() - 2000) / 800 * playerBr[g].getScore();
                            atkScore += (pl.getInitAtk()) * playerBr[g].getScore();
                            defScore += (pl.getInitDef()) * playerBr[g].getScore();
                            spdScore += (pl.getInitSpd()) * playerBr[g].getScore();
                            morScore += (pl.getInitMor()) * playerBr[g].getScore();
                            playerBattleTimeCnt++;
                            for (Move m : pl.getBoughtAtk()) {
                                moveCnt.put(m, moveCnt.get(m) + 1);
                                moveScore.put(m, moveScore.get(m) + playerBr[g].getScore());
                            }
                        }
                    }

                }//i for loop - for each round in a group

            }//h for loop - battle for every group

        }

        //Write total statistic
        totalStatWriter.write(("平均回數：" + totalRound / (noBattles + 0.0) + "\n最大回數：" + maxRound + "(" + maxRoundStr + ")\n\n"));

        //For each group
        for (PlayerList pl : p) {
            for (Player pl2 : pl) {
                totalStatWriter.write((pl2.getTotalStatDescription() + '\n'));
            }
        }
        totalStatWriter.flush();

        //write move score stat
        moveScoreWriter.write(("Abilities and Moves scores: " + "\nHP: " + hpScore / playerBattleTimeCnt + "\nATK: " + atkScore / playerBattleTimeCnt + "\nDEF: " + defScore / playerBattleTimeCnt + "\nSPD: " + spdScore / playerBattleTimeCnt + "\nMOR: " + morScore / playerBattleTimeCnt + '\n'));
        for (Move m : allMoves) {
            if (moveScore.get(m) == 0) {
                moveScoreWriter.write((m.getClass().getSimpleName() + " : never used\n"));
            } else {
                moveScoreWriter.write((m.getClass().getSimpleName() + " : " + moveScore.get(m) / moveCnt.get(m) + '\n'));
            }
        }
        moveScoreWriter.flush();

        return playerBr;

    }
}
