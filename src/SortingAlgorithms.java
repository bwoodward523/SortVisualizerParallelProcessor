import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.io.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.PitchShifter;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.writer.WriterProcessor;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.RandomAccessFile;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
public class SortingAlgorithms {
    private Random rand = new Random();

    // Sound Methods
    //static File inputFile = new File("src/sounds/longsound.wav");
    static File[] soundFiles = new File[100];
    /**********************************************************
     * METHOD: InitializeSoundFiles() *
     * DESCRIPTION: initializes and array full of all the necessary sound files for execution *
     * PARAMETERS: none *
     * RETURN VALUE: none *
     **********************************************************/
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
    /**********************************************************
     * METHOD: playSoudn() *
     * DESCRIPTION: plays a sound file *
     * PARAMETERS: file path *
     * RETURN VALUE: none *
     **********************************************************/
    private static void playSound(File file) throws Exception {
        Clip clip = AudioSystem.getClip();
        clip.open(AudioSystem.getAudioInputStream(file));
        clip.start();
        //Thread.sleep(clip.getMicrosecondLength() / 1000);
    }




    // Sorting Algorithms

    /**********************************************************
     * METHOD: selectionSort() *
     * DESCRIPTION: our implementation of the selection sort
     * description can be found here: https://www.geeksforgeeks.org/selection-sort-algorithm-2/*
     * PARAMETERS: array to be sorted, number of task subdivision, total tasks *
     * RETURN VALUE: none *
     **********************************************************/
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
                //sound.stop();
                //sound = playSound(convertArrayHeightToPitchRange(arr[i]) * 100);
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
    }

    /**********************************************************
     * METHOD: bubbleSort() *
     * DESCRIPTION: our implementation of the bubble sort
     * description can be found here: https://www.geeksforgeeks.org/bubble-sort-algorithm/*
     * PARAMETERS: array to be sorted, number of task subdivision, total tasks *
     * RETURN VALUE: none *
     **********************************************************/
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
    }

    /**********************************************************
     * METHOD: angelSort() *
     * DESCRIPTION: a sorting algorithm that our lovely minds came up with
     * swaps elements at random until the array is sorted
     * PARAMETERS: array to be sorted, number of task subdivision, total tasks *
     * RETURN VALUE: none *
     **********************************************************/
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
    }

    /**********************************************************
     * METHOD: quickSort() *
     * DESCRIPTION: our implementation of the quick sort
     * description can be found here: https://www.geeksforgeeks.org/quick-sort-algorithm/*
     * PARAMETERS: array to be sorted, number of task subdivision, total tasks, beginning and end of necessary sorting indicies *
     * RETURN VALUE: none *
     **********************************************************/
    public void quickSort(float arr[], int begin, int end, int taskNum, int taskTotal) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end, taskNum, taskTotal);

            quickSort(arr, begin, partitionIndex - 1, taskNum, taskTotal);
            quickSort(arr, partitionIndex + 1, end, taskNum, taskTotal);
        }
    }

    /**********************************************************
     * METHOD: cycleSort() *
     * DESCRIPTION: our implementation of the cycle sort
     * description can be found here: https://www.geeksforgeeks.org/cycle-sort/*
     * PARAMETERS: array to be sorted, number of task subdivision, total tasks *
     * RETURN VALUE: none *
     **********************************************************/
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
    }

    /**********************************************************
     * METHOD: mergeSort() *
     * DESCRIPTION: our implementation of the merge sort
     * description can be found here: https://www.geeksforgeeks.org/merge-sort/*
     * PARAMETERS: array to be sorted, number of task subdivision, total tasks *
     * RETURN VALUE: none *
     **********************************************************/
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
    }



    // Utility Methods for Sorting Algorithms

    /**********************************************************
     * METHOD: parition() *
     * DESCRIPTION: utility method necessary for quick sort
     * PARAMETERS: array to be sorted, number of task subdivision, total tasks, beginning and end of sort index *
     * RETURN VALUE: none *
     **********************************************************/
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

    /**********************************************************
     * METHOD: isSorted() *
     * DESCRIPTION: checks if an array is sorted
     * PARAMETERS: array *
     * RETURN VALUE: boolean of if the method is sorted *
     **********************************************************/
    public static boolean isSorted(float[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            if (a[i] > a[i + 1]) {
                return false;
            }
        }
        return true;
    }



    // Redraw Methods for Sorting Algorithms
    /**********************************************************
     * METHOD: clearQuadrant() *
     * DESCRIPTION: clears the quadrant of the array that is being sorted
     * PARAMETERS: number of task subdivision, total tasks *
     * RETURN VALUE: none *
     **********************************************************/
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
    /**********************************************************
     * METHOD: drawArrayRects() *
     * DESCRIPTION: draws the array rectangles
     * PARAMETERS: array of numbers, number of task subdivision, total tasks, how much pause between each draw *
     * RETURN VALUE: none *
     **********************************************************/
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
            if(pauseTime > 0 ){
                StdDraw.show();
                StdDraw.pause(pauseTime);
                float fractionalPosition = ((float) i / (float)arr.length) * 100.0f;
                try {
                    playSound(soundFiles[(int)fractionalPosition]);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            offset += rectWidth;
        }
    }


    //Use to redraw the array in the sorting functions (Requires double Buffering)
    /**********************************************************
     * METHOD: drawSortStep() *
     * DESCRIPTION: draws an individual step of the sort
     * PARAMETERS: number of task subdivision, total tasks, pause time, array *
     * RETURN VALUE: none *
     **********************************************************/
    public void drawSortStep(int pauseTime, float[] arr,
                             int taskNum, int taskTotal) {
        clearQuadrant(taskNum, taskTotal);
        if(pauseTime > 0){
            StdDraw.setPenColor(Color.GREEN);
        }
        drawArrayRects(arr, taskNum, taskTotal, pauseTime);
        StdDraw.show();
        StdDraw.pause(pauseTime);
    }
    /**********************************************************
     * METHOD: endOfSort() *
     * DESCRIPTION: plays the sound at the end of the sort and redraws the array
     * PARAMETERS: array *
     * RETURN VALUE: none *
     **********************************************************/
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
        float pauseTime = 1.0f/ (float)arr.length * 100.0f;
        if(pauseTime > 0 && pauseTime < 1){
            pauseTime = 1;
        }
        System.out.println("PauseTIme: " + pauseTime);
        drawSortStep( (int)pauseTime, arr,1,1);

    }
}