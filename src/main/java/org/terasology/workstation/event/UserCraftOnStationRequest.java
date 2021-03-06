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
package org.terasology.workstation.event;

import org.terasology.entitySystem.event.Event;
import org.terasology.network.ServerEvent;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
@ServerEvent
public class UserCraftOnStationRequest implements Event {
    private String workstationType;
    private String recipeId;
    private String resultId;

    public UserCraftOnStationRequest() {
    }

    public UserCraftOnStationRequest(String workstationType, String recipeId, String resultId) {
        this.workstationType = workstationType;
        this.recipeId = recipeId;
        this.resultId = resultId;
    }

    public String getWorkstationType() {
        return workstationType;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public String getResultId() {
        return resultId;
    }
}
