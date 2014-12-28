package stratchooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by joshua on 12/27/14.
 */
public class Menu extends JPanel
{
    public static final String CHOICE = "choice";
    private JButton[] choices;
    private int width, height, widthOne, heightOne;
    private int choice;

    public Menu(int height, String[] buttonText)
    {
        setChoice(0);
        this.width = 250;
        this.widthOne = width;
        this.height = height;
        this.heightOne = height / buttonText.length;
        this.choices = new JButton[buttonText.length];
        setLayout(new GridLayout(buttonText.length, 1));

        for(int k = 0; k < choices.length; k++)
        {
            choices[k] = new JButton(buttonText[k]);
            choices[k].setPreferredSize(new Dimension(widthOne, heightOne));
            add(choices[k]);
            final int chosen = k + 1;
            choices[k].addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    setChoice(chosen);
                }
            });
        }
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(width, height);
    }

    public int getChoice(){return choice;}
    public void setChoice(int choice)
    {
        firePropertyChange("choice", this.choice, choice);
        this.choice = 0;
    }
}
