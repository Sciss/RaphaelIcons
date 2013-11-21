package de.sciss.icons.raphael

import java.awt.{Shape, RenderingHints, Color}
import scala.swing.{Swing, Graphics2D, Component, MainFrame, Frame, SimpleSwingApplication}
import Swing._
import java.awt.geom.{Path2D, GeneralPath}
import scala.swing.event.MouseMoved

object Demo extends SimpleSwingApplication {
  lazy val top: Frame = new MainFrame {
    val methods = classOf[Shapes].getMethods.filter(_.getName.charAt(0).isUpper)

    contents = new Component {
      override def paintComponent(g2: Graphics2D): Unit = {
        g2.setColor(Color.white)
        g2.fillRect(0, 0, peer.getWidth, peer.getHeight)
        g2.setColor(Color.black)
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING  , RenderingHints.VALUE_ANTIALIAS_ON)
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE )

        var line    = 0
        val atOrig  = g2.getTransform

        def moveRight(): Unit = g2.translate(32, 0)
        def lineFeed(): Unit = {
          g2.setTransform(atOrig)
          line += 1
          g2.translate(0, line * 32)
        }

        methods.zipWithIndex.foreach { case (m, idx) =>
          if (idx % 14 == 0) {
            if (idx > 0) lineFeed()
          } else moveRight()

          val p = new GeneralPath(Path2D.WIND_EVEN_ODD)
          // println(m.getName)
          m.invoke(null, p)
          p.closePath()
          g2.fill(p)
        }
      }

      preferredSize = (14 * 32, 19 * 32)

      listenTo(mouse.moves)
      reactions += {
        case MouseMoved(_, pt, _) =>
          val col = pt.x / 32
          val row = pt.y / 32
          val idx = row * 14 + col
          if (idx >= 0 && idx < methods.length) {
            tooltip = methods(idx).getName
          }
      }
    }

    title     = "Raphael Icons"
    resizable = false
    pack()
    centerOnScreen()
  }
}