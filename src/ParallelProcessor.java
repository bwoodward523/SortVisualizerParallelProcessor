import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelProcessor {
    public void setupTasks(int taskNum, float[] arr, Sort.SortType algorithm) {
        ExecutorService executor = Executors.newFixedThreadPool(taskNum);
        Sort sorter = new Sort();
        SortingAlgorithms alg = new SortingAlgorithms();
        int subArraySize = arr.length / taskNum;
        float[][] sortedSubArrays = new float[taskNum][];

        for (int i = 0; i < taskNum; i++) {
            int start = i * subArraySize;
            int end = (i == taskNum - 1) ? arr.length : (i + 1) * subArraySize;
            float[] subArray = new float[end - start];
            System.arraycopy(arr, start, subArray, 0, end - start);

            int finalI = i;
            executor.submit(() -> {
                System.out.println("Executing task " + finalI + " by " + Thread.currentThread().getName());
                switch (algorithm) {
                    case SELECTION:
                        alg.selectionSort(subArray, finalI, taskNum);
                        break;
                    case BUBBLE:
                        alg.bubbleSort(subArray, finalI, taskNum);
                        break;
                    case ANGEL:
                        alg.angelSort(subArray, finalI, taskNum);
                        break;
                    case QUICK:
                        alg.quickSort(subArray, 0, subArray.length - 1, finalI, taskNum);
                        break;
                    case CYCLE:
                        alg.cycleSort(subArray, finalI, taskNum);
                        break;
                    case MERGE:
                        alg.mergeSort(subArray, finalI, taskNum);
                        break;
                }
                sortedSubArrays[finalI] = subArray;
            });
        }

        // Shutdown the executor
        executor.shutdown();
        try {
            // Wait for all tasks to finish
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        // Merge sorted subarrays
        float[] mergedArray = sortedSubArrays[0];
        for (int i = 1; i < sortedSubArrays.length; i++) {
            mergedArray = mergeArrays(mergedArray, sortedSubArrays[i], alg);
        }

        // Copy merged array back to original array
        System.arraycopy(mergedArray, 0, arr, 0, arr.length);
    }

    public static float[] mergeArrays(float[] arr1, float[] arr2, SortingAlgorithms sorter) {
        int n1 = arr1.length;
        int n2 = arr2.length;
        float[] mergedArray = new float[n1 + n2];

        int i = 0, j = 0, k = 0;

        while (i < n1 && j < n2) {
            if (arr1[i] < arr2[j]) {
                mergedArray[k++] = arr1[i++];
            } else {
                mergedArray[k++] = arr2[j++];
            }

            //sorter.drawSortStep(0, mergedArray, 1, 1); // Visualize the merging process
        }

        while (i < n1) {
            mergedArray[k++] = arr1[i++];
            sorter.drawSortStep(0, mergedArray, 1 ,1); // Visualize the merging process
        }

        while (j < n2) {
            mergedArray[k++] = arr2[j++];
            sorter.drawSortStep(0, mergedArray, 1, 1); // Visualize the merging process
        }

        return mergedArray;
    }
}



