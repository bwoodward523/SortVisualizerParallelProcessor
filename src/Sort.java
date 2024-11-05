import java.awt.*;
import java.util.Random;

public class Sort {
    int size = 1;
    float[] arr = new float[size];
    Random rand = new Random();
    boolean isDrawing = false;
    public void setSize(int s){
        size = s;
        arr = new float[size];
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
        for (int i = 0; i< arr.length; i++){
            StdDraw.filledRectangle(offset,0,width/2 -.005,arr[i]);
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
    //How to make it wait for drawing to finish before drawing again??
    public void bubbleStep(){
        for (int i = 0; i < size - 1; i++){
            for (int k = 0; k < size - i - 1; k++){
                if(arr[k] > arr[k+1]){
                    float temp = arr[k];
                    arr[k] = arr[k+1];
                    arr[k+1] = temp;
                }
                StdDraw.clear();
                drawArrayRects();
            }
        }
    }
    public static void main(String[] args) {
        System.out.println("Hello World");
        StdDraw.setCanvasSize(800,800);
        StdDraw.setScale(0,1);


        Sort sorter = new Sort();
        sorter.drawBackground(Color.BLACK);
        sorter.setSize(10);
        sorter.initializeArray();
        sorter.printArray();
        sorter.drawArrayRects();

        sorter.bubbleStep();
        sorter.setRandomPenColor();
        sorter.drawArrayRects();
    }
}
