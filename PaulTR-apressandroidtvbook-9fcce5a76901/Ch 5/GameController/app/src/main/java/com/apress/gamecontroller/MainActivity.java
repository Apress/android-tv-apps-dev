package com.apress.gamecontroller;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by Paul on 11/27/15.
 */
public class MainActivity extends Activity {

    private final GameController mController = new GameController();

    private TextView mButtonStateA;
    private TextView mButtonStateB;
    private TextView mButtonStateX;
    private TextView mButtonStateY;
    private TextView mButtonStateR1;
    private TextView mButtonStateL1;

    private TextView mJoystickState1AxisX;
    private TextView mJoystickState1AxisY;
    private TextView mJoystickState2AxisX;
    private TextView mJoystickState2AxisY;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mButtonStateA = (TextView) findViewById( R.id.button_a );
    mButtonStateB = (TextView) findViewById( R.id.button_b );
    mButtonStateX = (TextView) findViewById( R.id.button_x );
    mButtonStateY = (TextView) findViewById( R.id.button_y );
    mButtonStateR1 = (TextView) findViewById( R.id.button_r1 );
    mButtonStateL1 = (TextView) findViewById( R.id.button_l1 );

    mJoystickState1AxisX = (TextView) findViewById( R.id.joystick_1_axis_x );
    mJoystickState1AxisY = (TextView) findViewById( R.id.joystick_1_axis_y );
    mJoystickState2AxisX = (TextView) findViewById( R.id.joystick_2_axis_x );
    mJoystickState2AxisY = (TextView) findViewById( R.id.joystick_2_axis_y );
}

@Override
public boolean dispatchGenericMotionEvent(MotionEvent ev) {
    mController.handleMotionEvent(ev);

    //R2 and L2 (triggers) are also analog and use this callback
    mJoystickState1AxisX.setText(String.valueOf(
            mController.getJoystickPosition(GameController.JOYSTICK_1,
                    GameController.AXIS_X)));
    mJoystickState1AxisY.setText(String.valueOf(
            mController.getJoystickPosition(GameController.JOYSTICK_1,
                    GameController.AXIS_Y)));
    mJoystickState2AxisX.setText(String.valueOf(
            mController.getJoystickPosition(GameController.JOYSTICK_2,
                    GameController.AXIS_X)));
    mJoystickState2AxisY.setText(String.valueOf(
            mController.getJoystickPosition(GameController.JOYSTICK_2,
                    GameController.AXIS_Y)));
    return true;
}

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if( event.getKeyCode() == KeyEvent.KEYCODE_BACK ) {
            return super.dispatchKeyEvent(event);
        }

        mController.handleKeyEvent(event);

        updateTextViewForButton( mButtonStateA, mController.isButtonDown( GameController.BUTTON_A ) );
        updateTextViewForButton( mButtonStateB, mController.isButtonDown( GameController.BUTTON_B ) );
        updateTextViewForButton( mButtonStateX, mController.isButtonDown( GameController.BUTTON_X ) );
        updateTextViewForButton( mButtonStateY, mController.isButtonDown( GameController.BUTTON_Y ) );
        updateTextViewForButton( mButtonStateR1, mController.isButtonDown( GameController.BUTTON_R1 ) );
        updateTextViewForButton( mButtonStateL1, mController.isButtonDown( GameController.BUTTON_L1 ) );

        return true;
    }

    private void updateTextViewForButton( TextView textView, boolean pressed ) {
        if( pressed ) {
            textView.setText( getString( R.string.state_pressed ) );
        } else {
            textView.setText( getString( R.string.state_not_pressed ) );
        }
    }
}
