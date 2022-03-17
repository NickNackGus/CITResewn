package shcm.shsupercm.fabric.citresewn.config;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import shcm.shsupercm.fabric.citresewn.CITResewn;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Mixin configuration for CIT Resewn's mixins.
 */
public class CITResewnMixinConfiguration implements IMixinConfigPlugin {
    private static final String MIXINS_ROOT = "shcm.shsupercm.fabric.citresewn.mixin";

    /**
     * Is Broken Paths enabled in config.
     * @see BrokenPaths
     * @see CITResewnConfig#broken_paths
     */
    private boolean broken_paths;

    /**
     * Set of mod ids that had compatibility mixins loaded for them.
     */
    private Set<String> compatMods = new HashSet<>();

    @Override
    public void onLoad(String mixinPackage) {
        CITResewnConfig launchConfig = CITResewnConfig.read();

        this.broken_paths = launchConfig.broken_paths;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (!mixinClassName.startsWith(MIXINS_ROOT))
            return false;
        mixinClassName = mixinClassName.substring(MIXINS_ROOT.length() + 1);

        if (mixinClassName.startsWith("broken_paths"))
            return broken_paths;

        if (mixinClassName.startsWith("compat.")) {
            mixinClassName = mixinClassName.substring(7);
            String modid = mixinClassName.substring(0, mixinClassName.indexOf('.'));
            boolean loaded = FabricLoader.getInstance().isModLoaded(modid);
            if (loaded && compatMods.add(modid))
                CITResewn.info("Loading compatibility for " + modid);
            return loaded;
        }

        return true;
    }


    @Override
    public String getRefMapperConfig() { return null; }
    @Override
    public List<String> getMixins() { return null; }
    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) { }
    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) { }
    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) { }
}
