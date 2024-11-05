import java.awt.*;
import java.util.Random;

public class Sort {
    int size = 10;
    float[] arr = new float[size];
    Random rand = new Random();
    boolean isDrawing = false;

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
        }
    }
    private void setRandomPenColor(){
        int red = (int) (Math.random() * 256);
        int green = (int) (Math.random() * 256);
        int blue = (int) (Math.random() * 256);

        // Set pen color to random color
        StdDraw.setPenColor(new Color(red, green, blue));
    }
    private void drawArrayRects(){
        float width = 1f / size; //half of the screen width divided by the size
        float offsetter = (1f / size + width)/2;
        float offset = offsetter/2;
        isDrawing = true;
        for (int i = 0; i< arr.length; i++){
            //setRandomPenColor();
            StdDraw.filledRectangle(offset,0,width/2,arr[i]);
            //System.out.println(offset);
            offset += offsetter;

        }
        isDrawing = false;
    }
    public boolean getIsDrawing(){
        return isDrawing;
    }
    public void bubbleStep(){
        for (int i = 0; i < size - 1; i++){
            for (int k = 0; k < size - i - 1; k++){
                if(arr[k] > arr[k+1]){
                    float temp = arr[k];
                    arr[k] = arr[k+1];
                    arr[k+1] = temp;
                    StdDraw.clear();
                    drawArrayRects();
                }
            }
        }
    }
    public static void main(String[] args) {
        System.out.println("Hello World");
        StdDraw.setCanvasSize(800,800);
        StdDraw.setScale(0,1);
        //StdDraw.filledSquare(.5,.5,.1f);

        Sort sorter = new Sort();
        sorter.initializeArray();
        sorter.printArray();
        sorter.drawArrayRects();

//        for(int i = 0; i < 100; i++){
            sorter.bubbleStep();
//            StdDraw.clear();
//
//            sorter.drawArrayRects();
//
//        }
    }
}
