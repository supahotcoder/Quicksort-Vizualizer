package app;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class JGraph extends JComponent {

    private int[] points;
    private int max;

    public JGraph(int[] points) {
        this.points = points;
        max = Arrays.stream(points).summaryStatistics().getMax();
    }

    public void setPoints(int[] points) {
        this.points = points;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        final int BOX_WIDTH = this.getWidth() / points.length;
        final int BOX_HEIGHT = this.getHeight() / max;

        g2.setColor(Color.PINK);
        for (int i = 0; i < points.length; i++) {
            var rect = new Rectangle(i * BOX_WIDTH, this.getHeight() - (points[i] * BOX_HEIGHT), BOX_WIDTH - 1, points[i] * BOX_HEIGHT);
            g2.fill(rect);
        }
    }
}
