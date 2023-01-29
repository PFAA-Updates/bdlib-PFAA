package net.bdew.lib.network

import net.bdew.lib.BdLib
import net.bdew.lib.recipes.{RecipeLoader, RecipeParser, RecipesHelper}
import net.bdew.lib.recipes.gencfg.{ConfigSection, GenericConfigLoader, GenericConfigParser}

import java.io.{File, FileWriter}

object NetworkSecurity extends ConfigSection

object NetworkSecurityLoader {
  val loader = new RecipeLoader with GenericConfigLoader {
    override val cfgStore: ConfigSection = NetworkSecurity

    override def newParser(): RecipeParser = new RecipeParser with GenericConfigParser
  }

  def loadConfigFiles(): Unit = {
    if (!BdLib.configDir.exists()) {
      BdLib.configDir.mkdir()
      val nl = System.getProperty("line.separator")
      val f = new FileWriter(new File(BdLib.configDir, "readme.txt"))
      f.write("Any .cfg files in this directory will be loaded after the internal configuration, in alphabetic order" + nl)
      f.write("Files in 'overrides' directory with matching names cab be used to override internal configuration" + nl)
      f.close()
    }

    RecipesHelper.loadConfigs(
      modName = "BdLib",
      listResource = "/assets/bdlib/config/files.lst",
      configDir = BdLib.configDir,
      resBaseName = "/assets/bdlib/config/",
      loader = loader)

    val section = NetworkSecurity.getSection("messageWhitelist")
    SerializedMessageCodec.validClassPrefixes ++= section.getStringList("prefix")
    SerializedMessageCodec.validClasses ++= section.getStringList("exact")
    SerializedMessageCodec.enforceFilter = section.getBoolean("enforce")
    SerializedMessageCodec.validClassCache.clear()
  }
}