package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;

public class SortGUI extends JFrame {

    private final JGraph graph;

    private int[] array;
    private JMenuBar menuBar;
    private SortThread sort = null;

    private static int ARRAY_SIZE = 100;

    public SortGUI() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Sorting visualizer");

        JPanel mainPanel = new JPanel(new BorderLayout());

        setupMenu();

        array = generateArray(ARRAY_SIZE);
        graph = new JGraph(array);

        mainPanel.add(graph, BorderLayout.CENTER);

        this.setJMenuBar(menuBar);
        this.setContentPane(mainPanel);
        this.setPreferredSize(new Dimension(600, 350));
        this.pack();
    }

    private void setupMenu() {
        menuBar = new JMenuBar();

        JMenuItem shuffle = new JMenuItem("Shuffle");
        shuffle.addActionListener(this::shuffle);

        JMenuItem pause = new JMenuItem("Pause");
        pause.addActionListener(this::pause);

        JMenuItem resume = new JMenuItem("Resume");
        resume.addActionListener(this::resume);

        menuBar.add(shuffle);
        menuBar.add(pause);
        menuBar.add(resume);
    }

    private void resume(ActionEvent event) {
        sort = new SortThread(array);
        sort.start();
    }

    private void pause(ActionEvent event) {
        if (sort != null) sort.cancel();
    }

    private void shuffle(ActionEvent event) {
        if (sort == null || sort.isCancel()){
            array = generateArray(ARRAY_SIZE);
            updateGraph(array);
        }
    }

    private void updateGraph(int[] array) {
        graph.setPoints(array);
    }

    private static int[] generateArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            Random rand = new Random();
            array[i] = rand.nextInt(300);
        }
        return array;
    }

    private class SortThread extends Thread {

        private int[] array;
        private boolean cancel = false;
        private int sleep = 20;

        public SortThread(int[] array) {
            super();
            this.array = array;
        }

        public synchronized void cancel() {
            cancel = true;
        }

        public synchronized boolean isCancel() {
            return cancel;
        }

        @Override
        public void run() {
            quicksort(array);
            cancel();
        }

        private void quicksort(int[] array) {
            quickPartition(array, 0, array.length);
            graph.setPoints(array);
        }

        private void quickPartition(int[] array, int leftPos, int rightPos) {
            if (isCancel()) return;
            if (leftPos < rightPos) {
                int bound = leftPos;
                for (int i = leftPos + 1; i < rightPos; i++) {
                    if (array[i] < array[leftPos]) {
                        if (isCancel()) return;
                        swap(array, i, ++bound);
                        graph.setPoints(array);
                        try {
                            sleep(sleep);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                swap(array, leftPos, bound);
                quickPartition(array, leftPos, bound);
                quickPartition(array, bound + 1, rightPos);
            }
        }

        private void swap(int[] array, int a, int b) {
            int tmp = array[a];
            array[a] = array[b];
            array[b] = tmp;
        }
    }

}
