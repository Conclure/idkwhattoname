package me.conclure.concluresdinomod.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import me.conclure.concluresdinomod.entity.dinosaur.DinoStage;

import java.util.Arrays;
import java.util.List;

public class StageIndex {
    private final Int2ObjectMap<DinoStage> stageMap;
    private final Object2IntMap<DinoStage> stageIdMap;

    public StageIndex(List<DinoStage> stages) {
        this.stageMap = Int2ObjectMaps.unmodifiable(new Int2ObjectOpenHashMap<>() {
            {
                for (int i = 0; i < stages.size(); i++) {
                    this.put(i,stages.get(i));
                }
            }
        });
        this.stageIdMap = Object2IntMaps.unmodifiable(new Object2IntOpenHashMap<>() {
            {
                for (Int2ObjectMap.Entry<DinoStage> entry : StageIndex.this.stageMap.int2ObjectEntrySet()) {
                    this.put(entry.getValue(),entry.getIntKey());
                }
            }
        });
    }

    public static StageIndex create(DinoStage... stages) {
        return new StageIndex(Arrays.asList(stages));
    }

    public DinoStage getStageById(int id) {
        return this.stageMap.get(id);
    }

    public int getIdByStage(DinoStage stage) {
        return this.stageIdMap.getInt(stage);
    }
}
