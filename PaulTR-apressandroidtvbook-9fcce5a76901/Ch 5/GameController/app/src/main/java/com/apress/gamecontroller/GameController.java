package com.apress.gamecontroller;

import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Created by Paul on 11/27/15.
 */
public class GameController {
    public static final int BUTTON_A = 0;
    public static final int BUTTON_B = 1;
    public static final int BUTTON_X = 2;
    public static final int BUTTON_Y = 3;
    public static final int BUTTON_R1 = 4;
    public static final int BUTTON_R2 = 5;
    public static final int BUTTON_L1 = 6;
    public static final int BUTTON_L2 = 7;
    public static final int BUTTON_COUNT = 8;

    public static final int AXIS_X = 0;
    public static final int AXIS_Y = 1;
    public static final int AXIS_COUNT = 2;

    public static final int JOYSTICK_1 = 0;
    public static final int JOYSTICK_2 = 1;
    public static final int JOYSTICK_COUNT = 2;

    private final float mJoystickPositions[][];
    private final boolean mButtonState[];

    public GameController() {
        mButtonState = new boolean[BUTTON_COUNT];
        mJoystickPositions = new float[JOYSTICK_COUNT][AXIS_COUNT];
        resetState();
    }

    private void resetState() {
        for (int button = 0; button < BUTTON_COUNT; button++) {
            mButtonState[button] = false;
        }

        for (int joystick = 0; joystick < JOYSTICK_COUNT; joystick++) {
            for (int axis = 0; axis < AXIS_COUNT; axis++) {
                mJoystickPositions[joystick][axis] = 0.0f;
            }
        }
    }

    public float getJoystickPosition(int joystickIndex, int axis) {
        return mJoystickPositions[joystickIndex][axis];
    }

    public boolean isButtonDown(int buttonId) {
        return mButtonState[buttonId];
    }

    public void handleMotionEvent(MotionEvent motionEvent) {
        mJoystickPositions[JOYSTICK_1][AXIS_X] = motionEvent.getAxisValue( MotionEvent.AXIS_X );
        mJoystickPositions[JOYSTICK_1][AXIS_Y] = motionEvent.getAxisValue( MotionEvent.AXIS_Y );

        mJoystickPositions[JOYSTICK_2][AXIS_X] = motionEvent.getAxisValue( MotionEvent.AXIS_Z );
        mJoystickPositions[JOYSTICK_2][AXIS_Y] = motionEvent.getAxisValue( MotionEvent.AXIS_RZ );
    }

    public void handleKeyEvent(KeyEvent keyEvent) {
        boolean keyIsDown = keyEvent.getAction() == KeyEvent.ACTION_DOWN;

        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BUTTON_A) {
            mButtonState[BUTTON_A] = keyIsDown;
        } else if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BUTTON_B) {
            mButtonState[BUTTON_B] = keyIsDown;
        } else if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BUTTON_X) {
            mButtonState[BUTTON_X] = keyIsDown;
        } else if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BUTTON_Y) {
            mButtonState[BUTTON_Y] = keyIsDown;
        } else if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BUTTON_R1 ) {
            mButtonState[BUTTON_R1] = keyIsDown;
        } else if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BUTTON_R2 ) {
            mButtonState[BUTTON_R2] = keyIsDown;
        } else if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BUTTON_L1 ) {
            mButtonState[BUTTON_L1] = keyIsDown;
        } else if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BUTTON_L2 ) {
            mButtonState[BUTTON_L2] = keyIsDown;
        }
    }
}
