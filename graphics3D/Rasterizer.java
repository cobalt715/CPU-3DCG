package graphics3D;

import java.util.*;
import java.awt.*;
import java.awt.image.*;

//画像を作る
public class Rasterizer{
  private Pipeline pipeline;
  private BufferedImage image;
  private int[] pixels;
  private double[] zDistance;
  private int width,height;
  private int centerX,centerY;

  public BufferedImage render(java.util.List<Polygon3D> polygons,Camera camera){
    Arrays.fill(pixels,0xeeeeeeee);
    Arrays.fill(zDistance,0.0);

    for(Polygon3D polygon3D:polygons){
      for(Polygon3D projected:pipeline.process(polygon3D,camera)){
        setImage(projected);
      }
    }

    return image;
  }

  protected void setImage(Polygon3D projected){
    //Y軸が高い順にする
    //画面上に行く方が高いとする
    Vertex[] vertexs = new Vertex[]{projected.v0,projected.v1,projected.v2};

    Arrays.sort(vertexs,(a,b) -> Double.compare(a.pos.y,b.pos.y));

    Vertex top = vertexs[0];
    Vertex mid = vertexs[1];
    Vertex bot = vertexs[2];

    /*System.out.println(top.pos.y);
    System.out.println(mid.pos.y);
    System.out.println(bot.pos.y + "\n");*/

    Graphics2D g2 = image.createGraphics();

    Polygon pol = new Polygon(new int[]{(int)(top.pos.x) + centerX,(int)(mid.pos.x) + centerX,(int)(bot.pos.x) + centerX},
                              new int[]{(int)(top.pos.y) + centerY,(int)(mid.pos.y) + centerY,(int)(bot.pos.y) + centerY},
                              3);

    g2.setColor(Color.BLUE);
    g2.fill(pol);
  }

  public Rasterizer(int width,int height,Pipeline pipeline){
    this.pipeline = pipeline;
    this.width = width;
    this.height = height;
    centerX = width / 2;
    centerY = height / 2;

    image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

    pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

    zDistance = new double[pixels.length];
  }
}