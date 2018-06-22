package com.example.mohit.minesweeper;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;

public class MSButton extends AppCompatButton
{

    private int button=MainActivity.NO_ADJACENT_MINE;
    public final int x;
    public final int y;
    public boolean revealed=false;
    public boolean longClicked=false;

    public MSButton(Context context,int x, int y)
    {
        super(context);
        this.x=x;
        this.y=y;
    }

    public void LongClicked(boolean longClicked)
    {
        this.longClicked=longClicked;

    }

    public int getButton()
    {
        return button;
    }

    public void setButton(int button)
    {
        this.button=button;
    }
    public void setRevealed(boolean revealed)
    {
        this.revealed=revealed;
        setEnabled(false);
        if(button==MainActivity.MINE)
        {
            setBackgroundResource(R.drawable.mine);
        }
        else if(button==-5)
        {
            setBackgroundResource(R.drawable.mineblast);
        }
        else
        {
            setBackgroundColor(0xFF53A221);
            setBackgroundResource(R.drawable.button2_bg);
        }
    }
}