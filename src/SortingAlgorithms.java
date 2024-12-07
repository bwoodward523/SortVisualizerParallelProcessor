import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.File;
public class SortingAlgorithms {
    private Random rand = new Random();
    private static List<Clip> currentlyPlayingSounds = new ArrayList<>();

    // Sound Methods
    static File inputFile = new File("src/sounds/longsound.wav");
    static File[] soundFiles = new File[100];
    void InitializeSoundFiles() {
        File folder = new File("src/sounds/generated_wavs");
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".wav"));

        if (files != null && files.length >= 100) {
            for (int i = 0; i < 100; i++) {
                soundFiles[i] = files[i];
            }
        } else {
            throw new RuntimeException("Not enough .wav files in the directory");
        }
    }
    public void endOfSort(float[] arr) {
        for (int i = 0; i < arr.length; i++) {
            float fractionalPosition = ((float) i / (float)arr.length) * 100.0f;            StdDraw.setPenColor(StdDraw.RED);
           // System.out.println("Playing sound at position: " + fractionalPosition + " i is" + i);
           // System.out.println("" + arr.length);

            try {
                Thread.sleep(1/arr.length * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                playSound(soundFiles[(int)fractionalPosition]);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        float pauseTime = 1.0f/ (float)arr.length * 1000.0f;
        drawSortStep( 2, arr,1,1, Color.BLUE);
        System.out.println("what are you doing dummie");

    }
    private static void playSound(File file) throws Exception {
        Clip clip = AudioSystem.getClip();
        clip.open(AudioSystem.getAudioInputStream(file));
        clip.start();
        for(Clip c : currentlyPlayingSounds) {
            if(c.getMicrosecondPosition() > 200){
                c.stop();
            }
        }
        currentlyPlayingSounds.add(clip);
        //Thread.sleep(clip.getMicrosecondLength() / 1000);
    }



    // Sorting Algorithms
    public void selectionSort(float[] arr, int taskNum, int taskTotal) {
        //Clip sound = playSound(0);
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
                int fractionalPosition = (i / size) * 100;
                try {
                    playSound(soundFiles[fractionalPosition]);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
            drawSortStep(0, arr, taskNum, taskTotal);
        }
        System.out.println("Selection Sort Finished\n");
        endOfSort(arr);
    }

    public void bubbleSort(float[] arr, int taskNum, int taskTotal) {
        //Clip sound = playSound(0);
        int size = arr.length;

        for (int i = 0; i < size - 1; i++) {
            for (int k = 0; k < size - i - 1; k++) {
                if (arr[k] > arr[k + 1]) {
                    float temp = arr[k];
                    arr[k] = arr[k + 1];
                    arr[k + 1] = temp;
                    // sound.stop();
                    //sound = playSound(convertArrayHeightToPitchRange(arr[k]));
                    int fractionalPosition = (k / size) * 100;
                    try {
                        playSound(soundFiles[fractionalPosition]);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                drawSortStep(0, arr, taskNum, taskTotal);
            }
        }
        System.out.println("Bubble Sort Finished\n");
        //endOfSort(arr);

    }

    public void angelSort(float[] arr, int taskNum, int taskTotal) {
        //Clip sound = playSound(0);
        int size = arr.length;

        while (!isSorted(arr)) {
            for (int i = 0; i < size; i++) {
                if (rand.nextInt(2) == 0 && i < size - 1) {
                    float temp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = temp;
                    // sound.stop();
                    //sound = playSound(convertArrayHeightToPitchRange(arr[k]));
                    int fractionalPosition = (i / size) * 100;
                    try {
                        playSound(soundFiles[fractionalPosition]);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            drawSortStep(0, arr, taskNum, taskTotal);
        }
        System.out.println("Angel Sort Finished\n");
     //   endOfSort(arr);

    }

    public void quickSort(float arr[], int begin, int end, int taskNum, int taskTotal) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end, taskNum, taskTotal);

            quickSort(arr, begin, partitionIndex - 1, taskNum, taskTotal);
            quickSort(arr, partitionIndex + 1, end, taskNum, taskTotal);
        }
    }

    public void cycleSort(float[] arr, int taskNum, int taskTotal)
    {
        //Clip sound = playSound(0);
        int size = arr.length;
        for(int cycle_start = 0; cycle_start <= size-2; cycle_start++)
        {
            float item = arr[cycle_start];
            int pos = cycle_start;
            for(int i = cycle_start+1; i<size; i++)
            {
                if(arr[i] < item)
                {
                    pos++;
                }
            }
            if(pos == cycle_start)
            {
                continue;
            }
            while(item == arr[pos])
            {
                pos += 1;
            }
            if(pos != cycle_start)
            {
                float temp = item;
                item = arr[pos];
                arr[pos] = temp;
                // sound.stop();
                //sound = playSound(convertArrayHeightToPitchRange(arr[k]));
                int fractionalPosition = (pos / size) * 100;
                try {
                    playSound(soundFiles[fractionalPosition]);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            while(pos != cycle_start)
            {
                pos = cycle_start;
                for(int i = cycle_start+1; i<size; i++)
                {
                    if(arr[i] < item)
                    {
                        pos += 1;
                    }
                }
                while(item == arr[pos])
                {
                    pos += 1;
                }
                if(item != arr[pos])
                {
                    float temp = item;
                    item = arr[pos];
                    arr[pos] = temp;
                    // sound.stop();
                    //sound = playSound(convertArrayHeightToPitchRange(arr[k]));
                    int fractionalPosition = (pos / size) * 100;
                    try {
                        playSound(soundFiles[fractionalPosition]);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            drawSortStep(0, arr, taskNum, taskTotal);
        }
      //  endOfSort(arr);

    }
    public void mergeSort(float[] arr, int taskNum, int taskTotal) {
        //Clip sound = playSound(0);
        int size = arr.length;
        if (size > 1) {
            int mid = size / 2;
            float[] left = new float[mid];
            float[] right = new float[size - mid];

            System.arraycopy(arr, 0, left, 0, mid);
            System.arraycopy(arr, mid, right, 0, size - mid);

            mergeSort(left, taskNum, taskTotal);
            mergeSort(right, taskNum, taskTotal);

            int i = 0;
            int j = 0;
            int k = 0;

            while (i < left.length && j < right.length) {
                if (left[i] < right[j]) {
                    arr[k] = left[i];
                    i++;
                } else {
                    arr[k] = right[j];
                    j++;
                }
                k++;
                // sound.stop();
                //sound = playSound(convertArrayHeightToPitchRange(arr[k]));
                int fractionalPosition = (k / size) * 100;
                try {
                    playSound(soundFiles[fractionalPosition]);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                drawSortStep(0, arr, taskNum, taskTotal);
            }

            while (i < left.length) {
                arr[k] = left[i];
                i++;
                k++;
                // sound.stop();
                //sound = playSound(convertArrayHeightToPitchRange(arr[k]));
                int fractionalPosition = (k / size) * 100;
                try {
                    playSound(soundFiles[fractionalPosition]);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                drawSortStep(0, arr, taskNum, taskTotal);
            }

            while (j < right.length) {
                arr[k] = right[j];
                j++;
                k++;
                // sound.stop();
                //sound = playSound(convertArrayHeightToPitchRange(arr[k]));
                int fractionalPosition = (k / size) * 100;
                try {
                    playSound(soundFiles[fractionalPosition]);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                drawSortStep(0, arr, taskNum, taskTotal);
            }
        }
    //    endOfSort(arr);
    }



    // Utility Methods for Sorting Algorithms

    private static float getMax(float[] arr)
    {
        float max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max){
                max = arr[i];
            }
        }
        return max;
    }
    private int partition(float arr[], int begin, int end, int taskNum, int taskTotal) {
       // Clip sound = playSound(0);
        float pivot = arr[end];
        int i = (begin - 1);

        for (int j = begin; j < end; j++) {
            if (arr[j] <= pivot) {
                i++;

                float swapTemp = arr[i];
                arr[i] = arr[j];
                arr[j] = swapTemp;
                // sound.stop();
                //sound = playSound(convertArrayHeightToPitchRange(arr[i]));
                int fractionalPosition = (i / end) * 100;
                try {
                    playSound(soundFiles[fractionalPosition]);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            drawSortStep(0, arr, taskNum, taskTotal);
        }

        float swapTemp = arr[i + 1];
        arr[i + 1] = arr[end];
        arr[end] = swapTemp;

        return i + 1;
    }

    public static boolean isSorted(float[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            if (a[i] > a[i + 1]) {
                return false;
            }
        }
        return true;
    }



    // Redraw Methods for Sorting Algorithms
    private void clearQuadrant(int taskNum, int taskTotal, Color color) {
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
        StdDraw.setPenColor(color);
    }
    private void drawArrayRects(float[] arr, int taskNum,
                                int taskTotal, int pauseTime) {
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
            StdDraw.pause(pauseTime);
        }
    }
    public void drawBackground(Color color) {
        StdDraw.setPenColor(color);
        StdDraw.filledSquare(.5, .5, 1f);
    }


    //Use to redraw the array in the sorting functions (Requires double Buffering)
    public void drawSortStep(int pauseTime, float[] arr,
                             int taskNum, int taskTotal) {
        clearQuadrant(taskNum, taskTotal, Color.WHITE);
        drawArrayRects(arr, taskNum, taskTotal, pauseTime);
        StdDraw.show();
        StdDraw.pause(pauseTime);
    }
    public void drawSortStep(int pauseTime, float[] arr, int taskNum, int taskTotal, Color color) {
        clearQuadrant(taskNum, taskTotal, color);
        drawArrayRects(arr, taskNum, taskTotal, pauseTime);
        StdDraw.show();
        //StdDraw.pause(pauseTime);
    }
}