import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.io.*;
import java.util.Random;

import be.tarsos.dsp.AudioDispatcher;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
public class SortingAlgorithms {
    private Random rand = new Random();

    // Sound Methods
    static File inputFile = new File("src/sounds/hitHurt.wav");
    static File[] soundFiles = new File[100];
    void GenerateSoundFiles() {
        for (int i = 0; i < 100; i++) {
            soundFiles[i] = new File("src/sounds/hitHurt" + i + ".wav");
        }
    }
    private static void changePitch(File outputFile, float pitchShiftFactor) throws Exception {
        AudioInputStream inputStream = AudioSystem.getAudioInputStream(inputFile);
        AudioFormat format = inputStream.getFormat();

        AudioDispatcher dispatcher = AudioDispatcherFactory.fromPipe(
                inputFile.getAbsolutePath(), (int)format.getSampleRate(), 1024, 0);
        dispatcher.addAudioProcessor(new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, format.getSampleRate(), 1024, (pitchDetectionResult, audioEvent) -> {
            float pitch = pitchDetectionResult.getPitch();
            if (pitch != -1) {
                audioEvent.setFloatBuffer(audioEvent.getFloatBuffer());
            }
        }));

        // Convert AudioFormat to TarsosDSPAudioFormat
        TarsosDSPAudioFormat tarsosFormat = JVMAudioInputStream.toTarsosDSPFormat(format);

        dispatcher.addAudioProcessor(new WriterProcessor(tarsosFormat, new RandomAccessFile(outputFile, "rw")));
        dispatcher.run();
    }
    private static void playSound(File file) throws Exception {
        Clip clip = AudioSystem.getClip();
        clip.open(AudioSystem.getAudioInputStream(file));
        clip.start();
        //Thread.sleep(clip.getMicrosecondLength() / 1000);
    }


//        try {
//            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("laserShoot.wav"));
//            Clip clip = AudioSystem.getClip();
//            clip.open(audioInputStream);
//            clip.start();
//            AudioFormat baseFormat = audioInputStream.getFormat();
//            AudioFormat newFormat = new AudioFormat(
//                    baseFormat.getEncoding(),
//                    baseFormat.getSampleRate() * (float)Math.random() * 10000.0f,
//                    baseFormat.getSampleSizeInBits(),
//                    baseFormat.getChannels(),
//                    baseFormat.getFrameSize(),
//                    baseFormat.getFrameRate() * pitch,
//                    baseFormat.isBigEndian()
//            );
//            return clip;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public float convertArrayHeightToPitchRange(float height) {
        return (height * 2) - 1;
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
                //sound.stop();
                //sound = playSound(convertArrayHeightToPitchRange(arr[i]) * 100);
                int fractionalPosition = (i / size) * 100;
                try {
                    playSound(soundFiles[fractionalPosition]);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
            drawSortStep(2, arr, taskNum, taskTotal);
        }
        System.out.println("Selection Sort Finished\n");
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
    public void drawBackground(Color color) {
        StdDraw.setPenColor(color);
        StdDraw.filledSquare(.5, .5, 1f);
    }


    //Use to redraw the array in the sorting functions (Requires double Buffering)
    public void drawSortStep(int pauseTime, float[] arr,
                             int taskNum, int taskTotal) {
        clearQuadrant(taskNum, taskTotal);
        drawArrayRects(arr, taskNum, taskTotal);
        StdDraw.show();
        StdDraw.pause(pauseTime);
    }
}