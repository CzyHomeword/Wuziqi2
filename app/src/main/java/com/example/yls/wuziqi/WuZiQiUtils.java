package com.example.yls.wuziqi;

import android.graphics.Point;

import java.util.List;

/**
 * Created by yls on 2017/6/8.
 */

public class WuZiQiUtils {
    private static final int MAX_COUNT_IN_LINE=5;

    public static boolean checkFiveInLine(List<Point> piecesArray) {
        checkIsFull(piecesArray.size());

        for(Point p:piecesArray) {
            int x=p.x;
            int y=p.y;
            if(checkHorizontal(x,y,piecesArray)) return true;
            else if(checkVertical(x,y,piecesArray)) return true;
            else if(checkLeftDiagonal(x,y,piecesArray)) return true;
            else if(checkRightDiagonal(x,y,piecesArray)) return true;
        }
        return false;
    }

    public static boolean checkIsFull(int number) {
        if(number==WuZiQiPanel.MAX_PIECES_NUMBER) {
            return true;
        }
        return false;
    }

    private static boolean checkHorizontal(int x, int y, List<Point> piecesArray) {
        int count=1;

        for(int i=1;i<MAX_COUNT_IN_LINE;i++) {
            if(piecesArray.contains(new Point(x-i,y))) {
                count++;
            } else {
                break;
            }
        }
        if(count==MAX_COUNT_IN_LINE) return true;
        for(int i=1;i<MAX_COUNT_IN_LINE;i++) {
            if(piecesArray.contains(new Point(x+i,y))) {
                count++;
            } else {
                break;
            }
        }
        if(count==MAX_COUNT_IN_LINE) return true;

        return false;
    }

    private static boolean checkVertical(int x, int y, List<Point> piecesArray) {
        int count=1;

        for(int i=1;i<MAX_COUNT_IN_LINE;i++) {
            if(piecesArray.contains(new Point(x,y-i))) {
                count++;
            } else {
                break;
            }
        }
        if(count==MAX_COUNT_IN_LINE) return true;
        for(int i=1;i<MAX_COUNT_IN_LINE;i++) {
            if(piecesArray.contains(new Point(x,y+i))) {
                count++;
            } else {
                break;
            }
        }
        if(count==MAX_COUNT_IN_LINE) return true;

        return false;
    }

    private static boolean checkLeftDiagonal(int x, int y, List<Point> piecesArray) {
        int count=1;

        for(int i=1;i<MAX_COUNT_IN_LINE;i++) {
            if(piecesArray.contains(new Point(x-i,y-i))) {
                count++;
            } else {
                break;
            }
        }
        if(count==MAX_COUNT_IN_LINE) return true;
        for(int i=1;i<MAX_COUNT_IN_LINE;i++) {
            if(piecesArray.contains(new Point(x+i,y+i))) {
                count++;
            } else {
                break;
            }
        }
        if(count==MAX_COUNT_IN_LINE) return true;

        return false;
    }

    private static boolean checkRightDiagonal(int x, int y, List<Point> piecesArray) {
        int count=1;
        for(int i=1;i<MAX_COUNT_IN_LINE;i++) {
            if(piecesArray.contains(new Point(x-i,y+i))) {
                count++;
            } else {
                break;
            }
        }
        if(count==MAX_COUNT_IN_LINE) return true;
        for(int i=1;i<MAX_COUNT_IN_LINE;i++) {
            if(piecesArray.contains(new Point(x+i,y-i))) {
                count++;
            } else {
                break;
            }
        }
        if(count==MAX_COUNT_IN_LINE) return true;
        return false;
    }
}