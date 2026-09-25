package com.xltrs.commandadjust.functionlib;

import com.xltrs.commandadjust.Logshow;
import net.minecraft.commands.CommandSourceStack;

public class CommandRunAPI {
    public static void SetLevel(CommandSourceStack Source, int Level, String Command) {
        Logshow.debug("[CommandRun API] %s use level %s permission to run %s command", Source.getTextName(), Level, Command.split(" ")[0]);
        Source.getServer()
                .getCommands()
                .performCommand(
                        GetDispatcher.Get().parse(Command, Source.withPermission(Level)),
                        Command
                );
    }
}
