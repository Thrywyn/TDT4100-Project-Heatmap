package Heatmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Iterator;

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
                            String.format("%s;%s;%s;%s", objectivePoint.getName(), objectivePoint.getMap().getName(),
                                    objectivePoint.getX(), objectivePoint.getY()));
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
                    }
                    if (pp.getMatchType() != null) {
                        sb.append(";").append(pp.getMatchType().toString());
                    }
                    writer.println(sb);
                }
            }
        }
    }

    @Override
    public Heatmap read(String fileName) {
        // TODO Auto-generated method stub
        return null;
    }

    private static File getFile(String filename) {
        URL url = ImportExporter.class.getResource("saves");
        System.out.println(url);

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
