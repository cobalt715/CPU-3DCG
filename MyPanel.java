import javax.swing.*;
import java.awt.*;
import graphics3D.*;

public class MyPanel extends JPanel{
  public JLabel label;

  @Override
  protected void paintComponent(Graphics g){
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g;
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    Main.renderer.draw(g2);
  }

  public void time(){
    label.setText(Main.renderer.camera.toString());
  }

  public MyPanel(){
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    setBounds(0,0,screenSize.width,screenSize.height);
    setLayout(null);

    label = new JLabel(Main.renderer.camera.toString());
    label.setBounds(0,0,300,500);
    //位置を「左上」にする
    label.setHorizontalAlignment(SwingConstants.LEFT);//横方向：左寄せ
    label.setVerticalAlignment(SwingConstants.TOP);//縦方向：上寄せ
    //文字を大きくする
    label.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 30));
    add(label);
  }
}