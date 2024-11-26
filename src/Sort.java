import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.sound.sampled.*;
import javax.swing.*;
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
    private Clip playSound(float pitch, final File soundFile) {
        if (pitch == 0)
            pitch = 1;
        try {
            if (!soundFile.exists()) {
                System.err.println("Sound file does not exist: " + soundFile.getAbsolutePath());
                return null;
            }

            final AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            // Adjust the pitch using FloatControl
            if (clip.isControlSupported(FloatControl.Type.SAMPLE_RATE)) {
                FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.SAMPLE_RATE);
                control.setValue(control.getValue() * pitch);
            } else {
                System.err.println("Sample rate control not supported for this audio file.");
            }
            clip.start();
            return clip;
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Unsupported audio file format: " + soundFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error reading the audio file: " + soundFile.getAbsolutePath());
        } catch (LineUnavailableException e) {
            System.err.println("Audio line unavailable: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
//    private Clip playSound(float pitch, final File soundFile) {
//        System.out.println(soundFile.isDirectory());
//        if (pitch == 0)
//            pitch = 1;
//        try {
//            final AudioInputStream audioInputStream = getAudioInputStream(soundFile);//AudioSystem.getAudioInputStream(getClass().getResource("/sounds/hitHurt.wav"));
//            Clip clip = AudioSystem.getClip();
//
//            AudioFormat baseFormat = audioInputStream.getFormat();
//
////            if (baseFormat.getSampleRate() == 0) {
////                System.err.println("Base format sample rate is 0. Check the audio file format.");
////                return null;
////            }
//
//            float sampleRate = baseFormat.getSampleRate() == 0 ? 44100 : baseFormat.getSampleRate(); // Use 44100 if sample rate is 0
//
//            AudioFormat newFormat = new AudioFormat(
//                    baseFormat.getEncoding(),
//                    sampleRate * pitch,
//                    baseFormat.getSampleSizeInBits(),
//                    baseFormat.getChannels(),
//                    baseFormat.getFrameSize(),
//                    sampleRate * pitch,
//                    baseFormat.isBigEndian()
//            );
//            final AudioInputStream newStream = AudioSystem.getAudioInputStream(newFormat, audioInputStream);
//            AudioSystem.write(newStream, AudioFileFormat.Type.WAVE, soundFile);
//            clip.open(newStream);
//            clip.start();
//            return clip;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

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
            Clip sound = playSound(0, soundFile);
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
                    sound = playSound(convertArrayHeightToPitchRange(arr[i]) * 100, soundFile);
                }
                drawSortStep(0, arr, taskNum, taskTotal);
            }
            System.out.println("Selection Sort Finished\n");
        }
    }
    public void playSoundOnAllRects(){
        for (int i = 0; i < size; i++){
            playSound((arr[i])*2000, soundFile);
            StdDraw.pause(20);
        }
    }
    //Takes the height of the rectangle and converts it to a pitch range, -1 is the lowest pitch 1 is the highest pitch
    //The array ranges from 0 to 1
    public float convertArrayHeightToPitchRange ( float height){
        return (height * 2) - 1;
    }

    public void bubbleSort ( float[] arr, int taskNum, int taskTotal){
        Clip sound = playSound(0, soundFile);
        int size = arr.length;

        for (int i = 0; i < size - 1; i++) {
            for (int k = 0; k < size - i - 1; k++) {
                if (arr[k] > arr[k + 1]) {
                    float temp = arr[k];
                    arr[k] = arr[k + 1];
                    arr[k + 1] = temp;
                    sound.stop();
                    sound = playSound(convertArrayHeightToPitchRange(arr[k]), soundFile);
                }
                drawSortStep(0, arr, taskNum, taskTotal);


            }
        }
        System.out.println("Bubble Sort Finished\n");
    }

    public void angelSort(float[] arr, int taskNum, int taskTotal) {
        Clip sound = playSound(0, soundFile);
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
                    sound = playSound(convertArrayHeightToPitchRange(arr[i]), soundFile);
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
        JFrame frame = new JFrame("Drop-Down Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setLayout(new FlowLayout());

        // Create a drop-down (JComboBox)
        String[] types = {"Angel","Bubble", "Selection"};
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

        StdDraw.pause(1000);
        sorter.playSoundOnAllRects();
        sorter.setRandomPenColor();
        //sorter.drawArrayRects(sorter.arr);
    }


}

