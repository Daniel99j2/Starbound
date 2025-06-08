package com.daniel99j.starbound.magic.spell.other;

import com.daniel99j.starbound.magic.spell.Spell;
import net.minecraft.util.Identifier;

public class TransmutationSpell extends Spell {
//    private static final Map<Block, Block> TRANSMUTATIONS = new HashMap<>();
//    private BlockPos targetPos = null;
//
//    static {
//        // Define block transmutations
//        TRANSMUTATIONS.put(Blocks.STONE, Blocks.GOLD_ORE);
//        TRANSMUTATIONS.put(Blocks.DEEPSLATE, Blocks.DEEPSLATE_GOLD_ORE);
//        TRANSMUTATIONS.put(Blocks.IRON_ORE, Blocks.GOLD_ORE);
//        TRANSMUTATIONS.put(Blocks.DEEPSLATE_IRON_ORE, Blocks.DEEPSLATE_GOLD_ORE);
//        TRANSMUTATIONS.put(Blocks.COAL_ORE, Blocks.IRON_ORE);
//        TRANSMUTATIONS.put(Blocks.DEEPSLATE_COAL_ORE, Blocks.DEEPSLATE_IRON_ORE);
//        TRANSMUTATIONS.put(Blocks.SAND, Blocks.GLASS);
//        TRANSMUTATIONS.put(Blocks.COBBLESTONE, Blocks.STONE);
//        TRANSMUTATIONS.put(Blocks.GRAVEL, Blocks.CLAY);
//        TRANSMUTATIONS.put(Blocks.DIRT, Blocks.GRASS_BLOCK);
//        TRANSMUTATIONS.put(Blocks.OAK_LOG, Blocks.DARK_OAK_LOG);
//        TRANSMUTATIONS.put(Blocks.NETHERRACK, Blocks.NETHER_GOLD_ORE);
//        TRANSMUTATIONS.put(Blocks.COPPER_ORE, Blocks.IRON_ORE);
//        TRANSMUTATIONS.put(Blocks.DEEPSLATE_COPPER_ORE, Blocks.DEEPSLATE_IRON_ORE);
//    }
//
    public TransmutationSpell(Identifier id, int energyUsed, int cooldown, int color) {
        super(id, energyUsed, cooldown, color);
    }
//
//    @Override
//    protected boolean cast(ServerPlayerEntity player) {
//        // Use raycast to find target block
//        targetPos = null;
//        raycast(player, 20, 20, 0.5f, player.getEyePos(), player.getPitch(), player.getYaw(), false);
//
//        if (targetPos == null) {
//            return false; // No valid target block found
//        }
//
//        ServerWorld world = (ServerWorld) player.getWorld();
//        BlockState targetState = world.getBlockState(targetPos);
//        Block targetBlock = targetState.getBlock();
//
//        // Try to transmute the block
//        if (TRANSMUTATIONS.containsKey(targetBlock)) {
//            // Transmute the block
//            Block resultBlock = TRANSMUTATIONS.get(targetBlock);
//            world.setBlockState(targetPos, resultBlock.getDefaultState());
//
//            // Visual effect at the transmuted block
//            ParticleHelper.spawnParticlesAtPosition(world, Vec3d.ofCenter(targetPos),
//                    ParticleTypes.ENCHANT, 20, 0.5, 0.5, 0.5, 0.1);
//
//            // Play transmutation sound
//            SoundUtils.playSoundAtPosition(world, Vec3d.ofCenter(targetPos),
//                    SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0f, 1.0f);
//
//            return true;
//        }
//
//        // If no block transmutation, try to transmute nearby items
//        Box itemArea = new Box(targetPos).expand(3.0);
//        List<Entity> nearbyItems = world.getOtherEntities(player, itemArea, entity -> entity instanceof ItemEntity);
//
//        boolean transmutedAny = false;
//
//        for (Entity entity : nearbyItems) {
//            if (entity instanceof ItemEntity itemEntity) {
//                ItemStack stack = itemEntity.getStack();
//
//                if (stack.getItem() == Items.IRON_INGOT) {
//                    // Transmute iron to gold (at a ratio of 3:1)
//                    int count = stack.getCount();
//                    int goldAmount = count / 3;
//
//                    if (goldAmount > 0) {
//                        // Remove the original item
//                        itemEntity.discard();
//
//                        // Create the new transmuted item
//                        ItemEntity goldEntity = new ItemEntity(world,
//                                itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(),
//                                new ItemStack(Items.GOLD_INGOT, goldAmount));
//                        world.spawnEntity(goldEntity);
//
//                        // If there's remainder iron, spawn it too
//                        int remainder = count % 3;
//                        if (remainder > 0) {
//                            ItemEntity remainderEntity = new ItemEntity(world,
//                                    itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(),
//                                    new ItemStack(Items.IRON_INGOT, remainder));
//                            world.spawnEntity(remainderEntity);
//                        }
//
//                        // Visual effect
//                        ParticleHelper.spawnParticlesAtPosition(world, itemEntity.getPos().add(0, 0.5, 0),
//                                ParticleTypes.ENCHANT, 20, 0.3, 0.3, 0.3, 0.1);
//
//                        transmutedAny = true;
//                    }
//                } else if (stack.getItem() == Items.COAL) {
//                    // Transmute coal to iron (at a ratio of 4:1)
//                    int count = stack.getCount();
//                    int ironAmount = count / 4;
//
//                    if (ironAmount > 0) {
//                        // Remove the original item
//                        itemEntity.discard();
//
//                        // Create the new transmuted item
//                        ItemEntity ironEntity = new ItemEntity(world,
//                                itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(),
//                                new ItemStack(Items.IRON_INGOT, ironAmount));
//                        world.spawnEntity(ironEntity);
//
//                        // If there's remainder coal, spawn it too
//                        int remainder = count % 4;
//                        if (remainder > 0) {
//                            ItemEntity remainderEntity = new ItemEntity(world,
//                                    itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(),
//                                    new ItemStack(Items.COAL, remainder));
//                            world.spawnEntity(remainderEntity);
//                        }
//
//                        // Visual effect
//                        ParticleHelper.spawnParticlesAtPosition(world, itemEntity.getPos().add(0, 0.5, 0),
//                                ParticleTypes.ENCHANT, 20, 0.3, 0.3, 0.3, 0.1);
//
//                        transmutedAny = true;
//                    }
//                }
//            }
//        }
//
//        return transmutedAny;
//    }
//
//    @Override
//    protected void raycast(ServerPlayerEntity player, int remaining, int steps, float distance, Vec3d currentPos, float pitch, float yaw, boolean hit) {
//        if (remaining > 0) {
//            double yawRad = Math.toRadians(yaw);
//            double pitchRad = Math.toRadians(pitch);
//
//            double x = -Math.sin(yawRad) * Math.cos(pitchRad);
//            double y = -Math.sin(pitchRad);
//            double z = Math.cos(yawRad) * Math.cos(pitchRad);
//
//            Vec3d accelerationVector = new Vec3d(x, y, z).normalize().multiply(distance);
//
//            Vec3d newPos = currentPos.add(accelerationVector.x, accelerationVector.y, accelerationVector.z);
//
//            if (!player.getWorld().getBlockState(BlockPos.ofFloored(newPos)).getCollisionShape(player.getWorld(), BlockPos.ofFloored(newPos)).isEmpty()) {
//                targetPos = BlockPos.ofFloored(newPos);
//                ParticleHelper.spawnParticlesAtPosition(player.getWorld(), newPos, ParticleTypes.ENCHANT, 5, 0.2, 0.2, 0.2, 0);
//            } else {
//                raycast(player, remaining - 1, steps, distance, newPos, pitch, yaw, hit);
//            }
//        }
//    }
//
//    @Override
//    protected void castEffects(ServerPlayerEntity player) {
//        super.castEffects(player);
//
//        // Visual casting effect
//        ParticleHelper.spawnParticlesAtPosition(player.getWorld(), player.getPos().add(0, 1, 0),
//                ParticleTypes.ENCHANT, 30, 0.5, 1.0, 0.5, 0.1);
//
//        // Play transmutation sound
//        SoundUtils.playSoundAtPosition((ServerWorld) player.getWorld(), player.getEyePos(),
//                SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1.0f, 1.0f);
//    }
}
