package com.archer.lab10v2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by archer on 2017-12-24.
 */

public class MyGameView extends View {

    Bitmap environment;
    Bitmap nukeObject;
    Bitmap[][] movements;
    List<Move> wrongMoves;
    int positionX;
    int positionY;
    int objectW = 72;
    int objectH = 50;
    int envW = 1080;
    int envH = 650;
    float currentX = 0;
    float currentY = 0;
    int currentColumnIndex = 0;
    int currentRowIndex = 0;

    public MyGameView(Context context) {
        super(context);

        setMovements();

        wrongMoves = new ArrayList<>();
        for (int row = 2; row < 14; row += 2) {
            for (int col = 2; col < 14; col += 2) {
                Move move = new Move();
                move.setPosX(col * objectW);
                move.setPosY(row * objectH);
                wrongMoves.add(move);
            }
        }

        Bitmap e = BitmapFactory.decodeResource(getResources(), R.drawable.map);
        Bitmap n = BitmapFactory.decodeResource(getResources(), R.drawable.object);
        environment = Bitmap.createScaledBitmap(e, envW, envH, false);
        nukeObject = Bitmap.createScaledBitmap(n, 72, 50, false);
        positionX = objectW;
        positionY = objectH;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(environment, 0, 0, null);
        canvas.drawBitmap(movements[currentRowIndex][currentColumnIndex], positionX, positionY, null);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {

        }
    }

    public boolean isValid(int x, int y) {
        for (Move move: wrongMoves) {
            if(move.getPosX() == x && move.getPosY() == y) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentX = event.getX();
                currentY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getX() > currentX + 100 && positionX < envW - 2*objectW - 10) {
                    int predictedX = positionX + objectW;
                    if(isValid(predictedX, positionY)) {
                        positionX += objectW;
                        currentRowIndex = 2;
                        if (currentColumnIndex < 7) {
                            currentColumnIndex ++;
                        } else {
                            currentColumnIndex = 0;
                        }
                    }
                }
                else if(event.getX() < currentX - 100 && positionX > objectW + 10) {
                    int predictedX = positionX - objectW;
                    if(isValid(predictedX, positionY)) {
                        positionX -= objectW;
                        currentRowIndex = 1;
                        if (currentColumnIndex > 0) {
                            currentColumnIndex --;
                        } else {
                            currentColumnIndex = 7;
                        }
                    }
                }
                invalidate();
                if (event.getY() > currentY + 100 && positionY < envH - 2*objectH - 10) {
                    int predictedY = positionY + objectH;
                    if(isValid(positionX, predictedY)) {
                        positionY += objectH;
                        currentRowIndex = 0;
                        if (currentColumnIndex < 7) {
                            currentColumnIndex ++;
                        } else {
                            currentColumnIndex = 0;
                        }
                    }
                }
                else if(event.getY() < currentY - 100 && positionY > objectH + 10) {
                    int predictedY = positionY - objectH;
                    if(isValid(positionX, predictedY)) {
                        positionY -= objectH;
                        currentRowIndex = 3;
                        if (currentColumnIndex > 0) {
                            currentColumnIndex --;
                        } else {
                            currentColumnIndex = 7;
                        }
                    }
                }
                invalidate();
                break;
        }
        return true;
    }

    public void setMovements() {
        Bitmap tm = BitmapFactory.decodeResource(getResources(), R.drawable.movements);
        Bitmap m = Bitmap.createScaledBitmap(tm, 576, 200, false);
        movements = new Bitmap[4][8];
        int rows = 4;
        int columns = 8;
        int width = 72;
        int height = 50;
        int offsetY = 0;
        for (int i = 0; i < rows; i++) {
            int offsetX = 0;
            for(int j = 0; j < columns; j++) {
                Bitmap movement = Bitmap.createBitmap(m, offsetX, offsetY, width, height);
                movements[i][j] = movement;
                offsetX += width;
            }
            offsetY += height;
        }

    }
}
