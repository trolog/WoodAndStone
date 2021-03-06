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
package org.terasology.crafting.ui;

import org.terasology.crafting.system.CraftInHandRecipeRegistry;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.registry.CoreRegistry;
import org.terasology.rendering.gui.framework.UIDisplayElement;
import org.terasology.rendering.gui.framework.events.WindowListener;
import org.terasology.rendering.gui.windows.UIScreenInventory;

import javax.vecmath.Vector2f;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public class UICraftInHand extends UIScreenInventory {
    private EntityRef characterEntity;

    private UIAvailableInHandRecipesDisplay allRecipesDisplay;

    public UICraftInHand() {
        super();
        setId("WoodAndStone:CraftInHand");
        setModal(true);

        addWindowListener(
                new WindowListener() {
                    @Override
                    public void initialise(UIDisplayElement element) {
                    }

                    @Override
                    public void shutdown(UIDisplayElement element) {
                        windowClosed();
                    }
                }
        );
    }

    public void setCharacterEntity(EntityRef characterEntity) {
        this.characterEntity = characterEntity;
        updateRecipes();
    }

    public void updateRecipes() {
        cleanup();

        CraftInHandRecipeRegistry registry = CoreRegistry.get(CraftInHandRecipeRegistry.class);

        allRecipesDisplay = new UIAvailableInHandRecipesDisplay(new Vector2f(300, 400), registry, characterEntity);
        allRecipesDisplay.setHorizontalAlign(EHorizontalAlign.CENTER);
        allRecipesDisplay.setVerticalAlign(EVerticalAlign.TOP);
        addDisplayElement(allRecipesDisplay);
    }

    public void windowClosed() {
        cleanup();
    }

    private void cleanup() {
        if (allRecipesDisplay != null) {
            removeDisplayElement(allRecipesDisplay);
            allRecipesDisplay.dispose();
        }
    }
}
