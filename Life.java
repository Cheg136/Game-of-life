import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Life extends JFrame {
    private static final int GRID_SIZE = 100;
    private static final int CELL_SIZE = 5;
    private boolean[][] grid;
    private Timer timer;
    private Random rnd;

    public Life() 
    {
        grid = new boolean[GRID_SIZE][GRID_SIZE];
        rnd = new Random();
        initializeGrid();
        setTitle("Life");
        setSize(GRID_SIZE * CELL_SIZE, GRID_SIZE * CELL_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        int delay = 1;
        timer = new Timer(delay, new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                updateGrid();
                repaint();
            }
        });
        timer.start();
    }

    private void initializeGrid() {
        for (int i = 0; i < GRID_SIZE; i++) 
        {
            for (int j = 0; j < GRID_SIZE; j++) 
            {
                grid[i][j] = Math.random() < 0.5;
            }
        }
    }

    private void updateGrid() 
    {
        SwingWorker<Void, Void> worker = new SwingWorker<>() 
        {
            @Override
            protected Void doInBackground() 
            {
                boolean[][] newGrid = new boolean[GRID_SIZE][GRID_SIZE];
                for (int i = 0; i < GRID_SIZE; i++) 
                {
                    for (int j = 0; j < GRID_SIZE; j++) 
                    {
                        int neighbors = countNeighbors(i, j);
                        int life = rnd.nextInt(1001);

                        if (grid[i][j]) 
                        {
                            newGrid[i][j] = neighbors == 2 || neighbors == 3;
                        } 
                        else 
                        {
                            newGrid[i][j] = neighbors == 3;
                        }
                        if (life == 1000)
                        {
                        	newGrid[i][j] = true;
                        	newGrid[i - 1][j] = true;
                        	newGrid[i + 1][j] = true;
                        	newGrid[i][j - 1] = true;
                        	newGrid[i][j + 1] = true;
                        }
                    }
                }

                grid = newGrid;
                return null;
            }

            @Override
            protected void done() 
            {
                repaint();
            }
        };

        worker.execute();
    }
    private int countNeighbors(int x, int y) 
    {
        int count = 0;

        for (int i = -1; i <= 1; i++) 
        {
            for (int j = -1; j <= 1; j++) 
            {
                if (i == 0 && j == 0) 
                {
                    continue; 
                }

                int newX = (x + i + GRID_SIZE) % GRID_SIZE;
                int newY = (y + j + GRID_SIZE) % GRID_SIZE;

                if (grid[newX][newY]) 
                {
                    count++;
                }
            }
        }

        return count;
    }

    @Override
    public void paint(Graphics g) 
    {
        super.paint(g);

        for (int i = 0; i < GRID_SIZE; i++) 
        {
            for (int j = 0; j < GRID_SIZE; j++) 
            {
                if (grid[i][j]) 
                {
                    g.fillRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }

    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(() -> 
        {
            Life game = new Life();
            game.setVisible(true);
        });
    }
}
