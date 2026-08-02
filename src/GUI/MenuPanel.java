package GUI;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    public interface MenuListener{
        void onContinue();
        void onNewGame();
        void onStatus();
        void onCredits();
        void onQuit();
    }

    public MenuPanel(boolean hasSavedGame, boolean hasCompletedOrDied, MenuListener listener){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));
        setPreferredSize(new Dimension(400, 340));

        JLabel title = new JLabel("Yohane The Parhelion!");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("The Siren in the Mirror World!");
        subtitle.setFont(new Font("SansSerif", Font.ITALIC, 14));
        subtitle.setAlignmentX(CENTER_ALIGNMENT);

        add(title);
        add(subtitle);
        add(Box.createRigidArea(new Dimension(0, 25)));

        // Continue only shows up if a playthrough is currently saved
        if(hasSavedGame){
            add(makeMenuButton("Continue", e -> listener.onContinue()));
            add(Box.createRigidArea(new Dimension(0, 8)));
        }

        // label swaps to "New Game+" once the player has finished or died at least once
        String newGameLabel = hasCompletedOrDied ? "New Game+" : "New Game";
        add(makeMenuButton(newGameLabel, e -> listener.onNewGame()));
        add(Box.createRigidArea(new Dimension(0, 8)));

        add(makeMenuButton("Status", e -> listener.onStatus()));
        add(Box.createRigidArea(new Dimension(0, 8)));

        add(makeMenuButton("Credits", e -> listener.onCredits()));
        add(Box.createRigidArea(new Dimension(0, 8)));

        add(makeMenuButton("Quit", e -> listener.onQuit()));
    }

    //------------------------
    // helper
    //------------------------
    private JButton makeMenuButton(String text, java.awt.event.ActionListener action){
        JButton button = new JButton(text);
        button.setAlignmentX(CENTER_ALIGNMENT);
        button.addActionListener(action);
        return button;
    }
}
