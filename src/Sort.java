import java.awt.*;
import java.util.Random;
import javax.sound.sampled.*;

public class Sort {
    int size = 1;
    float[] arr = new float[size];
    Random rand = new Random();
    boolean isDrawing = false;
    public void setSize(int s){
        if (s > 0) {
            size = s;
            arr = new float[size];
        } else {
            throw new IllegalArgumentException("Size must be positive");
        }
    }

    public void printArray(){
        // Print the array
        for (float num : arr) {
            System.out.print(num + " ");
        }
    }

    private void initializeArray(){
        // Fill the array with random integers
        for (int i = 0; i < arr.length; i++) {
            arr[i] = rand.nextFloat(); //Initializes between 0 and 1
            if (arr[i] <= .01){
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
    private void setRandomPenColor(){
        int red = (int) (Math.random() * 256);
        int green = (int) (Math.random() * 256);
        int blue = (int) (Math.random() * 256);

        // Set pen color to random color
        StdDraw.setPenColor(new Color(red, green, blue));
    }
    /*
    * We should redraw only the rectangles that need to be redrawn to increase speed
    * */
    private void drawArrayRects(){
        float width = 1f / size; //half of the screen width divided by the size
        float offsetter = (1f / size + width)/2;
        float offset = offsetter/2;
        isDrawing = true;
        for (int i = 0; i< arr.length; i++){ //Half width is weird still gotta figure out what number to put in
            StdDraw.filledRectangle(offset,0,width/2 -.000005,arr[i]);
            offset += offsetter;

        }
        isDrawing = false;
    }
    public void drawBackground(Color color){
        StdDraw.setPenColor(color);
        StdDraw.filledSquare(.5,.5,1f);
    }
    public boolean getIsDrawing(){
        return isDrawing;
    }
    /* Optimization thots
    *  How to make it wait for drawing to finish before drawing again??
    *  WE can draw background color rect over old pos then draw new one on top instead of redrawing
    *  We can create some sort of object system that allows us to have rectangle objects and move functions
    *  ^^ this would be good
    */

    //Use to redraw the array in the sorting functions (Requires double Buffering)
    public void drawSortStep(int pauseTime){
        StdDraw.clear();
        drawArrayRects();
        StdDraw.show();
        StdDraw.pause(pauseTime);
    }
    public void selectionSort() {
        Clip sound = playSound(0);

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
            drawSortStep(0);
        }
    }
    //Takes the height of the rectangle and converts it to a pitch range, -1 is the lowest pitch 1 is the highest pitch
    //The array ranges from 0 to 1
    public float convertArrayHeightToPitchRange(float height){
        return (height * 2) - 1;
    }
    public void bubbleSort(){
        Clip sound = playSound(0);

        for (int i = 0; i < size - 1; i++){
            for (int k = 0; k < size - i - 1; k++){
                if(arr[k] > arr[k+1]){
                    float temp = arr[k];
                    arr[k] = arr[k+1];
                    arr[k+1] = temp;
                    sound.stop();
                    sound = playSound(convertArrayHeightToPitchRange(arr[k]));
                }
                drawSortStep(0);


            }
        }
    }
    public static void main(String[] args) {
        StdDraw.setCanvasSize(800,800);
        StdDraw.setScale(0,1);

        StdDraw.enableDoubleBuffering();

        Sort sorter = new Sort();
        sorter.drawBackground(Color.BLACK);
        sorter.setSize(500);
        sorter.initializeArray();
        sorter.printArray();
        sorter.drawArrayRects();

        //4 fun
        sorter.setRandomPenColor();

        //sorter.bubbleSort();
        sorter.selectionSort();

        sorter.setRandomPenColor();
        sorter.drawArrayRects();
    }
}
