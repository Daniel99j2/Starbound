package com.daniel99j.starbound.magic;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PrismLensTrailManager {
    private static final Map<ServerWorld, ArrayList<PrismLensTrail>> trails = new HashMap<>();

    public static void tick() {
        for(ServerWorld world : trails.keySet()) {
            for(PrismLensTrail trail : trails.get(world)) {
                trail.tick();
            }
            trails.get(world).removeIf((trail -> trail.age().getValue() > 200));
        }
    }

    public static void addTrail(ServerWorld world, Vec3d pos) {
        var list = trails.getOrDefault(world, new ArrayList<>());
        list.add(new PrismLensTrail(pos, LENS_TRAIL_TYPE.REGULAR, new MutableInt(0)));
        trails.put(world, list);
    }

    public static void addInvisTrail(ServerWorld world, Vec3d pos) {
        var list = trails.getOrDefault(world, new ArrayList<>());
        list.add(new PrismLensTrail(pos, LENS_TRAIL_TYPE.INVISIBLE, new MutableInt(0)));
        trails.put(world, list);
    }

    public static ArrayList<PrismLensTrail> getTrails(ServerWorld world) {
        return trails.getOrDefault(world, new ArrayList<>());
    }

    public record PrismLensTrail(Vec3d pos, LENS_TRAIL_TYPE type, MutableInt age) {
        private void tick() {
            age.add(1);
        }
    }

    public enum LENS_TRAIL_TYPE {
        REGULAR,
        INVISIBLE
    }
}
