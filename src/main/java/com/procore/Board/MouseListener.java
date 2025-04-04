package com.procore.Board;

import lombok.Getter;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

@Getter
public class MouseListener implements java.awt.event.MouseListener, MouseMotionListener {
    public static final int BUTTON_LEFT = MouseEvent.BUTTON1;

    private final boolean[] buttonsPressed;
    private int currentX;  // Current mouse X position
    private int currentY;  // Current mouse Y position
    private int lastX;     // Last mouse X position
    private int lastY;     // Last mouse Y position
    private int deltaX;    // Change in X
    private int deltaY;    // Change in Y

    public MouseListener() {
        buttonsPressed = new boolean[16]; // Tracks buttons being pressed
    }

    public void refresh() {
        deltaX = currentX - lastX;
        deltaY = currentY - lastY;
        lastX = currentX;
        lastY = currentY;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        buttonsPressed[e.getButton()] = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        buttonsPressed[e.getButton()] = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        currentX = e.getX();
        currentY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        currentX = e.getX();
        currentY = e.getY();
    }

    public boolean isButtonPressed(int button) {
        return buttonsPressed[button];
    }

}
