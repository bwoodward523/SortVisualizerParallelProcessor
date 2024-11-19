import java.awt.*;
import java.util.Random;
import javax.sound.sampled.*;
import javax.swing.*;
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
        ANGEL
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

    /*
     * We should redraw only the rectangles that need to be redrawn to increase speed
     * */
    private void drawArrayRects(float[] arr, int taskNum,
                                int taskTotal) {
        int quadrant = taskNum % taskTotal;
        int rows = (int) Math.sqrt(taskTotal);
        int cols = (int) Math.ceil((double) taskTotal / rows);
        int row = quadrant / cols;
        int col = quadrant % cols;

        float xOffset = (float) col / cols;
        float yOffset = (float) row / rows;
        float width = 1f / cols;
        float height = 1f / rows;

        int size = arr.length;
        float rectWidth = width / size;
        float offset = xOffset + rectWidth / 2;

        for (int i = 0; i < size; i++) {
            StdDraw.filledRectangle(offset, yOffset + arr[i] * height / 2, rectWidth / 2, arr[i] * height / 2);
            offset += rectWidth;
        }
    }

    private void clearQuadrant(int taskNum, int taskTotal) {
        int quadrant = taskNum % taskTotal;
        int rows = (int) Math.sqrt(taskTotal);
        int cols = (int) Math.ceil((double) taskTotal / rows);
        int row = quadrant / cols;
        int col = quadrant % cols;

        float xOffset = (float) col / cols;
        float yOffset = (float) row / rows;
        float width = 1f / cols;
        float height = 1f / rows;

        //int size = arr.length;
        //float rectWidth = width / size;
        //float offset = xOffset + rectWidth / 2;

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledRectangle(xOffset + width / 2, yOffset + height / 2, width / 2, height / 2);
        StdDraw.setPenColor(StdDraw.WHITE);
    }

    public void drawBackground(Color color) {
        StdDraw.setPenColor(color);
        StdDraw.filledSquare(.5, .5, 1f);
    }

    public boolean getIsDrawing() {
        return isDrawing;
    }
    /* Optimization thots
     *  How to make it wait for drawing to finish before drawing again??
     *  WE can draw background color rect over old pos then draw new one on top instead of redrawing
     *  We can create some sort of object system that allows us to have rectangle objects and move functions
     *  ^^ this would be good
     */

    //Use to redraw the array in the sorting functions (Requires double Buffering)
    public void drawSortStep(int pauseTime, float[] arr,
                             int taskNum, int taskTotal) {
        clearQuadrant(taskNum, taskTotal);
        drawArrayRects(arr, taskNum, taskTotal);
        StdDraw.show();
        StdDraw.pause(pauseTime);
    }

    public void selectionSort(float[] arr, int taskNum, int taskTotal) {
        {
            Clip sound = playSound(0);
            int size = arr.length;

            for (int i = 0; i < size - 1; i++) {
                int minIndex = i;
                for (int j = i + 1; j < size; j++) {
                    if (arr[j] < arr[minIndex]) {
                        minIndex = j;
                    }
                }
                if (minIndex != i) {
                    float temp = arr[i];
                    arr[i] = arr[minIndex];
                    arr[minIndex] = temp;
                    sound.stop();
                    sound = playSound(convertArrayHeightToPitchRange(arr[i]) * 100);
                }
                drawSortStep(0, arr, taskNum, taskTotal);
            }
            System.out.println("Selection Sort Finished\n");
        }
    }
    //Takes the height of the rectangle and converts it to a pitch range, -1 is the lowest pitch 1 is the highest pitch
    //The array ranges from 0 to 1
    public float convertArrayHeightToPitchRange ( float height){
        return (height * 2) - 1;
    }

    public void bubbleSort ( float[] arr, int taskNum, int taskTotal){
        Clip sound = playSound(0);
        int size = arr.length;

        for (int i = 0; i < size - 1; i++) {
            for (int k = 0; k < size - i - 1; k++) {
                if (arr[k] > arr[k + 1]) {
                    float temp = arr[k];
                    arr[k] = arr[k + 1];
                    arr[k + 1] = temp;
                    sound.stop();
                    sound = playSound(convertArrayHeightToPitchRange(arr[k]));
                }
                drawSortStep(0, arr, taskNum, taskTotal);


            }
        }
        System.out.println("Bubble Sort Finished\n");
    }

    public void angelSort(float[] arr, int taskNum, int taskTotal) {
        Clip sound = playSound(0);
        int size = arr.length;

        while(!isSorted(arr))
        {
            for(int i = 0; i < size; i++)
            {
                if(rand.nextInt(2) == 0 && i < size - 1)
                {
                    float temp = arr[i];
                    arr[i] = arr[i+1];
                    arr[i+1] = temp;
                    sound.stop();
                    sound = playSound(convertArrayHeightToPitchRange(arr[i]));
                }
            }
            drawSortStep(0, arr, taskNum, taskTotal);
        }
        System.out.println("Angel Sort Finished\n");
    }

    public static boolean isSorted(float[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            if (a[i] > a[i + 1]) {
                return false;
            }
        }
        return true;
    }

    static int factorial(int n)
    {
        int res = 1, i;
        for (i = 2; i <= n; i++)
            res *= i;
        return res;
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
        JFrame frame = new JFrame("Sorting Algorithm Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setLayout(new GridBagLayout());
        frame.getContentPane().setBackground(Color.DARK_GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create a drop-down (JComboBox)
        String[] types = {"Angel", "Bubble", "Selection"};
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

        gbc.gridx = 1;
        gbc.gridy = 3;
        frame.add(ejectButton, gbc);

        // Make the frame visible
        frame.setVisible(true);

        while (breakVar.get() != 1) {
            // Add an ActionListener to handle selection changes
            typeDropDown.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String selectedType = (String) typeDropDown.getSelectedItem();

                    // Change the panel's background color based on selection
                    switch (Objects.requireNonNull(selectedType)) {
                        case "Angel" -> type[0] = Sort.SortType.ANGEL;
                        case "Bubble" -> type[0] = Sort.SortType.BUBBLE;
                        case "Selection" -> type[0] = Sort.SortType.SELECTION;
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
        //processor.setupTasks(total[0], sorter.arr, type[0]);
        //processor.startThreads(sorter.arr, type[0]);

        //sorter.drawArrayRects(sorter.arr, 0, 1);
        printExpectedTime(type[0], size[0]);
        processor.setupTasks(total[0], sorter.arr, type[0]);

        sorter.setRandomPenColor();
        //sorter.drawArrayRects(sorter.arr);
    }


}

