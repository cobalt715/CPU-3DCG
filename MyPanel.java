import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import graphics3D.*;

public class MyPanel extends JPanel{
  private BufferedImage image;
  private int width,height;

  @Override
  protected void paintComponent(Graphics g){
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g;

    g2.drawImage(image,0,0,width,height,null);
  }

  public void setImage(BufferedImage image){
    this.image = image;
  }

  public MyPanel(){
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    this.width = screenSize.width;
    this.height = screenSize.height;
    setBounds(0,0,width,height);
    setLayout(null);
  }
}