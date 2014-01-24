/*
 * Copyright 2014 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.was;

import org.terasology.anotherWorld.BiomeProvider;
import org.terasology.anotherWorld.ChunkDecorator;
import org.terasology.anotherWorld.ChunkInformation;
import org.terasology.anotherWorld.PerlinLandscapeGenerator;
import org.terasology.anotherWorld.PluggableWorldGenerator;
import org.terasology.anotherWorld.coreBiome.DesertBiome;
import org.terasology.anotherWorld.coreBiome.ForestBiome;
import org.terasology.anotherWorld.coreBiome.PlainsBiome;
import org.terasology.anotherWorld.coreBiome.TaigaBiome;
import org.terasology.anotherWorld.coreBiome.TundraBiome;
import org.terasology.anotherWorld.decorator.BeachDecorator;
import org.terasology.anotherWorld.decorator.BlockCollectionFilter;
import org.terasology.anotherWorld.decorator.BlockFilter;
import org.terasology.anotherWorld.decorator.CaveDecorator;
import org.terasology.anotherWorld.decorator.layering.DefaultLayersDefinition;
import org.terasology.anotherWorld.decorator.layering.LayeringDecorator;
import org.terasology.anotherWorld.decorator.ore.OreDecorator;
import org.terasology.anotherWorld.decorator.structure.PocketStructureDefinition;
import org.terasology.anotherWorld.decorator.structure.VeinsStructureDefinition;
import org.terasology.anotherWorld.util.PDist;
import org.terasology.engine.CoreRegistry;
import org.terasology.engine.SimpleUri;
import org.terasology.growingFlora.BlockFloraDefinition;
import org.terasology.growingFlora.FloraDecorator;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.chunks.Chunk;
import org.terasology.world.generator.RegisterWorldGenerator;
import org.terasology.world.liquid.LiquidType;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
@RegisterWorldGenerator(id = "woodAndStoneTest", displayName = "Test Wood and Stone", description = "Generates the world for playing 'Wood and Stone' content mod")
public class TestWoodAndStoneWorldGenerator extends PluggableWorldGenerator {
    private BlockManager blockManager;

    public TestWoodAndStoneWorldGenerator(SimpleUri uri) {
        super(uri);
    }

    @Override
    protected void appendGenerators() {
        setSeaLevel(50);

        blockManager = CoreRegistry.get(BlockManager.class);

        final Block mantle = blockManager.getBlock("Core:MantleStone");
        final Block stone = blockManager.getBlock("Core:Stone");
        final Block water = blockManager.getBlock("Core:Water");
        final Block sand = blockManager.getBlock("Core:Sand");
        final Block dirt = blockManager.getBlock("Core:Dirt");
        final Block grass = blockManager.getBlock("Core:Grass");
        final Block snow = blockManager.getBlock("Core:Snow");
        final Block coal = blockManager.getBlock("Core:CoalOre");
        final Block iron = blockManager.getBlock("Core:IronOre");
        final Block gold = blockManager.getBlock("Core:GoldOre");

        setLandscapeGenerator(
                new PerlinLandscapeGenerator(0.3f, mantle, stone, water, LiquidType.WATER));

        addChunkDecorator(
                new BeachDecorator(new BlockCollectionFilter(stone), sand, 2, 5));

        setupLayers(stone, sand, dirt, grass, snow);

        setupOres(stone, coal, iron, gold);

        addChunkDecorator(
                new CaveDecorator(new BlockFilter() {
                    @Override
                    public boolean accepts(Chunk chunk, int x, int y, int z) {
                        return true;
                    }
                }, new PDist(0.1f, 0f), new PDist(5f, 1f), new PDist(70f, 60f), new PDist(70f, 10f), new PDist(2f, 0.5f))
        );

        setupFlora(grass, sand, snow);

        //List<Block> blocksToRemove = Arrays.asList(stone, sand, grass, dirt);
        //removeBlocks(blocksToRemove);
    }

    private void setupFlora(Block grass, Block sand, Block snow) {
        BlockFilter normalTreesGround = new BlockCollectionFilter(Arrays.asList(grass, snow));

        Block oakSapling = blockManager.getBlock("GrowingFlora:OakSaplingGenerated");
        Block pineSapling = blockManager.getBlock("GrowingFlora:PineSaplingGenerated");

        FloraDecorator floraDecorator = new FloraDecorator(new PDist(2f, 0.4f), new PDist(20f, 0.6f), new PDist(160f, 40f));

        // Forest
        floraDecorator.addTreeDefinition(ForestBiome.ID,
                new BlockFloraDefinition(0.9f, 0.8f, oakSapling, normalTreesGround));
        floraDecorator.addTreeDefinition(ForestBiome.ID,
                new BlockFloraDefinition(0.1f, 0.8f, pineSapling, normalTreesGround));

        // Plains
        floraDecorator.addTreeDefinition(PlainsBiome.ID,
                new BlockFloraDefinition(1f, 0.3f, oakSapling, normalTreesGround));

        // Tundra
        floraDecorator.addTreeDefinition(TundraBiome.ID,
                new BlockFloraDefinition(0.1f, 0.1f, oakSapling, normalTreesGround));
        floraDecorator.addTreeDefinition(TundraBiome.ID,
                new BlockFloraDefinition(0.9f, 0.7f, pineSapling, normalTreesGround));

        addChunkDecorator(floraDecorator);
    }

    private void removeBlocks(List<Block> blocksToRemove) {
        final BlockCollectionFilter filter = new BlockCollectionFilter(blocksToRemove);
        addChunkDecorator(
                new ChunkDecorator() {
                    @Override
                    public void initializeWithSeed(String seed) {
                    }

                    @Override
                    public void generateInChunk(Chunk chunk, ChunkInformation chunkInformation, BiomeProvider biomeProvider, int seaLevel) {
                        for (int x = 0; x < 16; x++) {
                            for (int z = 0; z < 16; z++) {
                                int level = chunkInformation.getGroundLevel(x, z);
                                for (int y = 1; y <= level; y++) {
                                    if (filter.accepts(chunk, x, y, z)) {
                                        chunk.setBlock(x, y, z, BlockManager.getAir());
                                    }
                                }
                            }
                        }
                    }
                });
    }

    private void setupOres(Block stone, final Block coal, final Block iron, final Block gold) {
        BlockFilter replacedBlocks = new BlockCollectionFilter(stone);
        OreDecorator oreDecorator = new OreDecorator(replacedBlocks);

        oreDecorator.addOreDefinition(
                "Core:CoalOre",
                new PocketStructureDefinition(
                        new PocketStructureDefinition.PocketBlockProvider() {
                            @Override
                            public Block getBlock(float distanceFromCenter) {
                                return coal;
                            }
                        }, new PDist(0.9f, 0.15f), new PDist(4f, 1f), new PDist(2f, 1f), new PDist(50f, 20f), new PDist(0f, 0.35f),
                        new PDist(1f, 0f), new PDist(0.7f, 0.1f), new PDist(0.2f, 0f), new PDist(0f, 0f)));

        oreDecorator.addOreDefinition(
                "Core:IronOre",
                new VeinsStructureDefinition(
                        new PDist(0.5f, 0.15f),
                        new VeinsStructureDefinition.VeinsBlockProvider() {
                            @Override
                            public Block getClusterBlock(float distanceFromCenter) {
                                return iron;
                            }

                            @Override
                            public Block getBranchBlock() {
                                return iron;
                            }
                        }, new PDist(2f, 0.3f), new PDist(35f, 10f), new PDist(4f, 1f), new PDist(0f, 0.55f), new PDist(45f, 5f),
                        new PDist(15f, 0f), new PDist(0f, 0f), new PDist(0f, 0f), new PDist(10f, 2.5f), new PDist(0.5f, 0.5f),
                        new PDist(0.5f, 0.3f), new PDist(1f, 0f), new PDist(1f, 0f)));

        oreDecorator.addOreDefinition(
                "Core:GoldOre",
                new VeinsStructureDefinition(
                        new PDist(0.4f, 0.1f),
                        new VeinsStructureDefinition.VeinsBlockProvider() {
                            @Override
                            public Block getClusterBlock(float distanceFromCenter) {
                                return gold;
                            }

                            @Override
                            public Block getBranchBlock() {
                                return gold;
                            }
                        }, new PDist(2f, 0.3f), new PDist(20f, 12f), new PDist(4f, 1f), new PDist(0f, 0.55f), new PDist(45f, 5f),
                        new PDist(15f, 0f), new PDist(0f, 0f), new PDist(0f, 0f), new PDist(10f, 2.5f), new PDist(0.5f, 0.5f),
                        new PDist(0.5f, 0.3f), new PDist(1f, 0f), new PDist(1f, 0f)));

        addChunkDecorator(oreDecorator);
    }

    private void setupLayers(Block stone, Block sand, Block dirt, Block grass, Block snow) {
        BlockFilter replacedBlocks = new BlockCollectionFilter(stone);

        LayeringDecorator layering = new LayeringDecorator();

        DefaultLayersDefinition desertDef = new DefaultLayersDefinition();
        desertDef.addLayerDefinition(new PDist(3, 1), replacedBlocks, sand, false);
        desertDef.addLayerDefinition(new PDist(4, 2), replacedBlocks, dirt, true);
        layering.addBiomeLayers(DesertBiome.ID, desertDef);

        DefaultLayersDefinition forestAndPlainsDef = new DefaultLayersDefinition();
        forestAndPlainsDef.addLayerDefinition(new PDist(1, 0), replacedBlocks, grass, false);
        forestAndPlainsDef.addLayerDefinition(new PDist(4, 2), replacedBlocks, dirt, true);
        layering.addBiomeLayers(ForestBiome.ID, forestAndPlainsDef);
        layering.addBiomeLayers(PlainsBiome.ID, forestAndPlainsDef);

        DefaultLayersDefinition tundraAndTaigaDef = new DefaultLayersDefinition();
        tundraAndTaigaDef.addLayerDefinition(new PDist(1, 0), replacedBlocks, snow, false);
        tundraAndTaigaDef.addLayerDefinition(new PDist(4, 2), replacedBlocks, dirt, true);
        layering.addBiomeLayers(TundraBiome.ID, tundraAndTaigaDef);
        layering.addBiomeLayers(TaigaBiome.ID, tundraAndTaigaDef);

        addChunkDecorator(layering);
    }
}