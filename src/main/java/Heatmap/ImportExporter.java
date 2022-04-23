package Heatmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.Scanner;

public class ImportExporter implements IReadWrite {

    @Override
    public void write(String fileName, Heatmap heatmap) throws FileNotFoundException {
        try (PrintWriter writer = new PrintWriter(getFile(fileName))) {

            writer.println("[Maps]");
            for (Map map : heatmap.getMaps()) {
                writer.println(String.format("%s;%s", map.getName(), map.getImgFileName()));
            }

            writer.println("[Teams]");
            for (Team team : heatmap.getTeams()) {
                StringBuilder sb = new StringBuilder();
                sb.append(team.getName()).append(";");
                Iterator<Player> it = team.getPlayers().iterator();
                while (it.hasNext()) {
                    sb.append(it.next().getName());
                    if (it.hasNext()) {
                        sb.append(";");
                    }
                }
                writer.println(sb);
            }

            writer.println("[MatchTypes]");
            for (MatchType matchType : heatmap.getMatchTypes()) {
                writer.println(matchType.toString());
            }

            writer.println("[ObjectivePoints]");
            for (Map map : heatmap.getMaps()) {
                for (ObjectivePoint objectivePoint : map.getObjectivePoints()) {
                    writer.println(
                            String.format("%s;%s;%s;%s", objectivePoint.getMap().getName(), objectivePoint.getX(),
                                    objectivePoint.getY(), objectivePoint.getName()));
                }
            }

            writer.println("[PlayerDefencePoints]");
            for (Map map : heatmap.getMaps()) {
                for (PlayerDefencePoint pp : map.getPlayerDefencePoints()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(pp.getMap().getName()).append(";");
                    sb.append(pp.getTeam().getName()).append(";");
                    sb.append(pp.getObjectivePoint().getName()).append(";");
                    sb.append(pp.getX()).append(";");
                    sb.append(pp.getY());
                    if (pp.getPlayer() != null) {
                        sb.append(";").append(pp.getPlayer().getName());
                    } else {
                        sb.append(";null");
                    }
                    if (pp.getMatchType() != null) {
                        sb.append(";").append(pp.getMatchType().toString());
                    } else {
                        sb.append(";null");
                    }
                    writer.println(sb);
                }
            }
        }
    }

    @Override
    public Heatmap read(String fileName) throws FileNotFoundException {
        Heatmap heatmap = new Heatmap();

        try {
            URL url = ImportExporter.class.getResource("saves");
            File file = new File(url.getFile() + File.separator + fileName + ".txt");

            if (!file.exists()) {
                throw new FileNotFoundException("File not found: " + file.getAbsolutePath());
            }
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.startsWith("[Maps]")) {
                        while (scanner.hasNextLine()) {
                            line = scanner.nextLine();
                            if (line.startsWith("[Teams]")) {
                                break;
                            }
                            String[] mapInfo = line.split(";");
                            Map map = new Map(mapInfo[0], mapInfo[1]);
                            heatmap.addMap(map);
                        }
                    } else if (line.startsWith("[Teams]")) {
                        while (scanner.hasNextLine()) {
                            line = scanner.nextLine();
                            if (line.startsWith("[MatchTypes]")) {
                                break;
                            }
                            String[] teamInfo = line.split(";");
                            Team team = new Team(teamInfo[0]);
                            for (int i = 1; i < teamInfo.length; i++) {
                                team.addPlayer(new Player(teamInfo[i]));
                            }
                            heatmap.addTeam(team);
                        }
                    } else if (line.startsWith("[MatchTypes]")) {
                        while (scanner.hasNextLine()) {
                            line = scanner.nextLine();
                            if (line.startsWith("[ObjectivePoints]")) {
                                break;
                            }
                            MatchType matchType = new MatchType(line);
                            heatmap.addMatchType(matchType);
                        }
                    } else if (line.startsWith("[ObjectivePoints]")) {
                        while (scanner.hasNextLine()) {
                            line = scanner.nextLine();
                            if (line.startsWith("[PlayerDefencePoints]")) {
                                break;
                            }
                            String[] objectivePointInfo = line.split(";");
                            ObjectivePoint objectivePoint = new ObjectivePoint(heatmap.getMap(objectivePointInfo[0]),
                                    Double.parseDouble(objectivePointInfo[1]),
                                    Double.parseDouble(objectivePointInfo[2]), objectivePointInfo[3]);
                            heatmap.getMap(objectivePointInfo[0]).addObjectivePoint(objectivePoint);
                        }
                    } else if (line.startsWith("[PlayerDefencePoints]")) {
                        while (scanner.hasNextLine()) {
                            line = scanner.nextLine();
                            if (line.isEmpty()) {
                                break;
                            }
                            String[] playerDefencePointInfo = line.split(";");
                            PlayerDefencePoint playerDefencePoint = new PlayerDefencePoint(
                                    heatmap.getMap(playerDefencePointInfo[0]),
                                    heatmap.getTeam(playerDefencePointInfo[1]),
                                    heatmap.getMap(playerDefencePointInfo[0]).getObjectivePoint(
                                            playerDefencePointInfo[2]),
                                    Double.parseDouble(playerDefencePointInfo[3]),
                                    Double.parseDouble(playerDefencePointInfo[4]));
                            if (!playerDefencePointInfo[5].equals("null")) {
                                playerDefencePoint.setPlayer(heatmap.getPlayer(playerDefencePointInfo[5]));
                            }
                            if (!playerDefencePointInfo[6].equals("null")) {
                                playerDefencePoint.setMatchType(heatmap.getMatchType(playerDefencePointInfo[6]));
                            }
                        }
                    }
                }
                return heatmap;
            } catch (Exception e) {
                throw new FileNotFoundException("File not found: " + file.getAbsolutePath());
            }
        } catch (FileNotFoundException e) {
            throw e;
        }
    }

    private static File getFile(String filename) {
        URL url = ImportExporter.class.getResource("saves");
        // System.out.println(url);

        File file = new File(url.getFile() + File.separator + filename + ".txt");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return file;
    }

}
