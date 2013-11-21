package de.sciss.icons

import java.awt.geom.{AffineTransform, GeneralPath, Path2D}
import java.awt.{Graphics2D, RenderingHints, Color, Graphics, Component, Shape}

package object raphael {
  def Apply(fun: Path2D => Unit): Shape = {
    val p = new GeneralPath(Path2D.WIND_EVEN_ODD)
    fun(p)
    p.closePath()
    p
  }

  def Icon(extent: Int = 32, fill: Color = Color.black)(fun: Path2D => Unit): javax.swing.Icon = {
    val p = new GeneralPath(Path2D.WIND_EVEN_ODD)
    fun(p)
    p.closePath()
    val shape = if (extent == 32) p else {
      val scale = extent/32f
      AffineTransform.getScaleInstance(scale, scale).createTransformedShape(p)
    }

    new IconImpl(extent, shape, fill)
  }

  private final class IconImpl(extent: Int, shape: Shape, fill: Color) extends javax.swing.Icon {
    def paintIcon(c: Component, g: Graphics, x: Int, y: Int): Unit = {
      val g2 = g.asInstanceOf[Graphics2D]
      val hints = g2.getRenderingHints
      val at    = g2.getTransform
      g2.setColor(fill)
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING  , RenderingHints.VALUE_ANTIALIAS_ON)
      g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE )
      g2.translate(x, y)
      g2.fill(shape)
      g2.setTransform(at)
      g2.setRenderingHints(hints)
    }

    def getIconWidth : Int = extent
    def getIconHeight: Int = extent
  }
}
