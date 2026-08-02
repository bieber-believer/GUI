package GUI;

import javax.swing.*;
import java.awt.*;

public class CreditsPanel extends JPanel {
    public interface CreditsListener {
        void onBack();
    }

    public CreditsPanel(CreditsListener listener){
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setPreferredSize(new Dimension(450, 340));

        JLabel titleLabel = new JLabel("Credits");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        // TODO: replace with your real sprite sources + developer names
        String creditsText =
                "Sprites & Assets:\n\n" +
                        "- Tile sprites: [Artist Name] (Year). [Title]. [Publisher/Site]. [Link]\n" +
                        "- Player sprite: [Artist Name] (Year). [Title]. [Publisher/Site]. [Link]\n" +
                        "- Bat sprite: [Artist Name] (Year). [Title]. [Publisher/Site]. [Link]\n\n" +
                        "Developed by:\n" +
                        "- Jasmine Yzabelle Y. Dy\n" +
                        "- Amber Chardonnay Viesca";

        JTextArea textArea = new JTextArea(creditsText);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        textArea.setBackground(this.getBackground());

        add(new JScrollPane(textArea), BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> listener.onBack());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
