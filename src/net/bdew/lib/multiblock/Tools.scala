/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.multiblock

import net.bdew.lib.block.BlockRef
import net.bdew.lib.multiblock.tile.{TileController, TileModule}
import net.minecraft.world.World

import scala.collection.mutable

object Tools {
  def canConnect(world: World, core: BlockRef, kind: String): Boolean = {
    val t = core.getTile[TileController](world).getOrElse(return false)
    t.getNumOfMoudules(kind) < t.cfg.modules.getOrElse(kind, return false)
  }

  def findConnections(world: World, start: BlockRef, kind: String) =
    (start.neighbours.values flatMap { case pos =>
      (pos.tile(world) flatMap {
        case t: TileModule => t.connected.cval
        case t: TileController => Some(pos)
        case _ => None
      }) filter (x => canConnect(world, x, kind))
    }).toList.distinct

  def getAdjancedConnected(w: World, core: BlockRef, pos: BlockRef, seen: mutable.Set[BlockRef]) =
    pos.neighbours.values
      .filterNot(seen.contains)
      .flatMap(_.getTile[TileModule](w))
      .filter(x => x.connected.contains(core))
      .map(_.mypos)

  def findReachableModules(world: World, core: BlockRef): Set[BlockRef] = {
    val seen = mutable.Set.empty[BlockRef]
    val queue = mutable.Queue.empty[BlockRef]
    queue ++= getAdjancedConnected(world, core, core, seen)
    while (queue.size > 0) {
      val current = queue.dequeue()
      seen.add(current)
      queue ++= getAdjancedConnected(world, core, current, seen)
    }
    return seen.toSet
  }
}
