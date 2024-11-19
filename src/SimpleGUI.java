import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class SimpleGUI {
    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("Drop-Down Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new FlowLayout());

        // Create a drop-down (JComboBox)
        String[] colors = {"Nothing","Red", "Blue", "Green", "Yellow", "White"};
        JComboBox<String> colorDropDown = new JComboBox<>(colors);

        // Create a panel to show the selected color
        JPanel colorPanel = new JPanel();
        colorPanel.setPreferredSize(new Dimension(300, 150));
        colorPanel.setBackground(Color.WHITE);

        // Add an ActionListener to handle selection changes
        colorDropDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedColor = (String) colorDropDown.getSelectedItem();

                // Change the panel's background color based on selection
                switch (Objects.requireNonNull(selectedColor)) {
                    case "Nothing" -> colorPanel.setBackground(Color.WHITE);
                    case "Red" -> colorPanel.setBackground(Color.RED);
                    case "Blue" -> colorPanel.setBackground(Color.BLUE);
                    case "Green" -> colorPanel.setBackground(Color.GREEN);
                    case "Yellow" -> colorPanel.setBackground(Color.YELLOW);
                    case "White" -> colorPanel.setBackground(Color.WHITE);
                }
            }
        });

        // Add components to the frame
        frame.add(new JLabel("Select a color:"));
        frame.add(colorDropDown);
        frame.add(colorPanel);

        // Make the frame visible
        frame.setVisible(true);
    }
}
