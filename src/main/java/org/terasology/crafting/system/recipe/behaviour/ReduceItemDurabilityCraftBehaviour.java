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
package org.terasology.crafting.system.recipe.behaviour;

import org.terasology.crafting.component.CraftInHandRecipeComponent;
import org.terasology.crafting.system.recipe.ItemCraftBehaviour;
import org.terasology.durability.DurabilityComponent;
import org.terasology.durability.ReduceDurabilityEvent;
import org.terasology.entitySystem.entity.EntityRef;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public class ReduceItemDurabilityCraftBehaviour implements ItemCraftBehaviour {
    private String itemType;
    private int durabilityUsed;

    public ReduceItemDurabilityCraftBehaviour(String itemType, int durabilityUsed) {
        this.itemType = itemType;
        this.durabilityUsed = durabilityUsed;
    }

    @Override
    public boolean isValid(EntityRef character, EntityRef item) {
        CraftInHandRecipeComponent craftComponent = item.getComponent(CraftInHandRecipeComponent.class);
        if (craftComponent == null || !craftComponent.componentType.equals(itemType)) {
            return false;
        }

        DurabilityComponent durability = item.getComponent(DurabilityComponent.class);
        if (durability == null) {
            return false;
        }

        return durability.durability >= durabilityUsed;
    }

    @Override
    public void processForItem(EntityRef character, EntityRef item) {
        item.send(new ReduceDurabilityEvent(1));
    }
}
