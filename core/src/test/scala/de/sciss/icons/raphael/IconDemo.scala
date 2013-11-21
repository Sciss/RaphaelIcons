package de.sciss.icons.raphael

import scala.swing.{ToggleButton, FlowPanel, Button, Frame, MainFrame, SimpleSwingApplication}
import java.awt.Font

object IconDemo extends SimpleSwingApplication {
  lazy val top: Frame = new MainFrame {
    val b1 = new Button("Stop!") {
      icon = Icon(extent = 128)(Shapes.StopSign)
      font = new Font("Sans", Font.PLAIN, 56)
    }

    val texIcon = Icon(extent = 24, fill = TexturePaint(24), shadow = WhiteShadow) _

    val b2 = new ToggleButton("View") {
      icon          = texIcon(Shapes.View)
      selectedIcon  = texIcon(Shapes.NoView)
      peer.putClientProperty("JButton.buttonType", "textured")
    }

    contents = new FlowPanel(b1, b2)

    title = "Icon Test"
    resizable = false
    pack()
    centerOnScreen()
  }
}
