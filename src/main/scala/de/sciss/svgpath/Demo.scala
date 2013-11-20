package de.sciss.svgpath

import java.awt.{RenderingHints, Color}
import scala.swing.{Swing, Graphics2D, Component, MainFrame, Frame, SimpleSwingApplication}
import Swing._
import java.awt.geom.{Path2D, GeneralPath}

object Demo extends SimpleSwingApplication {
  lazy val top: Frame = new MainFrame {
    contents = new Component {
      override def paintComponent(g2: Graphics2D): Unit = {
        g2.setColor(Color.white)
        g2.fillRect(0, 0, peer.getWidth, peer.getHeight)
        g2.setColor(Color.black)
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING  , RenderingHints.VALUE_ANTIALIAS_ON)
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE )

        // generated
        g2.fill {
          val p1 = new GeneralPath(Path2D.WIND_EVEN_ODD)
          p1.moveTo(16.0f, 8.286f)
          p1.curveTo(8.454f, 8.286f, 2.5f, 16.0f, 2.5f, 16.0f)
          p1.curveTo(2.5f, 16.0f, 8.454f, 23.715f, 16.0f, 23.715f)
          p1.curveTo(21.771f, 23.715f, 29.5f, 16.0f, 29.5f, 16.0f)
          p1.curveTo(29.5f, 16.0f, 21.771f, 8.286f, 16.0f, 8.286f)
          p1.moveTo(16.0f, 20.807f)
          p1.curveTo(13.351f, 20.807f, 11.193f, 18.65f, 11.193f, 15.999999f)
          p1.curveTo(11.193f, 13.349998f, 13.351f, 11.192999f, 16.0f, 11.192999f)
          p1.curveTo(18.649f, 11.192999f, 20.807f, 13.350999f, 20.807f, 15.999999f)
          p1.curveTo(20.807f, 18.648998f, 18.649f, 20.807f, 16.0f, 20.807f)
          p1.moveTo(16.0f, 13.194f)
          p1.curveTo(14.451f, 13.194f, 13.194f, 14.450001f, 13.194f, 16.0f)
          p1.curveTo(13.194f, 17.55f, 14.450001f, 18.806f, 16.0f, 18.806f)
          p1.curveTo(17.55f, 18.806f, 18.806f, 17.55f, 18.806f, 16.0f)
          p1.curveTo(18.806f, 14.451f, 17.55f, 13.194f, 16.0f, 13.194f)
          p1.closePath()
          p1
        }

        g2.translate(32, 0)
        g2.fill {
          val p1 = new GeneralPath(Path2D.WIND_EVEN_ODD)
          p1.moveTo(16.0f, 1.466f)
          p1.curveTo(7.973f, 1.466f, 1.466f, 7.973f, 1.466f, 16.0f)
          p1.curveTo(1.466f, 24.027f, 7.973f, 30.534f, 16.0f, 30.534f)
          p1.curveTo(24.027f, 30.534f, 30.534f, 24.027f, 30.534f, 16.0f)
          p1.curveTo(30.534f, 7.973f, 24.027f, 1.466f, 16.0f, 1.466f)
          p1.moveTo(14.757f, 8.0f)
          p1.lineTo(17.177f, 8.0f)
          p1.lineTo(17.177f, 10.573999f)
          p1.lineTo(14.757f, 10.573999f)
          p1.lineTo(14.757f, 8.0f)
          p1.moveTo(18.762f, 23.622f)
          p1.lineTo(16.1f, 23.622f)
          p1.curveTo(15.066f, 23.622f, 14.625f, 23.182f, 14.625f, 22.126f)
          p1.lineTo(14.625f, 15.261f)
          p1.curveTo(14.625f, 14.931f, 14.449f, 14.776999f, 14.141f, 14.776999f)
          p1.lineTo(13.261f, 14.776999f)
          p1.lineTo(13.261f, 12.4f)
          p1.lineTo(15.922999f, 12.4f)
          p1.curveTo(16.958f, 12.4f, 17.397f, 12.8619995f, 17.397f, 13.896f)
          p1.lineTo(17.397f, 20.783f)
          p1.curveTo(17.397f, 21.092001f, 17.573f, 21.267f, 17.880999f, 21.267f)
          p1.lineTo(18.760998f, 21.267f)
          p1.lineTo(18.760998f, 23.622f)
          p1.closePath()
          p1
        }
      }
    }

    size = (200, 200)
    centerOnScreen()
    open()
  }
}
