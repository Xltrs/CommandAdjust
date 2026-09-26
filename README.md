# 🧐 What is this mod for?
This is a **command permission level modify** and **operator adjust** mod,**features**:
- ⚡ **Modify** any **command permission level** in **real time**.
- 🐇 **Lightweight** and **easy to use**.
- 🧱 **Good Compatibility** with other mods. (_Except for similar mods,I can't be sure they are compatible_)
- 🧷 **Highly configurable**.
- 🔒 **Security**.

## 😃 Please take note before using this mod!

🔗 This mod is based on **minecraft original permission level system**,so **you need to know**:
-  Minecraft has **5 permission levels**
   - Like : 0(All)🤓,1(Moderator)😄,2(Gamemaster)🤠,3(Admin)😎,4(Owner)😇
   - If you **want to learn more**,please go to **[Here](https://minecraft.wiki/w/Permission_level)**
  
🔧 If you want to use this mod **to your server**,then **you need to know** how to **modify server config** and **how they work**.
- And you can **learn more about this at** **[Here](https://minecraft.wiki/w/Server.properties)**.

 ## 🤔 How to use this mod?
  
Just use these command:

```
/setcmdlevel <command name> <level>

/delcmdconfig <command name>

/checkcmdlevel <raw/config> <command name>

/checkcmdconfiglist
```

## 😮 What are they each used for?

---

 ```setcmdlevel``` command 🔧
 
It can **modify any commands permission level**😄,for example this example🧐,it **will modify** ```give``` command permission level **to 0**🔧,**then everyone can use it**:
```
/setcmdlevel give 0
```
**But just please note**🤨,it **can't modify** any **level 4 commands**😏,for example this example,**it won't work**:

```
/setcmdlevel stop 0
```

And it can still **disable any command**😮,for example this example,**it will disable ```give``` command**😲:
```
/setcmdlevel give 5
```
Then **everyone can't use ```give``` command**,**even if you are owner**😂. (because **vanilla has no level 5 permission**)

And you **can't disable ```setcmdlevel``` command**😏.


---

 ```delcmdconfig``` command 💾
 
It can **let any commands** in config form **config level back to original level**😄,for example this example🧐,it **will delete** ```give``` command **level config**💾,then ```give``` command **back to level 2**:

```
/delcmdconfig give
```

---

 ```checkcmdlevel``` command 🔍
 
It **can check** any command **config or original level**😃,for example this example,it can check ```give``` command **original level**🔍:

```
/checkcmdlevel raw give
```
And this example,it can check ```give``` command **config level**🔎:

```
/checkcmdlevel config give
```

---

```slrcommand``` command🎨

It can use **any permission level to run command**🎨,for example this example🧐,it will **use level 3 permission to run ```kick``` command**:
```
/slrcommand 3 kick @r
```
But you can't  **privilege escalation to run advanced command**😏,for example this example😮,if you **only have level 1 permission**🤔,**it won't work**:
```
/slrcommand 2 give @s diamond
```

---

 ```checkcmdconfiglist``` command 📠
 
It **can check** all command **config**😊,not need **any parameter**📠,this example can use directly:

```
/checkcmdconfiglist
```

---

## 🔧 About configurable
**Prevent** modify command **config level back to raw level**😮 (on by default)
 - CannotModifyCommandConfigLevelToRawLevel
  
Show **debug info**📺 (on by default)
 - Debug
  
**Make** debug **info visible**📑 (off by default)
 - ShowDebug

**Print Command Adjust logo in server starting**📟 (on by default)
- CommandAdjustLogoPrint

**Server can run any command**,even if that command is disable🎭 (on by default)
 - ServerCanRunAnyCommand
  
**Prevent** delete **level 4 command** config🧤 (off by default)
 - CannotDeleteKeyCommandConfig
 
**Prevent** modify **level 4** command🔒 (on by default)
 - KeyCommandGuard
  
**Modify** command permission level **list**,have some **example** to you **reference to modify** this list🔧
 - CommandLevelConfigList

## 😁 Reminder
Never modify ```execute``` command permission level **below 2**🤨,because ```execute``` command have a **privilege escalation bug**,it will be **let player** can use some parameter to **run any advanced commands(level 2 and below)** 😱,although **can't run** **level 3** and **above commands**😏,but it still **risk**😒.

If you want to **modify [WorldEdit](https://modrinth.com/plugin/worldedit) mod command**🧐,please **wrap in double quotes**,for example modify ```//pos1``` command,you should type:
  
  ```/setcmdlevel "/pos1" 2```

 But don't type: 

  ```/setcmdlevel "//pos1" 2```


## 🤗 FAQ
**Can I include this mod in my modpack? I want to let player cheat within the allowed limits😁.**
 - **Of course**. You can **include it in any modpack** without asking for permission😉. 

**What should I do at unknow command in config file🤔?**
 - Please use **"delcmdconfig"** command or **modify config file**,they **can all fix this problem**😏.
   
**Why don't fix "execute" command bug😠?**
 - Although I added **coping method**,but i can't **guarantee** there is **no way** to **get around it**😂.
   
**Why do i can't see downgraded command🤔?**
 - Because i don't know how to **modify command completion**,but i will **fix** this bug **in future**😅.

**A command can't see but downgraded to i can run it level,can it run🤨?**
 - **Of course**,it still **can run**,just you **can't see** it😅.

**Which this mod or [LuckPerms](https://modrinth.com/plugin/luckperms) is better😏?**
 - Can't say who is the **best**,need **depends** on your **situation**😄. If your server is **small** or **medium**,can **choose this mod**🤗;but if your server is **big**,please **choose [LuckPerms](https://modrinth.com/plugin/luckperms)** 😓.
