package edu_up_cs301.ludo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu_up_cs301.game.util.FlashSurfaceView;


/**
 * LudoSurfaceView
 * This draws the entire board, all the pieces, and the dice.
 * It also updates the board according to touch events and button presses * *
 */

public class LudoSurfaceView extends FlashSurfaceView {

    //instance variables
    protected LudoState state = new LudoState();
    private boolean[] isdrawn;


    /**
     * Constructors
     */
    public LudoSurfaceView(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public LudoSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public void setState(LudoState state) {
        this.state = state;
    }


    /**
     * onDraw
     * the starting place for all drawing that takes place
     * implements the use of helper methods.
     *
     * @param canvas - draw on this
     */
    @Override
    public void onDraw(Canvas canvas) {

        //define the canvas instance variables
        float heightAndWidth = canvas.getWidth();//height and width are the same because the surface view is a square
        float box = heightAndWidth / 15;
        //Draw a green filled square
        //Creating Color Objects
        Paint greenPaint = new Paint();
        Paint redPaint = new Paint();
        Paint bluePaint = new Paint();
        Paint yellowPaint = new Paint();
        Paint whitePaint = new Paint();
        Paint blackPaint = new Paint();
        Paint whitePaint2 = new Paint();
        //Creating the colors
        redPaint.setColor(Color.rgb(193, 23, 23));
        greenPaint.setColor(Color.rgb(50, 220, 50));
        bluePaint.setColor(Color.rgb(10, 10, 230));
        yellowPaint.setColor(Color.rgb(242, 228, 38));
        whitePaint.setColor(Color.rgb(255, 255, 255));
        whitePaint2.setColor(Color.rgb(255, 255, 255));
        blackPaint.setColor(Color.rgb(0, 0, 0));

        //draw the black background
        this.setBackgroundColor(Color.BLACK);

        //Drawing all HomeStretch and Opening Tiles
        float i, j;
        for (i = box; i < (box * 6); i = i + box) {
            j = (box * 7);
            canvas.drawRect(i, j, (float) (i + box), (float) (j + box), redPaint);
        } //Red Homestretch
        canvas.drawRect(box, (box * 6), (box * 2), (box * 7), redPaint); //Red Open Tile
        for (i = (box * 9); i < (box * 13); i = i + box) {
            j = (box * 7);
            canvas.drawRect(i, j, (float) (i + box), (float) (j + box), yellowPaint);
        } //Yellow Homestretch
        canvas.drawRect((box * 13), (box * 8), (box * 14), (box * 9), yellowPaint); //Yellow Open Tile
        for (j = box; j < (box * 6); j = j + box) {
            i = (box * 7);
            canvas.drawRect(i, j, (float) (i + box), (float) (j + box), greenPaint);
        } //Green Homestretch
        canvas.drawRect((box * 8), box, (box * 9), (box * 2), greenPaint); //Green Open Tile
        for (j = (box * 9); j < (box * 13); j = j + box) {
            i = (box * 7);
            canvas.drawRect(i, j, (float) (i + box), (float) (j + box), bluePaint);
        } //Blue Homestretch
        canvas.drawRect((box * 6), (box * 13), (box * 7), (box * 14), bluePaint); //Blue Open Tile

        //Drawing ALL Valid and Invalid small tile squares
        for (i = 0; i < ((box * 15) - box); i = i + box) {
            for (j = 0; j < ((box * 15)); j = j + box) {
                whitePaint2.setStyle(Paint.Style.STROKE);
                whitePaint2.setStrokeWidth(3);
                canvas.drawRect(i, j, (float) (i + box), (float) (i + box), whitePaint2);
            }
        }

        //draw red HomeBase
        drawHomeBase(canvas, box, 0, 0, redPaint, whitePaint);
        //draw green HomeBase
        drawHomeBase(canvas, box, box*9, 0, greenPaint, whitePaint);
        //draw yellow HomeBase
        drawHomeBase(canvas, box, box*9, box*9, yellowPaint, whitePaint);
        //draw blue HomeBase
        drawHomeBase(canvas, box, 0, box*9, bluePaint, whitePaint);


        //Drawing Center Square
        drawCenterSquare(canvas, box, whitePaint, redPaint, greenPaint, yellowPaint, bluePaint);

        //draw the safe space tiles
        drawStar((box * 2), (box * 8), canvas, box);
        drawStar((box * 8), (box * 12), canvas, box);
        drawStar((box * 6), (box * 2), canvas, box);
        drawStar((box * 12), (box * 6), canvas, box);

        //draw all the pieces
        drawPieces(canvas, box);

        drawDice(canvas, box); //draw the dice
    }


    /**
     * drawStar
     *
     * @param xPos   the initial x pos of the star
     * @param yPos   the initial y pos of the star
     * @param canvas Canvas object
     * @param box    This is the width of the board divided by 15. This is used to scale everything
     *               according to the screens dimensions
     */
    public void drawStar(float xPos, float yPos, Canvas canvas, float box) {
        Paint whitePaint = new Paint();
        whitePaint.setColor(Color.rgb(255, 255, 255));

        float xShift1 = (float) (0.1122449 * box);
        float xShift2 = (float) (0.5 * box);
        float xShift3 = (float) (0.88877551 * box);
        float yShift1 = (float) (0.295918 * box);
        float yShift2 = (float) (0.8877551 * box);
        float yShift3 = (float) (0.673469 * box);
        float yShift4 = (float) (0.08163 * box);

        PointF p1 = new PointF(xPos + xShift1, yPos + yShift1); //Bottom Left of Lower Star
        PointF p2 = new PointF(xPos + xShift3, yPos + yShift1); //Bottom Right of Lower Star
        PointF p3 = new PointF(xPos + xShift2, yPos + yShift2); //Top of Star

        PointF p4 = new PointF(xPos + xShift1, yPos + yShift3); //Left point of Upper Star
        PointF p5 = new PointF(xPos + xShift3, yPos + yShift3); //Right point of Upper Star
        PointF p6 = new PointF(xPos + xShift2, yPos + yShift4); //Bottom of Star


        Path bottomStar = new Path();
        bottomStar.moveTo(p1.x, p1.y);
        bottomStar.lineTo(p2.x, p2.y);
        bottomStar.lineTo(p3.x, p3.y);
        bottomStar.lineTo(p1.x, p1.y);
        bottomStar.close();
        canvas.drawPath(bottomStar, whitePaint);

        Path topStar = new Path();
        topStar.moveTo(p4.x, p4.y);
        topStar.lineTo(p5.x, p5.y);
        topStar.lineTo(p6.x, p6.y);
        topStar.lineTo(p4.x, p4.y);
        topStar.close();
        canvas.drawPath(topStar, whitePaint);
    }

    /**
     * drawDice
     * <p>
     * This draws the dice according to who's turn it is.
     *
     * @param canvas Canvas object
     * @param box    This is the width of the board divided by 15. This is used to scale everything
     *               according to the screens dimensions
     */
    public void drawDice(Canvas canvas, float box) {
        Paint greyPaint = new Paint();
        greyPaint.setColor(Color.rgb(100, 100, 100));
        Paint redPaint = new Paint();
        redPaint.setColor(Color.rgb(255, 0, 0));
        Paint greenPaint = new Paint();
        greenPaint.setColor(Color.rgb(0, 255, 0));
        Paint bluePaint = new Paint();
        bluePaint.setColor(Color.rgb(0, 0, 255));
        Paint yellowPaint = new Paint();
        yellowPaint.setColor(Color.rgb(255, 255, 0));
        Paint blackPaint = new Paint();
        blackPaint.setColor(Color.rgb(0, 0, 0));

        if (state.getStillPlayersTurn() == true) {
            switch (state.getWhoseMove()) { //don't shift where the dice is drawn
                case 0:
                    canvas.drawRect((float) (box * 2.3), (float) (box * 2.3), (float) (box * 3.7), (float) (box * 3.7), redPaint);
                    drawDots(0, 0, box, state.getDiceVal(), canvas, blackPaint);
                    break;

                case 1:
                    canvas.drawRect(((float) ((box * 11.3))), (float) (box * 2.3), ((float) ((box * 12.7))), (float) (box * 3.7), greenPaint);
                    drawDots(box * 9, 0, box, state.getDiceVal(), canvas, blackPaint);
                    break;
                case 2:
                    canvas.drawRect(((float) ((box * 11.3))), ((float) ((box * 11.3))), ((float) ((box * 12.7))), ((float) ((box * 12.7))), yellowPaint);
                    drawDots(box * 9, box * 9, box, state.getDiceVal(), canvas, blackPaint);
                    break;
                case 3:
                    canvas.drawRect(((float) ((box * 2.3))), ((float) ((box * 11.3))), ((float) (box * 3.7)), ((float) ((box * 12.7))), bluePaint);
                    drawDots(0, box * 9, box, state.getDiceVal(), canvas, blackPaint);
                    break;
            }
        }
        //else diceVal != 6 then shift
        else { //draw with a shift to reflect GUI Change
            switch (state.getWhoseMove() - 1) { //shift where the dice is drawn
                case 0:
                    canvas.drawRect((float) (box * 2.3), (float) (box * 2.3), (float) (box * 3.7), (float) (box * 3.7), redPaint);
                    drawDots(0, 0, box, state.getDiceVal(), canvas, blackPaint);
                    break;

                case 1:
                    canvas.drawRect(((float) ((box * 11.3))), (float) (box * 2.3), ((float) ((box * 12.7))), (float) (box * 3.7), greenPaint);
                    drawDots(box * 9, 0, box, state.getDiceVal(), canvas, blackPaint);
                    break;
                case 2:
                    canvas.drawRect(((float) ((box * 11.3))), ((float) ((box * 11.3))), ((float) ((box * 12.7))), ((float) ((box * 12.7))), yellowPaint);
                    drawDots(box * 9, box * 9, box, state.getDiceVal(), canvas, blackPaint);
                    break;
                case -1:
                    canvas.drawRect(((float) ((box * 2.3))), ((float) ((box * 11.3))), ((float) (box * 3.7)), ((float) ((box * 12.7))), bluePaint);
                    drawDots(0, box * 9, box, state.getDiceVal(), canvas, blackPaint);
                    break;
            }
        }
    }

    /**
     * drawDots
     * This draws the exact number of dots according to the dice roll
     *
     * @param xPos   the initial x pos of the star
     * @param yPos   the initial y pos of the star
     * @param canvas Canvas object
     * @param color
     */
    public void drawDots(float xPos, float yPos, float shift, int diceVal, Canvas canvas, Paint color) {
        switch (diceVal) {
            case 1:
                canvas.drawCircle(xPos + shift * 3, yPos + shift * 3, 18, color);
                break;
            case 2:
                canvas.drawCircle(xPos + (float) (shift * 2.7), yPos + (float) (shift * 2.7), 18, color);
                canvas.drawCircle(xPos + (float) (shift * 3.3), yPos + (float) (shift * 3.3), 18, color);
                break;
            case 3:
                canvas.drawCircle(xPos + (float) (shift * 2.7), yPos + (float) (shift * 2.7), 18, color);
                canvas.drawCircle(xPos + (float) (shift * 3.3), yPos + (float) (shift * 3.3), 18, color);
                canvas.drawCircle(xPos + (float) (shift * 3), yPos + (float) (shift * 3), 18, color);
                break;
            case 4:
                canvas.drawCircle(xPos + (float) (shift * 2.7), yPos + (float) (shift * 2.7), 15, color);
                canvas.drawCircle(xPos + (float) (shift * 2.7), yPos + (float) (shift * 3.3), 15, color);
                canvas.drawCircle(xPos + (float) (shift * 3.3), yPos + (float) (shift * 2.7), 15, color);
                canvas.drawCircle(xPos + (float) (shift * 3.3), yPos + (float) (shift * 3.3), 15, color);
                break;
            case 5:
                canvas.drawCircle(xPos + (float) (shift * 2.6), yPos + (float) (shift * 2.6), 15, color);
                canvas.drawCircle(xPos + (float) (shift * 2.6), yPos + (float) (shift * 3.4), 15, color);
                canvas.drawCircle(xPos + (float) (shift * 3.4), yPos + (float) (shift * 2.6), 15, color);
                canvas.drawCircle(xPos + (float) (shift * 3.4), yPos + (float) (shift * 3.4), 15, color);
                canvas.drawCircle(xPos + (float) (shift * 3), yPos + (float) (shift * 3), 15, color);
                break;
            case 6:
                canvas.drawCircle(xPos + (float) (shift * 2.6), yPos + (float) (shift * 2.6), 15, color);
                canvas.drawCircle(xPos + (float) (shift * 2.6), yPos + (float) (shift * 3.4), 15, color);
                canvas.drawCircle(xPos + (float) (shift * 3.4), yPos + (float) (shift * 2.6), 15, color);
                canvas.drawCircle(xPos + (float) (shift * 3.4), yPos + (float) (shift * 3.4), 15, color);
                canvas.drawCircle(xPos + (float) (shift * 2.6), yPos + (float) (shift * 3), 15, color);
                canvas.drawCircle(xPos + (float) (shift * 3.4), yPos + (float) (shift * 3), 15, color);
                break;
            default:
                break;
        }
    }

    /**
     * drawStartTiles
     * This draws the start tiles
     *
     * @param box    This is the width of the board divided by 15. This is used to scale everything
     *               according to the screens dimensions
     * @param canvas canvas object
     * @param paint  paint object
     * @param xShfit X Shift to reuse the same code the draw the start tiles in all four corners
     * @param yShift Y Shift to reuse the same code the draw the start tiles in all four corners
     */
    public void drawStartTiles(float box, Canvas canvas, Paint paint, float xShfit, float yShift) {

        int box1X1 = (int) (box * 2.3), box1Y1 = (int) (box * 0.7), box1X2 = (int) (box * 3.7), box1Y2 = (int) (box * 2.1);
        int box2X1 = (int) (box * 3.9), box2Y1 = (int) (box * 2.3), box2X2 = (int) (box * 5.3), box2Y2 = (int) (box * 3.7);
        int box3X1 = (int) (box * 2.3), box3Y1 = (int) (box * 3.9), box3X2 = (int) (box * 3.7), box3Y2 = (int) (box * 5.3);
        int box4X1 = (int) (box * 0.7), box4Y1 = (int) (box * 2.3), box4X2 = (int) (box * 2.1), box4Y2 = (int) (box * 3.7);
        canvas.drawRect(box1X1 + xShfit, box1Y1 + yShift, box1X2 + xShfit, box1Y2 + yShift, paint);
        canvas.drawRect(box2X1 + xShfit, box2Y1 + yShift, box2X2 + xShfit, box2Y2 + yShift, paint);
        canvas.drawRect(box3X1 + xShfit, box3Y1 + yShift, box3X2 + xShfit, box3Y2 + yShift, paint);
        canvas.drawRect(box4X1 + xShfit, box4Y1 + yShift, box4X2 + xShfit, box4Y2 + yShift, paint);


    }

    /**
     * drawPieces
     * this draws the pieces. They are moved according to touch events etc.
     *
     * @param canvas Canvas objects
     * @param box    This is the width of the board divided by 15. This is used to scale everything
     *               according to the screens dimensions
     */
    public void drawPieces(Canvas canvas, float box) {

        Paint greenPaint = new Paint();
        Paint redPaint = new Paint();
        Paint redPaint2 = new Paint();
        Paint bluePaint = new Paint();
        Paint yellowPaint = new Paint();
        Paint whitePaint = new Paint();


        redPaint.setColor(Color.rgb(255, 0, 0));
        redPaint2.setColor(Color.rgb(100, 0, 0));
        greenPaint.setColor(Color.rgb(80, 255, 95));
        bluePaint.setColor(Color.rgb(75, 150, 255));
        yellowPaint.setColor(Color.rgb(230, 190, 70));
        whitePaint.setColor(Color.rgb(255, 255, 255));

        //initialze the isdrawn array to false
        isdrawn = new boolean[16];
        Arrays.fill(isdrawn, false);


        int xPos, yPos;
        for (int i = 0; i < 16; i++) {
            if (state.pieces[i].getIsHome() == false) {//draw the pieces out of base
                if (isdrawn[i] == false) {//only draw the pieces that haven't been drawn yet
                    if (state.pieces[i].getOwner() == 0) {//use the red path
                        xPos = state.pieces[i].getCurrentXLoc();
                        yPos = state.pieces[i].getCurrentYLoc();
                        checkOverlap(state.pieces[i], i, (box * xPos) + box / 2, (box * yPos) + box / 2, box, canvas, redPaint);
                    }
                    if (state.pieces[i].getOwner() == 1) {//use the green path
                        xPos = state.pieces[i].getCurrentXLoc();
                        yPos = state.pieces[i].getCurrentYLoc();
                        checkOverlap(state.pieces[i], i, (box * xPos) + box / 2, (box * yPos) + box / 2, box, canvas, greenPaint);
                    }
                    if (state.pieces[i].getOwner() == 2) {//use the yellow path
                        xPos = state.pieces[i].getCurrentXLoc();
                        yPos = state.pieces[i].getCurrentYLoc();
                        checkOverlap(state.pieces[i], i, (box * xPos) + box / 2, (box * yPos) + box / 2, box, canvas, yellowPaint);
                    }
                    if (state.pieces[i].getOwner() == 3) {//use the blue path
                        xPos = state.pieces[i].getCurrentXLoc();
                        yPos = state.pieces[i].getCurrentYLoc();
                        checkOverlap(state.pieces[i], i, (box * xPos) + box / 2, (box * yPos) + box / 2, box, canvas, bluePaint);
                    }

                }
            } else {//draw the pieces in the start square
                float x, y;
                if (state.pieces[i].getOwner() == 0) {//red piece
                    x = (float) state.pieces[i].getStartXPos() * box;
                    y = (float) state.pieces[i].getStartYPos() * box;
                    canvas.drawCircle(x, y, 30, whitePaint);
                    canvas.drawCircle(x, y, 25, redPaint);
                }
                if (state.pieces[i].getOwner() == 1) {//green piece
                    x = (float) state.pieces[i].getStartXPos() * box;
                    y = (float) state.pieces[i].getStartYPos() * box;
                    canvas.drawCircle(x, y, 30, whitePaint);
                    canvas.drawCircle(x, y, 25, greenPaint);
                }
                if (state.pieces[i].getOwner() == 2) {//yellow piece
                    x = (float) state.pieces[i].getStartXPos() * box;
                    y = (float) state.pieces[i].getStartYPos() * box;
                    canvas.drawCircle(x, y, 30, whitePaint);
                    canvas.drawCircle(x, y, 25, yellowPaint);
                }
                if (state.pieces[i].getOwner() == 3) {//blue piece
                    x = (float) state.pieces[i].getStartXPos() * box;
                    y = (float) state.pieces[i].getStartYPos() * box;
                    canvas.drawCircle(x, y, 30, whitePaint);
                    canvas.drawCircle(x, y, 25, bluePaint);
                }
            }
        }
    }

    /*
     * FIXED BY AVERY GUILLERMO
     *
     */
    //Draws the pieces entirely and ensures that all pieces can be seen even if another piece lands
    //on the same spot.
    public void checkOverlap(Token piece, int currentIndex, float xPos, float yPos, float box, Canvas canvas, Paint paint) {
        Paint greenPaint = new Paint();
        Paint redPaint = new Paint();
        Paint bluePaint = new Paint();
        Paint yellowPaint = new Paint();
        Paint whitePaint = new Paint();

        redPaint.setColor(Color.rgb(255, 0, 0));
        greenPaint.setColor(Color.rgb(80, 255, 95));
        bluePaint.setColor(Color.rgb(75, 150, 255));
        yellowPaint.setColor(Color.rgb(230, 190, 70));
        whitePaint.setColor(Color.rgb(255, 255, 255));


        //initialize local instance variables
        boolean overlapHappened = false;
        List<Integer> overlapPieceIndexies = new ArrayList<>();
        ArrayList<Paint> paints = new ArrayList<>();
        overlapPieceIndexies.add(currentIndex);
        paints.add(paint);

        //store the indexies and the paints of the pieces that overlap
        for (int i = 0; i < 16; i++) {
            if ((i != currentIndex) &&
                    state.pieces[i].getIsHome() == false &&
                    (piece.getCurrentXLoc() == state.pieces[i].getCurrentXLoc()) &&
                    (piece.getCurrentYLoc() == state.pieces[i].getCurrentYLoc())
                    ) {
                //overlap found!!
                overlapHappened = true;
                overlapPieceIndexies.add(i);
                switch (state.pieces[i].getOwner()) {
                    case 0:
                        paints.add(redPaint);
                        break;
                    case 1:
                        paints.add(greenPaint);
                        break;
                    case 2:
                        paints.add(yellowPaint);
                        break;
                    case 3:
                        paints.add(bluePaint);
                        break;
                }
            }
        }
        if (overlapHappened == true) {
            drawOverlapPieces(canvas, box, xPos, yPos, overlapPieceIndexies, paints);
        } else {
            canvas.drawCircle(xPos, yPos, 30, whitePaint);
            canvas.drawCircle(xPos, yPos, 25, paint);
        }
    }

    public void drawHomeBase(Canvas canvas, float box, float xPos, float yPos, Paint colorPaint, Paint whitePaint){
        canvas.drawRect(xPos + 0, yPos + 0, xPos + (box * 6), yPos + (box * 6), colorPaint);
        PointF d11 = new PointF(xPos + box * 3, yPos +0);
        PointF d12 = new PointF(xPos + box * 6, yPos + box * 3);
        PointF d13 = new PointF(xPos + (box * 3), yPos + (box * 6));
        PointF d14 = new PointF((xPos + box * 0),  yPos + (box * 3)); //Bottom Left
        //Drawing the red diamond
        Path redDiamond = new Path();
        redDiamond.moveTo(d11.x, d11.y);
        redDiamond.lineTo(d12.x, d12.y);
        redDiamond.lineTo(d13.x, d13.y);
        redDiamond.lineTo(d14.x, d14.y);
        redDiamond.close();
        canvas.drawPath(redDiamond, whitePaint);
        drawStartTiles(box, canvas, colorPaint, xPos + 0, yPos + 0);

    }

    public void drawCenterSquare(Canvas canvas, float box, Paint whitePaint, Paint redPaint, Paint greenPaint, Paint yellowPaint, Paint bluePaint){
        canvas.drawRect((box * 6), (box * 6), (box * 9), (box * 9), whitePaint);
        PointF p1 = new PointF((box * 6), (box * 6)); //Top Left
        PointF p2 = new PointF((canvas.getWidth() / 2), (canvas.getWidth() / 2)); //Dead Center
        PointF p3 = new PointF((box * 9), (box * 6)); //Top Right
        PointF p4 = new PointF((box * 6), (box * 9)); //Bottom Left
        PointF p5 = new PointF((box * 9), (box * 9)); //Bottom Right
        //Drawing the green Center Triangle
        Path tri1 = new Path();
        tri1.moveTo(p1.x, p1.y);
        tri1.lineTo(p2.x, p2.y);
        tri1.lineTo(p3.x, p3.y);
        tri1.close();
        canvas.drawPath(tri1, greenPaint);

        //Drawing the red Center Triangle
        Path tri2 = new Path();
        tri2.moveTo(p1.x, p1.y);
        tri2.lineTo(p2.x, p2.y);
        tri2.lineTo(p4.x, p4.y);
        tri2.close();
        canvas.drawPath(tri2, redPaint);

        //Drawing the blue Center Triangle
        Path tri3 = new Path();
        tri3.moveTo(p4.x, p4.y);
        tri3.lineTo(p2.x, p2.y);
        tri3.lineTo(p5.x, p5.y);
        tri3.close();
        canvas.drawPath(tri3, bluePaint);

        //Drawing the yellow Center Triangle
        Path tri4 = new Path();
        tri4.moveTo(p3.x, p3.y);
        tri4.lineTo(p2.x, p2.y);
        tri4.lineTo(p5.x, p5.y);
        tri4.close();
        canvas.drawPath(tri4, yellowPaint);
    }


    public void drawOverlapPieces(Canvas canvas, float box, float xPos, float yPos, List<Integer> indexies, ArrayList<Paint> p) {

        Paint whitePaint = new Paint();
        whitePaint.setColor(Color.rgb(255, 255, 255));

        switch (indexies.size()) {
            case 2: //draw two overlapping pieces
                //draw first piece
                canvas.drawCircle(xPos - box/5, yPos + box/5, (float) 21, whitePaint);
                canvas.drawCircle(xPos - box/5, yPos + box/5, (float) 17.5, p.get(0));

                //draw second piece
                canvas.drawCircle(xPos + box/5, yPos - box/5, (float) 21, whitePaint);
                canvas.drawCircle(xPos + box/5, yPos - box/5, (float) 17.5, p.get(1));

                //set the two pieces to true
                this.isdrawn[indexies.get(0)] = true;
                this.isdrawn[indexies.get(1)] = true;
                break;
            case 3: //draw three overlapping pieces
                //draw first piece
                canvas.drawCircle(xPos - box/4, yPos + box/4, 15, whitePaint);
                canvas.drawCircle(xPos - box/4, yPos + box/4, (float) 12.5, p.get(0));

                //draw second piece
                canvas.drawCircle(xPos, yPos, 15, whitePaint);
                canvas.drawCircle(xPos, yPos, (float) 12.5, p.get(1));

                //draw third piece
                canvas.drawCircle(xPos + box/4, yPos - box/4, 15, whitePaint);
                canvas.drawCircle(xPos + box/4, yPos - box/4, (float) 12.5, p.get(2));

                //set the two pieces to true
                this.isdrawn[indexies.get(0)] = true;
                this.isdrawn[indexies.get(1)] = true;
                this.isdrawn[indexies.get(2)] = true;
                break;
            case 4: //draw four overlapping pieces
                //draw first piece
                canvas.drawCircle(xPos - box/4, yPos - box/4, 15, whitePaint);
                canvas.drawCircle(xPos - box/4, yPos - box/4, (float) 12.5, p.get(0));

                //draw second piece
                canvas.drawCircle(xPos + box/4, yPos - box/4, 15, whitePaint);
                canvas.drawCircle(xPos + box/4, yPos - box/4, (float) 12.5, p.get(1));

                //draw third piece
                canvas.drawCircle(xPos - box/4, yPos + box/4, 15, whitePaint);
                canvas.drawCircle(xPos - box/4, yPos + box/4, (float) 12.5, p.get(2));

                //draw fourth piece
                canvas.drawCircle(xPos + box/4, yPos + box/4, 15, whitePaint);
                canvas.drawCircle(xPos + box/4, yPos + box/4, (float) 12.5, p.get(3));

                //set the two pieces to true
                this.isdrawn[indexies.get(0)] = true;
                this.isdrawn[indexies.get(1)] = true;
                this.isdrawn[indexies.get(2)] = true;
                this.isdrawn[indexies.get(3)] = true;
                break;
            case 5: //draw five overlapping pieces
                //draw first piece
                canvas.drawCircle(xPos - box / 4, yPos - box / 4, 15, whitePaint);
                canvas.drawCircle(xPos - box / 4, yPos - box / 4, (float) 12.5, p.get(0));

                //draw second piece
                canvas.drawCircle(xPos + box / 4, yPos - box / 4, 15, whitePaint);
                canvas.drawCircle(xPos + box / 4, yPos - box / 4, (float) 12.5, p.get(1));

                //draw third piece
                canvas.drawCircle(xPos - box / 4, yPos + box / 4, 15, whitePaint);
                canvas.drawCircle(xPos - box / 4, yPos + box / 4, (float) 12.5, p.get(2));

                //draw fourth piece
                canvas.drawCircle(xPos + box / 4, yPos + box / 4, 15, whitePaint);
                canvas.drawCircle(xPos + box / 4, yPos + box / 4, (float) 12.5, p.get(3));

                //draw fifth piece
                canvas.drawCircle(xPos, yPos, 15, whitePaint);
                canvas.drawCircle(xPos, yPos, (float) 12.5, p.get(4));
                break;
            case 6: //draw six overlapping pieces
                //draw first piece
                canvas.drawCircle(xPos - box / 4, yPos - box / 4, 12, whitePaint);
                canvas.drawCircle(xPos - box / 4, yPos - box / 4, (float) 10, p.get(0));

                //draw second piece
                canvas.drawCircle(xPos + box / 4, yPos - box / 4, 12, whitePaint);
                canvas.drawCircle(xPos + box / 4, yPos - box / 4, (float) 10, p.get(1));

                //draw third piece
                canvas.drawCircle(xPos - box / 4, yPos + box / 4, 12, whitePaint);
                canvas.drawCircle(xPos - box / 4, yPos + box / 4, (float) 10, p.get(2));

                //draw fourth piece
                canvas.drawCircle(xPos + box / 4, yPos + box / 4, 12, whitePaint);
                canvas.drawCircle(xPos + box / 4, yPos + box / 4, (float) 10, p.get(3));

                //draw fifth piece
                canvas.drawCircle(xPos - box / 4, yPos, 12, whitePaint);
                canvas.drawCircle(xPos - box / 4, yPos, (float) 10, p.get(4));

                //draw sixth piece
                canvas.drawCircle(xPos + box / 4, yPos, 12, whitePaint);
                canvas.drawCircle(xPos + box / 4, yPos, (float) 10, p.get(5));
                break;
            case 7: //draw seven overlapping pieces
                //draw first piece
                canvas.drawCircle(xPos - box/4, yPos - box/4, 12, whitePaint);
                canvas.drawCircle(xPos - box/4, yPos - box/4, (float) 10, p.get(0));

                //draw second piece
                canvas.drawCircle(xPos + box/4, yPos - box/4, 12, whitePaint);
                canvas.drawCircle(xPos + box/4, yPos - box/4, (float) 10, p.get(1));

                //draw third piece
                canvas.drawCircle(xPos - box/4, yPos + box/4, 12, whitePaint);
                canvas.drawCircle(xPos - box/4, yPos + box/4, (float) 10, p.get(2));

                //draw fourth piece
                canvas.drawCircle(xPos + box/4, yPos + box/4, 12, whitePaint);
                canvas.drawCircle(xPos + box/4, yPos + box/4, (float) 10, p.get(3));

                //draw fifth piece
                canvas.drawCircle(xPos - box/4, yPos, 12, whitePaint);
                canvas.drawCircle(xPos - box/4, yPos, (float) 10, p.get(4));

                //draw sixth piece
                canvas.drawCircle(xPos + box/4, yPos, 12, whitePaint);
                canvas.drawCircle(xPos + box/4, yPos, (float) 10, p.get(5));

                //draw seventh piece
                canvas.drawCircle(xPos, yPos, 12, whitePaint);
                canvas.drawCircle(xPos, yPos, (float) 10, p.get(6));
                break;
            //Don't implement higher than seven because it is extremely improbable that more than 7 pieces
            //would land on the same tile. Even if it did, it would just draw over each other.
        }

    }

}

