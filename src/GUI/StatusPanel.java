package GUI;

import Game.OverallStats;
import LivingThings.Idol;

import javax.swing.*;
import java.awt.*;

public class StatusPanel  extends JPanel {
    public interface StatusListener {
        void onBack();
    }

    public StatusPanel(OverallStats overallStats, StatusListener listener){
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setPreferredSize(new Dimension(400, 340));

        JLabel titleLabel = new JLabel("Overall Status");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        StringBuilder sb = new StringBuilder();
        for(Idol idol : overallStats.getAqours()){
            sb.append("Times ").append(idol.getName()).append(" was saved: ")
                    .append(idol.getRescueCount()).append("\n");
        }
        sb.append("\nTimes Siren was defeated: ").append(overallStats.getNumSiren());
        sb.append("\nNo. of game overs: ").append(overallStats.getGamesLost());
        sb.append("\nTotal gold spent: ").append(overallStats.getGoldSpent()).append(" gp");

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        textArea.setBackground(this.getBackground());

        add(new JScrollPane(textArea), BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> listener.onBack());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
