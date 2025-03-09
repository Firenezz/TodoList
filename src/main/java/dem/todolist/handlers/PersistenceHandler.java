package dem.todolist.handlers;

import betterquesting.api.storage.BQ_Settings;
import betterquesting.core.BetterQuesting;
import betterquesting.handlers.SaveLoadHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dem.todolist.handlers.persistence.DatabasePersistence;
import dem.todolist.todo.TaskDatabase;
import dem.todolist.utils.NBTUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.world.WorldEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static dem.todolist.core.TodoSettings.curWorldDir;

public class PersistenceHandler {
    public static PersistenceHandler INSTANCE = new PersistenceHandler();

    public void saveData() {
        List<Future<Void>> allFutures = new ArrayList<>(5);

        allFutures.addAll(DatabasePersistence.INSTANCE.saveDatabases());

        for (Future<Void> future : allFutures) {
            try {
                future.get();
            } catch (InterruptedException e) {
                BetterQuesting.logger.warn("Saving interrupted!", e);
            } catch (ExecutionException e) {
                BetterQuesting.logger.warn("Saving failed!", e.getCause());
            }
        }
    }

    public void unload() {
        DatabasePersistence.INSTANCE.unloadDatabases();
    }

    @SubscribeEvent
    public void onWorldSave(WorldEvent.Save event) {
        if (!event.world.isRemote && BQ_Settings.curWorldDir != null && event.world.provider.dimensionId == 0) {
            PersistenceHandler.INSTANCE.saveData();
        }
    }

    @SubscribeEvent
    public void onWorldSave(WorldEvent.Load event) {
        if (!event.world.isRemote && BQ_Settings.curWorldDir != null && event.world.provider.dimensionId == 0) {
            PersistenceHandler.INSTANCE.saveData();
        }
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        MinecraftServer server = event.getServer();

        DatabasePersistence.INSTANCE.loadDatabases(server);
    }
}
