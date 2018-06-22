package com.example.mohit.minesweeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    LinearLayout rootLayout;

    public int GameStatus;

    public static final int NO_ADJACENT_MINE=0;

    public static final int MINE=-1;

    public static final int INCOMPLETE=-2;
    public static final int LOST=-1;
    public static final int WON=0;

    public double LEVEL;

    public int ROWS=10;
    public int COLUMN=7;

    public int numberOfMines;
    public ArrayList<MSButton> revealmine;

    public ArrayList<LinearLayout> rows;
    public MSButton board[][];


    int firstClick;
    int count=0;

    public static  int[] xArray={-1,-1,-1,0,+1,+1,+1,0},yArray={-1,0,+1,+1,+1,0,-1,-1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootLayout=findViewById(R.id.rootLayout);

        LEVEL=0.1;

        setupBoard();
    }

    public void setupBoard()
    {
        rows=new ArrayList<>();
        board=new MSButton[ROWS][COLUMN];
        revealmine=new ArrayList<>();

        GameStatus=INCOMPLETE;

        firstClick=0;
        count=0;

        rootLayout.removeAllViews();

        for(int i=0;i<ROWS;i++)
        {
            LinearLayout linearLayout=new LinearLayout(this);
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1);
            linearLayout.setLayoutParams(layoutParams);
            rows.add(linearLayout);
            rootLayout.addView(linearLayout);
        }

        for(int i=0;i<ROWS;i++)
        {
            for(int j=0;j<COLUMN;j++)
            {
                MSButton button=new MSButton(this,i,j);
                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1);
                button.setLayoutParams(layoutParams);
                button.setBackgroundResource(R.drawable.button_bg);
                button.setOnClickListener(this);
                button.setOnLongClickListener( this);
                board[i][j]=button;
                LinearLayout row=rows.get(i);
                row.addView(button);
            }
        }
    }

    @Override
    public boolean onLongClick(View view)
    {
        MSButton button=(MSButton) view;
        if(button.longClicked==false)
        {
            button.LongClicked(true);
        }
        else
        {
            button.LongClicked(false);
        }
        return false;
    }

    public void setupMine(int x, int y)
    {
        Random random=new Random();
        numberOfMines=(int)(ROWS*COLUMN*LEVEL);
        int xm;
        int ym;
        MSButton button;
        for(int i=0 ; i<numberOfMines;)
        {
            xm=random.nextInt(COLUMN);
            ym=random.nextInt(ROWS);
            button=board[ym][xm];
            if(button.getButton()==MINE||(button.x==x&&button.y==y))
            {
                continue;
            }
            else
            {
                button.setButton(MINE);
                revealmine.add(button);
                for(int j=0;j<8;j++)
                {
                    int adjx;
                    int adjy;
                    adjx=xArray[j]+button.x;
                    adjy=yArray[j]+button.y;
                    if(adjx>=0&&adjx<ROWS&&adjy>=0&&adjy<COLUMN)
                    {
                        MSButton button1=board[adjx][adjy];
                        if(button1.getButton()!=MINE)
                        {
                            button1.setButton(button1.getButton() + 1);
                        }
                    }
                }
                i++;
            }
        }
    }

    public void reveal(MSButton button)
    {
        if(button.getButton()>0)
        {
            button.setText(""+button.getButton());
            button.setRevealed(true);
            count++;
            return;
        }
        button.setText("");
        button.setRevealed(true);
        count++;
        for(int i=0;i<8;i++)
        {
            MSButton button1;
            int currentX=button.x;
            int currentY=button.y;
            int adjX,adjY;
            adjX=xArray[i]+currentX;
            adjY=yArray[i]+currentY;
            if(adjX>=0&&adjX<ROWS&&adjY>=0&&adjY<COLUMN)
            {
                button1=board[adjX][adjY];
                if(button1.revealed==false&&button1.getButton()!=MINE&&button1.getButton()>0)
                {
                    button1.setText(""+button1.getButton());
                    button1.setRevealed(true);
                    count++;
                }
                else if(button1.revealed==false&&button1.getButton()==0)
                {
                    button1.setText("");
                    button1.setRevealed(true);
                    reveal(button1);
                    count++;
                }
            }
        }
    }

    public void revealMines(MSButton button)
    {
        for(int i=0;i<numberOfMines;i++)
        {
            if(button!=revealmine.get(i))
            {
                revealmine.get(i).setRevealed(true);
            }
            else
            {
                revealmine.get(i).setButton(-5);
                revealmine.get(i).setRevealed(true);
            }
        }
    }

    @Override
    public void onClick(View view)
    {
        MSButton button = (MSButton) view;
        if(GameStatus==INCOMPLETE)
        {
            if(firstClick==0)
            {
                int x=button.x;
                int y=button.y;
                setupMine(x,y);
                reveal(button);
                firstClick=1;
            }
            else
            {
                if(button.getButton()==MINE)
                {
                    revealMines(button);
                    GameStatus=LOST;
                    Toast.makeText(this, "LOST, MINE HIT", Toast.LENGTH_LONG).show();
                }
                else
                {
                    reveal(button);
                    if(count==((ROWS*COLUMN)-numberOfMines-1))
                    {
                        GameStatus=WON;
                        Toast.makeText(this,"WON",Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }
}