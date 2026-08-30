# 🧐 What is this mod for?
This is a **command permission level modify** and **operator adjust** mod,**features**:
- ⚡ **Modify** any **command permission level** in **real time**.
- 🐇 **Lightweight** and **easy to use**.
- 🧱 **Good Compatibility** with other mods. (_Except for similar mods,I can't be sure they are compatibility_)
- 🧷 **Highly configurable**.
- 🔒 **Security**.

## 😃 Please take note before using this mod
🔗 This mod is based on **minecraft original permission level system**,so **you need know**:
-  Minecraft have **5 permission levels**
   - Like : 0(All)🤓,1(Moderator)😄,2(Gamemaster)🤠,3(Admin)😎,4(Owner)😇
   - If you **want to learn more**,please go to **[Here](https://minecraft.wiki/w/Permission_level)**
  
🔧 If you want to use this mod **to your server**,then **you need know** how to **modify server config** and **what do they work**.
- You can **learn more to** **[Here](https://minecraft.wiki/w/Server.properties)**

## 🤔 How to use this mod?
  
Just use these command:

```
/setcmdlevel <command name> <level>

/delcmdconfig <command name>

/checkcmdlevel <raw/config> <command name>

/checkcmdconfiglist
```

## 😮 What are they each used for?
### "setcmdlevel" command 🔧
It can **modify any commands permission level**😄,for example this example🧐,it **can modify** "give" command permission level *to 0*🔧,*then everyone can use it*:
```
/setcmdlevel give 0
```
**But just please note**🤨,it **can't modify** any *level 4* commands😏,for example this example,*It won't work*:

```
/setcmdlevel stop 0
```

### "delcmdconfig" command 💾
It can **let any commands** in config form **config level back to original level**😄,for example this example🧐,it **can delete** "give" command **level config**💾,then "give" command **back to level 2**:

```
/delcmdconfig give
```

### "checkcmdlevel" command 🔍
It **can check** any command **config or original level**😃,for example this example,it can check "give" command **original level**🔍:

```
/checkcmdlevel raw give
```
And this example,it can check "give" command **config level**🔎:

```
/checkcmdlevel config give
```

### "checkcmdconfiglist" command 📠
It **can check** all command **config**😊,not need **any parameter**📠,this example just can run:

```
/checkcmdconfiglist
```

## 🔧 About configurable
**Forbidden** modify command **config level back to raw level**😮 (on by default)
 - CannotModifyCommandConfigLevelToRawLevel
  
Show **debug info**📺 (on by default)
 - Debug
  
**Make** debug **info visible**📑 (off by default)
 - ShowDebug
  
**Forbidden** delete **level 4 command** config,it needs open **KeyCommandProtection** to work🧤 (off by default)
 - CannotDeleteKeyCommandConfig
 
**Forbidden** modify **level 4** command🔒 (on by default)
 - KeyCommandProtection
  
**Modify** command permission level **list**,have some **example** to you **reference to modify** this list🔧
 - ModifyCommandLevelList

## 😁 Reminder
- Don't modify **"execute"** command permission level **below 2**🤨,because "execute" command have a **privilege escalation bug**,it will be **let player** can use some parameter to **run any advanced commands(level 2 and below)** 😱,although **can't run** **level 3** and **above commands**😏,but it is **risk** too🤕🎯.
 
- If you want to **modify [WorldEdit](https://modrinth.com/plugin/worldedit) mod command**🧐,please **wrap in double quotes**,and **remember** don't type "//XXX",but rather type "/XXX"😄.
  
- Don't **type invalid configuration in config file**,it will be let mod **stop working**😊.

## 🤗 FAQ
### Can I include this mod in my modpack? I want to let player cheat within the allowed limits😁.
**Absolutely**! You can **include it in any modpack** without asking for permission😉. 

### What should I do at a command exist in config but not exist in game command😰?
Please use **"delcmdconfig"** command or **modify config file**,they **can all fix this problem**😏.

### Why don't fix "execute" command bug😠?
Although I added **coping method**,but i can't **guarantee** there is **no way** to **get around it**😂.

### Why do i can't see downgraded command🤔? 
Because i don't know how to **modify command completion**,but i will be **fixed** this bug **in future**😅.

### A command can't see but downgraded to i can run it level,can it run🤨?
**Of course**,it still **can run**,just you **can't see** it😉.

### Which this mod or [LuckPerms](https://modrinth.com/plugin/luckperms) is better🤨?
Can't say who is the **best**,need **depends** on your **situation**😄. If your server is **small** or **medium**,can **choose this mod**🤗;but if your server is **big**,please **choose [LuckPerms](https://modrinth.com/plugin/luckperms)** 😅.

### Why don't support multiple language🤨?
**Because** this mod is **server-side** mod,support **multiple language** will be **a bit hard**😅,but i will **support** multiple language **in future**😉.

### Why "checkcmdlevel" command to lie with your eyes wide open? It although say a command not exist in game,but that command is exist in game😱.
Do you use this mod in **Singleplayer**? This mod will **appear this bug** in singleplayer **at sometime**,you can **check debug log** to **figure out the problem** 🤗.
