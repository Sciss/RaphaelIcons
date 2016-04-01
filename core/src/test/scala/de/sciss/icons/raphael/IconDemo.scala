package de.sciss.icons.raphael

import scala.swing.{Button, FlowPanel, Frame, MainFrame, SimpleSwingApplication, ToggleButton}
import java.awt.Font

import de.sciss.submin.Submin

object IconDemo extends SimpleSwingApplication {
  override def startup(args: Array[String]): Unit = {
    val isDark = args.contains("--dark")
    Submin.install(isDark)
    super.startup(args)
  }

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
