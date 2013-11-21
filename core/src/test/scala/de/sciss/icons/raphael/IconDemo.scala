package de.sciss.icons.raphael

import scala.swing.{Button, Frame, MainFrame, SimpleSwingApplication}
import java.awt.Font

object IconDemo extends SimpleSwingApplication {
  lazy val top: Frame = new MainFrame {
    contents = new Button("Stop!") {
      icon = Icon(extent = 128)(Shapes.StopSign)
      font = new Font("Sans", Font.PLAIN, 56)
    }

    title = "Icon Test"
    resizable = false
    pack()
    centerOnScreen()
  }
}
