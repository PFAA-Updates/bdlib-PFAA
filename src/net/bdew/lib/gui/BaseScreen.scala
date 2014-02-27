/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.gui

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.Container
import scala.collection.mutable
import net.minecraft.client.Minecraft

abstract class BaseScreen(cont: Container, xSz: Int, ySz: Int) extends GuiContainer(cont) {
  xSize = xSz
  ySize = ySz

  val widgets = new WidgetContainerWindow(this, xSz, ySz)

  val background: Texture

  def rect = new Rect(guiLeft, guiTop, xSize, ySize)

  def getFontRenderer = Minecraft.getMinecraft.fontRenderer
  def getZLevel = zLevel

  override def initGui() {
    super.initGui()
    widgets.clear()
    buttonList.clear()
  }

  protected override def mouseClicked(x: Int, y: Int, bt: Int) {
    super.mouseClicked(x, y, bt)
    widgets.mouseClicked(Point(x, y) - rect.origin, bt)
  }

  protected override def keyTyped(c: Char, i: Int) =
    if (!widgets.keyTyped(c, i))
      super.keyTyped(c, i)

  protected override def drawGuiContainerForegroundLayer(x: Int, y: Int) =
    widgets.draw(Point(x, y) - rect.origin)

  protected override def drawScreen(x: Int, y: Int, f: Float) {
    super.drawScreen(x, y, f)

    val tip = mutable.MutableList.empty[String]

    widgets.handleTooltip(Point(x, y) - rect.origin, tip)

    import collection.JavaConversions._

    if (tip.size > 0)
      drawHoveringText(tip, x, y, getFontRenderer)
  }

  protected def drawGuiContainerBackgroundLayer(f: Float, x: Int, y: Int) {
    if (background != null)
      widgets.drawTexture(rect, background)
    widgets.drawBackground(Point(x, y))
  }
}
