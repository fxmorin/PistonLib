## PistonLib
### A mod/library that rewrites the piston system while keeping the vanilla mechanics and feel

### TODO
- Move all mod-related features into another mod *(Make this solely a library)*
- WIKI
- Blocks that can be placed on slime but break when the slime moves shouldn't break if the block below has the `fused` sticky property
- Dual-sided piston
- Quarter Slime blocks

### In Progress
- Piston Crushing, json recipe system that allows item entities to be crushed by pistons to turn into other blocks. Also entity crushing API

### Current Piston Features
- Configurable piston speed, per piston
- Configurable push limit, per piston
- Movable block entities
- Configurable sticky types
- Unlimited custom Sticky Groups (Slime, Honey, etc...)
- Individual sticky behavior per block sides
- Pull-only piston logic
- Piston behavior API, with new behaviors
- Piston stickiness API, with tons of new sticky types & sticky groups
- Piston Ticking API (Allow blocks to tick while being moved)
- Piston Merging/UnMerging API (Check the playlist xD)
- Piston Indirect Sticky API
- Piston Weight API (Blocks can be heavier than 1 block)
- Large Quasi API, on all axes and any distance
- Long piston's & Piston arms
- Tons of piston fixes for vanilla piston bugs. Which can all be toggled
- Piston Optimizations for Rendering and Computation
- Decoupled piston code. Piston wand to push blocks without pistons

### Other Features
- Double blocks can be pushed as a single block
- Auto Crafting Table using piston merging
- Half Slime/Honey/Powered/Redstone Lamp/Obsidian block
- Glue Block (Strong sticky)
- Togglable sticky block
- Sticky chain (chainstone)
- Axis-Locked blocks (only movable on one axis)
- Move counting block (power level based on amount moved)
- All sided observer
- Slippery blocks (blocks fall when not attached to any solid blocks)
- Obsidian Slabs & Stairs
- Config system to toggle individual features
- /pistonlib command to push/pull blocks with commands, override piston behavior for any block, and change config options in-game
- Full GameTest support through [GameTestLib](https://github.com/FxMorin/GameTestLib)

### Blocks that need textures
- axis_locked_block
- move_counting_block
- quasi_block
- weight_block
- most pistons
  
### Mods to make/update using the API
- [chains-link](https://www.curseforge.com/minecraft/mc-mods/chains-link)
- [More Pistons](https://www.curseforge.com/minecraft/mc-mods/more-pistons-jiraiyah-version)
- Player launcher pistons
- Colored Slime blocks & Honey Blocks

---

You can find a small amount of development progress in [this youtube playlist](https://www.youtube.com/embed/videoseries?list=PL3J0JOfWvCsvQNJqxBwXQnWM3b0sjXxAo)
[![PistonLib Development Playlist](https://img.youtube.com/vi/eukvh4gyeW0/0.jpg)](https://www.youtube.com/embed/videoseries?list=PL3J0JOfWvCsvQNJqxBwXQnWM3b0sjXxAo)
