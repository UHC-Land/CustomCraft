package com.smallaswater.customcraft;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.inventory.CraftingManager;
import cn.nukkit.inventory.FurnaceRecipe;
import cn.nukkit.inventory.Recipe;
import cn.nukkit.item.Item;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.utils.Utils;
import com.smallaswater.customcraft.recipe.CustomShapedRecipe;
import com.smallaswater.customcraft.recipe.CustomShapelessRecipe;
import io.netty.util.collection.CharObjectHashMap;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class CustomCraft extends PluginBase {

    public static final String VERSION = "?";

    private static CustomCraft instance;

    private Config craft = null;
    private Config nbt = null;
    private Config frame = null;
    private static final String CRAFT = "/craft.json";
    private static final String FRAME = "/frame.json";
    private static final String NBT_ITEM = "/nbtItems.json";

    @Getter
    private final ConcurrentHashMap<Player, ConcurrentHashMap<Recipe, Integer>> playerCraftCountMap = new ConcurrentHashMap<>();

    public static CustomCraft getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        this.getLogger().info("自定义合成开始加载，版本：" + VERSION);

        this.loadCraftConfig();
        this.loadFrameConfig();
        this.getCraftItemConfig();
        this.registerCraft();
        this.registerFrame();

        this.getServer().getPluginManager().registerEvents(new EventListener(this), this);

        this.getLogger().info("自定义合成加载成功!");
        this.getServer().getScheduler().scheduleTask(this, () -> {
            this.getLogger().info("此版本根据UHC-Land服务器需求进行了修改，可能不适用于所有服务器！");
            this.getLogger().info("插件开源链接 GitHub: https://github.com/UHC-Land/CustomCraft");
        });
    }

    @Override
    public void onDisable() {
        this.playerCraftCountMap.clear();
    }

    @SuppressWarnings("rawtypes")
    private void registerFrame() {
        CraftingManager manager = this.getServer().getCraftingManager();
        Map<String, Object> strings = frame.getAll();
        if (strings.size() > 0) {
            for (String outputName : strings.keySet()) {
                try {
                    Object o = strings.get(outputName);
                    if (o instanceof Map) {
                        Item outputItem = CraftItem.toItem((String) ((Map) o).get("output"));
                        if (CraftItem.isCraftItem(outputItem)) {
                            Item inputItem = CraftItem.toItem((String) ((Map) o).get("input"));
                            manager.registerFurnaceRecipe(new FurnaceRecipe(outputItem, inputItem));
                            manager.rebuildPacket();
                        }
                    }
                } catch (Exception e) {
                    this.getLogger().error("注册熔炉配方时出现错误：", e);
                }
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void registerCraft() {
        CraftingManager manager = this.getServer().getCraftingManager();
        List<Map> recipes = craft.getMapList("recipes");
        if (recipes.size() > 0) {
            for (Map recipe : recipes) {
                try {
                    if (Utils.toInt(recipe.get("type")) == 0) {
                        Item outputItem = CraftItem.toItem((String) recipe.get("output"));
                        if (!Item.isCreativeItem(outputItem)) {
                            Item.addCreativeItem(outputItem);
                        }
                        ArrayList<Item> inputItems = new ArrayList<>();
                        List items = (List) recipe.get("input");
                        for (Object s : items) {
                            if (s instanceof String) {
                                if (CraftItem.isCraftItem((String) s)) {
                                    Item input = CraftItem.toItem((String) s);
                                    inputItems.add(input);
                                }
                            }
                        }
                        CustomShapelessRecipe result = new CustomShapelessRecipe(outputItem, inputItems);
                        result.setMaxCraftCount(Utils.toInt(recipe.get("maxCraftCount")));
                        manager.registerRecipe(result);
                    } else {
                        Item outputItem = CraftItem.toItem((String) recipe.get("output"));
                        if (!Item.isCreativeItem(outputItem)) {
                            Item.addCreativeItem(outputItem);
                        }
                        String[] shape = (String[]) ((List) recipe.get("shape")).toArray(new String[0]);
                        Map input = (Map) recipe.get("input");
                        Map<Character, Item> ingredients = new CharObjectHashMap<>();
                        for (Object s : input.keySet()) {
                            if (s instanceof String) {
                                if (CraftItem.isCraftItem((String) input.get(s))) {
                                    Item inputs = CraftItem.toItem((String) input.get(s));
                                    ingredients.put(((String) s).charAt(0), inputs);
                                }
                            }
                        }

                        CustomShapedRecipe result = new CustomShapedRecipe(
                                outputItem,
                                shape,
                                ingredients,
                                new LinkedList<>());
                        result.setMaxCraftCount(Utils.toInt(recipe.get("maxCraftCount")));
                        manager.registerRecipe(result);
                    }
                } catch (Exception e) {
                    this.getLogger().error("注册合成配方时出现错误：\n" + recipe.toString(), e);
                }
            }
        }
        manager.rebuildPacket();
    }

    private void loadCraftConfig() {
        this.saveResource("craft.json");
        if (this.craft == null) {
            this.craft = new Config(this.getDataFolder() + CRAFT, Config.JSON);
        }
    }

    private void loadFrameConfig() {
        this.saveResource("frame.json");
        if (this.frame == null) {
            this.frame = new Config(this.getDataFolder() + FRAME, Config.JSON);
        }
    }

    private Config getCraftItemConfig() {
        this.saveResource("nbtItems.json");
        if (this.nbt == null) {
            this.nbt = new Config(this.getDataFolder() + NBT_ITEM, Config.JSON);
        }
        return nbt;
    }

    public static String getNbtItem(String string) {
        return instance.getCraftItemConfig().getString(string);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ("craft".equals(command.getName())) {
            if (sender instanceof Player) {
                if (args.length > 0) {
                    if ("add".equals(args[0])) {
                        if (args.length > 1) {
                            String name = args[1];
                            String tag = getNbtItem(name);
                            if (tag != null && !"".equals(tag)) {
                                sender.sendMessage(TextFormat.RED + "此物品名称已经存在..");
                            } else {
                                Item item = ((Player) sender).getInventory().getItemInHand();
                                Config config = getCraftItemConfig();
                                config.set(name, CraftItem.toStringItem(item));
                                config.save();
                                sender.sendMessage(TextFormat.GREEN + "名称" + name + "已保存到/nbtItems.json");
                            }
                            return true;
                        }
                    }
                }
            } else {
                sender.sendMessage(TextFormat.RED + "不要用控制台执行");
                return true;
            }

        }
        return false;
    }

    public static boolean toSaveItem(String name, Item item) {
        String tag = getNbtItem(name);
        if (tag != null && !"".equals(tag)) {
            return false;
        } else {
            Config config = CustomCraft.getInstance().getCraftItemConfig();
            config.set(name, CraftItem.toStringItem(item));
            config.save();
            return true;
        }
    }

}
