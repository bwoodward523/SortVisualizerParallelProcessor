import java.awt.*;
import java.util.Random;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.ImageIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Sort {
    int size = 1;
    float[] arr = new float[size];
    Random rand = new Random();
    boolean isDrawing = false;

    // Enumerators for sorting algorithms
    public enum SortType {
        BUBBLE,
        SELECTION,
        ANGEL,
        QUICK,
        RADIX
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

    private Clip playSound(float pitch) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("laserShoot.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            AudioFormat baseFormat = audioInputStream.getFormat();
            AudioFormat newFormat = new AudioFormat(
                    baseFormat.getEncoding(),
                    baseFormat.getSampleRate() * pitch,
                    baseFormat.getSampleSizeInBits(),
                    baseFormat.getChannels(),
                    baseFormat.getFrameSize(),
                    baseFormat.getFrameRate() * pitch,
                    baseFormat.isBigEndian()
            );
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    public static void main (String[]args){
        final int[] total = {1};
        final SortType[] type = {SortType.ANGEL};
        final int[] size = {0};
        AtomicInteger breakVar = new AtomicInteger();

        // Create the main frame
        JFrame frame = new JFrame("Sort Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setLayout(new FlowLayout());

        // Set custom icon
        ImageIcon icon = new ImageIcon("src/icon.png");
        frame.setIconImage(icon.getImage());

        // Create a drop-down (JComboBox)
        String[] types = {"Angel","Bubble", "Selection", "Quick"};
        JComboBox<String> typeDropDown = new JComboBox<>(types);
        JButton ejectButton = new JButton("Run");
        frame.add(new JLabel("Select a Sorting Algorithm:"));

        JLabel nodeLabel = new JLabel("Enter how many nodes you want:");
        // Create a text box (JTextField)
        JTextField nodeBox = new JTextField(20); // 20 columns wide

        JLabel coreLabel = new JLabel("Enter how many cores do you want to use:");
        JTextField coreBox = new JTextField(20); // 20 columns wide

        frame.add(typeDropDown);
        frame.add(ejectButton);
        frame.add(nodeLabel);
        frame.add(nodeBox);
        frame.add(coreLabel);
        frame.add(coreBox);




        // Make the frame visible
        frame.setVisible(true);


        while(breakVar.get() != 1){
            // Add an ActionListener to handle selection changes
            typeDropDown.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String selectedType = (String) typeDropDown.getSelectedItem();

                    // Change the panel's background color based on selection
                    switch (Objects.requireNonNull(selectedType)) {
                        case "Angel" -> type[0] = SortType.ANGEL;
                        case "Bubble" -> type[0] = SortType.BUBBLE;
                        case "Selection" -> type[0] = SortType.SELECTION;
                        case "Quick" -> type[0] = SortType.QUICK;
                    }
                }
            });
            ejectButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    breakVar.set(1);
                    size[0] = Integer.parseInt(nodeBox.getText()); // Get text from the text box
                    total[0] = Integer.parseInt(coreBox.getText());
                }
            });
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
        printExpectedTime(type[0], size[0]);
        processor.setupTasks(total[0], sorter.arr, type[0]);    

        sorter.setRandomPenColor();
        //sorter.drawArrayRects(sorter.arr);
    }


}

