import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.ImageIcon;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static javax.sound.sampled.AudioSystem.getAudioInputStream;

public class Sort {
    int size = 1;
    float[] arr = new float[size];
    Random rand = new Random();
    boolean isDrawing = false;
    File soundFile = new File("src/sounds/hitHurt.wav");

    // Enumerators for sorting algorithms
    public enum SortType {
        BUBBLE,
        SELECTION,
        ANGEL,
        QUICK,
        CYCLE,
        MERGE
    }

    public void setSize(int s) {
        if (s > 0) {
            size = s;
            arr = new float[size];
        } else {
            throw new IllegalArgumentException("Size must be positive");
        }
    }

    public void printArray() {
        // Print the array
        for (float num : arr) {
            System.out.print(num + " ");
        }
    }

    private void initializeArray() {
        // Fill the array with random integers
        for (int i = 0; i < arr.length; i++) {
            arr[i] = rand.nextFloat(); //Initializes between 0 and 1
            if (arr[i] <= .01) {
                arr[i] = .011f;
            }
        }
    }


    private void setRandomPenColor() {
        int red = (int) (Math.random() * 256);
        int green = (int) (Math.random() * 256);
        int blue = (int) (Math.random() * 256);

        // Set pen color to random color
        StdDraw.setPenColor(new Color(red, green, blue));
    }



    public void drawBackground(Color color) {
        StdDraw.setPenColor(color);
        StdDraw.filledSquare(.5, .5, 1f);
    }

    public static int factorial(int n) {
        if (n == 0) {
            return 1;
        }
        return n * factorial(n - 1);
    }

    public static void printExpectedTime(SortType type, int size)
    {
        switch (type) {
            case BUBBLE, SELECTION:
                double result = ((double) (size ^ 2) / (100^2))*0.01;
                System.out.println("Expected time complexity: "+result);
                break;
            case ANGEL:
                double result2 = (size*(factorial(size)))*0.01;
                System.out.println("Expected time complexity: "+result2);
                break;
        }
    }

    public static double calculateExpectedTime(SortType type, int size)
    {
        //BUBBLE,SELECTION,ANGEL,QUICK,CYCLE,MERGE
        double result = 0;
        switch (type) {
            case BUBBLE, SELECTION -> {
                result = ((double) (size ^ 2) / (100 ^ 2))*3;
            }
            case ANGEL,QUICK,MERGE -> {
                result = ((double)size * (factorial(size))) * 0.01;
            }
            case CYCLE -> {
                result = ((double)size) * 0.01;
            }
        }
        return result;
    }

    public static void main (String[]args){
        final int[] total = {1};
        final SortType[] type = {SortType.ANGEL};
        final int[] size = {0};
        AtomicInteger breakVar = new AtomicInteger();

        // Create the main frame
        JFrame frame = new JFrame("Sorting Algorithm Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setLayout(new GridBagLayout());
        frame.getContentPane().setBackground(Color.DARK_GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Set custom icon
        ImageIcon icon = new ImageIcon("src/icon.png");
        frame.setIconImage(icon.getImage());

        // Create a drop-down (JComboBox)
        String[] types = {"Angel", "Bubble", "Selection", "Quick", "Cycle", "Merge"};
        JComboBox<String> typeDropDown = new JComboBox<>(types);
        typeDropDown.setToolTipText("Select a Sorting Algorithm");

        JLabel typeLabel = new JLabel("Select a Sorting Algorithm: ");
        typeLabel.setForeground(Color.WHITE);

        // Create labels and text fields
        JLabel nodeLabel = new JLabel("Enter how many nodes you want:");
        nodeLabel.setForeground(Color.WHITE);
        JTextField nodeBox = new JTextField(20);

        JLabel coreLabel = new JLabel("Enter how many cores you want to use:");
        coreLabel.setForeground(Color.WHITE);
        JTextField coreBox = new JTextField(20);

        JLabel timeLabel = new JLabel("Estimated time: ");
        timeLabel.setForeground(Color.WHITE);
        JTextField estimatedTimeBox = new JTextField(20);
        estimatedTimeBox.setEditable(false);

        JButton ejectButton = new JButton("Run");

        // Add components to the frame
        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(typeLabel, gbc);

        gbc.gridx = 1;
        frame.add(typeDropDown, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(nodeLabel, gbc);

        gbc.gridx = 1;
        frame.add(nodeBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(coreLabel, gbc);

        gbc.gridx = 1;
        frame.add(coreBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        frame.add(timeLabel, gbc);

        gbc.gridx = 1;
        frame.add(estimatedTimeBox, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        frame.add(ejectButton, gbc);

        // Make the frame visible
        frame.setVisible(true);

        // Add listeners to update the estimatedTimeBox
        typeDropDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateEstimatedTime(typeDropDown, nodeBox, estimatedTimeBox, type);
            }
        });

        DocumentListener documentListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateEstimatedTime(typeDropDown, nodeBox, estimatedTimeBox, type);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateEstimatedTime(typeDropDown, nodeBox, estimatedTimeBox, type);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateEstimatedTime(typeDropDown, nodeBox, estimatedTimeBox, type);
            }
        };

        nodeBox.getDocument().addDocumentListener(documentListener);
        coreBox.getDocument().addDocumentListener(documentListener);

        ejectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                breakVar.set(1);
                size[0] = Integer.parseInt(nodeBox.getText()); // Get text from the text box
                total[0] = Integer.parseInt(coreBox.getText());
            }
        });

        while (breakVar.get() != 1) {
            // Keep the frame running
        }
        frame.dispose();

        StdDraw.setCanvasSize(800, 800);
        StdDraw.setScale(0, 1);

        StdDraw.enableDoubleBuffering();

        Sort sorter = new Sort();
        ParallelProcessor processor = new ParallelProcessor();
        sorter.drawBackground(Color.BLACK);


        sorter.setSize(size[0]);
        sorter.initializeArray();
        sorter.printArray();
        //sorter.drawArrayRects(sorter.arr, );

        //4 fun
        sorter.setRandomPenColor();

        // Setup Parrallel Processing
        //processor.setupTasks(total[0], sorter.arr, type[0]);
        //processor.startThreads(sorter.arr, type[0]);

        //sorter.drawArrayRects(sorter.arr, 0, 1);
        printExpectedTime(type[0], size[0]);
        processor.setupTasks(total[0], sorter.arr, type[0]);

        StdDraw.pause(1000);
        //sorter.playSoundOnAllRects();
        sorter.setRandomPenColor();
        //sorter.drawArrayRects(sorter.arr);
    }

    private static void updateEstimatedTime(JComboBox<String> typeDropDown, JTextField nodeBox, JTextField estimatedTimeBox, SortType[] type) {
        String selectedType = (String) typeDropDown.getSelectedItem();
        if (selectedType != null && !nodeBox.getText().isEmpty()) {
            int nodeCount = Integer.parseInt(nodeBox.getText());
            switch (Objects.requireNonNull(selectedType)) {
                case "Angel" -> type[0] = SortType.ANGEL;
                case "Bubble" -> type[0] = SortType.BUBBLE;
                case "Selection" -> type[0] = SortType.SELECTION;
                case "Quick" -> type[0] = SortType.QUICK;
                case "Cycle" -> type[0] = SortType.CYCLE;
                case "Merge" -> type[0] = SortType.MERGE;
            }
            double estimatedTime = calculateExpectedTime(type[0], nodeCount);
            estimatedTimeBox.setText(estimatedTime + " seconds");
        }
    }


}

