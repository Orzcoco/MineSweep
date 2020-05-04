import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class MineSweep implements ActionListener {
    JFrame jFrame = new JFrame();
    JButton jButton = new JButton();
    int seconds = 0;
    ImageIcon bombIcon = new ImageIcon(MineSweep.class.getResource("bomb.png"));
    ImageIcon winIcon = new ImageIcon(MineSweep.class.getResource("winflag.png"));
    JLabel jLabel = new JLabel("用时： " + seconds + "s");
    Timer timer = new Timer(1000, this);
    int ROW = 20;
    int COL = 20;
    JButton[][] btns = new JButton[ROW][COL];
    int[][] data = new int[ROW][COL];
    int leicount = 10;
    int unopened = ROW*COL;
    int opended = 0;

    int success = 0;//成功次数


    int LEIFLAG=-1;
    private int[][] direction = {{0,1},{1,1},{1,0},{1,-1},{0,-1},{-1,-1},{-1,0},{-1,1}};



    public MineSweep()
    {
        jFrame.setSize(600, 700);
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLayout(new BorderLayout());
        jFrame.setTitle("疯狂扫雷");

        addLei();
        setButtons();
        JButton jButton = new JButton();
        jFrame.add(jLabel, BorderLayout.NORTH);


//        Image temp=bombIcon.getImage().getScaledInstance(jButton.getWidth(),jButton.getHeight(),bombIcon.getImage().SCALE_DEFAULT);
//        bombIcon = new ImageIcon(temp);
//        jButton.setIcon(bombIcon);
//        jButton.setDisabledIcon(bombIcon);
// jFrame.add(jButton, BorderLayout.CENTER);
        timer.start();
        jFrame.setVisible(true);
    }


    private boolean isRange(int row, int col)
    {
        if(row<0||row>=ROW||col<0||col>=COL) return false;
        else return true;
    }

    private void setButtons()
    {
        Container container = new Container();
        container.setLayout(new GridLayout(ROW ,COL));
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                //JButton jButton= new JButton(data[i][j]+"");
                final JButton jButton= new JButton();
                jButton.setBackground(new Color(134, 39,238));
                jButton.setMargin(new Insets(0,0,0,0));
                jButton.addActionListener(this);
                jButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        JButton jButton1 = (JButton) e.getSource();
                        if(e.getButton()==MouseEvent.BUTTON3 && jButton1.isEnabled())
                        {
                            Image temp=winIcon.getImage().getScaledInstance(jButton1.getWidth(),jButton1.getHeight(),winIcon.getImage().SCALE_DEFAULT);
                            winIcon = new ImageIcon(temp);
                            jButton1.setIcon(winIcon);
                            jButton1.setDisabledIcon(winIcon);
                        }else if(e.getButton()==MouseEvent.BUTTON2 && jButton1.isEnabled())
                        {
                            jButton1.setIcon(null);
                        }
                    }
                });

                container.add(jButton);
                btns[i][j] = jButton;
            }

        }
        jFrame.add(container, BorderLayout.CENTER);
    }

    private void addLei()
    {
        Random random = new Random();
        for (int i = 0; i < leicount; ) {
            int r = random.nextInt(ROW);
            int c = random.nextInt(COL);
            if (data[r][c]!=LEIFLAG)
            {
                data[r][c] = LEIFLAG;
                i++;
            }
        }

        //计算周边雷的数量
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (data[i][j]==LEIFLAG) continue;
                int count = 0;
                for(int k = 0;k<8;k++)
                {
                    int tempx = i+direction[k][0];
                    int tempy = j+direction[k][1];
                    if(isRange(tempx, tempy) && data[tempx][tempy]==LEIFLAG) count++;
                }
                data[i][j] = count;
            }

        }
    }

    //打开格子
    private void openCell(int i, int j)
    {
        JButton jButton = btns[i][j];
        if (!jButton.isEnabled()) return;
        jButton.setIcon(null);
        jButton.setOpaque(true);
        jButton.setEnabled(false);
        jButton.setBackground(new Color(147, 189, 176));
        jButton.setText(data[i][j]+"");
        Font f=new Font("宋体",Font.BOLD,16);
        jButton.setFont(f);

        if(data[i][j]==0)
        {
            for(int k = 0;k<8;k++)
            {
                int tempx = i+direction[k][0];
                int tempy = j+direction[k][1];
                if(isRange(tempx, tempy))
                {
                    if(data[tempx][tempy]==0)
                        openCell(tempx, tempy);
                    else if(data[tempx][tempy]!=LEIFLAG)
                    {
                        jButton = btns[tempx][tempy];
                        jButton.setIcon(null);
                        jButton.setOpaque(true);
                        jButton.setEnabled(false);
                        jButton.setBackground(new Color(147, 189, 176));
                        jButton.setText(data[tempx][tempy]+"");
                    }
                }
            }
        }

    }



    public static void main(String[] args)
    {
        new MineSweep();

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() instanceof  Timer)
        {
            seconds++;
            jLabel.setText("用时： "+seconds+"s");
            timer.start();
            return;
        }
        JButton jButton = (JButton)e.getSource();
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (jButton.equals(btns[i][j]))
                {
                    if(data[i][j]==LEIFLAG)
                    {
                        lose();
                    }
                    else
                    {
                        openCell(i,j);
                        checkWin();
                    }
                    return;
                }
            }

        }

    }

    private void checkWin() {
        int count = 0;
        //遍历一遍看看是否所有雷找到了
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (btns[i][j].isEnabled())
                {
                    count++;
                }
            }
        }

        if(count==leicount)
        {
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    if(btns[i][j].isEnabled())
                    {
                        btns[i][j].setEnabled(false);
                        Image temp=winIcon.getImage().getScaledInstance(btns[i][j].getWidth(),btns[i][j].getHeight(),winIcon.getImage().SCALE_DEFAULT);
                        winIcon = new ImageIcon(temp);
                        btns[i][j].setIcon(winIcon);
                        btns[i][j].setDisabledIcon(winIcon);
                    }
                }
            }
            timer.stop();
            success++;
            int result = JOptionPane.showConfirmDialog(jFrame, "你赢啦！\n点击确定重新开始！","提示",JOptionPane.YES_NO_CANCEL_OPTION);
            if(result==JOptionPane.YES_OPTION)
            {
                restart();
            }
            else
            {
                System.exit(0);
            }
        }
    }

    private void restart() {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                btns[i][j].setBackground(new Color(134, 39,238));
                btns[i][j].setText("");
                btns[i][j].setEnabled(true);
                btns[i][j].setIcon(null);
                data[i][j]=0;
            }
        }

        if(success>0) {
            this.leicount += (5 * success);
            if(this.leicount>=22*22){
                JOptionPane.showMessageDialog(jFrame, "你通关啦！");
                System.exit(0);
                success = 0;
            }
        }
        addLei();
        timer.start();
        seconds = 0;
        jLabel.setText("用时： " + seconds + "s");
    }

    private void lose() {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if(btns[i][j].isEnabled())
                {
                    JButton jButton = btns[i][j];
                    if(LEIFLAG==data[i][j])
                    {
                        jButton.setEnabled(false);
                        //jButton.setIcon(bombIcon);
                        //jButton.setDisabledIcon(bombIcon);
                        Image temp=bombIcon.getImage().getScaledInstance(jButton.getWidth(),jButton.getHeight(),bombIcon.getImage().SCALE_DEFAULT);
                        bombIcon = new ImageIcon(temp);
                        jButton.setIcon(bombIcon);
                        jButton.setDisabledIcon(bombIcon);
                    }
                    else
                    {
                        jButton.setIcon(null);
                        jButton.setEnabled(false);
                        jButton.setOpaque(true);
                        jButton.setText(data[i][j]+"");
                    }
                }
            }
        }
        //JOptionPane.showMessageDialog(jFrame, "你踩到雷啦！\n点击确定重新开始");
        timer.stop();
        int result = JOptionPane.showConfirmDialog(jFrame, "你踩到雷啦！\n点击确定重新开始","提示",JOptionPane.YES_NO_CANCEL_OPTION);
        if(result==JOptionPane.YES_OPTION)
        {
            restart();
        }
        else
        {
            System.exit(0);
        }
    }
}
