package bettertodo.api.todo.todolist;

import java.util.UUID;

import net.minecraft.nbt.NBTTagList;

import chestlib.api.database.IUuidDatabase;
import chestlib.api.storage.INBTPartial;
import bettertodo.api.todo.ITodoList;

public interface ITodoListDatabase extends IUuidDatabase<ITodoList>, INBTPartial<NBTTagList, UUID> {

    ITodoList createNew(UUID uuid);

}
