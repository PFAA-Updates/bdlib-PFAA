/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.data

import net.bdew.lib.data.base.{DataSlotContainer, DataSlotNumeric, UpdateKind}
import net.minecraft.nbt.NBTTagCompound

case class DataSlotFloat(
    name: String,
    parent: DataSlotContainer,
    default: Float = 0
) extends DataSlotNumeric[Float](default) {
  def save(t: NBTTagCompound, kind: UpdateKind.Value) = t.setFloat(name, value)
  def load(t: NBTTagCompound, kind: UpdateKind.Value) = value = t.getFloat(name)
}
