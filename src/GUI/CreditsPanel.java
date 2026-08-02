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
                        "- Tile sprites: https://morain.itch.io/backgrounds-and-textures\n" +
                        "- Gold sprites: https://free-game-assets.itch.io/free-goblin-loot-icons-3232-pixel-art\n" +
                        "- Treasure sprites: https://schwarnhild.itch.io/basic-caves-and-dungeons-tileset-32x32-pixels\n" +
                        "- Lailaps sprite: https://netherzapdos.itch.io/paws-whiskers-isometric-dogs-pack\n" +
                        "- Player sprite:https://toffeecraft.itch.io/bunny-character-pixel\n" +
                        "- Siren sprite:https://maranza.itch.io/assets?download\n" +
                        "- Bat sprite: https://papoycore.itch.io/bat\n\n" +
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
