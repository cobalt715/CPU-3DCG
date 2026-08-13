import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.util.*;
import graphics3D.*;

public class Main{
  public static graphics3D.Renderer renderer = new graphics3D.Renderer(new Camera(new Point3D(350,-400,800),3500,3300));

  public static Cursor blankCursor;

  public static long lastTime = System.nanoTime();
  public static int count = 0;

  public static void main(String[] args){
    Polygon3D absMove  = new Polygon3D(new Point3D(-100,100,0),new Point3D(-100,0,0),new Point3D(100,100,0),Color.RED);
    Polygon3D move  = new Polygon3D(new Point3D(-100,100,0),new Point3D(-100,0,0),new Point3D(100,100,0),Color.RED);
    renderer.addPolygon3D(move);

    Polygon3D absMove2  = new Polygon3D(new Point3D(-100,100,0),new Point3D(100,100,0),new Point3D(-100,0,0),Color.RED);
    Polygon3D move2  = new Polygon3D(new Point3D(-100,100,0),new Point3D(100,100,0),new Point3D(-100,0,0),Color.RED);
    renderer.addPolygon3D(move2);

    Camera cam = new Camera(new Point3D(-150,0,0),0,0);

    renderer.addPolygon3D(new Polygon3D(new Point3D(0,0,0),new Point3D(0,0,0),new Point3D(0,100,100),Color.RED));

    Polygon3D floor1 = new Polygon3D(new Point3D(-1000,100,-1000),new Point3D(1000,100,-1000),new Point3D(1000,100,1000),Color.yellow);
    Polygon3D floor2 = new Polygon3D(new Point3D(-1000,100,-1000),new Point3D(1000,100,1000),new Point3D(-1000,100,1000),Color.yellow);

    renderer.addPolygon3D(floor1);
    renderer.addPolygon3D(floor2);


    try{
      //出展https://ja.wikipedia.org/wiki/%E3%83%A2%E3%83%8A%E3%83%BB%E3%83%AA%E3%82%B6#/media/%E3%83%95%E3%82%A1%E3%82%A4%E3%83%AB:Leonardo_da_Vinci_-_Mona_Lisa.jpg
      renderer.addPolygon3D(new Polygon3D(new Point3D(0,100,0),new Point3D(90,100,50),new Point3D(90,256,50),ImageIO.read(new File("Mona_Lisa.jpg")),
                            new double[]{0,959,959},new double[]{0,0,1451}));
      renderer.addPolygon3D(new Polygon3D(new Point3D(0,100,0),new Point3D(90,256,50),new Point3D(0,256,0),ImageIO.read(new File("Mona_Lisa.jpg")),
                            new double[]{0,959,0},new double[]{0,1451,1451}));

      //出展https://ja.wikipedia.org/wiki/%E7%A5%9E%E5%A5%88%E5%B7%9D%E6%B2%96%E6%B5%AA%E8%A3%8F#/media/%E3%83%95%E3%82%A1%E3%82%A4%E3%83%AB:The_Great_Wave_off_Kanagawa.jpg
      renderer.addPolygon3D(new Polygon3D(new Point3D(100,-90,0),new Point3D(230,-90,0),new Point3D(230,0,0),ImageIO.read(new File("Kanagawa.jpg")),
                            new double[]{0,1279,1279},new double[]{0,0,882}));
      renderer.addPolygon3D(new Polygon3D(new Point3D(100,-90,0),new Point3D(230,0,0),new Point3D(100,0,0),ImageIO.read(new File("Kanagawa.jpg")),
                            new double[]{0,1279,0},new double[]{0,882,882}));

      /*//前面
      renderer.addPolygon3D(new Polygon3D(new Point3D(100,-100,0),new Point3D(200,-100,0),new Point3D(200,0,0),ImageIO.read(new File("debug.png")),
                            new double[]{0,15,15},new double[]{0,0,15}));
      renderer.addPolygon3D(new Polygon3D(new Point3D(100,-100,0),new Point3D(200,0,0),new Point3D(100,0,0),ImageIO.read(new File("debug.png")),
                            new double[]{0,15,0},new double[]{0,15,15}));

      //背面
      renderer.addPolygon3D(new Polygon3D(new Point3D(100,-100,-100),new Point3D(200,0,-100),new Point3D(200,-100,-100),ImageIO.read(new File("debug.png")),
                            new double[]{15,0,0},new double[]{0,15,0}));
      renderer.addPolygon3D(new Polygon3D(new Point3D(100,-100,-100),new Point3D(100,0,-100),new Point3D(200,0,-100),ImageIO.read(new File("debug.png")),
                            new double[]{15,15,0},new double[]{0,15,15}));

      //右
      renderer.addPolygon3D(new Polygon3D(new Point3D(200,-100,0),new Point3D(200,-100,-100),new Point3D(200,0,-100),ImageIO.read(new File("debug.png")),
                            new double[]{0,15,15},new double[]{0,0,15}));
      renderer.addPolygon3D(new Polygon3D(new Point3D(200,-100,0),new Point3D(200,0,-100),new Point3D(200,0,0),ImageIO.read(new File("debug.png")),
                            new double[]{0,15,0},new double[]{0,15,15}));

      //左
      renderer.addPolygon3D(new Polygon3D(new Point3D(100,-100,0),new Point3D(100,0,-100),new Point3D(100,-100,-100),ImageIO.read(new File("debug.png")),
                            new double[]{15,0,0},new double[]{0,15,0}));
      renderer.addPolygon3D(new Polygon3D(new Point3D(100,-100,0),new Point3D(100,0,0),new Point3D(100,0,-100),ImageIO.read(new File("debug.png")),
                            new double[]{15,15,0},new double[]{0,15,15}));

      //上
      renderer.addPolygon3D(new Polygon3D(new Point3D(100,-100,-100),new Point3D(200,-100,-100),new Point3D(200,-100,0),ImageIO.read(new File("debug.png")),
                            new double[]{0,15,15},new double[]{0,0,15}));
      renderer.addPolygon3D(new Polygon3D(new Point3D(100,-100,-100),new Point3D(200,-100,0),new Point3D(100,-100,0),ImageIO.read(new File("debug.png")),
                            new double[]{0,15,0},new double[]{0,15,15}));

      //下
      renderer.addPolygon3D(new Polygon3D(new Point3D(100,0,-100),new Point3D(200,0,0),new Point3D(200,0,-100),ImageIO.read(new File("debug.png")),
                            new double[]{0,15,15},new double[]{0,15,0}));
      renderer.addPolygon3D(new Polygon3D(new Point3D(100,0,-100),new Point3D(100,-0,0),new Point3D(200,0,0),ImageIO.read(new File("debug.png")),
                            new double[]{0,0,15},new double[]{0,15,15}));*/

    }catch(Exception e){}

    renderer.addPolygon3D(new Polygon3D(new Point3D(300,0,0),new Point3D(500,0,200),new Point3D(300,200,0),Color.GREEN));
    renderer.addPolygon3D(new Polygon3D(new Point3D(300,0,200),new Point3D(500,0,0),new Point3D(500,200,0),Color.BLUE));

    renderer.addPolygon3D(new Polygon3D(new Point3D(100,0,350),new Point3D(50,0,300),new Point3D(50,200,300),Color.BLUE));

    SomeW some = new SomeW(new Point3D(600,0,-100),100);

    for(Polygon3D p3:some.pols){
      renderer.addPolygon3D(p3);
    }

    MakeBall ball = new MakeBall(new Point3D(100,-200,100),100);

    for(Polygon3D p3:ball.pols){
      renderer.addPolygon3D(p3);
    }

    for(int i = 0;i < 1000;i++){
      //renderer.addPolygon3D(new Polygon3D(new Point3D[]{new Point3D(100,-100,0),new Point3D(200,-100,0),new Point3D(200,0,0)},Color.GREEN));
    }

    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(null);
    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

    MyPanel panel = new MyPanel();
    frame.add(panel);

    panel.setFocusable(true);
    panel.requestFocusInWindow();
    Input input = new Input();
    panel.addKeyListener(input);
    MouseInput mInput = new MouseInput(frame);

    frame.setVisible(true);

    // 透明カーソル作成
    BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
    blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(img, new Point(0, 0), "blank");
    // フレームに設定
    frame.setCursor(blankCursor);

    Random ran = new Random();

    javax.swing.Timer timer = new javax.swing.Timer(16,e ->{
      //ゲーミング床
      /*int rgb = ran.nextInt();
      floor1.setTexture(Polygon3D.createSolidTexture(new Color(rgb)));
      floor2.setTexture(Polygon3D.createSolidTexture(new Color(rgb)));*/

      //回転
      cam.setBdire(cam.getBdire() + 50);
      cam.setVdire(cam.getVdire() + 10);
      move.verts[0].pos = Pipeline.rotate(Pipeline.relative(absMove.verts[0].pos,cam),cam);
      move.verts[1].pos = Pipeline.rotate(Pipeline.relative(absMove.verts[1].pos,cam),cam);
      move.verts[2].pos = Pipeline.rotate(Pipeline.relative(absMove.verts[2].pos,cam),cam);
      Point3D nor = new Point3D(move.normal());
      move.setNormal(nor,nor,nor);

      move2.verts[0].pos = Pipeline.rotate(Pipeline.relative(absMove2.verts[0].pos,cam),cam);
      move2.verts[1].pos = Pipeline.rotate(Pipeline.relative(absMove2.verts[1].pos,cam),cam);
      move2.verts[2].pos = Pipeline.rotate(Pipeline.relative(absMove2.verts[2].pos,cam),cam);
      Point3D nor2 = new Point3D(move2.normal());
      move2.setNormal(nor2,nor2,nor2);


      //経過時間
      count = (count + 1) % 60;
      //if(count == 0){
        long now = System.nanoTime();
        long delta = now - lastTime; // ナノ秒単位
        double deltaMillis = delta / 1_000_000.0; // ミリ秒に変換
        lastTime = now;
        System.out.println("1フレーム前との経過: " + deltaMillis + " ms");
      //}

      input.keyMove();
      mInput.update();
      panel.time();
      renderer.update();
      panel.repaint();
    });

    timer.start();
  }
}