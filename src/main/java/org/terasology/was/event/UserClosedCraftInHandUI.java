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
package org.terasology.was.event;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.Event;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public class UserClosedCraftInHandUI implements Event {
    private EntityRef handleEntity;
    private EntityRef toolHeadEntity;

    public UserClosedCraftInHandUI(EntityRef handleEntity, EntityRef toolHeadEntity) {
        this.handleEntity = handleEntity;
        this.toolHeadEntity = toolHeadEntity;
    }

    public EntityRef getHandleEntity() {
        return handleEntity;
    }

    public EntityRef getToolHeadEntity() {
        return toolHeadEntity;
    }
}
