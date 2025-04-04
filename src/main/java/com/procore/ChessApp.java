package com.procore;

import com.procore.Board.InitializeGameStart;
import com.procore.Board.MouseListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

public class ChessApp extends Canvas implements Runnable {
    public static ChessApp instance;

    private JFrame gameFrame;
    private Thread gameThread;
    private boolean isRunning;
    private InitializeGameStart gameStart;
    private MouseListener inputHandler;

    public ChessApp() {
        Dimension dimension = new Dimension(800, 600);
        setPreferredSize(dimension);
    }

    public static void main(String[] args) {
        instance = new ChessApp();
        instance.start();
    }

    private void initialize() {
        gameFrame = new JFrame("Chess Bot");
        gameFrame.add(this);
        gameFrame.pack();
        gameFrame.setResizable(false);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                stop();
            }
        });

        inputHandler = new MouseListener();
        addMouseListener(inputHandler);
        addMouseMotionListener(inputHandler);

        gameStart = new InitializeGameStart();

        gameFrame.setVisible(true);
    }

    private void cleanup() {

        System.out.println("Cleaning up resources...");

        if (inputHandler != null) {
            removeMouseListener(inputHandler);
            removeMouseMotionListener(inputHandler);
        }

        if (gameFrame != null) {
            gameFrame.dispose();
        }


        if (gameThread != null && gameThread.isAlive()) {
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Cleanup complete.");
    }


    public synchronized void start() {
        if (isRunning) return;
        isRunning = true;
        initialize();
        gameThread = new Thread(this, "Game Thread");
        gameThread.start();
    }


    public synchronized void stop() {
        if (!isRunning) return;
        isRunning = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        cleanup();
        gameFrame.dispose();
    }

    public void run() {
        long currentTime;
        long lastTime = System.nanoTime();
        float delta;

        while (isRunning) {
            currentTime = System.nanoTime();
            delta = (float) (currentTime - lastTime) / 1_000_000_000.0f;
            lastTime = currentTime;

            update(delta);
            render();
        }
    }

    private void update(float delta) {
         gameStart.update(inputHandler, delta);
         inputHandler.refresh();
    }

    private void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(2);
            requestFocus();
            return;
        }
        Graphics2D g = (Graphics2D) bs.getDrawGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        gameStart.render(g);

        g.dispose();
        bs.show();
    }
}
